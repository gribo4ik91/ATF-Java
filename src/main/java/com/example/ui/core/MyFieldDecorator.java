package com.example.ui.core;

import com.example.elements.impl.Group;
//import com.example.elements.impl.GroupModule;
import com.example.ui.core.browser.Browser;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WrapsElement;
import org.openqa.selenium.interactions.Locatable;
import org.openqa.selenium.support.pagefactory.DefaultFieldDecorator;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;
import org.openqa.selenium.support.pagefactory.FieldDecorator;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.springframework.cglib.proxy.Callback;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.InvocationHandler;

import java.lang.reflect.Field;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MyFieldDecorator extends DefaultFieldDecorator {


    private final Browser browser;

    private static final int ELEMENT_WAIT_TIMEOUT = 2;

    MyFieldDecorator(Browser browser) {
        super(new MyElementLocatorFactory(browser.getDriver(), ELEMENT_WAIT_TIMEOUT));
        this.browser = browser;
    }

    private MyFieldDecorator(Browser browser, ElementLocatorFactory factory) {
        super(factory);
        this.browser = browser;
    }

    @Override
    public Object decorate(ClassLoader loader, Field field) {
        var fieldType = field.getType();
        if (ElementContainer.class.isAssignableFrom(fieldType)) {
            return decorateContainer(loader, field);
        }
        if (Select.class.isAssignableFrom(fieldType)) {
            return new Select(proxyForLocator(loader, factory.createLocator(field)));
        }
        if (Group.class.isAssignableFrom(fieldType)) {
            return decorateGroup(field);
        }
//        if (Module.class.isAssignableFrom(fieldType)) {
//            return decorateModule(loader, field);
//        }
//        if (GroupModule.class.isAssignableFrom(fieldType)) {
//            return decorateGroupModule(field);
//        }
        return super.decorate(loader, field);
    }

    @Override
    protected WebElement proxyForLocator(final ClassLoader loader, final ElementLocator locator) {
        java.lang.reflect.InvocationHandler handler = new ElementHandler(locator);

        WebElement proxy;
        proxy = (WebElement) Proxy.newProxyInstance(
                loader, new Class[] {WebElement.class, WrapsElement.class, Locatable.class}, handler);
        return proxy;
    }

//    private Object decorateModule(final ClassLoader loader, final Field field) {
//        var element = proxyForLocator(loader, factory.createLocator(field));
//        return buildModule(element, field.getType());
//    }

    private Object decorateContainer(final ClassLoader loader, final Field field) {
        var element = proxyForLocator(loader, factory.createLocator(field));
        try {
            return field.getType()
                    .getDeclaredConstructor(WebElement.class, String.class, Browser.class)
                    .newInstance(element, field.getName(), browser);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private <T extends ElementContainer> Object decorateGroup(final Field field) {
        return Enhancer.create( Group.class, (InvocationHandler) (obj, method, args) -> {
            var elements = factory.createLocator(field).findElements();
            var genericTypeName = ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0].getTypeName();
            List<T> group = elements.stream()
                    .map(el -> {
                        try {
                            return ((Class<T>) Class.forName(genericTypeName))
                                    .getDeclaredConstructor(WebElement.class, String.class, Browser.class)
                                    .newInstance(el, field.getName(), browser);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }).collect(Collectors.toList());
            return method.invoke(new Group<>(group), args);
        });
    }

//    @SuppressWarnings("unchecked")
//    private <T extends Module> Object decorateGroupModule(final Field field) {
//        return Enhancer.create(GroupModule.class, (InvocationHandler) (obj, method, args) -> {
//            var elements = factory.createLocator(field).findElements();
//            var genericTypeName = ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0].getTypeName();
//            ArrayList<T> output = new ArrayList<>();
//            for (var element : elements) {
//                try {
//                    output.add(buildModule(element, (Class<T>) Class.forName(genericTypeName)));
//                } catch (ClassNotFoundException e) {
//                    e.printStackTrace();
//                }
//            }
//            return method.invoke(new GroupModule(output), args);
//        });
//    }

//    private <T> T buildModule(SearchContext searchContext, Class<T> fieldType) {
//        var factory = new MyElementLocatorFactory(searchContext, ELEMENT_WAIT_TIMEOUT);
//        try {
//            return fieldType
//                    .getDeclaredConstructor(FieldDecorator.class)
//                    .newInstance(new MyFieldDecorator(browser, factory));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
}
