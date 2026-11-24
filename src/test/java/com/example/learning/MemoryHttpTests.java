package com.example.learning;

import com.example.learning.hello.HelloController;
import com.example.learning.hello.HelloService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.UnsatisfiedDependencyException;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;

@SpringBootTest
@ActiveProfiles("local")
class MemoryHttpTests {

    @Autowired
    WebApplicationContext context;

    @Test
    void helloUsesProfileGreeting() throws Exception {
        var response = send("GET", "/hello?name=Java", "");
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentAsString()).isEqualTo("{\"message\":\"Hi, Java!\"}");
    }

    @Test
    void helloControllerCannotStartWithoutItsServiceBean() {
        new ApplicationContextRunner().withUserConfiguration(HelloController.class).run(context -> {
            assertThat(context.getStartupFailure()).isInstanceOf(UnsatisfiedDependencyException.class)
                    .hasMessageContaining(HelloService.class.getName());
        });
    }

    @Test
    void postApiBindsQueryPathAndBodyAndRejectsInvalidInputs() throws Exception {
        var firstPage = send("GET", "/posts?name=Java&pageNum=1&pageSize=1", "");
        assertThat(firstPage.getStatus()).isEqualTo(200);
        assertThat(firstPage.getContentAsString()).contains("Java Developer", "\"total\":2").doesNotContain("Java Lead");
        assertThat(send("GET", "/posts/2", "").getContentAsString()).contains("QA Engineer");
        assertThat(send("GET", "/posts/999", "").getStatus()).isEqualTo(404);
        assertThat(send("GET", "/posts?pageSize=101", "").getStatus()).isEqualTo(400);
        assertThat(send("POST", "/posts", "{}").getStatus()).isEqualTo(400);
        var created = send("POST", "/posts", "{\"name\":\"Support Engineer\"}");
        assertThat(created.getStatus()).isEqualTo(201);
        assertThat(created.getContentAsString()).contains("Support Engineer");
    }

    private MockHttpServletResponse send(String method, String path, String body) throws Exception {
        return MockMvcBuilders.webAppContextSetup(context).build()
                .perform(request(HttpMethod.valueOf(method), path).contentType("application/json").content(body))
                .andReturn().getResponse();
    }
}
