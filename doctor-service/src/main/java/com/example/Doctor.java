package com.example;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Table("doctor")
@AllArgsConstructor
@NoArgsConstructor
public class Doctor {
    @Id
    Long id;

    String firstName;
    String lastName;
    String email;
    String phone;
    String specialty;
    String centreName;
    String location;
}
