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
 * Outgoing authorized FeignClient calls configuration. Defines call behavior for calls to the User service.
 * @author Gabriel Pérez (digitar120)
 * @see com.digitar120.shoppingcartapp.service.CartService#findByUserId(Integer) 
 */

@Configuration
public class UserFeignClientConfiguration {

    @Bean
    public OAuth2AuthorizedClientManager authorizedClientManager(
            ClientRegistrationRepository clientRegistrationRepository,
            OAuth2AuthorizedClientRepository authorizedClientRepository) {

        OAuth2AuthorizedClientProvider authorizedClientProvider =
                OAuth2AuthorizedClientProviderBuilder.builder()
                        .authorizationCode()
                        .refreshToken()
                        .clientCredentials()
                        .build();

        DefaultOAuth2AuthorizedClientManager authorizedClientManager =
                new DefaultOAuth2AuthorizedClientManager(
                        clientRegistrationRepository, authorizedClientRepository);
        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);

        return authorizedClientManager;
    }

    /**
     * This bean adds a header containing an OAuth2 token to the HTTP request. The access token is preconfigured.
     * @param manager
     * @return
     */
    @Bean
    public RequestInterceptor oauth2FeignRequestInterceptor(OAuth2AuthorizedClientManager manager){
        return requestTemplate -> {
            OAuth2AuthorizeRequest auth2AuthorizeRequest = OAuth2AuthorizeRequest
                    .withClientRegistrationId("keycloak")
                    .principal("principal")
                    .build();

            OAuth2AuthorizedClient client = manager.authorize(auth2AuthorizeRequest);
            requestTemplate.header("Authorization", "Bearer " + client.getAccessToken().getTokenValue());
        };
    }

    /**
     * Part of the circuit breaker pattern applied to outgoing calls.
     * @return
     */
    @Bean
    public ErrorDecoder errorDecoder() {
        return new CustomErrorDecoder();
    }
}
