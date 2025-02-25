package com.power;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @ClassName I18nDemoApplication
 * @Description TODO
 * @Author power
 * @Date 2025/2/26 0:54
 * @Version 1.0
 */
@SpringBootApplication
@MapperScan("com.power.mapper")
public class I18nDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(I18nDemoApplication.class, args);
    }
}
