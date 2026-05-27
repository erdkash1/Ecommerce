package com.iggy.ecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ECommerceBackEndApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ECommerceBackEndApiApplication.class, args);
    }
}