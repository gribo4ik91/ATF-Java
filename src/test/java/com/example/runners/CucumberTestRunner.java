package com.example.runners;

import org.junit.platform.suite.api.*;



@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameter(key = "cucumber.glue", value = "com.example.hook, com.example.steps")
@ExcludeTags("ignore")
@ConfigurationParameter(key = "cucumber.plugin", value =
        "pretty, summary, com.example.hook.listeners.CucumberAllureStepListener, io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm")
@ConfigurationParameter(key = "cucumber.execution.monochrome", value = "true")
@ConfigurationParameter(key = "cucumber.execution.step-notifications", value = "true")
public class CucumberTestRunner {
}
