package com.unison.scada.availability.domain.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;


public class UserDTO {
    @Getter
    @Setter
    @RequiredArgsConstructor
    public static class Request{
        private final String id;
        private final String password;
        private final String role;
        private final String name;
    }

    @Getter
    @Setter
    @RequiredArgsConstructor
    @AllArgsConstructor
    public static class Response{
        private final String id;
        private final String name;
        private final String role;
        private final LocalTime lastLoginTime;
        private Error error;
    }
}
