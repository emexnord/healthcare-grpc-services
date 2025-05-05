package com.example;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;

@Table("patient")
@AllArgsConstructor
public class Patient {
    @Id
    Long id;

    String firstName;
    String lastName;
    String email;
    String phone;
    String address;

}
