package org.example;

import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringApplication {
    public static void main(String[] args) {
        org.springframework.boot.SpringApplication.run(SpringApplication.class, args);

        System.out.println("http://localhost:8091/");
        System.out.println("http://localhost:8091/index");

        System.out.println("http://localhost:8091/login");


        System.out.println("http://localhost:8091/people/userCreate");
    }
}
