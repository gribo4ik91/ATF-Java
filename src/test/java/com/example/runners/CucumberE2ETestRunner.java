package com.example.runners;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        tags = "@e2e",
        glue = "com.example.steps",
        stepNotifications = true,
        plugin = {"pretty", "html:target/cucumber-reports.html", "json:target/cucumber.json"},
        monochrome = true
)

@ContextConfiguration()
public class CucumberE2ETestRunner {
}