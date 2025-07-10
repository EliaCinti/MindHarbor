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
    private boolean notified;

    /**
     * Constructs an Appointment with the given parameters.
     * Validates that all required fields are valid.
     *
     * @param id          The unique identifier of the appointment.
     * @param date        The date of the appointment.
     * @param time        The time of the appointment.
     * @param description A brief description of the appointment.
     * @param notified    Whether the appointment has been notified to the patient.
     * @throws IllegalArgumentException if any validation fails.
     */
    public Appointment(int id, LocalDate date, LocalTime time, String description, boolean notified) {
        validateAppointment(id, date, time, description);
        this.id = id;
        this.date = date;
        this.time = time;
        this.description = description;
        this.notified = notified;
    }

    /**
     * Convenience constructor that creates an appointment with default notification status.
     * <p>
     * This constructor delegates to the full constructor, explicitly setting the
     * 'notified' parameter to false. It's designed to simplify appointment creation
     * when a psychologist accepts a new appointment request, where notifications
     * are always initially unread.
     * </p>
     *
     * @param id          The unique identifier of the appointment.
     * @param date        The date of the appointment.
     * @param time        The time of the appointment.
     * @param description A brief description of the appointment.
     * @see #Appointment(int, LocalDate, LocalTime, String, boolean) The full constructor
     * @throws IllegalArgumentException if any validation fails.
     */
    public Appointment(int id, LocalDate date, LocalTime time, String description) {
        this(id, date, time, description, false);
    }

    /**
     * Validates that all fields of the appointment contain valid data.
     *
     * @param id          The appointment ID to validate
     * @param date        The appointment date to validate
     * @param time        The appointment time to validate
     * @param description The appointment description to validate
     * @throws IllegalArgumentException if any validation fails
     */
    private void validateAppointment(int id, LocalDate date, LocalTime time, String description) {
        if (id < 0) {
            throw new IllegalArgumentException("Appointment ID cannot be negative");
        }

        if (date == null) {
            throw new IllegalArgumentException("Appointment date cannot be null");
        }

        if (time == null) {
            throw new IllegalArgumentException("Appointment time cannot be null");
        }

        if (description == null) {
            throw new IllegalArgumentException("Appointment description cannot be null");
        }
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
     * Checks whether this appointment has been notified to the patient.
     * <p>
     * An appointment is considered "notified" when the patient has viewed it
     * after it was confirmed by a psychologist. This method is used to determine
     * if a notification indicator should be displayed in the patient's home page.
     * </p>
     *
     * @return {@code true} if the appointment has been viewed by the patient,
     *         {@code false} if it still requires notification
     */
    public boolean isNotified() {
        return notified;
    }

    /**
     * Sets the notification status of this appointment.
     * <p>
     * This method allows explicitly setting whether the appointment has been
     * notified to the patient. It is typically used when loading appointment data
     * from persistence or when managing notification states programmatically.
     * </p>
     *
     * @param notified the new notification status: {@code true} if the appointment
     *                should be marked as viewed by the patient, {@code false} otherwise
     * @see #markAsNotified() A convenience method when you only need to mark as notified
     */
    public void setNotified(boolean notified) {
        this.notified = notified;
    }

    /**
     * Marks this appointment as notified to the patient.
     * <p>
     * This is a convenience method that sets the notification status to {@code true}.
     * It's typically called when a patient views their appointment list, indicating
     * they've seen all new appointments and should no longer receive notifications
     * for them.
     * </p>
     *
     * @see #setNotified(boolean) The general method for setting notification status
     */
    public void markAsNotified() {
        this.notified = true;
    }

    public boolean isDataEquivalent(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Appointment that = (Appointment) o;
        return getId() == that.getId() &&
                isNotified() == that.isNotified() &&
                getDate().equals(that.getDate()) &&
                getTime().equals(that.getTime()) &&
                getDescription().equals(that.getDescription());
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
