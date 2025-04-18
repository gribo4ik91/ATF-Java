package com.example.hook;
import com.example.global.GlobalMap;
import com.example.global.data.DataGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.example.global.GlobalMapKey.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Service
@RequiredArgsConstructor
@Slf4j
public class TestDataInitializer {

    private final DataGenerator dataGenerator;
    private final Environment env;

    public void initializeGlobalMap() {
        assertNotNull(dataGenerator, "DataGenerator must not be null");
        assertNotNull(env, "Environment must not be null");

        log.info("Initializing test data into GlobalMap");

        var email = dataGenerator.createEmail();
        var address = dataGenerator.createRandomAddress();

        GlobalMap.getInstance().put(GENERATED_EMAIL_API, "api-"+email);
        GlobalMap.getInstance().put(GENERATED_EMAIL_UI, "ui-"+email);
        GlobalMap.getInstance().put(NADA_MAIL, email);
        GlobalMap.getInstance().put(USER_ID, RandomStringUtils.randomNumeric(25));
        GlobalMap.getInstance().put(PHONE_NUMBER, dataGenerator.createPhoneNumber());
        GlobalMap.getInstance().put(FIRST_NAME, "A" + RandomStringUtils.randomAlphabetic(5));
        GlobalMap.getInstance().put(LAST_NAME, "B" + RandomStringUtils.randomAlphabetic(5));
        GlobalMap.getInstance().put(PASSWORD, env.getProperty("portal.user.password"));

        GlobalMap.getInstance().put(GENERATED_ADDRESS, address);
        GlobalMap.getInstance().put(GENERATED_STREET, address.get("Street"));
        GlobalMap.getInstance().put(GENERATED_CITY, address.get("City"));
        GlobalMap.getInstance().put(GENERATED_BUILDINGNUMBER, address.get("Building Number"));
        GlobalMap.getInstance().put(GENERATED_STATE, address.get("State or Province"));
        GlobalMap.getInstance().put(GENERATED_POSTCODE, address.get("Postcode"));
        GlobalMap.getInstance().put(GENERATED_COUNTRY, address.get("County"));
    }
}