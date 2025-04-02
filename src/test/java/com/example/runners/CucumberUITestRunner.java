package com.example.runners;

import org.junit.platform.suite.api.*;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameter(key = "cucumber.glue", value = "com.example.steps")
@IncludeTags("UI")
@ConfigurationParameter(key = "cucumber.plugin", value =
        "pretty, summary, html:target/cucumber-reports.html, json:target/cucumber.json, io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm")
@ConfigurationParameter(key = "cucumber.execution.monochrome", value = "true")
@ConfigurationParameter(key = "cucumber.execution.step-notifications", value = "true")
public class CucumberUITestRunner {
}
