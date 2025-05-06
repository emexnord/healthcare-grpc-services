package com.example;

import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.time.LocalTime;

public record Appointment(
        @Id Long id,
        Long patientId,
        String patientName,
        Long doctorId,
        String doctorName,
        String location,
        LocalDate appointmentDate,
        LocalTime appointmentTime,
        String reason) {
}