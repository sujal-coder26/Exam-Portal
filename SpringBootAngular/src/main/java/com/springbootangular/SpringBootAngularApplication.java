package com.springbootangular;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBootAngularApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootAngularApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
    }
}
