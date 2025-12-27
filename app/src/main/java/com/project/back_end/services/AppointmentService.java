package com.project.back_end.services;

import com.project.back_end.models.Appointment;
import com.project.back_end.models.Doctor;
import com.project.back_end.models.Patient;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.PatientRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

// 1. **Add @Service Annotation**:
//    - To indicate that this class is a service layer class for handling business logic.
//    - The `@Service` annotation should be added before the class declaration to mark it as a Spring service component.
//    - Instruction: Add `@Service` above the class definition.

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final TokenService tokenService;    // private final @Lazy TokenService tokenService;

// 2. **Constructor Injection for Dependencies**:
//    - The `AppointmentService` class requires several dependencies like `AppointmentRepository`, `Service`, `TokenService`, `PatientRepository`, and `DoctorRepository`.
//    - These dependencies should be injected through the constructor.
//    - Instruction: Ensure constructor injection is used for proper dependency management in Spring.

    public AppointmentService(AppointmentRepository appointmentRepository, PatientRepository patientRepository, DoctorRepository doctorRepository, TokenService tokenService) {
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.tokenService = tokenService;
    }

// 3. **Add @Transactional Annotation for Methods that Modify Database**:
//    - The methods that modify or update the database should be annotated with `@Transactional` to ensure atomicity and consistency of the operations.
//    - Instruction: Add the `@Transactional` annotation above methods that interact with the database, especially those modifying data.

// 4. **Book Appointment Method**:
//    - Responsible for saving the new appointment to the database.
//    - If the save operation fails, it returns `0`; otherwise, it returns `1`.
//    - Instruction: Ensure that the method handles any exceptions and returns an appropriate result code.
    @Transactional
    public int bookAppointment(Appointment appointment) {
        try {
            // Validate patient exists
            Optional<Patient> patient = patientRepository.findById(appointment.getPatient().getId());
            if (patient.isEmpty()) {
                return 0; // Patient not found
            }

            // Validate doctor exists and is available
            Optional<Doctor> doctor = doctorRepository.findById(appointment.getDoctor().getId());
            if (doctor.isEmpty()) {
                return 0; // Doctor not found
            }

            // Check if doctor is available at the requested time
            if (appointmentRepository.existsByDoctorIdAndAppointmentTime(
                appointment.getDoctor().getId(), appointment.getAppointmentTime())) {
                return 0; // Time slot already booked
            }

            appointmentRepository.save(appointment);
            return 1;
        } catch (Exception e) {
            // todo: Logging e.g. e.printStackTrace();
            return 0;
        }
    }

// 5. **Update Appointment Method**:
//    - This method is used to update an existinge.printStackTrace appointment based on its ID.
//    - It validates whether the patient ID matches, checks if the appointment is available for updating, and ensures that the doctor is available at the specified time.
//    - If the update is successful, it saves the appointment; otherwise, it returns an appropriate error message.
//    - Instruction: Ensure proper validation and error handling is included for appointment updates.
    @Transactional
    public ResponseEntity<Map<String, String>> updateAppointment(Appointment appointment) {
        Map<String, String> response = new HashMap<>();

        // check whether appointment exists
        Optional<Appointment> optionalExisting = appointmentRepository.findById(appointment.getId());
        if (optionalExisting.isEmpty()) {
            response.put("error", "Appointment not found");
            return ResponseEntity.badRequest().body(response);
        }
        Appointment existing = optionalExisting.get();

        // check whether patient is still the same
        if (!existing.getPatient().getId().equals(appointment.getPatient().getId())) {
            response.put("error", "Patient ID mismatch");
            return ResponseEntity.status(HttpStatus.FORBIDDEN.value()).body(response); // FORBIDDEN.value : 483
        }

        // check whether there is a conflict with other appointments for the same doctor
        boolean conflict = appointmentRepository.existsByDoctorIdAndAppointmentTimeAndIdNot(
                existing.getDoctor().getId(),
                appointment.getAppointmentTime(),
                existing.getId());
        if (conflict) {
            response.put("error", "Time slot already booked for this doctor");
            return ResponseEntity.badRequest().body(response);
        }

        try {
            // update appointment
            existing.setAppointmentTime(appointment.getAppointmentTime());
            existing.setStatus(appointment.getStatus());
            existing.setReason(appointment.getReason());
            // existing.setNotes(appointment.getNotes());
            appointmentRepository.save(existing);

            response.put("success", "Appointment updated successfully");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // todo: Logging e.g. e.printStackTrace();
            response.put("error", "Failed to update appointment");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(response); // INTERNAL_SERVER_ERROR.value : 500
        }
    }

// 6. **Cancel Appointment Method**:
//    - This method cancels an appointment by deleting it from the database.
//    - It ensures the patient who owns the appointment is trying to cancel it and handles possible errors.
//    - Instruction: Make sure that the method checks for the patient ID match before deleting the appointment.
    @Transactional
    public ResponseEntity<Map<String, String>> cancelAppointment(long id, String token) {
        Map<String, String> response = new HashMap<>();

        Optional<Appointment> optionalExisting = appointmentRepository.findById(id);
        if (optionalExisting.isEmpty()) {
            response.put("error", "Appointment not found");
            return ResponseEntity.badRequest().body(response);
        }
        Appointment existing = optionalExisting.get();

        // ensures the patient who owns the appointment is trying to cancel it
        Long patientId = tokenService.getPatientIdFromToken(token);
        if (!existing.getPatient().getId().equals(patientId)) {
            response.put("error", "Unauthorized cancellation");
            return ResponseEntity.status(HttpStatus.FORBIDDEN.value()).body(response); // FORBIDDEN.value : 483
        }

        try {
            appointmentRepository.delete(existing);

            response.put("success", "Appointment cancelled successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("error", "Failed to cancel appointment");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(response); // INTERNAL_SERVER_ERROR.value : 500
        }
    }

// 7. **Get Appointments Method**:
//    - This method retrieves a list of appointments for a specific doctor on a particular day, optionally filtered by the patient's name.
//    - It uses `@Transactional` to ensure that database operations are consistent and handled in a single transaction.
//    - Instruction: Ensure the correct use of transaction boundaries, especially when querying the database for appointments.
    @Transactional
    public Map<String, Object> getAppointment(String patientName, LocalDate date, String token) {
        Map<String, Object> result = new HashMap<>();

        Long doctorId = tokenService.getDoctorIdFromToken(token);
        if (doctorId == null) {
            result.put("error", "Invalid token or doctor not found");
            return result;
        }

        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.plusDays(1).atStartOfDay();

        List<Appointment> appointments;
        if (patientName != null && !patientName.isEmpty()) {
            appointments =
                    appointmentRepository
                            .findByDoctorIdAndPatientNameContainingIgnoreCaseAndAppointmentTimeBetween(
                                    doctorId, patientName, start, end);
        } else {
            appointments = appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(doctorId, start, end);
        }

        result.put("appointments", appointments);
        return result;
    }

// 8. **Change Status Method**:
//    - This method updates the status of an appointment by changing its value in the database.
//    - It should be annotated with `@Transactional` to ensure the operation is executed in a single transaction.
//    - Instruction: Add `@Transactional` before this method to ensure atomicity when updating appointment status.
    @Transactional
    public void updateAppointmentStatus(Long appointmentId, int status) {
        Optional<Appointment> optionalExisting = appointmentRepository.findById(appointmentId);
        optionalExisting.ifPresent(appointment -> {
            appointment.setStatus(status);
            appointmentRepository.save(appointment);
        });
    }

}
