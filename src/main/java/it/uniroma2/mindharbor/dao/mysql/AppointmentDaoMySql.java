package it.uniroma2.mindharbor.dao.mysql;

import it.uniroma2.mindharbor.dao.AppointmentDao;
import it.uniroma2.mindharbor.dao.ConnectionPoolManager;
import it.uniroma2.mindharbor.dao.mysql.constants.AppointmentDaoMySqlQueries;
import it.uniroma2.mindharbor.exception.DAOException;
import it.uniroma2.mindharbor.model.Appointment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * MySQL implementation of the {@link AppointmentDao} interface.
 * <p>
 * This class manages appointment data stored in a MySQL database, providing methods
 * to create, retrieve, update, and delete appointment records. All SQL queries are
 * centralized in {@link AppointmentDaoMySqlQueries} for better maintainability.
 * </p>
 * <p>
 * This implementation uses connection pooling through {@link ConnectionPoolManager}
 * to improve performance while maintaining robustness.
 * </p>
 */
public class AppointmentDaoMySql implements AppointmentDao {

    private static final Logger logger = Logger.getLogger(AppointmentDaoMySql.class.getName());

    /**
     * Saves a new appointment in the database.
     *
     * @param appointment     The appointment to be saved
     * @param patientUsername The username of the patient this appointment belongs to
     * @throws DAOException   If there is an error executing the SQL query
     */
    @Override
    public void saveAppointment(Appointment appointment, String patientUsername) throws DAOException {
        try (Connection connection = ConnectionPoolManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(AppointmentDaoMySqlQueries.INSERT_APPOINTMENT)) {

            stmt.setInt(1, appointment.getId());
            stmt.setDate(2, Date.valueOf(appointment.getDate()));
            stmt.setTime(3, Time.valueOf(appointment.getTime()));
            stmt.setString(4, appointment.getDescription());
            stmt.setBoolean(5, appointment.isNotified());
            stmt.setString(6, patientUsername);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new DAOException("Failed to save appointment, no rows affected.");
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error saving appointment", e);
            throw new DAOException("Error saving appointment: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves an appointment by its ID from the database.
     *
     * @param appointmentId The ID of the appointment to retrieve
     * @return The appointment with the specified ID, or null if not found
     * @throws DAOException If there is an error executing the SQL query
     */
    @Override
    public Appointment retrieveAppointment(int appointmentId) throws DAOException {
        try (Connection connection = ConnectionPoolManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(AppointmentDaoMySqlQueries.SELECT_APPOINTMENT_BY_ID)) {

            stmt.setInt(1, appointmentId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractAppointmentFromResultSet(rs);
                }
            }

            return null; // Appointment not found

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error retrieving appointment", e);
            throw new DAOException("Error retrieving appointment: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves all appointments for a specific patient from the database.
     *
     * @param patientUsername The username of the patient
     * @return A list of appointments for the patient, empty list if none found
     * @throws DAOException If there is an error executing the SQL query
     */
    @Override
    public List<Appointment> retrieveAppointmentsByPatient(String patientUsername) throws DAOException {
        List<Appointment> appointments = new ArrayList<>();

        try (Connection connection = ConnectionPoolManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(AppointmentDaoMySqlQueries.SELECT_APPOINTMENTS_BY_PATIENT)) {

            stmt.setString(1, patientUsername);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    appointments.add(extractAppointmentFromResultSet(rs));
                }
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error retrieving appointments by patient", e);
            throw new DAOException("Error retrieving appointments by patient: " + e.getMessage(), e);
        }

        return appointments;
    }

    /**
     * Retrieves all appointments for a specific psychologist from the database.
     *
     * @param psychologistUsername The username of the psychologist
     * @return A list of appointments for the psychologist, empty list if none found
     * @throws DAOException If there is an error executing the SQL query
     */
    @Override
    public List<Appointment> retrieveAppointmentsByPsychologist(String psychologistUsername) throws DAOException {
        List<Appointment> appointments = new ArrayList<>();

        try (Connection connection = ConnectionPoolManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(AppointmentDaoMySqlQueries.SELECT_APPOINTMENTS_BY_PSYCHOLOGIST)) {

            stmt.setString(1, psychologistUsername);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    appointments.add(extractAppointmentFromResultSet(rs));
                }
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error retrieving appointments by psychologist", e);
            throw new DAOException("Error retrieving appointments by psychologist: " + e.getMessage(), e);
        }

        return appointments;
    }

    /**
     * Retrieves all appointments scheduled for a specific date from the database.
     *
     * @param date The date for which appointments should be retrieved
     * @return A list of appointments on the specified date, empty list if none found
     * @throws DAOException If there is an error executing the SQL query
     */
    @Override
    public List<Appointment> retrieveAppointmentsByDate(LocalDate date) throws DAOException {
        List<Appointment> appointments = new ArrayList<>();

        try (Connection connection = ConnectionPoolManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(AppointmentDaoMySqlQueries.SELECT_APPOINTMENTS_BY_DATE)) {

            stmt.setDate(1, Date.valueOf(date));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    appointments.add(extractAppointmentFromResultSet(rs));
                }
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error retrieving appointments by date", e);
            throw new DAOException("Error retrieving appointments by date: " + e.getMessage(), e);
        }

        return appointments;
    }

    /**
     * Retrieves all appointments that have not been notified to the patient yet.
     *
     * @param patientUsername The username of the patient
     * @return A list of unnotified appointments, empty list if none found
     * @throws DAOException If there is an error executing the SQL query
     */
    @Override
    public List<Appointment> retrieveUnnotifiedAppointments(String patientUsername) throws DAOException {
        List<Appointment> appointments = new ArrayList<>();

        try (Connection connection = ConnectionPoolManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(AppointmentDaoMySqlQueries.SELECT_UNNOTIFIED_APPOINTMENTS)) {

            stmt.setString(1, patientUsername);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    appointments.add(extractAppointmentFromResultSet(rs));
                }
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error retrieving unnotified appointments", e);
            throw new DAOException("Error retrieving unnotified appointments: " + e.getMessage(), e);
        }

        return appointments;
    }

    /**
     * Updates an existing appointment in the database.
     *
     * @param appointment The appointment with updated information
     * @throws DAOException If there is an error executing the SQL query or if the appointment doesn't exist
     */
    @Override
    public void updateAppointment(Appointment appointment) throws DAOException {
        try (Connection connection = ConnectionPoolManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(AppointmentDaoMySqlQueries.UPDATE_APPOINTMENT)) {

            stmt.setDate(1, Date.valueOf(appointment.getDate()));
            stmt.setTime(2, Time.valueOf(appointment.getTime()));
            stmt.setString(3, appointment.getDescription());
            stmt.setBoolean(4, appointment.isNotified());
            stmt.setInt(5, appointment.getId());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new DAOException("Appointment not found: " + appointment.getId());
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error updating appointment", e);
            throw new DAOException("Error updating appointment: " + e.getMessage(), e);
        }
    }

    /**
     * Updates the notification status of a specific appointment.
     *
     * @param appointmentId The ID of the appointment to update
     * @param notified The new notification status
     * @throws DAOException If there is an error executing the SQL query or if the appointment doesn't exist
     */
    @Override
    public void updateAppointmentNotificationStatus(int appointmentId, boolean notified) throws DAOException {
        try (Connection connection = ConnectionPoolManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(AppointmentDaoMySqlQueries.UPDATE_APPOINTMENT_NOTIFICATION)) {

            stmt.setBoolean(1, notified);
            stmt.setInt(2, appointmentId);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new DAOException("Appointment not found: " + appointmentId);
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error updating appointment notification status", e);
            throw new DAOException("Error updating appointment notification status: " + e.getMessage(), e);
        }
    }

    /**
     * Updates the notification status for a list of appointments in a batch operation.
     * Uses a transaction to ensure that all updates succeed or all fail.
     *
     * @param appointments The list of appointments to update
     * @throws DAOException If there is an error executing the SQL query
     */
    @Override
    public void updateAppointmentsNotificationStatus(List<Appointment> appointments) throws DAOException {
        if (appointments == null || appointments.isEmpty()) {
            return;
        }

        // Use try-with-resources to ensure the connection is properly closed/returned to the pool
        try (Connection connection = ConnectionPoolManager.getInstance().getConnection()) {
            try {
                // Begin transaction
                connection.setAutoCommit(false);

                // Prepare and execute batch updates
                try (PreparedStatement stmt = connection.prepareStatement(AppointmentDaoMySqlQueries.UPDATE_APPOINTMENT_NOTIFICATION)) {
                    for (Appointment appointment : appointments) {
                        stmt.setBoolean(1, appointment.isNotified());
                        stmt.setInt(2, appointment.getId());
                        stmt.addBatch();
                    }

                    int[] results = stmt.executeBatch();

                    // Check if all updates were successful
                    for (int i = 0; i < results.length; i++) {
                        if (results[i] == 0) {
                            throw new DAOException("Appointment not found: " + appointments.get(i).getId());
                        }
                    }

                    // If everything went well, commit the transaction
                    connection.commit();
                }
            } catch (SQLException | DAOException e) {
                // Safe rollback
                try {
                    connection.rollback();
                } catch (SQLException rollbackEx) {
                    logger.log(Level.SEVERE, "Error rolling back transaction", rollbackEx);
                }

                logger.log(Level.SEVERE, "Error updating appointment notification statuses", e);
                throw new DAOException("Error updating appointment notification statuses: " + e.getMessage(), e);

            } finally {
                // Reset auto-commit mode before returning connection to pool
                try {
                    connection.setAutoCommit(true);
                } catch (SQLException resetEx) {
                    logger.log(Level.WARNING, "Error resetting auto-commit", resetEx);
                }
            }
        } catch (SQLException connectionEx) {
            logger.log(Level.SEVERE, "Failed to obtain database connection", connectionEx);
            throw new DAOException("Failed to obtain database connection: " + connectionEx.getMessage(), connectionEx);
        }
    }

    /**
     * Deletes an appointment from the database.
     *
     * @param appointmentId The ID of the appointment to delete
     * @throws DAOException If there is an error executing the SQL query
     */
    @Override
    public void deleteAppointment(int appointmentId) throws DAOException {
        try (Connection connection = ConnectionPoolManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(AppointmentDaoMySqlQueries.DELETE_APPOINTMENT)) {

            stmt.setInt(1, appointmentId);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new DAOException("Appointment not found: " + appointmentId);
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error deleting appointment", e);
            throw new DAOException("Error deleting appointment: " + e.getMessage(), e);
        }
    }

    /**
     * Checks if an appointment with the given ID exists in the database.
     *
     * @param appointmentId The ID to check
     * @return true if the appointment exists, false otherwise
     * @throws DAOException If there is an error executing the SQL query
     */
    @Override
    public boolean appointmentExists(int appointmentId) throws DAOException {
        try (Connection connection = ConnectionPoolManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(AppointmentDaoMySqlQueries.CHECK_APPOINTMENT_EXISTS)) {

            stmt.setInt(1, appointmentId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

            return false;

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error checking if appointment exists", e);
            throw new DAOException("Error checking if appointment exists: " + e.getMessage(), e);
        }
    }

    /**
     * Generates the next available appointment ID based on the highest ID currently in the database.
     * Uses COALESCE to handle the case where there are no appointments yet.
     *
     * @return The next available appointment ID
     * @throws DAOException If there is an error executing the SQL query
     */
    @Override
    public int getNextAppointmentId() throws DAOException {
        try (Connection connection = ConnectionPoolManager.getInstance().getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(AppointmentDaoMySqlQueries.GET_MAX_APPOINTMENT_ID)) {

            if (rs.next()) {
                int maxId = rs.getInt(1);
                return maxId + 1;
            }

            return 1; // If no appointments exist yet or if MAX(id) returned null

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error getting next appointment ID", e);
            throw new DAOException("Error getting next appointment ID: " + e.getMessage(), e);
        }
    }

    /**
     * Extracts appointment data from a ResultSet row.
     *
     * @param rs The ResultSet containing appointment data
     * @return The constructed Appointment object
     * @throws SQLException If there is an error reading from the ResultSet
     */
    private Appointment extractAppointmentFromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        LocalDate date = rs.getDate("date").toLocalDate();
        LocalTime time = rs.getTime("time").toLocalTime();
        String description = rs.getString("description");
        boolean notified = rs.getBoolean("notified");

        return new Appointment(id, date, time, description, notified);
    }
}