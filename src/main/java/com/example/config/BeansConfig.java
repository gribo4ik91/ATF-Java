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
import org.springframework.context.annotation.*;

@ComponentScan(excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = Browser.class))
@Configuration
@Lazy
@PropertySource(ignoreResourceNotFound = true, value = {
        "classpath:env/application.properties",
        "classpath:properties/atf.properties",
        "classpath:roles/user.roles.properties"
})
public class BeansConfig {


    @Bean
    public Browser browser() {
        return new Browser();
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


//    private boolean isApiTest() {
//        // Логика для проверки, что это API тест
//        return System.getProperty("test.type", "UI").equalsIgnoreCase("API");
////        return System.getgetProperty("test.type", "ui").equals("api");
//    }

    @Bean
    @Scope("singleton")
    public WebDriver driver(WebDriverConfig webDriverConfig) {
//        if (isApiTest()) {
//            return null;
//        }
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
