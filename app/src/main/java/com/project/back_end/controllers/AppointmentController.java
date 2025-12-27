package com.project.back_end.controllers;


import com.project.back_end.models.Appointment;
import com.project.back_end.services.AppointmentService;
import com.project.back_end.services.ServiceClass;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

// 1. Set Up the Controller Class:
//    - Annotate the class with `@RestController` to define it as a REST API controller.
//    - Use `@RequestMapping("/appointments")` to set a base path for all appointment-related endpoints.
//    - This centralizes all routes that deal with booking, updating, retrieving, and canceling appointments.

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final ServiceClass service;

// 2. Autowire Dependencies:
//    - Inject `AppointmentService` for handling the business logic specific to appointments.
//    - Inject the general `Service` class, which provides shared functionality like token validation and appointment checks.

    public AppointmentController(AppointmentService appointmentService, ServiceClass service) {
        this.appointmentService = appointmentService;
        this.service = service;
    }

// 3. Define the `getAppointments` Method:
//    - Handles HTTP GET requests to fetch appointments based on date and patient name.
//    - Takes the appointment date, patient name, and token as path variables.
//    - First validates the token for role `"doctor"` using the `Service`.
//    - If the token is valid, returns appointments for the given patient on the specified date.
//    - If the token is invalid or expired, responds with the appropriate message and status code.
    @GetMapping("/{date}/{patientName}/{token}")
    public ResponseEntity<?> getAppointments(@PathVariable String date,
                                             @PathVariable String patientName,
                                             @PathVariable String token) {
        // Validation token (doctor)
        if (!service.validateToken(token, "doctor").getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED.value()).body(Map.of("error", "Invalid or expired token")); // UNAUTHORIZED.value() : 401
        }

        LocalDate appointmentDate = LocalDate.parse(date);
        Map<String, Object> result = appointmentService.getAppointment(patientName, appointmentDate, token);

        if (result.containsKey("error")) {
            return ResponseEntity.badRequest().body(result);
        }
        return ResponseEntity.ok(result);
    }


// 4. Define the `bookAppointment` Method:
//    - Handles HTTP POST requests to create a new appointment.
//    - Accepts a validated `Appointment` object in the request body and a token as a path variable.
//    - Validates the token for the `"patient"` role.
//    - Uses service logic to validate the appointment data (e.g., check for doctor availability and time conflicts).
//    - Returns success if booked, or appropriate error messages if the doctor ID is invalid or the slot is already taken.
    @PostMapping("/{token}")
    public ResponseEntity<?> bookAppointment(@PathVariable String token,
                                             @RequestBody Appointment appointment) {
        // Validation token (patient)
        if (!service.validateToken(token, "patient").getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED.value()).body(Map.of("error", "Invalid or expired token")); // UNAUTHORIZED.value() : 401
        }

        int result = appointmentService.bookAppointment(appointment);
        if (result == -1) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid doctor ID"));
        } else if (result == 0) {
            return ResponseEntity.badRequest().body(Map.of("error", "Appointment slot not available"));
        }

        return ResponseEntity.status(HttpStatus.CREATED.value()).body(Map.of("success", "Appointment booked successfully")); // CREATED.value() : 201
    }


// 5. Define the `updateAppointment` Method:
//    - Handles HTTP PUT requests to modify an existing appointment.
//    - Accepts a validated `Appointment` object and a token as input.
//    - Validates the token for `"patient"` role.
//    - Delegates the update logic to the `AppointmentService`.
//    - Returns an appropriate success or failure response based on the update result.
    @PutMapping("/{token}")
    public ResponseEntity<?> updateAppointment(@PathVariable String token,
                                               @RequestBody Appointment appointment) {
        // Validation token (patient)
        if (!service.validateToken(token, "patient").getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED.value()).body(Map.of("error", "Invalid or expired token")); // UNAUTHORIZED.value() : 401
        }

        return appointmentService.updateAppointment(appointment);
    }


// 6. Define the `cancelAppointment` Method:
//    - Handles HTTP DELETE requests to cancel a specific appointment.
//    - Accepts the appointment ID and a token as path variables.
//    - Validates the token for `"patient"` role to ensure the user is authorized to cancel the appointment.
//    - Calls `AppointmentService` to handle the cancellation process and returns the result.
    @DeleteMapping("/{id}/{token}")
    public ResponseEntity<?> cancelAppointment(@PathVariable Long id,
                                               @PathVariable String token) {
        // Validation token (patient)
        if (!service.validateToken(token, "patient").getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED.value()).body(Map.of("error", "Invalid or expired token")); // UNAUTHORIZED.value() : 401
        }

        return appointmentService.cancelAppointment(id, token);
    }

}
