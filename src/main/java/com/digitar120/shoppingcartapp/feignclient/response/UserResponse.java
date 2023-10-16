package com.digitar120.shoppingcartapp.feignclient.response;

import lombok.*;

@Getter @Setter @ToString @NoArgsConstructor @AllArgsConstructor
public class UserResponse {
    private Integer id;
    private String name;
    private String lastName;

    public UserResponse(Integer id){
        this.id = id;
    }
}
