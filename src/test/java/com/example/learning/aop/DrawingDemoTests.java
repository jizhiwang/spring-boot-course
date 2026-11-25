package com.example.learning.aop;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(OutputCaptureExtension.class)
class DrawingDemoTests {

    @Test
    void adviceRunsBeforeDrawOnly(CapturedOutput output) {
        DrawingDemo.main(new String[0]);

        assertThat(output.getOut().lines()).containsExactly("开始绘制", "画一个圆", "擦掉一个圆");
    }
}
