package com.example.learning.temporary;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;

// 保留已有的 Retention 和 RetentionPolicy 导入

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@interface LessonTag {
    String value();
}

public class AnnotationDemo {

    @LessonTag("岗位查询")
    public static void listPosts() {
        for (int i = 0; i < 5 ; i++) {
            System.out.println(i);
        }
    }

    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        var method = AnnotationDemo.class.getDeclaredMethod("listPosts");
        var tag = method.getAnnotation(LessonTag.class);

        if (tag != null) {
            System.out.println("即将执行：" + tag.value());
            method.invoke(null);
        }
    }
}