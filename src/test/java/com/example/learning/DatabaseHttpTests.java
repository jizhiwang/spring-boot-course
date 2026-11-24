package com.example.learning;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;

@SpringBootTest
@ActiveProfiles("test-db")
class DatabaseHttpTests {

    @Autowired
    WebApplicationContext context;

    @Autowired
    JdbcTemplate jdbc;

    @Test
    void xmlMapperFiltersPaginatesAndReturnsGeneratedId() throws Exception {
        var page = send("GET", "/posts?name=Java&pageNum=2&pageSize=1", "");
        assertThat(page.getStatus()).isEqualTo(200);
        assertThat(page.getContentAsString()).contains("Java Lead", "\"total\":2").doesNotContain("Java Developer");
        var created = send("POST", "/posts", "{\"name\":\"Database Engineer\"}");
        assertThat(created.getStatus()).isEqualTo(201);
        Long id = jdbc.queryForObject("SELECT id FROM learn_post WHERE name = 'Database Engineer'", Long.class);
        assertThat(created.getContentAsString()).contains("\"id\":" + id);
        assertThat(send("GET", "/posts/" + id, "").getContentAsString()).contains("Database Engineer");
    }

    @Test
    void courseCrudAndRollbackAreVisibleThroughHttpAndSql() throws Exception {
        var created = send("POST", "/courses", "{\"name\":\"Spring MVC\",\"labels\":[\"web\"]}");
        assertThat(created.getStatus()).isEqualTo(201);
        String id = jdbc.queryForObject("SELECT id FROM learn_course WHERE name = 'Spring MVC'", String.class);
        assertThat(send("GET", "/courses/" + id, "").getContentAsString()).contains("Spring MVC", "web");

        var updated = send("PUT", "/courses/" + id, "{\"name\":\"Spring Boot\",\"labels\":[\"java\"]}");
        assertThat(updated.getStatus()).isEqualTo(200);
        assertThat(updated.getContentAsString()).contains("Spring Boot", "java").doesNotContain("web");
        assertThat(send("GET", "/courses?name=Boot&pageSize=1", "").getContentAsString())
                .contains("Spring Boot", "\"total\":1");
        assertThat(send("POST", "/courses", "{\"name\":\"\",\"labels\":[\"java\"]}").getStatus())
                .isEqualTo(400);

        var rejected = send("POST", "/courses", "{\"name\":\"Rollback\",\"labels\":[\"dup\",\"dup\"]}");
        assertThat(rejected.getStatus()).isEqualTo(409);
        assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM learn_course WHERE name = 'Rollback'", Long.class))
                .isZero();
        assertThat(send("PUT", "/courses/" + id,
                "{\"name\":\"Broken edit\",\"labels\":[\"dup\",\"dup\"]}").getStatus()).isEqualTo(409);
        assertThat(send("GET", "/courses/" + id, "").getContentAsString()).contains("Spring Boot", "java")
                .doesNotContain("Broken edit", "dup");
    }

    private MockHttpServletResponse send(String method, String path, String body) throws Exception {
        return MockMvcBuilders.webAppContextSetup(context).build()
                .perform(request(HttpMethod.valueOf(method), path).contentType("application/json").content(body))
                .andReturn().getResponse();
    }
}
