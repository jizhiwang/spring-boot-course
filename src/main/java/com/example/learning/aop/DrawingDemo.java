package com.example.learning.aop;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DrawingDemo {

    public static void main(String[] args) {
        try (var context = new ClassPathXmlApplicationContext("aop/drawing.xml")) {
            Drawing drawing = context.getBean(Drawing.class);
            drawing.draw();
            drawing.erase();
        }
    }
}
