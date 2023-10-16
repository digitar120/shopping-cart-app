package com.digitar120.shoppingcartapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ShoppingCartAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShoppingCartAppApplication.class, args);
	}

}
