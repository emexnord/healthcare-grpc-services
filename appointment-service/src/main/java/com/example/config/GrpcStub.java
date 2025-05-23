package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.grpc.client.GrpcChannelFactory;

import com.example.doctor.DoctorServiceGrpc;
import com.example.patient.PatientServiceGrpc;

@Configuration
public class GrpcStub {
    @Bean
    DoctorServiceGrpc.DoctorServiceBlockingStub doctorServiceBlockingStub(GrpcChannelFactory channels) {
        return DoctorServiceGrpc.newBlockingStub(channels.createChannel("doctorService"));
    }

    @Bean
    PatientServiceGrpc.PatientServiceBlockingStub patientServiceBlockingStub(GrpcChannelFactory channels) {
        return PatientServiceGrpc.newBlockingStub(channels.createChannel("patientService"));
    }
}
