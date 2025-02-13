package com.example.hook;

import com.example.global.GlobalMap;
import com.example.global.data.DataGenerator;
import com.example.ui.utils.datateble.paramreplacers.PlaceholderReplacer;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.DataTableType;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

//import javax.annotation.PostConstruct;

import java.util.stream.Collectors;

import static com.example.global.GlobalMapKey.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Service
@Slf4j
public class Hook {

    @Autowired
    private DataGenerator dataGenerator;

    @Autowired
    private Environment env;



    @PostConstruct
    public void before() {
        // Проверка, что зависимости инжектированы
        assertNotNull(dataGenerator, "DataGenerator не должен быть null");
        assertNotNull(env, "Environment не должен быть null");

      log.info("The @PostConstruct method is executed");

        // Логика инициализации
        var email = dataGenerator.createEmail();
        GlobalMap.getInstance().put(GENERATED_EMAIL, email);
        GlobalMap.getInstance().put(NADA_MAIL, email);
        GlobalMap.getInstance().put(USER_ID,  RandomStringUtils.randomNumeric(25));

        GlobalMap.getInstance().put(PHONE_NUMBER, dataGenerator.createPhoneNumber());
        GlobalMap.getInstance().put(FIRST_NAME, "A" + RandomStringUtils.randomAlphabetic(5));
        GlobalMap.getInstance().put(LAST_NAME, "B" + RandomStringUtils.randomAlphabetic(5));
        GlobalMap.getInstance().put(PASSWORD, env.getProperty("portal.user.password"));
        GlobalMap.getInstance().put(GENERATED_ADDRESS, dataGenerator.createRandomAddress());
        GlobalMap.getInstance().put(GENERATED_STREET, dataGenerator.createRandomAddress().get("Street"));
        GlobalMap.getInstance().put(GENERATED_CITY, dataGenerator.createRandomAddress().get("City"));
        GlobalMap.getInstance().put(GENERATED_BUILDINGNUMBER, dataGenerator.createRandomAddress().get("Building Number"));
        GlobalMap.getInstance().put(GENERATED_STATE, dataGenerator.createRandomAddress().get("State or Province"));
        GlobalMap.getInstance().put(GENERATED_POSTCODE, dataGenerator.createRandomAddress().get("Postcode"));
        GlobalMap.getInstance().put(GENERATED_COUNTRY, dataGenerator.createRandomAddress().get("County"));
    }







}
