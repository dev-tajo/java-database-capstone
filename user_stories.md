## Admin User Stories

<!-- Admins interagieren mit dem System um sich selbst ein- und auszuloggen, Ärzte einzutragen, inaktiv bzw. nicht verfügbar zu schalten und Statistiken abzufragen. -->

# Admin: Sich mit Ihrem Benutzernamen und Passwort sicher im Portal anmelden, um die Plattform zu verwalten

**Title:**
_As a Admin, I want mich sicher im Portal anmelden mit meinem Benutzernamen und Passwort, so that ich die Plattform verwalten kann._

**Acceptance Criteria:**
1. Ein Admin kann sich (erfolgreich) anmelden und Verwaltungsaufgaben durchführen.
2. Ein Patient kann sich anmelden, aber keine Verwaltungsaufgaben durchführen.
3. Ein Arzt kann sich anmelden, aber nur ihm erlaubte Verwaltungsaufgaben durchführen.

**Priority:** Medium
**Story Points:** 7
**Notes:**
Mindestens 1 Admin muss dem System ab der Erst-Initialisierung bekannt sein. Dieser kann dann weitere Admins im System anlegen. Das initiale Passwort sollte sofort geändert werden (müssen).
Der Zugang soll gegen Brute Force Angriffe u.ä. geschützt sein, so dass z.B. nach Fehleingaben die nächsten Versuchsmöglichkeiten immer stärker verzögert werden.


# Admin: Sich vom Portal abmelden, um den Systemzugang zu schützen

**Title:**
_As a Admin, I want mich beim Portal abmelden, so that kein anderer Nutzer meinen Zugang missbrauchen kann._

**Acceptance Criteria:**
1. Ein Admin kann sich erfolgreich abmelden.
2. Nach dem Abmelden können keine Verwaltungsaufgaben mehr vom Nutzer getätigt werden.
3. Ein Wieder-Anmelden erfordert erneut Benutzername und Passwort.

**Priority:** Medium
**Story Points:** 1
**Notes:**


# Admin: Ärzte im Portal hinzufügen

**Title:**
_As a Admin, I want Ärzte im Portal hinzufügen, so that diese Ärzte das Portal nutzen können._

**Acceptance Criteria:**
1. Ein Admin kann Ärzte im System anlegen.
2. Ein Patient oder ein Arzt kann (andere) Ärzte nicht anlegen.
4. Der (neu angelegte) Arzt kann anschließend die Plattform nutzen.

**Priority:** High
**Story Points:** 3
**Notes:**


# Admin: Das Profil eines Arztes im Portal auf inaktiv setzen

**Title:**
_As a Admin, I want das Profil eines Arztes auf inaktiv setzen können, so that dieser Arzt seinen Zugang nicht mehr nutzen kann._

**Acceptance Criteria:**
1. Ein Admin kann einen Arzt im System ab einem bestimmten Zeitpunkt auf inaktiv setzen.
2. Ein Arzt kann sich nicht selbst (oder andere Ärzte) auf inaktiv setzen.
2. Der Arzt kann sich ab dem angegebenen Zeitpunkt nicht mehr im Portal anmelden.
3. Der Arzt kann - falls er zum Zeitpunkt des Inaktiv-Setzens noch angemeldet war - ab dann keine Einträge mehr verändern.
4. Die Patienten mit noch anstehenden Terminen für diesen Arzt werden informiert, dass sie neue Termine benötigen. Idealerweise mit einem Vorschlag für einen neuen Termin bei einem anderen Arzt.

**Priority:** Middle
**Story Points:** 12
**Notes:**
Es ist mit den Stakeholdern zu klären, wie mit Patienten verfahren wird, die diesen Arzt als ihren aktuellen Arzt kennen (ggf. automatisch zu anderem Arzt "mappen"? Berücksichtigung der Auslastung?)
Optional: Den Patienten alternative Termin-Vorschläge machen (zeitlich begrenzt reserviert) für einen anderen Arzt.


# Admin: Eine gespeicherte Prozedur in MySQL CLI ausführen, um die Anzahl der Termine pro Monat zu ermitteln und Nutzungsstatistiken zu verfolgen

**Title:**
_As a Admin, I want eine gespeicherte Prozedur im SQL CLI ausführen, so that ich die Anzahl der Termine pro Monat ermitteln und die Nutzerstatistik verfolgen kann._

**Acceptance Criteria:**
1. Ein Admin kann die monatliche Statistik für einen zurückliegenden Zeitraum abfragen.

**Priority:** Low
**Story Points:** 5
**Notes:**
Es muss entschieden werden, ob und welche Filterkriterien bei der Abfrage angegeben werden können.

-------------

## Patient User Stories

<!-- Patienten interagieren mit dem Portal, um Ärzte zu finden und Termine zu verwalten. -->


# Patient: Eine Liste von Ärzten anzeigen, ohne sich anzumelden, um Optionen vor der Registrierung zu erkunden

**Title:**
_As a Patient, I want die Anzeige der Liste von verfügbaren Ärzten ohne mich anzumelden, so that ich die Optionen vor der Registrierung erkunden kann._

**Acceptance Criteria:**
1. Ohne Anmeldung wird die Liste der im System verfügbaren Ärzte ausgegeben.
2. Die Liste ist leer wenn im System (noch) keine Ärzte verfügbar sind.
3. Ausgehend von der Liste der Ärzte kann sich der unangemeldete Nutzer Details zu den verfügbaren Ärzten anzeigen lassen.  

**Priority:** Low
**Story Points:** 7
**Notes:** 


# Patient: Sich mit Ihrer E-Mail und Ihrem Passwort registrieren, um Termine zu buchen

**Title:**
_As a Patient, I want mich mit eMail-Adresse und Passwort am Portal registrieren, so that ich später mögliche Termine ansehen und buchen kann._

**Acceptance Criteria:**
1. Ein Nutzer kann sich als Patient mit einer gültigen eMail-Adresse und einem Passwort beim Portal registrieren.
2. Ein Nutzer kann sich mit einer bereits im System bekannter eMail-Adresse nicht registrieren (ggf. Hinweis auf Passwort-Rücksetzen).
3. Ein Nutzer kann sich mit ungültiger eMail-Adresse oder ungültigem Passwort nicht registrieren.
4. Die angegebene Mail-Adresse wird über eine Mail an diese Mail-Adresse und einem enthaltenen Verifikations-Link auf Gültigkeit geprüft. 

**Priority:** Middle
**Story Points:** 9
**Notes:**
Es sind Regeln für zu akzeptierende Passwörter festzulegen.


# Patient: Sich im Portal einloggen, um Ihre Termine zu verwalten

**Title:**
_As a Patient, I want mich mit eMail-Adresse und Passwort einloggen, so that ich meine Termine verwalten kann._

**Acceptance Criteria:**
1. Der Patient kann sich mit eMail-Adresse und seinem Passwort anmelden.
2. Der Patient kann nach Anmeldung mögliche Termine bei verfügbaren Ärzten sehen.
3. Der Patient kann nach Anmeldung freie Termine bei verfügbaren Ärzten buchen.
4. Der Patient kann nach Anmeldung gebuchte Termine bei verfügbaren Ärzten (unter Berücksichtigung einer Stornierungsfrist bis zum Termin) wieder stornieren.

**Priority:** High
**Story Points:** 9
**Notes:**
Es sind Stornierungsfristen für die Stornierung von Terminen festzulegen. Ggf. sollte die Art des Termins bei dieser Stornierungsfrist berücksichtigt werden.
Dem Patienten muss bei der Buchung eines Termins mitgeteilt werden, welche Stornierungsfristen einzuhalten sind und welche Kosten mit einer späteren Stornierung oder mit einem Versäumen des Termins verbunden sind.


# Patient: Sich aus dem Portal ausloggen, um Ihr Konto zu sichern

**Title:**
_As a Patient, I want mich ausloggen, so that mein Konto gesichert ist._

**Acceptance Criteria:**
1. Der Patient kann sich ausloggen.
2. Ein Nutzer ohne Anmeldung kann keine Aktionen ausführen, die eine Anmeldung erfordern würden.

**Priority:** Low
**Story Points:** 1
**Notes:**


# Patient: Sich einloggen und einen einstündigen Termin buchen, um mit einem Arzt zu konsultieren

**Title:**
_As a Patient, I want mich einloggen und einen einstündigen Termin buchen, so that ich einen Arzt konsultieren kann._

**Acceptance Criteria:**
1. Der Patient kann sich einloggen
2. Der Patient kann einen (einstündigen) Termin bei einem der verfügbaren Arzt buchen.

**Priority:** Middle
**Story Points:** 5
**Notes:**
Es sollen bei der Anzeige der verfügbaren Termine des betreffenden Arztes sowohl bereits belegte Termine als auch die vom jeweiligen Arzt reservierten Zeiten berücksichtigt werden. 


# Patient: Meine bevorstehenden Termine einsehen, damit ich mich entsprechend vorbereiten kann

**Title:**
_As a Patient, I want meine bevorstehenden Termine einsehen, so that ich mich entsprechend vorbereiten kann._

**Acceptance Criteria:**
1. Der Patient kann die bevorstehenden Termine sehen.
2. Der Patient kann ggf. vorhandene Details zu den Terminen einsehen (mitzubringende Unterlagen, geplante Termindauer) .

**Priority:** Middle
**Story Points:** 7
**Notes:**
Es ist zu definieren, welcher Zeitraum ab dem aktuellen Datum als Dauer für "bevorstehend" gilt (1 Tag, 3 Tage, 1 Woche ?)


-------------

## Doctor User Stories

<!-- Ärzte verwalten ihre Verfügbarkeit und Termine. -->

# Arzt: Sich im Portal einloggen, um meine eigenen Termine zu verwalten

**Title:**
_As a Arzt, I want mich im Portal einloggen, so that ich meine Termine verwalten kann._

**Acceptance Criteria:**
1. Der Arzt kann sich einloggen.
2. Der eingeloggte Arzt kann seine eigenen Arzt-Termine bearbeiten (z.B. während eines Termins Nachfolgetermine eintragen, Notizen hinterlegen, ggf. Termine stornieren).
3. Ein User kann - wenn nicht er als Arzt eingeloggt ist - keine (Arzt-)Termine ändern.
4. Der eingeloggte Arzt kann als eingeloggter User die Termine seiner Arzt-Kollegen nur eingeschränkt ändern (z.B. Hinweise an den behandelnden Arzt hinterlegen).

**Priority:** High
**Story Points:** 5
**Notes:**
Wer ausser dem betroffenen Arzt selbst sollte dessen Termine auch ändern können und in welchem Umfang? (z.B. Admin: löschen und dabei Patient und Arzt informieren, anderer Arzt: Hinweise hinterlegen) 


# Arzt: Sich aus dem Portal ausloggen, um meine Daten zu schützen

**Title:**
_As a Arzt, I want mich ausloggen, so that ich meine Daten geschützt sind._

**Acceptance Criteria:**
1. Arzt kann sich ausloggen
2. Ein User kann - wenn nicht er als Arzt eingeloggt ist - keine (Arzt-)Termine ändern.
3. Ein User kann - wenn nicht er als Arzt eingeloggt ist - keine Details zu den Terminen sehen.

**Priority:** High
**Story Points:** 3
**Notes:**
Festlegen, wer ausser dem betroffenen Arzt selbst auch Details zu dessen Terminen sehen können sollte. (Admin? andere Ärzte?) 


# Arzt: Meinen Terminkalender einsehen, um organisiert zu bleiben

**Title:**
_As a Arzt, I want mich meinen Terminkalender einsehen, so that ich organisiert bleibe._
Ein
**Acceptance Criteria:**
1. Der Arzt kann seine Termine in Form eines Terminkalenders sehen.
2. Der Arzt kann Details zu seinen Terminen sehen.

**Priority:** High
**Story Points:** 5
**Notes:**
Es ist festzulegen wie die Termine (ohne/mit Details) dargestellt werden.


# Arzt: Ihre Nichtverfügbarkeit markieren, um Patienten nur die verfügbaren Zeiten anzuzeigen

**Title:**
_As a Arzt, I want in meinem Terminkalender meine Nichtverfügbarkeit markieren, so that Patienten nur die verfügbaren Zeiten angezeigt bekommen._

**Acceptance Criteria:**
1. Der Arzt kann seine Nichtverfügbarkeits-Zeiten in seinen Terminkalenders eintragen.
2. Alle User - auch nicht eingeloggte - können die dann noch verfügbaren Zeiten des Arztes sehen.

**Priority:** Medium
**Story Points:** 5
**Notes:**
Außer dem zuständigen Arzt, dem zugehörigen Patienten sowie den Admins sollen keine Details zu dem jeweiligen Termin des Arztes einsehbar sein (nur dass er belegt ist). 


# Arzt: Ihr Profil mit Fachrichtung und Kontaktdaten aktualisieren, damit Patienten aktuelle Informationen haben

**Title:**
_As a Arzt, I want mein Profil und meine Kontaktdaten aktualisieren können, so that Patienten aktuelle Informationen bekommen._

**Acceptance Criteria:**
1. Der Arzt kann sein Profil und seine Kontaktdaten selbst ändern.
2. Alle User - auch nicht eingeloggte - können das Profil des Ärztes sehen.

**Priority:** Low
**Story Points:** 3
**Notes:**


# Arzt: Die Patientendetails für bevorstehende Termine einsehen, damit ich vorbereitet sein kann

**Title:**
_As a Arzt, I want die Patientendetails für bevorstehende Termine einsehen, so that ich mich vorbereiten kann._

**Acceptance Criteria:**
1. Der Arzt kann die Details zu den Terminen und die Details zu seinen Patienten einsehen.
2. Der Patient kann nur seine eigenen Termine und vom Arzt freigegebene Termin-Details einsehen.

**Priority:** Medium
**Story Points:** 9
**Notes:**
- Festlegen, ob andere Ärzte auch alle oder nur bestimmte Details zu (nicht eigenen) Patienten einsehen können (vermutlich ja, um bei Abwesenheit des zuständigen Arztes auch Hinweise an den Patienten geben zu können).
- Festlegen, welche Details ein Admin zu einem Termin oder zu Details über den Patienten einsehen kann, um ggf. Dringlichkeiten bei anstehenden Termin-Absagen einschätzen zu können oder Termine bei anderen Ärzten zu finden.

