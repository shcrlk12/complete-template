package com.unison.scada.availability.api.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userService;
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteUser(@PathVariable("id") String id){

        return ResponseEntity.ok()
                .body(userService.deleteUser(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO.Response> getUserDetail(@PathVariable("id") String id){

        UserDTO.Response result = userService.findByUser(id);

        return ResponseEntity.ok()
                .body(result);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<JSONResponse<UserDTO.Response, Error>> getUserDetail2()
    {
        System.out.println("test");
        return null;
    }
}
