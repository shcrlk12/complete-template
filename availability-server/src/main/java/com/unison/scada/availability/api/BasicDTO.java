package com.unison.scada.availability.api;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class BasicDTO {

    @Getter
    @Builder
    @RequiredArgsConstructor
    public static class Response{
        private final boolean success;
        private final String message;
    }

}
