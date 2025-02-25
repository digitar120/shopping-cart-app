package com.digitar120.shoppingcartapp.context;

import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * This class enables a reactive Circuit Breaker pattern on FeignClient calls to the User Service.
 * @author Gabriel Pérez (digitar120)
 */
@Configuration
public class CircuitBreakerConfig {
    @Bean
    public ReactiveCircuitBreaker reactiveCircuitBreaker(ReactiveCircuitBreakerFactory reactiveResilience4JCircuitBreakerFactory){
        return reactiveResilience4JCircuitBreakerFactory.create("userClient");
    }
}
