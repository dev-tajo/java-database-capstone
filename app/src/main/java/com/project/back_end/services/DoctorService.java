package com.project.back_end.services;

import com.project.back_end.DTO.Login;
import com.project.back_end.models.Appointment;
import com.project.back_end.models.Doctor;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.DoctorRepository;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;


// 1. **Add @Service Annotation**:
//    - This class should be annotated with `@Service` to indicate that it is a service layer class.
//    - The `@Service` annotation marks this class as a Spring-managed bean for business logic.
//    - Instruction: Add `@Service` above the class declaration.

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;
    private final TokenService tokenService;

// 2. **Constructor Injection for Dependencies**:
//    - The `DoctorService` class depends on `DoctorRepository`, `AppointmentRepository`, and `TokenService`.
//    - These dependencies should be injected via the constructor for proper dependency management.
//    - Instruction: Ensure constructor injection is used for injecting dependencies into the service.
    @Autowired
    public DoctorService(DoctorRepository doctorRepository,
                         AppointmentRepository appointmentRepository,
                         TokenService tokenService) {
        this.doctorRepository = doctorRepository;
        this.appointmentRepository = appointmentRepository;
        this.tokenService = tokenService;
    }

// 3. **Add @Transactional Annotation for Methods that Modify or Fetch Database Data**:
//    - Methods like `getDoctorAvailability`, `getDoctors`, `findDoctorsByName`, `filterDoctorsBy*` should be annotated with `@Transactional`.
//    - The `@Transactional` annotation ensures that database operations are consistent and wrapped in a single transaction.
//    - Instruction: Add the `@Transactional` annotation above the methods that perform database operations or queries.

// 4. **getDoctorAvailability Method**:
//    - Retrieves the available time slots for a specific doctor on a particular date and filters out already booked slots.
//    - The method fetches all appointments for the doctor on the given date and calculates the availability by comparing against booked slots.
//    - Instruction: Ensure that the time slots are properly formatted and the available slots are correctly filtered.
    @Transactional
    public List<String> getDoctorAvailability(Long doctorId, LocalDate date) {
        Optional<Doctor> optionalExisting = doctorRepository.findById(doctorId);
        if (optionalExisting.isEmpty()) {
            return Collections.emptyList();
        }
        Doctor doctor = optionalExisting.get();
        final List<String> allTimeSlotsPerDay = doctor.getAvailableTimes();

        List<Appointment> appointmentsBookedOnThisDay = appointmentRepository
                .findByDoctorIdAndAppointmentTimeBetween(doctorId,
                        date.atStartOfDay(),
                        date.plusDays(1).atStartOfDay()
                );
        //todo: consider non-1h-slots (e.g. longer than 1h) : regard duration of appointments
        Set<String> bookedTimeSlots = appointmentsBookedOnThisDay.stream()
                .map(a -> a.getAppointmentTime().toLocalTime().toString())
                .collect(Collectors.toSet());

        //todo: consider non-1h-slots (e.g. longer than 1h)
        final List<String> availableSlots = allTimeSlotsPerDay.stream()
                .filter(slot -> !bookedTimeSlots.contains(slot))
                .sorted()
                .collect(Collectors.toList());
        return availableSlots;
    }

// 5. **saveDoctor Method**:
//    - Used to save a new doctor record in the database after checking if a doctor with the same email already exists.
//    - If a doctor with the same email is found, it returns `-1` to indicate conflict; `1` for success, and `0` for internal errors.
//    - Instruction: Ensure that the method correctly handles conflicts and exceptions when saving a doctor.
    public int saveDoctor(Doctor doctor) {
        try {
            if (doctorRepository.findByEmail(doctor.getEmail()) != null) {
                return -1; // conflict: already exists
            }
            doctorRepository.save(doctor);
            return 1; // successfully saved
        } catch (Exception e) {
            return 0; // internal error
        }
    }

// 6. **updateDoctor Method**:
//    - Updates an existing doctor's details in the database. If the doctor doesn't exist, it returns `-1`.
//    - Instruction: Make sure that the doctor exists before attempting to save the updated record and handle any errors properly.
    public int updateDoctor(Doctor doctor) {
        try {
            Optional<Doctor> optionalExisting = doctorRepository.findById(doctor.getId());
            if (optionalExisting.isEmpty()) {
                return -1; // conflict: not found
            }
            Doctor existing = optionalExisting.get();
            existing.setName(doctor.getName());
            existing.setEmail(doctor.getEmail());
            existing.setPhone(doctor.getPhone());
            existing.setPassword(doctor.getPassword()); // todo: also change password ?
            existing.setMedicalLicense(doctor.getMedicalLicense());
            existing.setAcademicDegree(doctor.getAcademicDegree());
            existing.setSpecialty(doctor.getSpecialty());
            existing.setAvailableTimes(doctor.getAvailableTimes());

            doctorRepository.save(doctor);
            return 1; // successfully updated
        } catch (Exception e) {
            return 0; // internal error
        }
    }

// 7. **getDoctors Method**:
//    - Fetches all doctors from the database. It is marked with `@Transactional` to ensure that the collection is properly loaded.
//    - Instruction: Ensure that the collection is eagerly loaded, especially if dealing with lazy-loaded relationships (e.g., available times). 
    @Transactional
    public List<Doctor> getDoctors() {
        // todo: Ensure that the collection is eagerly loaded, especially if dealing with lazy-loaded relationships
        return doctorRepository.findAll();
    }

// 8. **deleteDoctor Method**:
//    - Deletes a doctor from the system along with all appointments associated with that doctor.
//    - It first checks if the doctor exists. If not, it returns `-1`; otherwise, it deletes the doctor and their appointments.
//    - Instruction: Ensure the doctor and their appointments are deleted properly, with error handling for internal issues.
    @Transactional
    public int deleteDoctor(long doctorId) {
        try {
            Optional<Doctor> optionalExist = doctorRepository.findById(doctorId);
            if (optionalExist.isEmpty()) {
                return -1; // conflict: not found
            }
            appointmentRepository.deleteAllByDoctorId(doctorId);
            doctorRepository.deleteById(doctorId);
            return 1; // successfully deleted
        } catch (Exception e) {
            return 0; // internal error
        }
    }

// 9. **validateDoctor Method**:u
//    - Validates a doctor's login by checking if the email and password match an existing doctor record.
//    - It generates a token for the doctor if the login is successful, otherwise returns an error message.
//    - Instruction: Make sure to handle invalid login attempts and password mismatches properly with error responses.
    public ResponseEntity<Map<String, String>> validateDoctor(Login login) {
        Map<String, String> response = new HashMap<>();
        Doctor doctor = doctorRepository.findByEmail(login.getIdentifier());
        if (doctor == null || !doctor.getPassword().equals(login.getPassword())) {
            response.put("error", "Invalid credentials");
            return ResponseEntity.badRequest().body(response);
        }
        String token = tokenService.generateToken(doctor.getEmail());
        response.put("token", token);
        return ResponseEntity.ok(response);
    }

// 10. **findDoctorByName Method**:
//    - Finds doctors based on partial name matching and returns the list of doctors with their available times.
//    - This method is annotated with `@Transactional` to ensure that the database query and data retrieval are properly managed within a transaction.
//    - Instruction: Ensure that available times are eagerly loaded for the doctors.
    // todo: maybe method should named as findDoctorsByName ?
    @Transactional
    public Map<String, Object> findDoctorByName(String name) {
        Map<String, Object> result = new HashMap<>();
        List<Doctor> doctors = doctorRepository.findByNameLike(name);
        result.put("doctors", doctors);
        // todo: Ensure that available times are eagerly loaded for the doctors.
        return result;
    }

// 11. **filterDoctorsByNameSpecialtyAndTime Method**:
//    - Filters doctors based on their name, specialty, and availability during a specific time (AM/PM).
//    - The method fetches doctors matching the name and specialty criteria, then filters them based on their availability during the specified time period.
//    - Instruction: Ensure proper filtering based on both the name and specialty as well as the specified time period.
    @Transactional
    public Map<String, Object> filterDoctorsByNameSpecialtyAndTime(String name, String specialty, String amOrPm) {
        List<Doctor> doctors = doctorRepository.findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(name, specialty);
        Map<String, Object> result = new HashMap<>();
        result.put("doctors", filterDoctorsByTime(doctors, amOrPm));
        return result;
    }

// 12. **filterDoctorByTime Method**:
//    - Filters a list of doctors based on whether their available times match the specified time period (AM/PM).
//    - This method processes a list of doctors and their available times to return those that fit the time criteria.
//    - Instruction: Ensure that the time filtering logic correctly handles both AM and PM time slots and edge cases.
private List<Doctor> filterDoctorsByTime(List<Doctor> doctors, String amOrPm) {
    return doctors.stream().filter(d -> {
        final List<String> availableTimeSlots = d.getAvailableTimes();
        if (availableTimeSlots == null) {
            return false;
        }
        return availableTimeSlots.stream().anyMatch(timeSlotString -> {
            try {
                // timeSlotString format like "09:00-10:00" : take start time before '-'
                String start = timeSlotString.contains("-") ? timeSlotString.split("-")[0] : timeSlotString;
                java.time.LocalTime startTime = java.time.LocalTime.parse(start);
                // todo: consider end (e.g. slot with more than 1 hour)
                if (StringUtils.isBlank(amOrPm)) {
                    return true; // not specified whether AM or PM : all slots allowed
                } else {
                    return (amOrPm.toLowerCase().contains("am") ? startTime.isBefore(LocalTime.NOON) :
                            (amOrPm.toLowerCase().contains("pm") && (startTime.isAfter(LocalTime.NOON)
                             || startTime.equals(LocalTime.of(12, 0)))));
                }
            } catch (Exception e) {
                return false;
            }
        });
    }).collect(Collectors.toList());
}

// 13. **filterDoctorByNameAndTime Method**:
//    - Filters doctors based on their name and the specified time period (AM/PM).
//    - Fetches doctors based on partial name matching and filters the results to include only those available during the specified time period.
//    - Instruction: Ensure that the method correctly filters doctors based on the given name and time of day (AM/PM).
@Transactional
public Map<String, Object> filterDoctorByNameAndTime(String name, String amOrPm) {
    List<Doctor> doctors = doctorRepository.findByNameLike(name);
    Map<String, Object> result = new HashMap<>();
    result.put("doctors", filterDoctorsByTime(doctors, amOrPm));
    return result;
}

// 14. **filterDoctorByNameAndSpecialty Method**:
//    - Filters doctors by name and specialty.
//    - It ensures that the resulting list of doctors matches both the name (case-insensitive) and the specified specialty.
//    - Instruction: Ensure that both name and specialty are considered when filtering doctors.
    @Transactional
    public Map<String, Object> filterDoctorByNameAndSpecialty(String name, String specialty) {
        List<Doctor> doctors = doctorRepository.findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(name, specialty);
        Map<String, Object> result = new HashMap<>();
        result.put("doctors", doctors);
        return result;
    }

// 15. **filterDoctorByTimeAndSpecialty Method**:
//    - Filters doctors based on their specialty and availability during a specific time period (AM/PM).
//    - Fetches doctors based on the specified specialty and filters them based on their available time slots for AM/PM.
//    - Instruction: Ensure the time filtering is accurately applied based on the given specialty and time period (AM/PM).
    @Transactional
    public Map<String, Object> filterDoctorByTimeAndSpecialty(String specialty, String amOrPm) {
        List<Doctor> doctors = doctorRepository.findBySpecialtyIgnoreCase(specialty);
        Map<String, Object> result = new HashMap<>();
        result.put("doctors", filterDoctorsByTime(doctors, amOrPm));
        return result;
    }

// 16. **filterDoctorBySpecialty Method**:
//    - Filters doctors based on their specialty.
//    - This method fetches all doctors matching the specified specialty and returns them.
//    - Instruction: Make sure the filtering logic works for case-insensitive specialty matching.
    @Transactional
    public Map<String, Object> filterDoctorBySpecialty(String specialty) {
        List<Doctor> doctors = doctorRepository.findBySpecialtyIgnoreCase(specialty);
        Map<String, Object> result = new HashMap<>();
        result.put("doctors", doctors);
        return result;
    }

// 17. **filterDoctorsByTime Method**:
//    - Filters all doctors based on their availability during a specific time period (AM/PM).
//    - The method checks all doctors' available times and returns those available during the specified time period.
//    - Instruction: Ensure proper filtering logic to handle AM/PM time periods.
    @Transactional
    public Map<String, Object> filterDoctorsByTime(String amOrPm) {
        List<Doctor> doctors = doctorRepository.findAll();
        Map<String, Object> result = new HashMap<>();
        result.put("doctors", filterDoctorsByTime(doctors, amOrPm));
        return result;
    }

}
