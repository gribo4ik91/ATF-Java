package com.example.api;

import com.example.api.models.Contact;
import com.example.api.models.User;

import static com.example.ATFAssert.assertAll;
import static com.example.ATFAssert.assertThat;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;


public class Comparator {



    public void compareUsers(User user, User userObjectResponse) {

        assertThat("User 'firstName' field does not match", userObjectResponse.getFirstName(), equalToIgnoringCase(user.getFirstName()), true);
        assertThat("User 'lastName' field does not match", userObjectResponse.getLastName(), equalToIgnoringCase(user.getLastName()), true);
        assertThat("User 'email' field does not match", userObjectResponse.getEmail(), equalToIgnoringCase(user.getEmail()), true);
assertAll();
    }
    public void compareContact(Contact user, Contact userObjectResponse) {

        assertThat("User 'firstName' field does not match", userObjectResponse.getFirstName(), equalToIgnoringCase(user.getFirstName()), true);
        assertThat("User 'lastName' field does not match", userObjectResponse.getLastName(), equalToIgnoringCase(user.getLastName()), true);
        assertThat("User 'birthdate' field does not match", userObjectResponse.getBirthdate(), equalToIgnoringCase(user.getBirthdate()), true);
        assertThat("User 'email' field does not match", userObjectResponse.getEmail(), equalToIgnoringCase(user.getEmail()), true);
        assertThat("User 'phone' field does not match", userObjectResponse.getPhone(), equalToIgnoringCase(user.getPhone()), true);
        assertThat("User 'street1' field does not match", userObjectResponse.getStreet1(), equalToIgnoringCase(user.getStreet1()), true);
        assertThat("User 'street2' field does not match", userObjectResponse.getStreet2(), equalToIgnoringCase(user.getStreet2()), true);
        assertThat("User 'city' field does not match", userObjectResponse.getCity(), equalToIgnoringCase(user.getCity()), true);
        assertThat("User 'stateProvince' field does not match", userObjectResponse.getStateProvince(), equalToIgnoringCase(user.getStateProvince()), true);
        assertThat("User 'postalCode' field does not match", userObjectResponse.getPostalCode(), equalToIgnoringCase(user.getPostalCode()), true);
   assertAll();
    }


}
