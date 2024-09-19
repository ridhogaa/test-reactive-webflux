package com.bmi.bds.testwebflux;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private TestFluxService testFluxService;

    @GetMapping("test")
    public ResponseEntity<?> invoke() {
        return ResponseEntity.ok(testFluxService.invoke());
    }
}
