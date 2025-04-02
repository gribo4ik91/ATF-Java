package com.example.runners;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;


@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameter(key = "cucumber.glue", value = "com.example.steps")
@ConfigurationParameter(key = "cucumber.filter.tags", value = "@e2e")
@ConfigurationParameter(key = "cucumber.plugin", value =
        "pretty, summary, html:target/cucumber-reports.html, json:target/cucumber.json, io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm")
@ConfigurationParameter(key = "cucumber.execution.monochrome", value = "true")
@ConfigurationParameter(key = "cucumber.execution.step-notifications", value = "true")
public class CucumberE2ETestRunner {
}
