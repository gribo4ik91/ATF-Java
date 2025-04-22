package com.example;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.assertj.core.api.ErrorCollector;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Slf4j
public class ATFSoftAssert extends ErrorCollector {


    @SuppressWarnings("unchecked")
    private List<Throwable> getErrors() {
        try {
            return (List<Throwable>) FieldUtils.readField(this, "errors", true);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void assertAll() {
        List<Throwable> collectedErrors = getErrors();

        if (!CollectionUtils.isEmpty(collectedErrors)) {
            StringBuilder sb = new StringBuilder(
                    String.format("There were %d errors:", collectedErrors.size()));

            collectedErrors.forEach(assertError -> {
                sb.append(String.format("\n  %s(%s)", assertError.getClass().getName(), assertError.getMessage()));
                log.error(assertError.getMessage());
            });
            throw new AssertionError(sb.toString());
        }
    }
}