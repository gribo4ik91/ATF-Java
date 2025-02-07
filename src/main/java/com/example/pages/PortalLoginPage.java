package com.example.pages;

import com.example.elements.impl.InputText;
import com.example.elements.impl.Button;
import com.example.elements.impl.Span;
import com.example.ui.core.AbstractPage;
import com.example.ui.core.FindByName;
import com.example.ui.core.Page;
import com.example.ui.core.browser.Browser;

import lombok.Getter;

import net.datafaker.providers.base.Text;
import org.openqa.selenium.support.FindBy;


import java.awt.*;

@Getter
@Page(name = "Portal Login Page", url = "")
public class PortalLoginPage extends AbstractPage {

    @FindBy(className = "login_logo")
    private Label loginLogo;

//    @FindBy(xpath = "/html/body/div[3]/span")
    @FindBy(id = "error")
    private Span errorMessage;



//    @FindByName(name = "Incorrect username or password")
//    @FindBy(id = "error")
//    private Span errorMessage;

    @FindBy(id = "email")
    private InputText username;

    @FindBy(id = "password")
    private InputText password;

    @FindBy(id = "submit")
    private Button submitButton;

    @FindBy(id = "signup")
    private Button signUpButton;

    public PortalLoginPage(Browser browser) {
        super(browser);
    }
}
