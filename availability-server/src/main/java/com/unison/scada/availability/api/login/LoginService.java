package com.unison.scada.availability.api.login;

public interface LoginService {
    LoginDTO.Response login(LoginDTO.Request request);
}
