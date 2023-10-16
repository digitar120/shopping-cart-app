package com.digitar120.shoppingcartapp.service;

import com.digitar120.shoppingcartapp.feignclient.UserClient;
import com.digitar120.shoppingcartapp.feignclient.response.UserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestService {

    private UserClient userClient;
    public TestService(UserClient userClient) {
        this.userClient = userClient;
    }

    public List<UserResponse> getAllUsers(){
        return userClient.getAllUsers();
    }
}
