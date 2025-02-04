package com.unison.scada.availability.api.login;

import lombok.Getter;
import lombok.Setter;

public class LoginDTO {
    @Setter
    @Getter
    public static class Request{
        private String username;
        private String password;
    }

    public record Response(String id, String name, String role) {}

}
