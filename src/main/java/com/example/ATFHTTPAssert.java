package com.example;



import static org.hamcrest.Matchers.is;

public class ATFHTTPAssert {


    public static void assertStatus(int responseCode, HttpStatus status) {
        ATFAssert.assertThat("Assert that HTTP-status from response is correct",
                "Response status code does not match", responseCode, is(status.getValue()));
    }
}
