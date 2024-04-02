package com.unison.scada.availability.test;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/home")
public class TestController {

    @GetMapping()
    public String testMethod() {
        return "되냐고요 이거";
    }
}
