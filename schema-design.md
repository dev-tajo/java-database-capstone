## MySQL Database Design

### Database: smart_clinic

#### Table: Admins

| Spaltenname | Datentyp     | Einschränkungen             |
|-------------|--------------|-----------------------------|
| id          | BIGINT       | PRIMARY KEY, AUTO_INCREMENT |
| username    | VARCHAR(63)  | NOT EMPTY, UNIQUE           |
| password    | VARCHAR(126) | NOT EMPTY                   |
| email       | VARCHAR(126) | NOT EMPTY                   |
| phone       | VARCHAR(31)  |                             |

##### Beschreibung
- **id**: Eindeutige Identifikation in der Datenbanktabelle.
- **username**: eindeutiger Nutzername.
- **password**: (gehashtes) Passwort. 
- **email**: eMail-Adresse.
- **phone**: Telefonnummer.


#### Table: Doctors

| Spaltenname     | Datentyp     | Einschränkungen             |
|-----------------|--------------|-----------------------------|
| id              | BIGINT       | PRIMARY KEY, AUTO_INCREMENT |
| email           | VARCHAR(126) | NOT EMPTY, UNIQUE           |
| password        | VARCHAR(126) | NOT EMPTY                   |
| phone           | VARCHAR(31)  |                             |
| name            | VARCHAR(126) | NOT EMPTY                   |
| academic_degree | VARCHAR(31)  |                             |
| medical_license | VARCHAR(126) |                             |
| specialty       | VARCHAR(254) |                             |

##### Beschreibung
- **id**: Eindeutige Identifikation in der Datenbanktabelle.
- **email**: eMail-Adresse.
- **password**: (gehashtes) Passwort.
- **phone**: Telefonnummer.
- **name**: Name
- **academic_degree**: Akademischer Grad, z.B. "Dr. med.".
- **medical_license**: Approbation, z.B. "Arzt" oder "Psychotherapeut".
- **specialty**: Spezialisierung, z.B. "Allgemeinmedizin" oder "Chirurg".


#### Table: Patients

| Spaltenname   | Datentyp     | Einschränkungen             |
|---------------|--------------|-----------------------------|
| id            | BIGINT       | PRIMARY KEY, AUTO_INCREMENT |
| email         | VARCHAR(126) | NOT EMPTY, UNIQUE           |
| password      | VARCHAR(126) | NOT EMPTY                   |
| phone         | VARCHAR(31)  |                             |
| first_name    | VARCHAR(63)  | NOT EMPTY                   |
| last_name     | VARCHAR(63)  | NOT EMPTY                   |
| date_of_birth | DATE         | NOT NULL                    |
| address       | VARCHAR(254) | NOT EMPTY                   |

##### Beschreibung
- **id**: Eindeutige Identifikation in der Datenbanktabelle.
- **email**: eMail-Adresse.
- **password**: (gehashtes) Passwort.
- **phone**: Telefonnummer.
- **first_name**: Vorname.
- **last_name**: Nachname.
- **date_of_birth**: Geburtsdatum.
- **address**: Adresse.


#### Table: Appointments

| Spaltenname           | Datentyp                                    | Einschränkungen                               |
|-----------------------|---------------------------------------------|-----------------------------------------------|
| id                    | BIGINT                                      | PRIMARY KEY, AUTO_INCREMENT                   |
| doctor_fk             | BIGINT                                      | NOT NULL, FOREIGN KEY REFERENCES Doctors(id)  |
| patient_fk            | BIGINT                                      | NOT NULL, FOREIGN KEY REFERENCES Patients(id) |
| appointment_date_from | DATETIME                                    | NOT NULL                                      |
| appointment_duration  | TIME                                        | NOT NULL, DEFAULT('1:0:0')                    |
| reason                | VARCHAR(255)                                |                                               |
| result                | VARCHAR(255)                                |                                               |
| status                | ENUM('Scheduled', 'Completed', 'Cancelled') | NOT NULL, DEFAULT 'Scheduled'                 |

##### Beschreibung
- **id**: Eindeutige Identifikation für jeden Termin.
- **doctor_fk**: Verweist auf die DoctorID in der Tabelle `Doctors`, um die Beziehung zwischen Terminen und Arzt
  herzustellen.
- **patient_fk**: Verweist auf die PatientID in der Tabelle `Patients`, um die Beziehung zwischen Terminen und Patient
  herzustellen.
- **appointment_date_from**: Das Datum und die Uhrzeit des Termins (geplanter Beginn).
- **appointment_duration**: Die (geplante) Dauer des Termins.
- **reason**: Falls im Vorfeld bekannt, das geplante Thema der Konsultation.
- **result**: Das Ergebnis der Konsultation: z.B. Diagnose, Handlungsempfehlungen, ggf. geplante Wiedervorstellung.
- **status**: ENUM-Datentyp: es sind nur die Werte 'Scheduled', 'Completed' oder 'Cancelled' erlaubt.


## MongoDB Collection Design

### Collection: prescriptions
```json
{
  "_id": "ObjectId",
  "patientId": "ObjectId",
  "patientName": "String",
  "doctorId" : "ObjectId",
  "appointmentId": "ObjectId",
  "prescriptionDate": "date",
  "prescription": {
    "type": "array",
    "items": {
      "type": "object",
      "properties": {
        "medication": {
          "type": "string"
        },
        "dosage": {
          "type": "string"
        }
      }
    },
    "minItems": 1,
    "uniqueItems": true
  },
  "doctorNotes": "String",
  "required": [
    "patientId",
    "doctorId",
    "prescriptionDate",
    "prescription"
  ]
}
```
