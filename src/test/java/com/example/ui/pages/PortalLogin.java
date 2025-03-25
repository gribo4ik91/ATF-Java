package com.example.ui.pages;

import com.example.ui.elements.impl.InputText;
import com.example.ui.elements.impl.Button;
import com.example.ui.elements.impl.Span;
import com.example.ui.core.AbstractPage;
import com.example.ui.core.Page;
import com.example.ui.core.browser.Browser;

import lombok.Getter;
import org.openqa.selenium.support.FindBy;

import java.awt.*;

@Getter
@Page(name = "Portal Login", url = "")
public class PortalLogin extends AbstractPage {

    @FindBy(className = "login_logo")
    private Label loginLogo;

    @FindBy(id = "error")
    private Span errorMessage;


    @FindBy(id = "email")
    private InputText username;

    @FindBy(id = "password")
    private InputText password;

    @FindBy(id = "submit")
    private Button submitButton;

    @FindBy(id = "signup")
    private Button signUpButton;

    public PortalLogin(Browser browser) {
        super(browser);
    }
}
