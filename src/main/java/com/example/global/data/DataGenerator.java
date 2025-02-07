package com.example.global.data;

import com.example.global.GlobalMap;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Value;
import org.apache.commons.lang3.RandomUtils;
//import com.github.javafaker.Faker;
//import com.github.javafaker.Faker;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.example.global.GlobalMapKey.EMAIL_DOMAIN;
import static com.example.global.GlobalMapKey.EMAIL_PREFIX;

public class DataGenerator {

    @Value("${domain}")
    private String domain;


    public String createEmail(EnvironmentPrefix environmentPrefix) {
        return createEmail(environmentPrefix, DateTimeFormatter.ofPattern("ddMMyyHHmmss"));
    }


    public String createEmail(EnvironmentPrefix environmentPrefix, DateTimeFormatter dateTimeFormat) {
        LocalDateTime now = LocalDateTime.now();
        final String emailPrefix = String.format("%s%s", environmentPrefix, now.format(dateTimeFormat));

        return String.format("%s@%s", emailPrefix, domain);
    }


    public String createPhoneNumber() {
        DecimalFormat df = new DecimalFormat("00000000000");
        return df.format(RandomUtils.nextLong()).substring(0, 11);
    }

    public Map<String, String> createRandomAddress() {
        String[] randomCounty = {"Cumbria", "Cumberland", "Cornwall", "Cleveland", "Bedfordshire", "East Sussex"};

        Faker ukFake = new Faker(Locale.UK);
        Map<String, String> address = new HashMap<>();
        address.put("Postcode", ukFake.address().zipCode());
        address.put("Building Number", ukFake.address().buildingNumber());
        address.put("Street", ukFake.address().streetName());
        address.put("City", ukFake.address().city());
        address.put("State or Province", ukFake.address().state());
        address.put("County", randomCounty[RandomUtils.nextInt(0, randomCounty.length - 1)]);

        return address;
    }


    public enum EnvironmentPrefix {
        PORTAL("portal"),
        REST("api");

        private String env;

        EnvironmentPrefix(String env) {
            this.env = env;
        }

        public String toString() {
            return env;
        }
    }


}
