package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication(scanBasePackages = "com.example")
public class Application {

    public static void main(String[] args) {
        // Запуск Spring Boot приложения
        SpringApplication.run(Application.class, args);
    }
}