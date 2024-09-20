package com.digitar120.shoppingcartapp.feignclient;

import com.digitar120.shoppingcartapp.context.UserFeignClientConfiguration;
import com.digitar120.shoppingcartapp.feignclient.response.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(
        name = "userclient"
        , url = "localhost:9001"
        , path = "/user"
        , configuration = UserFeignClientConfiguration.class
        , fallback = UserFeignClientFallback.class)
public interface UserClient {

    @GetMapping
    public List<UserResponse> getAllUsers();

    @GetMapping("/{userId}")
    public UserResponse getUserByUserId(@PathVariable Integer userId);
}


