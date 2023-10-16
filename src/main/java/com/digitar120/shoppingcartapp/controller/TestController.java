package com.digitar120.shoppingcartapp.controller;

import com.digitar120.shoppingcartapp.feignclient.response.UserResponse;
import com.digitar120.shoppingcartapp.service.TestService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/test-endpoint")
public class TestController {

    private final TestService service;
    public TestController(TestService service) {
        this.service = service;
    }

    @GetMapping
    public List<UserResponse> getAllUsers(){
        return service.getAllUsers();
    }
}
