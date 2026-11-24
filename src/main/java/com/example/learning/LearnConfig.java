package com.example.learning;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "learn")
public record LearnConfig(String greeting, String punctuation) {
}