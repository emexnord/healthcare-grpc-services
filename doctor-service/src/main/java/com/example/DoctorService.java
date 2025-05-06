package com.example;

import org.springframework.stereotype.Service;

import com.example.doctor.DoctorDetails;
import com.example.doctor.DoctorDetailsRequest;
import com.example.doctor.DoctorRegistrationRequest;
import com.example.doctor.DoctorRegistrationResponse;
import com.example.doctor.DoctorServiceGrpc;

import io.grpc.stub.StreamObserver;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class DoctorService extends DoctorServiceGrpc.DoctorServiceImplBase {

    private final DoctorRepository doctorRepository;

    @Override
    public void registerDoctor(DoctorRegistrationRequest request,
            StreamObserver<DoctorRegistrationResponse> responseObserver) {
        Doctor doctor = new Doctor(
                null,
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                request.getPhoneNumber(),
                request.getSpecialty(),
                request.getCenterName(),
                request.getLocation());

        doctor = doctorRepository.save(doctor);
        responseObserver.onNext(DoctorRegistrationResponse.newBuilder().setDoctorId(doctor.id).build());
        responseObserver.onCompleted();
    }

    @Override
    public void getDoctorDetails(DoctorDetailsRequest request,
            StreamObserver<DoctorDetails> responseObserver) {
        var doctor = doctorRepository.findById(request.getDoctorId());
        if (doctor.isPresent()) {
            var d = doctor.get();
            var details = DoctorDetails.newBuilder()
                    .setDoctorId(d.id)
                    .setFirstName(d.firstName)
                    .setLastName(d.lastName)
                    .setEmail(d.email)
                    .setPhoneNumber(d.phone)
                    .setSpecialty(d.specialty)
                    .setCenterName(d.centreName)
                    .setLocation(d.location)
                    .build();
            responseObserver.onNext(details);
        } else {
            responseObserver
                    .onError(io.grpc.Status.NOT_FOUND.withDescription("Doctor not found").asRuntimeException());
        }
        responseObserver.onCompleted();
    }

}
