package it.uniroma2.mindharbor.dao;

import it.uniroma2.mindharbor.exception.DAOException;
import it.uniroma2.mindharbor.model.Appointment;

import java.time.LocalDate;
import java.util.List;

/**
 * Data Access Object interface for Appointment entities.
 * <p>
 * This interface defines the contract for all operations related to appointment data persistence.
 * Implementations will provide specific logic for different storage mechanisms (e.g., CSV, MySQL).
 * </p>
 */
public interface AppointmentDao {

    /**
     * Saves a new appointment in the persistence system.
     * <p>
     * This method stores a new appointment and associates it with the specified patient.
     * </p>
     *
     * @param appointment      The appointment to be saved
     * @param patientUsername  The username of the patient this appointment belongs to
     * @throws DAOException    If there is an error during the save operation
     */
    void saveAppointment(Appointment appointment, String patientUsername) throws DAOException;

    /**
     * Retrieves an appointment by its unique identifier.
     *
     * @param appointmentId    The ID of the appointment to retrieve
     * @return                 The appointment with the specified ID, or null if not found
     * @throws DAOException    If there is an error accessing the data storage
     */
    Appointment retrieveAppointment(int appointmentId) throws DAOException;

    List<Appointment> retrieveAllAppointments() throws DAOException;

    /**
     * Retrieves all appointments for a specific patient.
     *
     * @param patientUsername  The username of the patient
     * @return                 A list of appointments for the patient, empty list if none found
     * @throws DAOException    If there is an error accessing the data storage
     */
    List<Appointment> retrieveAppointmentsByPatient(String patientUsername) throws DAOException;

    /**
     * Retrieves all appointments for a specific psychologist.
     *
     * @param psychologistUsername  The username of the psychologist
     * @return                      A list of appointments for the psychologist, empty list if none found
     * @throws DAOException         If there is an error accessing the data storage
     */
    List<Appointment> retrieveAppointmentsByPsychologist(String psychologistUsername) throws DAOException;

    /**
     * Retrieves all appointments scheduled for a specific date.
     *
     * @param date             The date for which appointments should be retrieved
     * @return                 A list of appointments on the specified date, empty list if none found
     * @throws DAOException    If there is an error accessing the data storage
     */
    List<Appointment> retrieveAppointmentsByDate(LocalDate date) throws DAOException;

    /**
     * Retrieves all appointments that have not been notified to the patient yet.
     *
     * @param patientUsername  The username of the patient
     * @return                 A list of unnotified appointments, empty list if none found
     * @throws DAOException    If there is an error accessing the data storage
     */
    List<Appointment> retrieveUnnotifiedAppointments(String patientUsername) throws DAOException;

    /**
     * Updates an existing appointment in the persistence system.
     *
     * @param appointment      The appointment with updated information
     * @throws DAOException    If there is an error updating the appointment or if it doesn't exist
     */
    void updateAppointment(Appointment appointment) throws DAOException;

    /**
     * Updates the notification status of a specific appointment.
     *
     * @param appointmentId    The ID of the appointment to update
     * @param notified         The new notification status (true = notified, false = not notified)
     * @throws DAOException    If there is an error updating the status or if the appointment doesn't exist
     */
    void updateAppointmentNotificationStatus(int appointmentId, boolean notified) throws DAOException;

    /**
     * Updates the notification status for a list of appointments in a batch operation.
     * <p>
     * This method is more efficient than updating appointments individually when multiple
     * appointments need to be marked as notified simultaneously.
     * </p>
     *
     * @param appointments     The list of appointments to update
     * @throws DAOException    If there is an error updating any of the appointments
     */
    void updateAppointmentsNotificationStatus(List<Appointment> appointments) throws DAOException;

    /**
     * Deletes an appointment from the persistence system.
     *
     * @param appointmentId    The ID of the appointment to delete
     * @throws DAOException    If there is an error during deletion or if the appointment doesn't exist
     */
    void deleteAppointment(int appointmentId) throws DAOException;

    /**
     * Checks if an appointment with the given ID exists.
     *
     * @param appointmentId    The ID to check
     * @return                 true if the appointment exists, false otherwise
     * @throws DAOException    If there is an error accessing the data storage
     */
    boolean appointmentExists(int appointmentId) throws DAOException;

    /**
     * Generates the next available appointment ID.
     * <p>
     * This method is particularly useful for storage systems that don't support auto-incrementing IDs.
     * </p>
     *
     * @return                 The next available appointment ID
     * @throws DAOException    If there is an error accessing the data storage
     */
    int getNextAppointmentId() throws DAOException;
}