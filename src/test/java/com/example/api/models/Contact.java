package com.example.api.models;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Contact {
    // Getters and Setters
    private String firstName;
    private String lastName;
    private String birthdate;
    private String email;
    private String phone;
    private String street1;
    private String street2;
    private String city;
    private String stateProvince;
    private String postalCode;
    private String country;
    private String owner;


    public Contact() {
    }

    public Contact( String firstName, String lastName, String birthdate, String email,
                   String phone, String street1, String street2, String city,
                   String stateProvince, String postalCode, String country, String owner) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.birthdate = birthdate;
        this.email = email;
        this.phone = phone;
        this.street1 = street1;
        this.street2 = street2;
        this.city = city;
        this.stateProvince = stateProvince;
        this.postalCode = postalCode;
        this.country = country;
        this.owner = owner;

    }

}
