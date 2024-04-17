package com.unison.scada.availability.api.login;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/login")
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;

    @PostMapping("/")
    public LoginDTO.Response login(LoginDTO.Request request){
        System.out.println(request.getUsername() + " kjwon" + request.getPassword());
        return loginService.login(request);
    }

    @GetMapping("/auth")
    public ResponseEntity<LoginDTO.Response> test() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.ok().body(new LoginDTO.Response("", "", ""));
        }

        String roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return ResponseEntity.ok().body(new LoginDTO.Response("", "", roles));
    }
}
