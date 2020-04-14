package com.example.springsecurityacl;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringSecurityAclApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(SpringSecurityAclApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("fgdfgdfg");
    }
}
