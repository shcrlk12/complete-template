package com.unison.scada.availability.global.config.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unison.scada.availability.domain.login.LoginDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class AppAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    private final ObjectMapper objectMapper = new ObjectMapper();

    protected void handle(HttpServletRequest request, HttpServletResponse response,
                          Authentication authentication) throws IOException, ServletException {
    }
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        Map<String, Object> data = new HashMap<>();
        String errorMessage = getErrorMessage(e);

        data.put("status", HttpServletResponse.SC_UNAUTHORIZED);
        data.put("error", true);
        data.put("message", errorMessage);
        System.out.println("Authentication failed: " + e.getMessage());

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        response.getWriter().write(objectMapper.writeValueAsString(data));
    }

    private static String getErrorMessage(AuthenticationException e) {
        String errorMessage;

        if(e instanceof BadCredentialsException){
            errorMessage= "비밀번호 불일치";
        }
        else if(e instanceof InternalAuthenticationServiceException){
            errorMessage= "Exception1";
        }
        else if(e instanceof DisabledException){
            errorMessage= "Exception2";
        }
        else if(e instanceof CredentialsExpiredException){
            errorMessage= "비밀번호 만료";
        }
        else{
            errorMessage = "로그인에 실패 하였습니다";
        }
        return errorMessage;
    }
}
