package it.uniroma2.mindharbor.model;

import it.uniroma2.mindharbor.exception.BookingNotAllowedException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Patient extends User {
    private String psychologist;
    private LocalDate birthday;
    private List<Appointment> appointmentList = new ArrayList<>();
    public Patient(String username, String name, String surname, String gender, String psychologist, LocalDate birthday) {
        super(username, name, surname, gender);
        setPsychologist(psychologist);
        setBirthday(birthday);
    }

    public String getPsychologist() {
        return psychologist;
    }

    public void setPsychologist(String psychologist) {
        this.psychologist = psychologist;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public List<Appointment> getAppointmentList() {
        return appointmentList;
    }

    public void setAppointmentList(List<Appointment> appointmentList) {
        this.appointmentList = appointmentList;
    }

    /**
     * Adds an appointment to the patient's appointment list if it is not already present.
     *
     * @param appointment The appointment to be added.
     * @throws BookingNotAllowedException If the appointment is already in the list.
     */
    public void addAppointment(Appointment appointment) throws BookingNotAllowedException {
        if (appointmentList.contains(appointment)) {
            throw new BookingNotAllowedException();
        }
        appointmentList.add(appointment);
    }

    /**
     * Retrieves an appointment for a specific date.
     *
     * @param date The date of the appointment to find.
     * @return The appointment on the given date, or null if no appointment is found.
     */
    public Appointment getAppointmentByDate(LocalDate date) {
        for (Appointment appointment : appointmentList) {
            if (appointment.getDate().isEqual(date))
                return appointment;
        }
        return null;
    }

    /**
     * Retrieves a list of appointments scheduled before the given date.
     *
     * @param date The reference date.
     * @return A list of appointments that occur before the given date.
     */
    public List<Appointment> getAppointmentsBeforeDate(LocalDate date) {
        List<Appointment> appointmentListBefore = new ArrayList<>();
        for (Appointment appointment : appointmentList) {
            if(appointment.getDate().isBefore(date))
                appointmentListBefore.add(appointment);
        }
        return appointmentListBefore;
    }

    /**
     * Retrieves a list of appointments scheduled after the given date.
     *
     * @param date The reference date.
     * @return A list of appointments that occur after the given date.
     */
    public List<Appointment> getAppointmentsAfterDate(LocalDate date) {
        List<Appointment> appointmentListAfter = new ArrayList<>();
        for (Appointment appointment : appointmentList) {
            if(appointment.getDate().isAfter(date))
                appointmentListAfter.add(appointment);
        }
        return appointmentListAfter;
    }

    /**
     * Checks if two patients are equal based on their username.
     *
     * @param obj The object to compare.
     * @return true if the given object is a Patient and has the same username, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Patient patient)
            return patient.getUsername().equals(this.getUsername());
        return false;
    }

    /**
     * Returns the hash code of the patient.
     *
     * @return The hash code.
     */
    @Override
    public int hashCode(){
        return super.hashCode();
    }

}
