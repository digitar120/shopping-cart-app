package com.digitar120.shoppingcartapp.context;

import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;

public class UserFeignClientConfiguration {

    @Bean
    public OkHttpClient client(){
        return new OkHttpClient();
    }
}
