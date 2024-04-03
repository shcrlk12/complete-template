package com.unison.scada.availability.domain.user;

import lombok.Getter;
import lombok.Setter;


public class UserDTO {
    @Getter
    @Setter
    public static class Request{
        private String username;
        private String password;
    }

    @Getter
    @Setter
    public static class Response{
        private String username;
        private String password;
    }
}
