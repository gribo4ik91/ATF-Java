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
//package com.example;
//
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
//import org.hamcrest.Matcher;
//import org.hamcrest.MatcherAssert;
//import org.junit.jupiter.api.Assertions;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Slf4j
//public class ATFAssert {
//
//    private final static String DEFAULT_FAILURE_MESSAGE = "The actual value doesn't match the expected value";
//    private final static String MESSAGE_EXPECTED_ACTUAL = "{}{} - expected: {}{} - actual: {}";
//
//    private static final ThreadLocal<List<AssertionError>> softErrors = ThreadLocal.withInitial(ArrayList::new);
//
//    public static void assertNotNull(String fieldName, Object obj, boolean soft) {
//        try {
//            Assertions.assertNotNull(obj, "Unexpected null for: " + fieldName);
//        } catch (AssertionError e) {
//            handleError(e, soft);
//        }
//    }
//
//    public static <T> void assertThat(String longMessage, String failureMessage, T actual, Matcher<? super T> matcher, boolean soft) {
//        String fullMessage = "Assert that " + longMessage;
//        log.info(MESSAGE_EXPECTED_ACTUAL, fullMessage, System.lineSeparator(), matcher.toString(), System.lineSeparator(), actual);
//
//        try {
//            if (StringUtils.isEmpty(failureMessage)) {
//                MatcherAssert.assertThat(actual, matcher);
//            } else {
//                MatcherAssert.assertThat(failureMessage, actual, matcher);
//            }
//        } catch (AssertionError e) {
//            handleError(e, soft);
//        }
//    }
//
//    public static <T> void assertThat(String message, T actual, Matcher<? super T> matcher, boolean soft) {
//        assertThat("", message, actual, matcher, soft);
//    }
//
//    public static <T> void assertThat(T actual, Matcher<? super T> matcher, boolean soft) {
//        assertThat(DEFAULT_FAILURE_MESSAGE, actual, matcher, soft);
//    }
//
//    public static void fail(String failureMessage, boolean soft) {
//        if (StringUtils.isBlank(failureMessage)) {
//            failureMessage = DEFAULT_FAILURE_MESSAGE;
//        }
//
//        handleError(new AssertionError(failureMessage), soft);
//    }
//
//    private static void handleError(AssertionError e, boolean soft) {
//        if (soft) {
//            softErrors.get().add(e);
//        } else {
//            throw e;
//        }
//    }
//
//    public static void assertAll() {
//        List<AssertionError> errors = softErrors.get();
//        if (!errors.isEmpty()) {
//            String summary = errors.stream()
//                    .map(Throwable::getMessage)
//                    .collect(Collectors.joining(System.lineSeparator()));
//            softErrors.remove();
//            throw new AssertionError("Soft assertion failures:\n" + summary);
//        }
//        softErrors.remove();
//    }
//
//    public static void clearSoft() {
//        softErrors.get().clear();
//    }
//}
