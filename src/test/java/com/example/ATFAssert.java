package com.example;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Assertions;


@Slf4j
public class ATFAssert {

    final static String DEFAULT_FAILURE_MESSAGE = "The actual value doesn't match the expected value";
    private final static String MESSAGE_EXPECTED_ACTUAL = "{}{} - expected: {}{} - actual: {}";

    public static void assertNotNull(String fieldName, Object obj) {
        Assertions.assertNotNull(obj, "Unexpected null for: " + fieldName);
    }

    public static <T> void assertThat(String message, T actual, Matcher<? super T> matcher) {
        assertThat("", message, actual, matcher);
    }

    public static <T> void assertThat(String longMessage, String failureMessage, T actual, Matcher<? super T> matcher) {
        String fullMessage = "Assert that " + longMessage;
        log.info(MESSAGE_EXPECTED_ACTUAL, fullMessage, System.lineSeparator(), matcher.toString(), System.lineSeparator(), actual);
        if (StringUtils.isEmpty(failureMessage)) {
            MatcherAssert.assertThat(actual, matcher);
        } else {
            MatcherAssert.assertThat(failureMessage, actual, matcher);
        }
    }

    public static void fail(String failureMessage) {
        if (StringUtils.isBlank(failureMessage)) {
            failureMessage = DEFAULT_FAILURE_MESSAGE;
        }

        Assertions.fail(failureMessage);

    }

    public static <T> void assertThat(T actual, Matcher<? super T> matcher) {
        assertThat(DEFAULT_FAILURE_MESSAGE, actual, matcher);
    }
}
