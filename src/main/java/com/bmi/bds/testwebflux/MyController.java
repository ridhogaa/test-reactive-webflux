package com.bmi.bds.testwebflux;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@BaseController(value = "/my")
@RequiredArgsConstructor
public class MyController {

    private final TestFluxService service;

    @GetMapping
    public Object getAllUsers() {
        return service.invoke();
    }

    @PostMapping
    public String createUser() {
        return "User created";
    }
}

