package com.unison.scada.availability.domain.login;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;

    @PostMapping("/login")
    public LoginDTO.Response login(LoginDTO.Request request){
        System.out.println(request.getUsername() + " kjwon" + request.getPassword());
        return loginService.login(request);
    }
}
