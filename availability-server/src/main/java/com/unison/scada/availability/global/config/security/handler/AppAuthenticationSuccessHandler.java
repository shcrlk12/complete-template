package com.unison.scada.availability.global.config.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AppAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final ObjectMapper objectMapper = new ObjectMapper();

    protected void handle(HttpServletRequest request, HttpServletResponse response,
                          Authentication authentication) throws IOException, ServletException {
    }
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException
    {
        Map<String, Object> data = new HashMap<>();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String Role = userDetails.getAuthorities().toString();

        data.put("status", HttpServletResponse.SC_OK);
        data.put("id",  userDetails.getUsername());
        data.put("role",  Role.substring(1, Role.length()-1));
        data.put("message", "로그인에 성공했습니다.");

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);

        response.getWriter().write(objectMapper.writeValueAsString(data));
    }
}