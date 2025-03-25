package com.example.ui.pages;

import com.example.ui.elements.impl.Button;
import com.example.ui.elements.impl.InputText;
import com.example.ui.elements.impl.Span;
import com.example.ui.core.AbstractPage;
import com.example.ui.core.Page;
import com.example.ui.core.browser.Browser;
import lombok.Getter;
import org.openqa.selenium.support.FindBy;

@Getter
@Page(name = "Add Contact", url = "/addContact")
public class AddContact extends AbstractPage {

    @FindBy(id = "firstName")
    private InputText firstName;

    @FindBy(id = "lastName")
    private InputText lastName;

    @FindBy(id = "birthdate")
    private InputText birthdate;

    @FindBy(id = "email")
    private InputText email;

    @FindBy(id = "phone")
    private InputText phone;

    @FindBy(id = "street1")
    private InputText street1;

    @FindBy(id = "street2")
    private InputText street2;

    @FindBy(id = "city")
    private InputText city;

    @FindBy(id = "stateProvince")
    private InputText stateOrProvince;

    @FindBy(id = "postalCode")
    private InputText postalCode;

    @FindBy(id = "country")
    private InputText country;

    @FindBy(id = "submit")
    private Button submitButton;

    @FindBy(id = "cancel")
    private Button cancelButton;

    @FindBy(id = "logout")
    private Button logoutButton;

    @FindBy(id = "error")
    private Span errorMessage;

    public AddContact(Browser browser) {
        super(browser);
    }
}
