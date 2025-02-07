package com.example;

//import com.example.config.UserRoleConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

//@EnableConfigurationProperties(UserRoleConfig.class)
@SpringBootApplication(scanBasePackages = "com.example")
public class Application {

    public static void main(String[] args) {
        // Запуск Spring Boot приложения
        SpringApplication.run(Application.class, args);
    }
}