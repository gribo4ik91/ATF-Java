package com.example.steps.ui.stepDefinitions;

import com.example.elements.IButton;
import com.example.elements.impl.table.Table;

import com.example.pages.PortalLogin;

import com.example.ui.core.browser.Browser;
import com.example.ui.utils.datateble.ManageDataTable;
import com.example.ui.utils.WaitUtils;
import io.cucumber.java.DataTableType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import io.cucumber.datatable.DataTable;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;

import static com.example.ATFAssert.*;
import static java.lang.String.format;
import static org.hamcrest.CoreMatchers.is;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.Map;


@SuppressWarnings("SpringJavaAutowiredMembersInspection")
@CucumberContextConfiguration
@SpringBootTest
@Slf4j
public class UiSteps {

    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    LocalDateTime now = LocalDateTime.now();

    @DataTableType(replaceWithEmptyString = {"-empty-", "-blank-"})
    public DataTable defaultDatatable(DataTable dataTable) {
        return manageDataTable.overrideData(dataTable);
    }

    @Autowired
    private Browser browser;

    @Autowired
    private ManageDataTable manageDataTable;

    @Value("${default.ui.page.timeout}")
    private int defaultUiPageTimeout;

    @Value("${default.ui.timeout}")
    private int defaultUiTimeout;

    @Autowired
    private Environment env;

    @Value("${file.output.path}")
    private String filePath;





    @Given("I am on the {string} page")
    @Given("User is on the {string} page")
    public void userIsOnPage(String pageName) {
        var page = WaitUtils.waitUntilNotNull(() -> browser.findPageByName(pageName), defaultUiPageTimeout);
        assertNotNull(pageName + " page not found or cannot be initiated", page);
        browser.verifyAndSetPage(page);
    }


    @When("I click on {string}")
    @When("User clicks on {string}")
    public void clickOnElement(String elementName) {
        var field = WaitUtils.waitUntilNotNull(() -> (IButton) browser.findElementContainer(elementName), defaultUiTimeout);
        assertNotNull(elementName, field);
        field.click();
    }

//    @Then("I check the following fields for {string} module")
//    public void theFollowingFieldsAreMatched(String moduleName, DataTable dataTable) {
//        var module = browser.findModule(moduleName);
//        assertNotNull(moduleName, module);
//        for (Map<String, String> data : dataTable.asMaps()) {
//            var element = data.get("Fields");
//            var value = data.get("Values");
//            var elementName = module.getElementByName(element);
//
//            assertNotNull(element, elementName.getText());
//            WaitUtils.waitUntilNotNull(() -> (elementName.getText().contains(value)), defaultUiTimeout);
//            assertThat(elementName.getText(), containsString(value));
//        }
//    }
    @Then("^The table '(.*)' (matches|includes|not includes) the following records$")
    public void tableMatchesRecords(String elementName, String status, DataTable dataTable) {
        final var tableComponent = (Table) browser.findElementContainer(elementName);

        switch (status) {
            case "matches":
                manageDataTable.matchesRecordsInTable(tableComponent, dataTable);
                break;
            case "includes":
                manageDataTable.includesRecordsInTable(tableComponent, dataTable);
                break;
            case "not includes":
                manageDataTable.notIncludesRecordsInTable(tableComponent, dataTable);
                break;
            default:
                fail("Unknown status: " + status);
        }
    }

    @Then("The following field is displayed with values")
    @Then("The following fields are displayed with values")
    @Then("The following field(s) should be displayed with values")
    public void theFollowingFieldsShouldBeDisplayedWithValues(DataTable dataTable) {
        for (Map<String, String> row : dataTable.asMaps()) {
            var field = row.get("Fields");
            var value = row.get("Values");
            isDisplayedWithValue(field, value);
        }
    }

    @Then("{} is displayed with the following value: {string}")
    public void isDisplayedWithValue(String component, String value) {
        value = manageDataTable.replaceData(value);
        var element = browser.findElementContainer(component);
        assertThat((format("The %s value does not match with %s", component, value)),
                WaitUtils.waitUntilNotNull(() -> element.getText().replaceAll("\n", ""), defaultUiTimeout), is(value));
    }

    @Then("{} is displayed with value: {string}")
    @Then("{} should be displayed with value: {string}")
    public void waitUntilIsDisplayedWithValue(String component, String value) {
        var element = browser.findElementContainer(component);
        WaitUtils.waitUntilCondition(() -> {
                    try {
                        return element.getWrappedElement().isDisplayed();
                    } catch (Exception e) {
                        return false;
                    }
                },
                true, defaultUiTimeout);
        assertThat(element.getWrappedElement().getText(), is(value));
    }




    @When("User completes the following fields on the page")
    public void completeElementsOnPage( DataTable dataTable) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            for (Map<String, String> row : dataTable.asMaps()) {
                var field = row.get("Fields");
                var value = row.get("Values");


                writer.write(" Tested on: " + dtf.format(now));
                writer.write("Action performed on field: " + field + " with value: " + value);
                writer.newLine();

                var component = browser.findElementContainer(field);
                component.doAction(value);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @When("User navigates to the login page")
    public void navigateToLoginPage() {
        browser.goToUrl(env.getProperty("home.page.url"));
        var loginPage = (PortalLogin) browser.findPageByName("Portal Login");
        browser.verifyAndSetPage(loginPage);

    }
}
