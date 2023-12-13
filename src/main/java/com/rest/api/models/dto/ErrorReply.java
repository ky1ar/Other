package com.rest.api.models.dto;

import lombok.Getter;

@Getter
public class ErrorReply {
    private String errorMessage;

    public ErrorReply(String errorMessage){
        this.errorMessage = errorMessage;
    }
}
