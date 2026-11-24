package com.example.learning.hello;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    private final HelloService helloService;

    public HelloController(HelloService helloService) {
        this.helloService = helloService;
    }

    @GetMapping("/hello")
    public Greeting hello(@RequestParam(defaultValue = "Spring Boot") String name) {
        return new Greeting(helloService.greet(name));
    }

    public record Greeting(String message) {
    }
}
