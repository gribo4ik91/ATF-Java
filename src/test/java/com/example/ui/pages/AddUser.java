package com.example.ui.pages;

import com.example.ui.elements.impl.Button;
import com.example.ui.elements.impl.InputText;
import com.example.ui.core.AbstractPage;
import com.example.ui.core.Page;
import com.example.ui.core.browser.Browser;
import lombok.Getter;
import org.openqa.selenium.support.FindBy;


@Getter
@Page(name = "Add User", url = "/addUser")
public class AddUser extends AbstractPage {

    @FindBy(id = "firstName")
    private InputText firstName;

    @FindBy(id = "lastName")
    private InputText lastName;

    @FindBy(id = "email")
    private InputText email;

    @FindBy(id = "password")
    private InputText password;

    @FindBy(id = "submit")
    private Button submitButton;

    @FindBy(id = "cancel")
    private Button cancelButton;

    public AddUser(Browser browser) {
        super(browser);
    }
}
