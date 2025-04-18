package com.example.steps.api.stepDefinitions;

import com.example.HttpStatus;

import com.example.api.Comparator;
import com.example.api.RetrofitServices;
import com.example.api.models.Contact;
import com.example.api.models.HttpError;
import com.example.api.models.User;
//import com.example.config.CucumberSpringContextConfig;
import com.example.global.GlobalMap;
//import com.example.sql.Queries;
import com.example.sql.DbUtils;
import com.example.ui.utils.datateble.ManageDataTable;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.DataTableType;
import io.cucumber.java.en.*;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import retrofit2.Response;

import java.io.IOException;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.ATFAssert.*;
import static com.example.ATFHTTPAssert.assertStatus;
import static com.example.global.GlobalMapKey.*;
import static java.util.Objects.requireNonNull;
import static org.hamcrest.CoreMatchers.is;


@Slf4j
public class APISteps {


    private Response<?> response;


    @Autowired
    private DbUtils dBUtils;

    @Autowired
    private JdbcTemplate jdbcTemplate;

//    private Queries queries;

    private List<Map<String, Object>> results;
    private Map<String, String> selectorInDB;
    private Map<String, String> contactInfo;
    private User userInserted;


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

    // Обрабатываем несколько вариантов шагов, чтобы покрыть разные формулировки сценариев
    @When("New (contact|user) is created with the following details:$")
    @When("User adds a (contact|user) with the following details:$")
    @When("I add a (contact|user) with the following details:$")
    @When("Add a new (contact|user) with the following details:$")
    public void addContact(String type, DataTable dataTable) throws Exception {
        // Преобразуем DataTable в Map: ключ = имя поля, значение = значение поля
        Map<String, String> fieldMap = toFieldMap(dataTable);

        // Обрабатываем тип — contact или user
        switch (type.toLowerCase()) {
            case "contact" -> {
                Contact contact = new Contact();
                populateFields(contact, fieldMap); // Заполняем поля через reflection

                // Отправляем запрос через retrofit
                var response = retrofit.getAuthorizationService().addContact(contact).execute();

                // Сохраняем объект для последующего использования
                GlobalMap.getInstance().put(GENERATED_REQUEST, contact);

                // Сохраняем и валидируем ответ
                storeHttpResponse(response);
                compareContactResponse(response.body());
            }
            case "user" -> {
                User user = new User();
                populateFields(user, fieldMap); // Заполняем поля через reflection

                var response = retrofit.getAuthorizationServiceForAddUser().addUser(user).execute();
                GlobalMap.getInstance().put(GENERATED_REQUEST, user);

                storeHttpResponse(response);
                compareUserResponse(response.body().getUser());
            }
            default -> fail("Unknown type: " + type); // Защита от неправильных значений
        }
    }

    // Преобразует Cucumber DataTable в обычный Map
// Пример:
// | Fields   | Values  |
// | Email    | test@mail.com |
// -> {"Email": "test@mail.com"}
    public Map<String, String> toFieldMap(DataTable table) {
        return table.asMaps().stream()
                .collect(Collectors.toMap(
                        row -> row.get("Fields"),
                        row -> row.get("Values"),
                        (v1, v2) -> v1 // в случае дубликатов — берём первое значение
                ));
    }

    // Универсальный метод для установки значений полей объекта через reflection
    public void populateFields(Object target, Map<String, String> values) {
        Class<?> clazz = target.getClass();

        values.forEach((fieldName, value) -> {
            try {
                // Находим поле по имени без учёта регистра
                Field field = Arrays.stream(clazz.getDeclaredFields())
                        .filter(f -> f.getName().equalsIgnoreCase(fieldName))
                        .findFirst()
                        .orElse(null);

                if (field != null) {
                    field.setAccessible(true); // Делаем поле доступным для изменения (если оно private)
                    Object typedValue = convertType(field.getType(), value); // Преобразуем строку в нужный тип
                    field.set(target, typedValue); // Устанавливаем значение
                }
            } catch (Exception e) {
                throw new RuntimeException("Failed to set field: " + fieldName, e);
            }
        });
    }

    // Преобразование строки в нужный тип Java
    private Object convertType(Class<?> type, String value) {
        if (type == String.class) return value;
//        if (type == int.class || type == Integer.class) return Integer.parseInt(value);
//        if (type == long.class || type == Long.class) return Long.parseLong(value);
//        if (type == boolean.class || type == Boolean.class) return Boolean.parseBoolean(value);
//        if (type == LocalDate.class) return LocalDate.parse(value); // пример для даты
        // Добавь другие типы, если нужно
        return value; // если тип неизвестен — оставляем как строку
    }




    @When("User is logged in as the new user")
    @And("A new user can log in")
    public void newUserCanLogIn() throws IOException {
        var email = GlobalMap.getInstance().get(GENERATED_EMAIL_API);
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

    @And("The user can be deleted")
    @And("I can delete a new user")
    @And("Delete a new user")
    public void deleteNewUser() throws IOException {

        var deleteResponse = retrofit.getAuthorizationServiceWithNewToken().deleteUser().execute();

        storeHttpResponse(deleteResponse);

    }


    @Then("the response status code should be {string}")
    public void theResponseStatusCodeShouldBe(String responseCode) {
        checkStatusResponseCode(HttpStatus.valueOfString(responseCode));
    }


    @Then("the contact should appear in the contact list")
    public void getContactList() throws IOException {
        response = retrofit.getAuthorizationService().getContactList().execute();
        storeHttpResponse(response);
    }


    @Then("I should see my contact in the list")
    public void iShouldSeeMyContactInTheList() {
        List<Contact> contacts = (List<Contact>) response.body();

        assertNotNull("response", contacts);
        var userEmail = GlobalMap.getInstance().get(GENERATED_EMAIL_API);
        Contact myContactFromResponse = new Contact();
        for (int i = 0; i < contacts.size(); i++) {
            if (contacts.get(i).getEmail().equalsIgnoreCase((String) userEmail)) {
                myContactFromResponse = contacts.get(i);
            }
        }

        compareContactResponse(myContactFromResponse);
        // assertThat("User 'email' field does not match", test3.get, equalToIgnoringCase(user.getEmail()));



    }




    public static String getValueIgnoreCase(Map<String, String> map, String key) {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (entry.getKey().equalsIgnoreCase(key)) {
                return entry.getValue();
            }
        }
        return null; // Возвращаем null, если ключ не найден
    }


    public void checkStatusResponseCode(HttpStatus responseCode) {
        int actualResponseStatus = (int) GlobalMap.getInstance().get(HTTP_RESPONSE_CODE);

        assertStatus(actualResponseStatus, requireNonNull(responseCode));
    }


    public void compareContactResponse(final Contact myContactFromResponse) {
        var request = GlobalMap.getInstance().get(GENERATED_REQUEST);
        auth.compareContact((Contact) request, myContactFromResponse);
    }

    public void compareUserResponse(final User myContactFromResponse) {
        var request = GlobalMap.getInstance().get(GENERATED_REQUEST);
        auth.compareUsers((User) request, myContactFromResponse);
    }


}

