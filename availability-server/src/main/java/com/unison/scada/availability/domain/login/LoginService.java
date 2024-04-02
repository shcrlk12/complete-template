package com.unison.scada.availability.domain.login;

public interface LoginService {
    LoginDTO.Response login(LoginDTO.Request request);
}
