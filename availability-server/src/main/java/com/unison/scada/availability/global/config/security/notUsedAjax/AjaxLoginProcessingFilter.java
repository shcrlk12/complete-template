package com.unison.scada.availability.global.config.security.notUsedAjax;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.unison.scada.availability.domain.user.UserDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;


import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.springframework.http.HttpMethod.POST;

public class AjaxLoginProcessingFilter extends AbstractAuthenticationProcessingFilter {

    private final ObjectMapper objectMapper = new ObjectMapper();
    public AjaxLoginProcessingFilter() {
        super(new AntPathRequestMatcher("/login", POST.name()));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {

//        if (!isAjax(request)) {
//            throw new IllegalStateException("Authentication is not supported");
//        }

        System.out.println(request.getRequestURL());
        ServletInputStream inputStream = request.getInputStream();
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

        UserDTO.Request userDTO = objectMapper.readValue(messageBody, UserDTO.Request.class);

        if (!StringUtils.hasText(userDTO.getUsername())
                || !StringUtils.hasText(userDTO.getPassword())) {
            throw new IllegalArgumentException("username or password is empty");
        }

        AjaxAuthenticationToken ajaxAuthenticationToken
                = new AjaxAuthenticationToken(userDTO.getUsername(), userDTO.getPassword());
        setDetails(request, ajaxAuthenticationToken);

        return this.getAuthenticationManager().authenticate(ajaxAuthenticationToken);
    }

    protected void setDetails(HttpServletRequest request, AjaxAuthenticationToken authRequest) {
        authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }

    private boolean isAjax(HttpServletRequest request) {
        return "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
    }
}