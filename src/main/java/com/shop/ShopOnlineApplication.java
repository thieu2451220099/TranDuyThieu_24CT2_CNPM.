package com.shop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ShopOnlineApplication {

    public static void main(String[] args) {

        SpringApplication.run(ShopOnlineApplication.class, args);

        System.out.println();
        System.out.println("========================================");
        System.out.println("       THIEU_SHOP DA KHOI DONG");
        System.out.println("========================================");
        System.out.println("Website : http://localhost:8080");
        System.out.println("Admin   : http://localhost:8080/admin");
        System.out.println("========================================");
        System.out.println();
    }
}