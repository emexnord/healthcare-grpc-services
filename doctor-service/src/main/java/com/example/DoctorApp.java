package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DoctorApp {
    public static void main(String[] args) {
        SpringApplication.run(DoctorApp.class, args);
        System.out.println("Doctor Service is running...");
    }
}
