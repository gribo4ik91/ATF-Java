package com.example.pages;


import com.example.elements.impl.Button;
import com.example.elements.impl.InputText;
import com.example.elements.impl.Label;
import com.example.ui.core.AbstractPage;
import com.example.ui.core.Page;
import com.example.ui.core.browser.Browser;
import lombok.Getter;
import org.openqa.selenium.support.FindBy;

@Getter
@Page(name = "Contact Details", url = "/contactDetails")
public class ContactDetails  extends AbstractPage {

    @FindBy(id = "logout")
    private Button logout;

    @FindBy(id = "edit-contact")
    private Button editContactButton;

    @FindBy(id = "delete")
    private Button deleteContactButton;

    @FindBy(id = "return")
    private Button returnToContactListButton;

    @FindBy(id = "firstName")
    private Label firstName;

    @FindBy(id = "lastName")
    private Label lastName;

    @FindBy(id = "birthdate")
    private Label birthdate;

    @FindBy(id = "email")
    private Label email;

    @FindBy(id = "phone")
    private Label phone;

    @FindBy(id = "street1")
    private Label street1;

    @FindBy(id = "street2")
    private Label street2;

    @FindBy(id = "city")
    private Label city;

    @FindBy(id = "stateProvince")
    private Label stateOrProvince;

    @FindBy(id = "postalCode")
    private Label postalCode;

    @FindBy(id = "country")
    private Label country;


    public ContactDetails(Browser browser) {
        super(browser);
    }
}
