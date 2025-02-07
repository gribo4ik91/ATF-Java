package com.example.steps.api.stepDefinitions;

import com.example.HttpStatus;

import com.example.api.Comparator;
import com.example.api.RetrofitServices;
import com.example.api.models.Contact;
import com.example.api.models.HttpError;
import com.example.api.models.User;
import com.example.global.GlobalMap;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.*;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import retrofit2.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.example.ATFAssert.*;
import static com.example.ATFHTTPAssert.assertStatus;
import static com.example.global.GlobalMapKey.*;
import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.BDDAssumptions.given;

@Slf4j
public class APISteps {

    private Response<?> response;


    @Autowired
    private RetrofitServices retrofit;
    @Autowired
    private Comparator auth;

    @Autowired
    private ObjectMapper objectMapper;

    protected void storeHttpResponse(Response<?> response) throws IOException {
        GlobalMap.getInstance().put(HTTP_RESPONSE_CODE, response.code());
        GlobalMap.getInstance().put(HTTP_RESPONSE_BODY, response.body());
        GlobalMap.getInstance().put(HTTP_RESPONSE_ERROR_BODY, checkForHttpError(response));
    }

    private HttpError checkForHttpError(Response<?> response) throws IOException {
        if (Objects.nonNull(response.errorBody())) {
            String errorBody = response.errorBody().string();
            log.warn(errorBody);

            try {
                return objectMapper.readValue(errorBody, HttpError.class);
            } catch (IOException e) {
                return HttpError.builder().message(errorBody).build();
            }
        }
        return null;
    }

    @When("I add a (contact|user) with the following details:$")
    public void addContact(String type, DataTable dataTable) throws IOException {
        Contact contact = new Contact();
        User user = new User();
        Map<String, String> map = new HashMap<>();
        for (Map<String, String> row : dataTable.asMaps()) {
            var field = row.get("Fields");
            var value = row.get("Values");

            map.put(field, value);
        }
        switch (type) {
            case "contact":
                contact.setFirstName(getValueIgnoreCase(map, "FirstName"));
                contact.setLastName(getValueIgnoreCase(map, "LastName"));
                contact.setBirthdate(getValueIgnoreCase(map, "Birthdate"));
                contact.setEmail(getValueIgnoreCase(map, "Email"));
                contact.setPhone(getValueIgnoreCase(map, "Phone"));
                contact.setStreet1(getValueIgnoreCase(map, "Street1"));
                contact.setStreet2(getValueIgnoreCase(map, "Street2"));
                contact.setCity(getValueIgnoreCase(map, "City"));
                contact.setStateProvince(getValueIgnoreCase(map, "StateProvince"));
                contact.setPostalCode(getValueIgnoreCase(map, "PostalCode"));
                contact.setCountry(getValueIgnoreCase(map, "Country"));

                var response = retrofit.getAuthorizationService().addContact(contact).execute();
                GlobalMap.getInstance().put(GENERATED_REQUEST, contact);

                storeHttpResponse(response);
                compareContactResponse(response.body());

                break;
            case "user":
                user.setFirstName(getValueIgnoreCase(map, "FirstName"));
                user.setLastName(getValueIgnoreCase(map, "LastName"));
                user.setEmail(getValueIgnoreCase(map, "Email"));
                user.setPassword(getValueIgnoreCase(map, "Password"));

                var userResponse = retrofit.getAuthorizationServiceForAddUser().addUser(user).execute();
                GlobalMap.getInstance().put(GENERATED_REQUEST, user);

                storeHttpResponse(userResponse);
                compareUserResponse(userResponse.body().getUser());
                break;
            default:
                fail("Unknown type: " + type);
        }
    }

    @When("I logged in with the new user")
    @And("New user can log in")
    public void newUserCanLogIn() throws IOException {
        var email = GlobalMap.getInstance().get(GENERATED_EMAIL);
        var password = GlobalMap.getInstance().get(PASSWORD);

        User newUser = new User();
        newUser.setEmail(String.valueOf(email));
        newUser.setPassword(String.valueOf(password));
        var logInResponse = retrofit.getAuthorizationService().loginUser(newUser).execute();

        storeHttpResponse(logInResponse);

    }

    @And("I can logout successfully with the new user")
    @And("New user can log out")
    public void newUserCanLogOut() throws IOException {

        var logOutResponse = retrofit.getAuthorizationServiceWithNewToken().logoutUser().execute();

        storeHttpResponse(logOutResponse);

    }

    @And("I can delete a new user")
    @And("Delete a new user")
    public void deleteNewUser() throws IOException {

        var deleteResponse = retrofit.getAuthorizationServiceWithNewToken().deleteUser().execute();

        storeHttpResponse(deleteResponse);

    }

    public static String getValueIgnoreCase(Map<String, String> map, String key) {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (entry.getKey().equalsIgnoreCase(key)) {
                return entry.getValue();
            }
        }
        return null; // Возвращаем null, если ключ не найден
    }

    @Then("the response status code should be {string}")
    public void theResponseStatusCodeShouldBe(String responseCode) {
        checkStatusResponseCode(HttpStatus.valueOfString(responseCode));
    }

    public void checkStatusResponseCode(HttpStatus responseCode) {
        int actualResponseStatus = (int) GlobalMap.getInstance().get(HTTP_RESPONSE_CODE);

        assertStatus(actualResponseStatus, requireNonNull(responseCode));
    }

    @Then("the contact should appear in the contact list")
    public void getContactList() throws IOException {
        response = retrofit.getAuthorizationService().getContactList().execute();
        storeHttpResponse(response);
    }


    private void compareContactResponse(final Contact myContactFromResponse) {
        var request = GlobalMap.getInstance().get(GENERATED_REQUEST);
        //  User authenticatedUser = GlobalMap.getInstance().getAs(HTTP_RESPONSE_BODY, User.class);
        auth.compareContact((Contact) request, myContactFromResponse);
    }

    private void compareUserResponse(final User myContactFromResponse) {
        var request = GlobalMap.getInstance().get(GENERATED_REQUEST);
        //  User authenticatedUser = GlobalMap.getInstance().getAs(HTTP_RESPONSE_BODY, User.class);
        auth.compareUsers((User) request, myContactFromResponse);
    }

    @Then("I should see my contact in the list")
    public void iShouldSeeMyContactInTheList() throws IOException {
        List<Contact> contacts = (List<Contact>) response.body();
//        assert contacts != null && !contacts.isEmpty();
//        var request = GlobalMap.getInstance().get(GENERATED_REQUEST);
        var userEmail = GlobalMap.getInstance().get(GENERATED_EMAIL);
        Contact myContactFromResponse = new Contact();
        for (int i = 0; i < contacts.size(); i++) {
            if (contacts.get(i).getEmail().equalsIgnoreCase((String) userEmail)) {
                myContactFromResponse = contacts.get(i);
            }
        }

        compareContactResponse(myContactFromResponse);
        // assertThat("User 'email' field does not match", test3.get, equalToIgnoringCase(user.getEmail()));

        assertNotNull("response", contacts);

    }

}

