package com.example.global.data;

import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Value;
import org.apache.commons.lang3.RandomUtils;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class DataGenerator {

    @Value("${domain}")
    private String domain;


    public String createEmail() {
        return createEmail("test", DateTimeFormatter.ofPattern("ddMMyyHHmmss"));
    }


    public String createEmail(String environmentPrefix, DateTimeFormatter dateTimeFormat) {
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



}
