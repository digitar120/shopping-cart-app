package com.digitar120.shoppingcartapp.feignclient;

import com.digitar120.shoppingcartapp.feignclient.response.UserResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

/**
 * Fallback behavior for the {@link UserClient} FeignClient client. Currently, a work-in-progress.
 * @author Gabriel Pérez (digitar120)
 * @see UserClient
 * @see UserResponse
 */
@Component
public class UserFeignClientFallback implements UserClient {

    /**
     * Build a signalling response for upper layers to notice that the call failed.
     * @return An {@code ArrayList}, containing a single {@code UserResponse} with ID {@code -1}.
     */
    @Override
    public List<UserResponse> getAllUsers(){
        ArrayList<UserResponse> fallbackList = new ArrayList<>();
        fallbackList.add(new UserResponse(-1));
        return fallbackList;
    }

    /**
     * Build a signalling response for upper layers to notice that the call failed.
     * @param userId ID to perform the query with in the Users endpoint.
     * @return An {@code UserResponse} object with ID {@code -1}.
     */
    @Override
    public UserResponse getUserByUserId(@PathVariable("id") Integer userId){
        return new UserResponse(-1);
    }
}
