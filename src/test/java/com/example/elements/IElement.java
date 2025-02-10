package com.example.elements;
/**
 * Common behavior for all elements
 */
public interface IElement {



    String getAttributeValue(String attribute);

    String getText();

    void doAction(String value);
}