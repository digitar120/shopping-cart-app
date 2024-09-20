package com.digitar120.shoppingcartapp.feignclient;

import com.digitar120.shoppingcartapp.feignclient.response.UserResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserFeignClientFallback implements UserClient {
    @Override
    public List<UserResponse> getAllUsers(){
        ArrayList<UserResponse> fallbackList = new ArrayList<>();
        fallbackList.add(new UserResponse(-1));
        return fallbackList;
    }

    @Override
    public UserResponse getUserByUserId(@PathVariable("id") Integer userId){
        return new UserResponse(-1);
    }
}
