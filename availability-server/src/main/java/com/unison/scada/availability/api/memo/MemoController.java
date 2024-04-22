package com.unison.scada.availability.api.memo;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/memo")
@RequiredArgsConstructor
public class MemoController {

    @PostMapping("register")
    public void registerMemo(){

    }
}
