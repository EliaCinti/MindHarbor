package it.uniroma2.mindharbor.model;

import it.uniroma2.mindharbor.exception.BookingNotAllowedException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a patient in the system, extending the {@link User} with specific attributes and behaviors
 * pertinent to a patient, such as managing appointments and maintaining a relationship with a psychologist.
 */
public class Patient extends User {
    private String psychologist;
    private LocalDate birthday;
    private List<Appointment> appointmentList = new ArrayList<>();

    /**
     * Constructs a new patient with specified personal and medical details.
     *
     * @param username     the unique identifier for the patient.
     * @param name         the first name of the patient.
     * @param surname      the last name of the patient.
     * @param gender       the gender of the patient.
     * @param psychologist the psychologist assigned to the patient.
     * @param birthday     the birthdate of the patient.
     */
    public Patient(String username, String name, String surname, String gender, String psychologist, LocalDate birthday) {
        super(username, name, surname, gender);
        setPsychologist(psychologist);
        setBirthday(birthday);
    }

    /**
     * Retrieves the psychologist assigned to the patient.
     *
     * @return the psychologist's identifier.
     */
    public String getPsychologist() {
        return psychologist;
    }

    /**
     * Assigns a psychologist to the patient.
     *
     * @param psychologist the psychologist's identifier to set.
     */
    public void setPsychologist(String psychologist) {
        this.psychologist = psychologist;
    }

    /**
     * Retrieves the patient's birthday.
     *
     * @return the birthday as a LocalDate object.
     */
    public LocalDate getBirthday() {
        return birthday;
    }

    /**
     * Sets the patient's birthday.
     *
     * @param birthday the new birthday to set as a LocalDate object.
     */
    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    /**
     * Retrieves the list of appointments for the patient.
     *
     * @return a list of Appointment objects.
     */
    public List<Appointment> getAppointmentList() {
        return appointmentList;
    }

    /**
     * Sets the list of appointments for the patient.
     *
     * @param appointmentList the list of Appointment objects to set.
     */
    public void setAppointmentList(List<Appointment> appointmentList) {
        this.appointmentList = appointmentList;
    }

    /**
     * Adds an appointment to the patient's list if it doesn't already exist, ensuring no duplicates.
     *
     * @param appointment The appointment to be added.
     * @throws BookingNotAllowedException if the appointment already exists in the list.
     */
    public void addAppointment(Appointment appointment) throws BookingNotAllowedException {
        if (appointmentList.contains(appointment)) {
            throw new BookingNotAllowedException();
        }
        appointmentList.add(appointment);
    }


    /**
     * Finds an appointment on a specific date.
     *
     * @param date The date to search for an appointment.
     * @return The appointment if found, or null if there is no appointment on that date.
     */
    public Appointment getAppointmentByDate(LocalDate date) {
        for (Appointment appointment : appointmentList) {
            if (appointment.getDate().isEqual(date))
                return appointment;
        }
        return null;
    }


    /**
     * Retrieves all appointments before a given date.
     *
     * @param date The cutoff date.
     * @return A list of all appointments before the specified date.
     */
    public List<Appointment> getAppointmentsBeforeDate(LocalDate date) {
        List<Appointment> appointmentListBefore = new ArrayList<>();
        for (Appointment appointment : appointmentList) {
            if (appointment.getDate().isBefore(date))
                appointmentListBefore.add(appointment);
        }
        return appointmentListBefore;
    }


    /**
     * Retrieves all appointments after a given date.
     *
     * @param date The starting date.
     * @return A list of all appointments after the specified date.
     */
    public List<Appointment> getAppointmentsAfterDate(LocalDate date) {
        List<Appointment> appointmentListAfter = new ArrayList<>();
        for (Appointment appointment : appointmentList) {
            if (appointment.getDate().isAfter(date))
                appointmentListAfter.add(appointment);
        }
        return appointmentListAfter;
    }

    public boolean isDataEquivalent(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Patient patient = (Patient) o;
        return getUsername().equals(patient.getUsername()) &&
                getName().equals(patient.getName()) &&
                getSurname().equals(patient.getSurname()) &&
                getGender().equals(patient.getGender()) &&
                java.util.Objects.equals(getPsychologist(), patient.getPsychologist()) &&
                getBirthday().equals(patient.getBirthday());
    }

    /**
     * Compares this patient with another object to determine equality, based primarily on the username.
     *
     * @param obj The object to compare with this patient.
     * @return true if the other object is a Patient with the same username, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Patient patient)
            return patient.getUsername().equals(this.getUsername());
        return false;
    }


    /**
     * Generates a hash code for a patient, derived from the username.
     *
     * @return the hash code.
     */
    @Override
    public int hashCode() {
        return super.hashCode();
    }



}
