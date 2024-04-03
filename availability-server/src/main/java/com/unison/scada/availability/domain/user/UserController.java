package com.unison.scada.availability.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    @PostMapping("/new")
    public boolean createUser(@RequestBody UserDTO.Request request){
        return userService.createUser(request);
    }

    @GetMapping("/list")
    public List<UserDTO.Response> createUser(){
        return userService.findByAllUser();
    }
}
