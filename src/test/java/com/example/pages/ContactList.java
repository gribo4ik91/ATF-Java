package com.example.pages;

import com.example.elements.impl.Button;
import com.example.elements.impl.InputText;
import com.example.elements.impl.table.Table;
import com.example.ui.core.AbstractPage;
import com.example.ui.core.Page;
import com.example.ui.core.browser.Browser;
import lombok.Getter;
import org.openqa.selenium.support.FindBy;

import java.awt.*;

@Getter
@Page(name = "Contact List", url = "/contactList")
public class ContactList extends AbstractPage {


    @FindBy(className = "main-content")
    private Label loginLogo;

    @FindBy(id = "logout")
    private Button logout;

    @FindBy(id = "add-contact")
    private Button addNewContactButton;

    @FindBy(className = "contactTableBodyRow")
    private Button contactDetailsButton;

//    @FindBy(id = "myTable")
    @FindBy(className = "contactTable")
    private Table contactTable;


//    @FindBy(id = "submit")
//    private com.example.elements.impl.Button submitButton;
//
//    @FindBy(id = "signup")
//    private Button sign_up_Button;

    public ContactList(Browser browser) {
        super(browser);
    }
}
