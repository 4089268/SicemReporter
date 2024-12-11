package com.nerus.reporter.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class HomeController {

    @GetMapping
    public String index() {
        return "API Version: 1.0.0";
    }
}
