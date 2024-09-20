package com.digitar120.shoppingcartapp.context;

import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CircuitBreakerConfig {
    @Bean
    public ReactiveCircuitBreaker reactiveCircuitBreaker(ReactiveCircuitBreakerFactory reactiveResilience4JCircuitBreakerFactory){
        return reactiveResilience4JCircuitBreakerFactory.create("userClient");
    }
}
