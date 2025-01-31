package it.uniroma2.mindharbor.beans;

import java.time.LocalDate;

public class PatientBean extends UserBean {
    private LocalDate birthDate;

    public PatientBean(String username, String password, String type, String name, String surname, String gender ,LocalDate birthdate) {
        super(username, password, type, name, surname, gender);
        this.birthDate = birthdate;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
}
