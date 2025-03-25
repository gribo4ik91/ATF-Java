package com.example.ui.pages;

import com.example.ui.elements.impl.Button;
import com.example.ui.elements.impl.table.Table;
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

    @FindBy(className = "contactTable")
    private Table contactTable;

    public ContactList(Browser browser) {
        super(browser);
    }
}
