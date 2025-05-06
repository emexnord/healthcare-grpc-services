package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.patient.PatientDetails;
import com.example.patient.PatientDetailsRequest;
import com.example.patient.PatientRegistrationRequest;
import com.example.patient.PatientRegistrationResponse;
import com.example.patient.PatientServiceGrpc;

import io.grpc.stub.StreamObserver;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PatientService extends PatientServiceGrpc.PatientServiceImplBase {

    @Autowired
    private final PatientRepository patientRepository;

    @Override
    public void registerPatient(PatientRegistrationRequest request,
            StreamObserver<PatientRegistrationResponse> responseObserver) {
        Patient patient = new Patient(
                null,
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                request.getPhoneNumber(),
                request.getAddress());

        patient = patientRepository.save(patient);
        responseObserver.onNext(PatientRegistrationResponse.newBuilder().setPatientId(patient.id).build());
        responseObserver.onCompleted();
    }

    @Override
    public void getPatientDetails(PatientDetailsRequest request,
            StreamObserver<PatientDetails> responseObserver) {
        var patient = patientRepository.findById(request.getPatientId());
        if (patient.isPresent()) {
            var p = patient.get();
            var details = PatientDetails.newBuilder()
                    .setPatientId(p.id)
                    .setFirstName(p.firstName)
                    .setLastName(p.lastName)
                    .setEmail(p.email)
                    .setPhoneNumber(p.phone)
                    .setAddress(p.address)
                    .build();
            responseObserver.onNext(details);
        } else {
            responseObserver
                    .onError(io.grpc.Status.NOT_FOUND.withDescription("Patient not found").asRuntimeException());
        }
        responseObserver.onCompleted();
    }
}
