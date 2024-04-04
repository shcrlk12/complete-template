package com.unison.scada.availability.domain.user;

import com.unison.scada.availability.domain.login.LoginDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class CheckAuth {

    @GetMapping("/auth")
    public ResponseEntity<LoginDTO.Response> test(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.ok().body(new LoginDTO.Response("", "",""));
        }

        String roles = authentication.getAuthorities().stream()
                                        .map(GrantedAuthority::getAuthority)
                                        .collect(Collectors.joining(","));

        return ResponseEntity.ok().body(new LoginDTO.Response("", "",roles));
    }
}
