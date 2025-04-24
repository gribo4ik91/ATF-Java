package com.example.steps.ui.stepDefinitions;

import com.example.ui.core.browser.Browser;
import com.example.ui.elements.impl.Button;
import com.example.ui.elements.impl.table.Table;
import com.example.ui.pages.PortalLogin;
import com.example.ui.utils.WaitUtils;
import com.example.ui.utils.datateble.ManageDataTable;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.DataTableType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;

import java.util.Map;

import static com.example.ATFAssert.*;
import static java.lang.String.format;
import static org.hamcrest.CoreMatchers.is;


@CucumberContextConfiguration
@SpringBootTest
@Slf4j
public class UiSteps {


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


    @Given("I am on the {string} page")
    @Given("User is on the {string} page")
    public void userIsOnPage(String pageName) {
        var page = WaitUtils.waitUntilNotNull(() -> browser.findPageByName(pageName), defaultUiPageTimeout);
        assertNotNull(pageName + " page was founded successfully ", page);
        browser.verifyAndSetPage(page);
    }


    @When("I click on {string}")
    @When("User clicks on {string}")
    public void clickOnElement(String elementName) {
        var field = WaitUtils.waitUntilNotNull(() -> (Button) browser.findElementContainer(elementName), defaultUiTimeout);
        assertNotNull(elementName, field);
        field.click();
    }


    @When("User clicks on {string} and is redirected to the {string} page")
    public void clickOnElementAndRederect(String elementName, String pageName) {
        var field = WaitUtils.waitUntilNotNull(() -> (Button) browser.findElementContainer(elementName), defaultUiTimeout);
        assertNotNull(elementName, field);
        field.click();


        var page = WaitUtils.waitUntilNotNull(() -> browser.findPageByName(pageName), defaultUiPageTimeout);
        assertNotNull(pageName + " page was founded successfully ", page);
        browser.setPage(page);
    }

    @Then("^The table '(.*)' (includes|not includes) the following records$")
    public void tableMatchesRecords(String elementName, String status, DataTable dataTable) {
        final var tableComponent = (Table) browser.findElementContainer(elementName);

        switch (status) {

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
                WaitUtils.waitUntilNotNull(() -> element.getText().replaceAll("\n", ""), defaultUiTimeout), is(value), false);
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
        }, true, defaultUiTimeout);
        assertThat(element.getWrappedElement().getText(), is(value), false);
    }


    @When("User completes the following fields on the page")
    public void completeElementsOnPage(DataTable dataTable) {

        for (Map<String, String> row : dataTable.asMaps()) {
            var field = row.get("Fields");
            var value = row.get("Values");
            var component = browser.findElementContainer(field);
            component.doAction(value);

        }
    }

    @When("User navigates to the login page")
    public void navigateToLoginPage() {
        browser.goToUrl(env.getProperty("home.page.url"));
        var loginPage = (PortalLogin) browser.findPageByName("Portal Login");
        browser.verifyAndSetPage(loginPage);

    }
}
