package it.uniroma2.mindharbor.model;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Represents an appointment request that links a specific appointment with the patient and psychologist involved.
 * <p>
 * This class extends the basic {@link Appointment} functionality by adding references to the
 * patient who is requesting the appointment and the psychologist who will provide the consultation.
 * It serves as a bridge between the appointment scheduling system and the user management system.
 * </p>
 * <p>
 * AppointmentRequest is particularly useful in workflows where:
 * <ul>
 *   <li>Patients request appointments with specific psychologists</li>
 *   <li>The system needs to track appointment ownership and relationships</li>
 *   <li>Validation is required to ensure patients can only book with their assigned psychologist</li>
 *   <li>Appointment confirmation workflows need access to both parties</li>
 * </ul>
 * </p>
 *
 * @see Appointment for the base appointment functionality
 * @see Patient for patient-specific operations
 * @see Psychologist for psychologist-specific operations
 */
public class AppointmentRequest extends Appointment{
    private Patient patient;
    private Psychologist psychologist;

    /**
     * Constructs an AppointmentRequest with the specified appointment details and associated users.
     * <p>
     * This constructor creates a complete appointment request that includes both the scheduling
     * information (inherited from Appointment) and the user relationship information. The
     * appointment is created with the default notification status (not notified).
     * </p>
     *
     * @param id           The unique identifier of the appointment
     * @param date         The date when the appointment is scheduled
     * @param time         The time when the appointment is scheduled
     * @param description  A brief description or notes about the appointment
     * @param patient      The patient who is requesting the appointment
     * @param psychologist The psychologist who will conduct the consultation
     * @throws IllegalArgumentException if any appointment validation fails
     * @see Appointment#Appointment(int, LocalDate, LocalTime, String) for validation rules
     */
    public AppointmentRequest(int id, LocalDate date, LocalTime time, String description, Patient patient, Psychologist psychologist) {
        super(id, date, time, description);
        this.patient = patient;
        this.psychologist = psychologist;
    }

    /**
     * Gets the patient associated with this appointment request.
     * <p>
     * The patient represents the individual who is seeking psychological consultation
     * and has requested this specific appointment time slot.
     * </p>
     *
     * @return The patient who requested the appointment
     */
    public Patient getPatient() {
        return patient;
    }
    /**
     * Sets the patient for this appointment request.
     * <p>
     * This method allows updating the patient association, which may be useful
     * in scenarios where appointment ownership needs to be transferred or
     * corrected during the appointment management process.
     * </p>
     *
     * @param patient The patient to associate with this appointment request
     */
    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    /**
     * Gets the psychologist associated with this appointment request.
     * <p>
     * The psychologist represents the healthcare professional who will
     * conduct the consultation session at the scheduled appointment time.
     * </p>
     *
     * @return The psychologist who will conduct the appointment
     */
    public Psychologist getPsychologist() {
        return psychologist;
    }

    /**
     * Sets the psychologist for this appointment request.
     * <p>
     * This method allows updating the psychologist association, which may be
     * necessary in cases where the assigned psychologist changes or when
     * correcting appointment assignments during the scheduling process.
     * </p>
     *
     * @param psychologist The psychologist to associate with this appointment request
     */
    public void setPsychologist(Psychologist psychologist) {
        this.psychologist = psychologist;
    }
}
