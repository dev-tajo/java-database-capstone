## MySQL Database Design

### Database: smart_clinic

#### Table: Admins

| Spaltenname     | Datentyp         | Einschränkungen                |
|-----------------|------------------|--------------------------------|
| AdminID         | INT              | PRIMARY KEY, AUTO_INCREMENT    |
| Username        | VARCHAR(63)      | NOT NULL                       |
| Email           | VARCHAR(126)     | UNIQUE                         |
| Permissions     | BIT(16)          |                                |

##### Beschreibung
- **AdminID**: Eindeutige Identifikation des Admin in der Datenbank.
- **FirstName**: Vorname des Admin.
- **LastName**: Nachname des Admin.
- **PhoneNumber**: Telefonnummer des Admin.
- **Email**: eMail-Adresse des Admin.
- **Permissions**: Rechte des Admin. (tbd.)


#### Table: Doctors

| Spaltenname     | Datentyp         | Einschränkungen                |
|-----------------|------------------|--------------------------------|
| DoctorID        | INT              | PRIMARY KEY, AUTO_INCREMENT    |
| FirstName       | VARCHAR(63)      | NOT NULL                       |
| LastName        | VARCHAR(63)      | NOT NULL                       |
| AcademicDegree  | VARCHAR(31)      |                                |
| MedicalLicense  | VARCHAR(126)     |                                |
| Specialty       | VARCHAR(254)     |                                |
| PhoneNumber     | VARCHAR(31)      |                                |
| Email           | VARCHAR(126)     | UNIQUE                         |

##### Beschreibung
- **DoctorID**: Eindeutige Identifikation des Arztes in der Datenbank.
- **FirstName**: Vorname des Arztes.
- **LastName**: Nachname des Arztes.
- **AcademicDegree**: Akademischer Grad, z.B. "Dr. med.".
- **MedicalLicense**: Approbation, z.B. "Arzt" oder "Psychotherapeut".
- **Specialty**: Spezialisierung, z.B. "Allgemeinmedizin" oder "Chirurg".
- **PhoneNumber**: Telefonnummer des Arztes.
- **Email**: eMail-Adresse des Arztes.


#### Table: Patients

| Spaltenname     | Datentyp         | Einschränkungen                |
|-----------------|------------------|--------------------------------|
| PatientID       | INT              | PRIMARY KEY, AUTO_INCREMENT    |
| FirstName       | VARCHAR(63)      | NOT NULL                       |
| LastName        | VARCHAR(63)      | NOT NULL                       |
| DateOfBirth     | DATE             | NOT NULL                       |
| PhoneNumber     | VARCHAR(31)      |                                |
| Email           | VARCHAR(126)     | UNIQUE                         |

##### Beschreibung
- **PatientID**: Eindeutige Identifikation des Patienten in der Datenbank.
- **FirstName**: Vorname des Patienten.
- **LastName**: Nachname des Patienten.
- **DateOfBirth**: Geburtsdatum des Patienten.
- **PhoneNumber**: Telefonnummer des Patienten.
- **Email**: eMail-Adresse des Patienten.


#### Table: Appointments

| Spaltenname     | Datentyp         | Einschränkungen                |
|-----------------|------------------|--------------------------------|
| AppointmentID   | INT              | PRIMARY KEY, AUTO_INCREMENT    |
| PatientID       | INT              | NOT NULL, FOREIGN KEY REFERENCES Patients(PatientID) |
| DoctorID        | INT              | NOT NULL, FOREIGN KEY REFERENCES Doctors(DoctorID)   |
| AppointmentDateFrom | DATETIME     | NOT NULL                       |
| AppointmentDateTo | DATETIME       | NOT NULL                       |
| Reason          | VARCHAR(255)     |                                |
| Result          | VARCHAR(255)     |                                |
| Status          | ENUM('Scheduled', 'Completed', 'Cancelled') | NOT NULL, DEFAULT 'Scheduled' |

##### Beschreibung
- **AppointmentID**: Eindeutige Identifikation für jeden Termin.
- **PatientID**: Verweist auf die PatientID in der Tabelle `Patients`, um die Beziehung zwischen Terminen und Patient herzustellen.
- **DoctorID**: Verweist auf die DoctorID in der Tabelle `Doctors`, um die Beziehung zwischen Terminen und Arzt herzustellen.
- **AppointmentDateFrom**: Das Datum und die Uhrzeit des Termins (geplanter Beginn).
- **AppointmentDateTo**: Das Datum und die Uhrzeit des Termins (geplantes Ende).
- **Reason**: Falls im Vorfeld bekannt, das geplante Thema der Konsultation.
- **Result**: Das Ergebnis der Konsultation: z.B. Diagnose, Handlungsempfehlungen, ggf. geplante Wiedervorstellung.
- **Status**: ENUM-Datentyp: es sind nur die Werte 'Scheduled', 'Completed' oder 'Cancelled' erlaubt.



## MongoDB Collection Design

### Collection: prescriptions
```json
{
  "_id": "ObjectId",
  "patientID": "ObjectId",
  "patientName": "String",
  "doctorID" : "ObjectID",
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
    "patientID",
    "doctorID",
    "prescriptionDate",
    "prescription"
  ]
}
```
