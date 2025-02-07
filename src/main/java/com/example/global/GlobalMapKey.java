package com.example.global;

import lombok.Getter;

import java.util.Arrays;


public enum GlobalMapKey {


    EMAIL_PREFIX(""),
    EMAIL_DOMAIN(""),
    NADA_MAIL(""),
    GENERATED_EMAIL(""),

    PHONE_NUMBER(""),
    PASSWORD(""),
    FIRST_NAME(""),
    LAST_NAME(""),


    GENERATED_POSTCODE(""),
    GENERATED_BUILDINGNUMBER(""),
    GENERATED_STREET(""),
    GENERATED_CITY(""),

//    OWNER(""),

    GENERATED_COUNTRY(""),
    GENERATED_STATE(""),


    USER_ID(""),


    HTTP_RESPONSE_CODE(""),
    HTTP_RESPONSE_BODY(""),
    HTTP_RESPONSE_ERROR_BODY(""),
    GENERATED_REQUEST(""),
    GENERATED_ADDRESS("");



    private String description;

    GlobalMapKey(String description) {
        this.description = description;
    }

//    public static GlobalMapKey getByDescription(String description) {
//        return Arrays.stream(GlobalMapKey.values()).filter(d -> d.getDescription().equals(description)).findFirst().orElse(null);
//    }
public String getDescription() {
    return description;
}
    public static GlobalMapKey getByName(String name) {
        return Arrays.stream(GlobalMapKey.values()).filter(d -> d.name().equals(name)).findFirst().orElse(null);
    }
//    public static GlobalMapKey getByDescription(String description) {
//        return Arrays.stream(GlobalMapKey.values()).filter(d -> d.getDescription().equals(description)).findFirst().orElse(null);
//    }
}
