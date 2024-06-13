package com.unison.scada.availability.api.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalTime;


public class UserDTO {
    @Getter
    @RequiredArgsConstructor
    public static class Request{
        private final String id;
        private final String password;
        private final String role;
        private final String name;
    }

    @Getter
    @RequiredArgsConstructor
    @AllArgsConstructor
    public static class Response{
        private final String id;
        private final String name;
        private final String role;
        private final String lastLoginTime;
        private Error error;
    }
}
