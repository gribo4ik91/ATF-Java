package com.example.config;


import io.github.bonigarcia.wdm.WebDriverManager;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import dev.failsafe.Failsafe;
import dev.failsafe.RetryPolicy;


import java.time.Duration;


public class WebDriverConfig {

    private static final Logger logger = LoggerFactory.getLogger(WebDriverConfig.class);

    public WebDriver driver() {

            logger.info("Creating WebDriver bean");

            RetryPolicy<Object> retryPolicy = RetryPolicy.builder()
                    .withDelay(Duration.ofSeconds(2))
                    .withMaxRetries(3)
                    .build();
            ChromeOptions options = new ChromeOptions();
//        options.addArguments("--window-size=1920,1080");
            options.addArguments("--start-maximized");

            // WebDriver setup with retry policy
            return Failsafe.with(retryPolicy).get(() -> {
                WebDriverManager.chromedriver().setup();
                return new ChromeDriver(options);
            });

        }

}
