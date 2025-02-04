package com.unison.scada.availability.global.config.security;


import com.unison.scada.availability.global.CorsProperties;
import com.unison.scada.availability.global.config.security.handler.*;
import com.unison.scada.availability.global.filter.CookieAttributeFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig{

    private final UserDetailsService userDetailsService;
    private final CorsProperties corsProperties;

    @Bean
    public AuthenticationSuccessHandler appAuthenticationSuccessHandler(){
        return new AppAuthenticationSuccessHandler();
    }

    @Bean
    public AuthenticationFailureHandler appAuthenticationFailureHandler(){
        return new AppAuthenticationFailureHandler();

    }
    @Bean
    public LogoutSuccessHandler appLogoutSuccessHandler(){
        return new AppLogoutSuccessHandler();
    }

    @Bean
    public AccessDeniedHandler appAppAccessDeniedHandler(){
        return new AppAccessDeniedHandler();
    }
    @Bean
    public AuthenticationEntryPoint appAuthenticationEntryPoint(){
        return new AppAuthenticationEntryPoint();
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(corsCustomizer -> corsCustomizer.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(Arrays.asList("http://" +
                                                            corsProperties.getIp() +
                                                            ":" + corsProperties.getPort(),
                                                            "http://" +
                                                            corsProperties.getDomain() +
                                                            ":" + corsProperties.getPort()
                            ));
                    config.setAllowedMethods(Collections.singletonList("*"));
                    config.setAllowCredentials(true);
                    config.setAllowedHeaders(Collections.singletonList("*"));
                    config.setMaxAge(248461212L); //1시간
                    return config;
                }))
                .headers((headerConfig) ->
                        headerConfig.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable
                        )
                )
                .authorizeHttpRequests(authorizeRequest ->
                        authorizeRequest
                                .requestMatchers(
                                        AntPathRequestMatcher.antMatcher("/api/wind-farm/daily/register"),
                                        AntPathRequestMatcher.antMatcher("/api/wind-farm/daily/reset")
                                ).hasAnyRole("MANAGER", "ADMIN")
                                .requestMatchers(
                                        AntPathRequestMatcher.antMatcher("/api/login/**")
                                ).hasAnyRole("USER", "MANAGER", "ADMIN")
                                .requestMatchers(
                                        AntPathRequestMatcher.antMatcher("/api/wind-farm/**")
                                ).hasAnyRole("USER","MANAGER", "ADMIN")
                                .requestMatchers(
                                        AntPathRequestMatcher.antMatcher("/api/memo/**")
                                ).hasAnyRole("MANAGER", "ADMIN")
                                .requestMatchers(
                                        AntPathRequestMatcher.antMatcher("/api/reports/**")
                                ).hasAnyRole("USER", "MANAGER", "ADMIN")
                                .requestMatchers(
                                        AntPathRequestMatcher.antMatcher("/api/user/**")
                                ).hasAnyRole("ADMIN")
                                .requestMatchers(
                                        AntPathRequestMatcher.antMatcher("/h2-console/**")
                                ).permitAll()
                ).formLogin((formLogin) ->
                        formLogin
                                .loginPage("/api/login")
                                .usernameParameter("username")
                                .passwordParameter("password")
                                .successHandler(appAuthenticationSuccessHandler())
                                .failureHandler(appAuthenticationFailureHandler())
                                .permitAll()
                )
                .logout((logout) ->
                        logout.deleteCookies("JSESSIONID")
                                .invalidateHttpSession(false)
                                .logoutUrl("/logout")
                                .logoutSuccessHandler(appLogoutSuccessHandler())
                ).exceptionHandling((handling) ->
                        handling
                                .authenticationEntryPoint(appAuthenticationEntryPoint())
                                .accessDeniedHandler(appAppAccessDeniedHandler())
                )
                .addFilterBefore(cookieAttributeFilter(),UsernamePasswordAuthenticationFilter.class)
                .userDetailsService(userDetailsService);
        ;

        return http.build();
    }

    @Bean
    CookieAttributeFilter cookieAttributeFilter(){
        return new CookieAttributeFilter();
    }
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) ->
                web
                        .ignoring()
                        .requestMatchers(
                                PathRequest.toStaticResources().atCommonLocations()
                        );
    }

    //암호화 기능
    @Bean
    public static BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}