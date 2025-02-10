package com.example.api.models;


import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HttpError {

    @JsonProperty("message")
    private String message;


}
