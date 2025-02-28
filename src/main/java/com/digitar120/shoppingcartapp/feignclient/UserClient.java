package com.digitar120.shoppingcartapp.feignclient;

import com.digitar120.shoppingcartapp.context.UserFeignClientConfiguration;
import com.digitar120.shoppingcartapp.feignclient.response.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * Definitions of actions for the FeignClient HTTP client.
 * @author Gabriel Pérez (digitar120)
 * @see UserResponse
 * @see UserFeignClientFallback
 * @see UserFeignClientConfiguration
 */
@FeignClient(
        name = "userclient"
        , url = "host.docker.internal:9001"
        , path = "/user"
        , configuration = UserFeignClientConfiguration.class
        , fallback = UserFeignClientFallback.class
)
public interface UserClient {

    /**
     * Query the Users service for all registered users.
     * @return A list of users registered in the Users endpoint.
     */
    @GetMapping
    public List<UserResponse> getAllUsers();

    /**
     * Query the Users service for a specific user ID.
     * @param userId ID to perform the query with in the Users endpoint.
     * @return A matching user, if the search is positive.
     */
    @GetMapping("/{userId}")
    public UserResponse getUserByUserId(@PathVariable Integer userId);
}


