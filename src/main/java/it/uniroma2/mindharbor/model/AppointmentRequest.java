package it.uniroma2.mindharbor.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class AppointmentRequest extends Appointment{
    private Patient patient;
    private Psychologist psychologist;

    /**
     * Constructs an Appointment with the given parameters.
     *
     * @param id          The unique identifier of the appointment.
     * @param date        The date of the appointment.
     * @param time        The time of the appointment.
     * @param description A brief description of the appointment.
     */
    public AppointmentRequest(int id, LocalDate date, LocalTime time, String description, Patient patient, Psychologist psychologist) {
        super(id, date, time, description);
        this.patient = patient;
        this.psychologist = psychologist;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Psychologist getPsychologist() {
        return psychologist;
    }

    public void setPsychologist(Psychologist psychologist) {
        this.psychologist = psychologist;
    }
}
