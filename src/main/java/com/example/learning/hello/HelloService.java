package com.example.learning.hello;

import com.example.learning.LearnConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class HelloService {
    private static final Logger log = LoggerFactory.getLogger(HelloService.class);
    private final LearnConfig config;

    public HelloService(LearnConfig config) {
        this.config = config;
    }

    public String greet(String name) {
        log.debug("Building greeting for name={}", name);
        return config.greeting() + ", " + name + config.punctuation();
    }
}
