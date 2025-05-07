package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AppointmentApp {
    public static void main(String[] args) {
        SpringApplication.run(AppointmentApp.class, args);
        System.out.println("Appointment Service is running...");
    }
}
