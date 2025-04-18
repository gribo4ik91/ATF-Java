package com.example.config;


import com.example.api.Comparator;
import com.example.api.RetrofitServices;
import com.example.global.data.DataGenerator;
import com.example.ui.core.browser.Browser;
import com.example.ui.utils.datateble.ManageDataTable;
import com.example.ui.utils.datateble.paramreplacers.PlaceholderReplacer;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.*;

@ComponentScan(excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = Browser.class))
@Configuration
@Lazy
@PropertySource(ignoreResourceNotFound = true, value = {
        "classpath:env/application.properties"
})
public class BeansConfig {


    @Bean
    public Browser browser(ObjectProvider<WebDriver> webDriverProvider) {
        return new Browser(webDriverProvider);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true)
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .findAndRegisterModules();
    }

    @Bean
    public WebDriverConfig webDriverConfig() {

        return new WebDriverConfig();
    }



    @Bean
    public WebDriver driver(WebDriverConfig webDriverConfig) {
            return webDriverConfig.driver();
    }

    @Bean
    public ManageDataTable manageDataTable() {
        return new ManageDataTable();
    }

    @Bean
    public DataGenerator dataGenerate() {
        return new DataGenerator();
    }

    @Bean
    public PlaceholderReplacer hashParamReplacer() {
        return new PlaceholderReplacer();  }

    @Bean
    public RetrofitServices retrofitServices() {
        return new RetrofitServices();
    }

    @Bean
    public Comparator integrationAuth() {
        return new Comparator();
    }
}
