# Spring Boot gRPC Project - Health Care Application

# Patient Service GRPC Calls

## List Services

```bash
grpcurl -plaintext localhost:9090 list
```

## gRPC UI

```bash
grpcui -plaintext localhost:9090
```

## Register Patient

```bash
grpcurl -plaintext -d '{
  "first_name": "John",
  "last_name": "Thomas",
  "email": "john.thomas@example.com",
  "phone_number": "123-456-7890",
  "address": "123 Main St"
}' localhost:9090 com.example.patient.PatientService/RegisterPatient
```

## Get Patient

```bash
grpcurl -plaintext -d '{
  "patient_id": 1
}' localhost:9090 com.example.patient.PatientService/GetPatientDetails
```

# Doctor Service

## List Services

```bash
grpcurl -plaintext localhost:9091 list
```

## Register Doctor

```bash
grpcurl -d '{
  "first_name": "Tim",
  "last_name": "Jacob",
  "email": "tim.jacob@example.com",
  "phone": "123-456-7890",
  "specialty": "Cardiology",
  "centre_name": "Health Centre",
  "location": "New York"
}' -plaintext localhost:9091 com.example.doctor.DoctorService/RegisterDoctor
```

## Get Doctor

```bash
grpcurl -plaintext -d '{
  "doctor_id": 1
}' localhost:9091 com.example.doctor.DoctorService/GetDoctorDetails
```

## List Services

```bash
grpcurl -plaintext localhost:9092 list
```

## Get Appointment Availability - Server Streaming

```bash
grpcurl -plaintext -d '{
  "doctor_id": 1
}' localhost:9092 com.example.appointment.AppointmentService/GetAppointmentAvailability
```

## Book Appointment

```bash
grpcurl -plaintext -d '{
  "doctor_id": 1,
  "patient_id": 1,
  "appointment_date": "2025-02-15",
  "appointment_time": "10:00",
  "reason": "Routine check-up"
}' localhost:9092 com.example.appointment.AppointmentService/BookAppointment
```

## Stream Patients - Client Streaming

```bash
grpcurl -d @ -plaintext localhost:9090 com.example.patient.PatientService/StreamPatients <<EOM
{
  "first_name": "John",
  "last_name": "Smith",
  "email": "john.smith@example.com",
  "phone": "1234567890",
  "address": "123 Main St"
}
{
  "first_name": "Jane",
  "last_name": "Smith",
  "email": "jane.smith@example.com",
  "phone": "0987654321",
  "address": "456 Elm St"
}
EOM
```

## Chat with Doctor - Bidirectional Streaming

```bash
grpcurl -d @ -plaintext localhost:9091 com.example.doctor.DoctorService/Chat <<EOM
{
  "message": "Hello, Doctor!",
  "from": "Patient",
  "to": "Doctor",
  "timestamp": "2023-10-01T12:00:00Z"
}
{
  "message": "How are you?",
  "from": "Patient",
  "to": "Doctor",
  "timestamp": "2023-10-01T12:01:00Z"
}
EOM
```
