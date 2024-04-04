package com.unison.scada.availability.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    @PostMapping("/new")
    public ResponseEntity<Boolean> createUser(@RequestBody UserDTO.Request request){

        boolean result = userService.createUser(request);

        return ResponseEntity.ok()
                .body(result);
    }

    @GetMapping("/list")
    public ResponseEntity<List<UserDTO.Response>> createUser(){

        List<UserDTO.Response> result = userService.findByAllUser();

        return ResponseEntity.ok()
                .body(result);
    }
}
