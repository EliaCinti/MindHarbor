package it.uniroma2.mindharbor.model;

import it.uniroma2.mindharbor.exception.BookingNotAllowedException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a psychologist in the system. Extends the User class by adding
 * psychologist-specific attributes such as office location and hourly consultation cost.
 */
public class Psychologist extends User {
    private String office;
    private String hourlyCost;
    private List<Patient> patients;


    /**
     * Constructs a Psychologist with the specified details.
     *
     * @param username   The psychologist's username.
     * @param name       The psychologist's first name.
     * @param surname    The psychologist's last name.
     * @param gender     The psychologist's gender.
     * @param office     The office location of the psychologist.
     * @param hourlyCost The hourly cost charged by the psychologist.
     */
    public Psychologist(String username, String name, String surname, String gender, String office, String hourlyCost) {
        super(username, name, surname, gender);
        setOffice(office);
        setHourlyCost(hourlyCost);
    }

    /**
     * Returns the office location of the psychologist.
     *
     * @return The office location.
     */
    public String getOffice() {
        return office;
    }

    /**
     * Sets the office location for the psychologist.
     *
     * @param office The new office location.
     */
    public void setOffice(String office) {
        this.office = office;
    }

    /**
     * Returns the hourly consultation cost of the psychologist.
     *
     * @return The hourly cost.
     */
    public String getHourlyCost() {
        return hourlyCost;
    }

    /**
     * Sets the hourly consultation cost for the psychologist.
     *
     * @param hourlyCost The new hourly consultation cost.
     */
    public void setHourlyCost(String hourlyCost) {
        this.hourlyCost = hourlyCost;
    }

    /**
     * Returns a list of patients under the care of this psychologist.
     *
     * @return A list of patients.
     */
    public List<Patient> getPatients() {
        return patients;
    }

    /**
     * Sets the list of patients.
     *
     * @param patients the list of patients to be set
     */
    public void setPatients(List<Patient> patients) {
        this.patients = patients;
    }

    /**
     * Confirms an appointment for a specific patient, ensuring that the appointment does not
     * conflict with existing appointments and that the patient is under the care of this psychologist.
     *
     * @param appointment The appointment to be confirmed.
     * @param patient     The patient for whom the appointment is being confirmed.
     * @throws BookingNotAllowedException If the patient is not under the care of this psychologist,
     *                                    or if there is a time conflict with another appointment.
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

    /**
     * Retrieves all appointments for this psychologist on a specific date.
     *
     * @param date The date for which to retrieve appointments.
     * @return A list of appointments on the specified date.
     */
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
