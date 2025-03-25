package com.example.ui.elements.impl.table;

import com.example.ui.action.ClickAction;
import com.example.ui.action.GetTextAction;
//import com.example.ui.elements.IButton;
import com.example.ui.elements.impl.Group;
import com.example.ui.core.ElementContainer;
import com.example.ui.core.browser.Browser;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Collectors;



public class Cell  extends ElementContainer  {

    private final Lazy<Group<Cell>> subCellGroupComponent = new Lazy<>(() -> new Group<>(
            getWrappedElement().findElements(By.xpath("./td[not(@hidden='true')]"))
                    .stream()
                    .map(el -> new Cell(el, "", browser)).collect(Collectors.toList())));

    public Cell(final WebElement element, final String name, final Browser browser) {
        super(element, name, browser);
    }

//    @Override
    public void click() {
        new ClickAction(this, browser).execute();
    }

    @Override
    public String getText() {
        return new GetTextAction(this, browser).execute().replace("\n", " ").strip();
    }

    public List<String> getSubCellTexts() {
        return subCellGroupComponent.get().getAll()
                .stream()
                .map(Cell::getText) // Используем метод `getText()`, чтобы получать чистый текст
                .collect(Collectors.toList());
    }


}
