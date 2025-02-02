package it.uniroma2.mindharbor.beans;

import java.time.LocalDate;

/**
 * The {@code PatientBean} class represents a patient in the system.
 * It extends the {@code UserBean} class and includes additional information
 * about the patient's birth date.
 */
public class PatientBean extends UserBean {
    private LocalDate birthDate;

    /**
     * Constructs a {@code PatientBean} instance.
     *
     * @param username  The patient's username.
     * @param password  The patient's password.
     * @param type      The user type (e.g., "patient").
     * @param name      The patient's first name.
     * @param surname   The patient's last name.
     * @param gender    The patient's gender.
     * @param birthdate The patient's birth date.
     */
    public PatientBean(String username, String password, String type, String name, String surname, String gender, LocalDate birthdate) {
        super(username, password, type, name, surname, gender);
        this.birthDate = birthdate;
    }

    /**
     * Gets the patient's birth date.
     *
     * @return The birth date of the patient.
     */
    public LocalDate getBirthDate() {
        return birthDate;
    }

    /**
     * Sets the patient's birth date.
     *
     * @param birthDate The new birth date of the patient.
     */
    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
}
