package com.project.back_end.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDate;


// @Entity annotation:
//    - Marks the class as a JPA entity, meaning it represents a table in the database.
//    - Required for persistence frameworks (e.g., Hibernate) to map the class to a database table.

@Entity
public class Patient {

    protected Patient() {
    }

    public Patient(String username, String email, String password, String phone, String firstName, String lastName, LocalDate dateOfBirth, String address) {
        setUsername(username);
        setEmail(email);
        setPassword(password);
        setPhone(phone);
        setFirstName(firstName);
        setLastName(lastName);
        setAddress(address);
    }

    // 1. 'id' field:
//    - Type: private Long
//    - Description:
//      - Represents the unique identifier for each patient.
//      - The @Id annotation marks it as the primary key.
//      - The @GeneratedValue(strategy = GenerationType.IDENTITY) annotation auto-generates the ID value when a new record is inserted into the database.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    // 'username' field:
//    - Type: private String
//    - Description:
//      - Represents the username of the patient.
//      - Used to log into the system.
//      - @NotNull validation ensures that this field cannot be null when creating or updating a patient.
    @NotBlank(message = "username ist erforderlich")
    @Column(unique = true)
    private String username;

    // 4. 'password' field:
//    - Type: private String
//    - Description:
//      - Represents the patient's password for login authentication.
//      - The @NotNull annotation ensures that a password must be provided.
//      - The @Size(min = 6) annotation ensures that the password must be at least 6 characters long.
    @NotNull(message = "password ist erforderlich, darf nicht leer bleiben")
    @Size(min = 6, message = "password soll mindestens 6 Zeichen haben")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JsonIgnore
    private String password;

    // 3. 'email' field:
//    - Type: private String
//    - Description:
//      - Represents the patient's email address.
//      - The @NotNull annotation ensures that an email address must be provided.
//      - The @Email annotation validates that the email address follows a valid email format (e.g., patient@example.com).
    @NotBlank(message = "email ist erforderlich, darf nicht leer bleiben")
    @Email(message = "email soll eine gültige Mail-Adresse sein")
    private String email;

    // 5. 'phone' field:
//    - Type: private String
//    - Description:
//      - Represents the patient's phone number.
//      - The @NotNull annotation ensures that a phone number must be provided.
//      - The @Pattern(regexp = "^[0-9]{10}$") annotation validates that the phone number must be exactly 10 digits long.
    @NotNull(message = "phone ist erforderlich, darf nicht leer bleiben")
    @Pattern(regexp = "^[0-9]{10}$", message = "phone soll exakt 10 Ziffern haben" )
    private String phone;

    // 2. 'firstName' field:
//    - Type: private String
//    - Description:
//      - Represents the patient's first name.
//      - The @NotNull annotation ensures that the patient's name is required.
//      - The @Size(min = 3, max = 100) annotation ensures that the first name length is between 3 and 50 characters.
//      - Provides validation for correct input and user experience.
    @NotBlank(message = "firstName ist erforderlich, soll nicht leer sein")
    @Size(min = 3, max = 50, message = "firstName soll mindestens 3, maximal 50 Zeichen haben")
    private String firstName;

    // 'lastName' field:
//    - Type: private String
//    - Description:
//      - Represents the patient's last name.
//      - The @NotNull annotation ensures that the patient's name is required.
//      - The @Size(min = 3, max = 100) annotation ensures that the last name length is between 3 and 50 characters.
//      - Provides validation for correct input and user experience.
    @NotBlank(message = "lastName ist erforderlich, soll nicht leer sein")
    @Size(min = 3, max = 50, message = "lastName soll mindestens 3, maximal 50 Zeichen haben")
    private String lastName;

    @NotNull(message = "dateOfBirth ist erforderlich")
    @Past(message = "dateOfBirth soll in der Vergangenheit liegen")
    private LocalDate dateOfBirth;

    // 6. 'address' field:
//    - Type: private String
//    - Description:
//      - Represents the patient's address.
//      - The @NotNull annotation ensures that the address must be provided.
//      - The @Size(max = 255) annotation ensures that the address does not exceed 255 characters in length, providing validation for the address input.
    @NotBlank(message = "address ist erforderlich")
    @Size(max = 255, message = "address darf nicht länger als 255 Zeichen sein")
    private String address;

// 7. Getters and Setters:
//    - Standard getter and setter methods are provided for all fields: id, name, email, password, phone, and address.
//    - These methods allow access and modification of the fields of the Patient class.

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() { return lastName; }

    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
