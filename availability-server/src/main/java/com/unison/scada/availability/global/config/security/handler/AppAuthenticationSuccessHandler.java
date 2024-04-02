package com.unison.scada.availability.global.config.security.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import java.io.IOException;
import java.net.URLEncoder;

public class AppAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
//    private RequestCache requestCache = new HttpSessionRequestCache();
//
//    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
//                                        Authentication authentication) throws IOException, ServletException {
//        setDefaultTargetUrl("/availability/annually");
//        SavedRequest savedRequest = requestCache.getRequest(request,response);
//
//        if(savedRequest != null){
//            // 인증 받기 전 url로 이동하기
//            String targetUrl = savedRequest.getRedirectUrl();
//            redirectStrategy.sendRedirect(request,response,targetUrl);
//        }else{
//            // 기본 url로 가도록 함함
//            redirectStrategy.sendRedirect(request,response,getDefaultTargetUrl());
//        }
//    }

}