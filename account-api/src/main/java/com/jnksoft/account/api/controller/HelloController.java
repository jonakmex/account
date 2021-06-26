package com.jnksoft.account.api.controller;

import com.jnksoft.account.api.controller.response.HelloResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @GetMapping("/hello")
    public ResponseEntity<HelloResponse> hello(@RequestParam(name = "name", required = false) String name){
        HelloResponse helloResponse = new HelloResponse();
        helloResponse.message = "Hello ";
        if(name != null)
            helloResponse.message += name;
        else
            helloResponse.message += "World!";
        return ResponseEntity.ok(helloResponse);
    }
}
