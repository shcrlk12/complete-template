package com.unison.scada.availability.global.config.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unison.scada.availability.domain.user.Error;
import com.unison.scada.availability.global.config.security.exception.SecurityExceptionCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AppAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        Map<String, Object> data = new HashMap<>();

        data.put("code", SecurityExceptionCode.AUTHENTICATION_ENTRY_POINT.getCode());
        data.put("message", SecurityExceptionCode.AUTHENTICATION_ENTRY_POINT.getMessage());

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(objectMapper.writeValueAsString(data));
    }
}
