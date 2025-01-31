package it.uniroma2.mindharbor.model;

import it.uniroma2.mindharbor.exception.BookingNotAllowedException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Psychologist extends User {
    private String office;
    private String hourlyCost;
    private List<Patient> patients;

    public Psychologist(String username, String name, String surname, String gender, String office, String hourlyCost) {
        super(username, name, surname, gender);
        setOffice(office);
        setHourlyCost(hourlyCost);
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public String getHourlyCost() {
        return hourlyCost;
    }

    public void setHourlyCost(String hourlyCost) {
        this.hourlyCost = hourlyCost;
    }

    public List<Patient> getPatients() {
        return patients;
    }

    /**
     * Confirms an appointment for a specific patient, adding it to their list of appointments.
     *
     * @param appointment The appointment to be confirmed.
     * @param patient     The patient for whom the appointment is being confirmed.
     * @throws BookingNotAllowedException If the patient is not under the care of this psychologist.
     */
    public void confirmAppointment(Appointment appointment, Patient patient) throws BookingNotAllowedException {
        if (patients == null || !patients.contains(patient)) {
            throw new BookingNotAllowedException();
        }
        List<Appointment> appointments = getAppointmentsByDate(appointment.getDate());
        for (Appointment a : appointments) {
            if (a.getTime().equals(appointment.getTime()))
                throw new BookingNotAllowedException();
        }
        patient.addAppointment(appointment);
    }

    public List<Appointment> getAppointmentsByDate(LocalDate date) {
        List<Appointment> appointments = new ArrayList<>();
        for (Patient patient : patients) {
            if (patient.getAppointmentByDate(date) != null) {
                appointments.add(patient.getAppointmentByDate(date));
            }
        }
        return appointments;
    }

}
