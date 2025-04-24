package com.example.steps.sql.stepDefinitions;

import com.example.api.models.Contact;
import com.example.api.models.User;
import com.example.global.GlobalMap;
import com.example.sql.DbUtils;
import com.example.steps.api.stepDefinitions.APISteps;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import retrofit2.Response;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.example.ATFAssert.assertThat;
import static com.example.global.GlobalMapKey.GENERATED_REQUEST;
import static org.hamcrest.CoreMatchers.is;

@Slf4j
public class SQLSteps {

    private Response<?> response;


    @Autowired
    private DbUtils dBUtils;

    @Autowired
    private APISteps apiSteps;


    private List<Map<String, Object>> results;
    private Map<String, String> selectorInDB;
    private Map<String, String> contactInfo;


    @Given("User query for all users in {string} table")
    public void queryAllInfo(String tableName) {
        results = dBUtils.findAll(tableName);
        log.info("DB result :{}", results);
    }


    @Then("User should get {int} users")
    public void verifyCount(int expected) {
        assertThat(results.size(), is(expected), false);
    }


    @Given("Details from {string} table")
    public void contactDetailsFromTable(String tableName, DataTable dataTable) {
        Object fields;
        selectorInDB = apiSteps.toFieldMap(dataTable);

        if (Objects.equals(tableName, "Accounts")) {
            fields = new User();
        } else {
            fields = new Contact();
        }

        apiSteps.populateFields(fields, selectorInDB);

        GlobalMap.getInstance().put(GENERATED_REQUEST, fields);

        String userEmail = selectorInDB.get("email");
        log.info("User email :{}", userEmail);
        results = dBUtils.findByEmail(tableName, userEmail);

        log.info("DB result from {}: {}", tableName, results);

    }


    @Then("All the pieces of information from {string} are the same")
    public void allThePiecesOfInformationAreTheSame(String tableName) {

        log.info("DB test :{}", mapToUser(results));
        if (Objects.equals(tableName, "Accounts")) {
            apiSteps.compareUserResponse(mapToUser(results));

        } else {
            apiSteps.compareContactResponse(mapToContact(results));
        }

    }


    public static Contact mapToContact(List<Map<String, Object>> data) {
        if (data == null || data.isEmpty()) {
            throw new IllegalArgumentException("List is null or empty");
        }

        Map<String, Object> row = data.get(0); // Берём первую строку

        Contact contact = new Contact();
        contact.setFirstName(getIgnoreCase(row, "firstName"));
        contact.setLastName(getIgnoreCase(row, "lastName"));
        contact.setBirthdate(getIgnoreCase(row, "birthdate"));
        contact.setEmail(getIgnoreCase(row, "email"));
        contact.setPhone(getIgnoreCase(row, "phone"));
        contact.setStreet1(getIgnoreCase(row, "street1"));
        contact.setStreet2(getIgnoreCase(row, "street2"));
        contact.setCity(getIgnoreCase(row, "city"));
        contact.setStateProvince(getIgnoreCase(row, "stateProvince"));
        contact.setPostalCode(getIgnoreCase(row, "postalCode"));
        contact.setCountry(getIgnoreCase(row, "country"));


        return contact;
    }

    public static User mapToUser(List<Map<String, Object>> data) {
        if (data == null || data.isEmpty()) {
            throw new IllegalArgumentException("List is null or empty");
        }

        Map<String, Object> row = data.get(0); // берём первую строку

        // Создаём объект User
        User user = new User();
        user.setFirstName(getIgnoreCase(row, "firstName"));
        user.setLastName(getIgnoreCase(row, "lastName"));
        user.setEmail(getIgnoreCase(row, "email"));
        user.setPassword(getIgnoreCase(row, "password"));

        return user;
    }

    private static String getIgnoreCase(Map<String, Object> map, String key) {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (entry.getKey().equalsIgnoreCase(key)) {
                return entry.getValue() != null ? entry.getValue().toString() : null;
            }
        }
        return null;
    }

}
