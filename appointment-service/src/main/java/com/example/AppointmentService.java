package com.example;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.appointment.AppointmentAvailabilityRequest;
import com.example.appointment.AppointmentAvailabilityResponse;
import com.example.appointment.AppointmentServiceGrpc;
import com.example.appointment.AppointmentSlot;
import com.example.appointment.BookAppointmentRequest;
import com.example.appointment.BookAppointmentResponse;
import com.example.doctor.DoctorDetailsRequest;
import com.example.doctor.DoctorServiceGrpc;
import com.example.patient.PatientDetailsRequest;
import com.example.patient.PatientServiceGrpc;

import io.grpc.stub.StreamObserver;

@Service
public class AppointmentService extends AppointmentServiceGrpc.AppointmentServiceImplBase {

        private final AppointmentRepository appointmentRepository;
        private final DoctorServiceGrpc.DoctorServiceBlockingStub doctorServiceBlockingStub;
        private final PatientServiceGrpc.PatientServiceBlockingStub patientServiceBlockingStub;

        public AppointmentService(AppointmentRepository appointmentRepository,
                        DoctorServiceGrpc.DoctorServiceBlockingStub doctorServiceBlockingStub,
                        PatientServiceGrpc.PatientServiceBlockingStub patientServiceBlockingStub) {
                this.appointmentRepository = appointmentRepository;
                this.doctorServiceBlockingStub = doctorServiceBlockingStub;
                this.patientServiceBlockingStub = patientServiceBlockingStub;
        }

        @Override
        public void bookAppointment(BookAppointmentRequest request,
                        StreamObserver<BookAppointmentResponse> responseObserver) {
                try {
                        var doctorResponse = doctorServiceBlockingStub.getDoctorDetails(
                                        DoctorDetailsRequest.newBuilder().setDoctorId(request.getDoctorId()).build());

                        var patientResponse = patientServiceBlockingStub
                                        .getPatientDetails(PatientDetailsRequest.newBuilder()
                                                        .setPatientId(request.getPatientId()).build());

                        var appointment = new Appointment(
                                        null,
                                        request.getPatientId(),
                                        patientResponse.getFirstName(),
                                        request.getDoctorId(),
                                        doctorResponse.getFirstName(),
                                        doctorResponse.getLocation(),
                                        LocalDate.parse(request.getAppointmentDate()),
                                        LocalTime.parse(request.getAppointmentTime()),
                                        request.getReason());

                        appointment = appointmentRepository.save(appointment);
                        responseObserver.onNext(
                                        BookAppointmentResponse.newBuilder().setAppointmentId(appointment.id())
                                                        .build());
                        responseObserver.onCompleted();

                } catch (Exception e) {
                        responseObserver.onError(
                                        io.grpc.Status.NOT_FOUND.withDescription(e.getMessage()).asRuntimeException());
                        System.err.println("Error occurred while booking appointment: " + e.getMessage());
                        e.printStackTrace();
                }

        }

        @Override
        public void getAppointmentAvailability(AppointmentAvailabilityRequest request,
                        StreamObserver<AppointmentAvailabilityResponse> responseObserver) {
                List<LocalDateTime> hardcodedAppointments = Arrays.asList(
                                LocalDateTime.of(2025, 1, 7, 9, 0),
                                LocalDateTime.of(2025, 1, 8, 9, 30),
                                LocalDateTime.of(2025, 1, 8, 10, 0),
                                LocalDateTime.of(2025, 1, 8, 10, 30),
                                LocalDateTime.of(2025, 1, 9, 11, 0),
                                LocalDateTime.of(2025, 1, 11, 11, 30),
                                LocalDateTime.of(2025, 1, 11, 13, 0),
                                LocalDateTime.of(2025, 1, 12, 13, 30),
                                LocalDateTime.of(2025, 1, 12, 14, 0),
                                LocalDateTime.of(2025, 1, 13, 14, 30));

                Random random = new Random();
                int i = 0;
                while (i < 10) {
                        Collections.shuffle(hardcodedAppointments, random);
                        var slots = hardcodedAppointments.stream()
                                        .limit(2)
                                        .map(dateTime -> AppointmentSlot.newBuilder()
                                                        .setAppointmentDate(dateTime.toLocalDate().toString())
                                                        .setAppointmentTime(dateTime.toLocalTime().toString())
                                                        .build())
                                        .collect(Collectors.toList());

                        var response = AppointmentAvailabilityResponse.newBuilder()
                                        .addAllResponses(slots)
                                        .setAvailabilityAsOf(LocalDate.now().toString())
                                        .build();

                        responseObserver.onNext(response);

                        try {
                                Thread.sleep(2000); // Simulate delay
                        } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                        }
                        i++;
                }

                responseObserver.onCompleted();
        }

}
