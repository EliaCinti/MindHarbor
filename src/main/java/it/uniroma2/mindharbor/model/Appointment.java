package it.uniroma2.mindharbor.model;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Represents an appointment with a specific date, time, and description.
 */
public class Appointment {
    private final int id;
    private final LocalDate date;
    private final LocalTime time;
    private final String description;

    /**
     * Constructs an Appointment with the given parameters.
     *
     * @param id          The unique identifier of the appointment.
     * @param date        The date of the appointment.
     * @param time        The time of the appointment.
     * @param description A brief description of the appointment.
     */
    public Appointment(int id, LocalDate date, LocalTime time, String description) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.description = description;
    }

    /**
     * Gets the unique identifier of the appointment.
     *
     * @return The appointment ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the date of the appointment.
     *
     * @return The appointment date.
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Gets the time of the appointment.
     *
     * @return The appointment time.
     */
    public LocalTime getTime() {
        return time;
    }

    /**
     * Gets the description of the appointment.
     *
     * @return The appointment description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Checks if two appointments are equal based on their date.
     *
     * @param object The object to compare.
     * @return true if the object is an Appointment and has the same date, false otherwise.
     */
    @Override
    public boolean equals(Object object) {
        if(object instanceof Appointment appointment)
            return this.date.isEqual(appointment.getDate());
        return false;
    }

    /**
     * Returns a hash code for this appointment.
     *
     * @return The hash code based on the appointment date.
     */
    @Override
    public int hashCode() {
        return date.hashCode();
    }
}
