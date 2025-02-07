package com.example.api;

import com.example.api.models.Contact;
import com.example.api.models.User;

import static com.example.ATFAssert.assertThat;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;

//@SuppressWarnings("SpringJavaAutowiredMembersInspection")
public class Comparator {

//    @Autowired
//    private RetrofitServices retrofit;

    public void compareUsers(User user, User userObjectResponse) {

        assertThat("User 'firstName' field does not match", userObjectResponse.getFirstName(), equalToIgnoringCase(user.getFirstName()));
        assertThat("User 'lastName' field does not match", userObjectResponse.getLastName(), equalToIgnoringCase(user.getLastName()));
        assertThat("User 'email' field does not match", userObjectResponse.getEmail(), equalToIgnoringCase(user.getEmail()));

    }
    public void compareContact(Contact user, Contact userObjectResponse) {

        assertThat("User 'firstName' field does not match", userObjectResponse.getFirstName(), equalToIgnoringCase(user.getFirstName()));
        assertThat("User 'lastName' field does not match", userObjectResponse.getLastName(), equalToIgnoringCase(user.getLastName()));
        assertThat("User 'birthdate' field does not match", userObjectResponse.getBirthdate(), equalToIgnoringCase(user.getBirthdate()));
        assertThat("User 'email' field does not match", userObjectResponse.getEmail(), equalToIgnoringCase(user.getEmail()));
        assertThat("User 'phone' field does not match", userObjectResponse.getPhone(), equalToIgnoringCase(user.getPhone()));
        assertThat("User 'street1' field does not match", userObjectResponse.getStreet1(), equalToIgnoringCase(user.getStreet1()));
        assertThat("User 'street2' field does not match", userObjectResponse.getStreet2(), equalToIgnoringCase(user.getStreet2()));
        assertThat("User 'city' field does not match", userObjectResponse.getCity(), equalToIgnoringCase(user.getCity()));
        assertThat("User 'stateProvince' field does not match", userObjectResponse.getStateProvince(), equalToIgnoringCase(user.getStateProvince()));
        assertThat("User 'postalCode' field does not match", userObjectResponse.getPostalCode(), equalToIgnoringCase(user.getPostalCode()));
    }


}
