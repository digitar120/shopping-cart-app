package com.digitar120.shoppingcartapp.context;

import com.digitar120.shoppingcartapp.exception.CustomErrorDecoder;
import feign.RequestInterceptor;
import feign.auth.BasicAuthRequestInterceptor;
import feign.codec.ErrorDecoder;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;

/**
 * Outgoing FeignClient calls configuration. Defines call behavior for calls to the User service.
 * @author Gabriel Pérez (digitar120)
 * @see com.digitar120.shoppingcartapp.service.CartService#findByUserId(Integer) 
 */

@Configuration
public class UserFeignClientConfiguration {

    /**
     * Part of the circuit breaker pattern applied to outgoing calls.
     * @return
     */
    @Bean
    public ErrorDecoder errorDecoder() {
        return new CustomErrorDecoder();
    }
}
