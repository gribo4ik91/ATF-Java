package com.example.elements.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ForkJoinPool;

import static java.util.stream.Collectors.toList;

import com.example.ui.core.ElementContainer;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.Quotes;

import com.google.common.collect.Lists;


import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor
public class Group<T extends ElementContainer> {

    public static final int SUBLIST_SIZE = 20;

    private List<T> elements;

    public Group(List<T> elements) {
        this.elements = elements;
    }

    public T get(int index) {
        return elements.get(index);
    }


    public List<T> getAll() {
        return elements;
    }

    public List<String> getTextFromGroupComponent() {
        return elements.stream().map(e -> e.getWrappedElement().getText().trim()).collect(toList());
    }

}
