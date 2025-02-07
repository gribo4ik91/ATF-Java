package com.example.elements.impl.table;

import com.example.action.ClickAction;
import com.example.action.GetTextAction;
import com.example.elements.IButton;
import com.example.elements.impl.Group;
import com.example.ui.core.ElementContainer;
import com.example.ui.core.browser.Browser;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class Cell  extends ElementContainer implements IButton {

    private Supplier<Group<Cell>> subCellGroupComponent = () -> new Group<>(
            getWrappedElement().findElements(By.xpath("./td[not(@hidden='true')]"))
                    .stream()
                    .map(el -> new Cell(el, "", browser)).collect(Collectors.toList()));

    public Cell(final WebElement element, final String name, final Browser browser) {
        super(element, name, browser);
    }

    @Override
    public void click() {
        new ClickAction(this, browser).execute();
    }

    @Override
    public String getText() {
        String text = new GetTextAction(this, browser).execute();
        text = text.replaceAll("\\r|\\n|\\r\\n", " ").strip();
        return text;
    }

    public List<String> getSubCellTexts() {
        return subCellGroupComponent.get().getAll()
                .stream()
                .map(el -> el.getWrappedElement().getText())
                .collect(toList());
    }


}
