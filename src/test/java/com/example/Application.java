package com.example;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


@SpringBootApplication(scanBasePackages = "com.example")
public class Application {

    public static void main(String[] args) {
        // Запуск Spring Boot приложения
        SpringApplication.run(Application.class, args);
    }
}