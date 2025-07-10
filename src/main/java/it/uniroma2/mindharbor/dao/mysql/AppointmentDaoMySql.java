package it.uniroma2.mindharbor.dao.mysql;

import it.uniroma2.mindharbor.dao.AbstractObservableDao;
import it.uniroma2.mindharbor.dao.AppointmentDao;
import it.uniroma2.mindharbor.dao.ConnectionFactory;
import it.uniroma2.mindharbor.dao.mysql.constants.AppointmentDaoMySqlQueries;
import it.uniroma2.mindharbor.exception.DAOException;
import it.uniroma2.mindharbor.model.Appointment;
import it.uniroma2.mindharbor.patterns.observer.DaoOperation;

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

public class AppointmentDaoMySql extends AbstractObservableDao implements AppointmentDao {

    private static final Logger logger = Logger.getLogger(AppointmentDaoMySql.class.getName());

    private Connection getConnection() throws DAOException {
        try {
            return ConnectionFactory.getConnection();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Failed to get database connection", e);
            throw new DAOException("Error obtaining database connection: " + e.getMessage(), e);
        }
    }

    @Override
    public void saveAppointment(Appointment appointment, String patientUsername) throws DAOException {
        Connection connection = getConnection();
        try (PreparedStatement stmt = connection.prepareStatement(AppointmentDaoMySqlQueries.INSERT_APPOINTMENT)) {
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
        Object[] syncPackage = {appointment, patientUsername};
        notifyObservers(DaoOperation.INSERT, "Appointment", String.valueOf(appointment.getId()), syncPackage);
    }

    @Override
    public Appointment retrieveAppointment(int appointmentId) throws DAOException {
        Connection connection = getConnection();
        try (PreparedStatement stmt = connection.prepareStatement(AppointmentDaoMySqlQueries.SELECT_APPOINTMENT_BY_ID)) {
            stmt.setInt(1, appointmentId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractAppointmentFromResultSet(rs);
                }
            }
            return null;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error retrieving appointment", e);
            throw new DAOException("Error retrieving appointment: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Appointment> retrieveAllAppointments() throws DAOException {
        List<Appointment> allAppointments = new ArrayList<>();
        Connection connection = getConnection();
        try (PreparedStatement stmt = connection.prepareStatement(AppointmentDaoMySqlQueries.SELECT_ALL_APPOINTMENTS);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                allAppointments.add(extractAppointmentFromResultSet(rs));
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error retrieving all appointments", e);
            throw new DAOException("Error retrieving all appointments: " + e.getMessage(), e);
        }
        return allAppointments;
    }

    @Override
    public List<Appointment> retrieveAppointmentsByPatient(String patientUsername) throws DAOException {
        List<Appointment> appointments = new ArrayList<>();
        Connection connection = getConnection();
        try (PreparedStatement stmt = connection.prepareStatement(AppointmentDaoMySqlQueries.SELECT_APPOINTMENTS_BY_PATIENT)) {
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

    @Override
    public List<Appointment> retrieveAppointmentsByPsychologist(String psychologistUsername) throws DAOException {
        List<Appointment> appointments = new ArrayList<>();
        Connection connection = getConnection();
        try (PreparedStatement stmt = connection.prepareStatement(AppointmentDaoMySqlQueries.SELECT_APPOINTMENTS_BY_PSYCHOLOGIST)) {
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

    @Override
    public List<Appointment> retrieveAppointmentsByDate(LocalDate date) throws DAOException {
        List<Appointment> appointments = new ArrayList<>();
        Connection connection = getConnection();
        try (PreparedStatement stmt = connection.prepareStatement(AppointmentDaoMySqlQueries.SELECT_APPOINTMENTS_BY_DATE)) {
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

    @Override
    public List<Appointment> retrieveUnnotifiedAppointments(String patientUsername) throws DAOException {
        List<Appointment> appointments = new ArrayList<>();
        Connection connection = getConnection();
        try (PreparedStatement stmt = connection.prepareStatement(AppointmentDaoMySqlQueries.SELECT_UNNOTIFIED_APPOINTMENTS)) {
            stmt.setString(1, patientUsername);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    appointments.add(extractAppointmentFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error retrieving unnoticed appointments", e);
            throw new DAOException("Error retrieving unnoticed appointments: " + e.getMessage(), e);
        }
        return appointments;
    }

    @Override
    public void updateAppointment(Appointment appointment) throws DAOException {
        Connection connection = getConnection();
        try (PreparedStatement stmt = connection.prepareStatement(AppointmentDaoMySqlQueries.UPDATE_APPOINTMENT)) {
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
        notifyObservers(DaoOperation.UPDATE, "Appointment", String.valueOf(appointment.getId()), appointment);
    }

    @Override
    public void updateAppointmentNotificationStatus(int appointmentId, boolean notified) throws DAOException {
        Connection connection = getConnection();
        try (PreparedStatement stmt = connection.prepareStatement(AppointmentDaoMySqlQueries.UPDATE_APPOINTMENT_NOTIFICATION)) {
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

        Appointment updatedAppointment = retrieveAppointment(appointmentId);
        if (updatedAppointment != null) {
            notifyObservers(DaoOperation.UPDATE, "Appointment", String.valueOf(appointmentId), updatedAppointment);
        }
    }

    @Override
    public void updateAppointmentsNotificationStatus(List<Appointment> appointments) throws DAOException {
        if (appointments == null || appointments.isEmpty()) {
            return;
        }

        Connection connection = getConnection();
        try {
            connection.setAutoCommit(false);
            try (PreparedStatement stmt = connection.prepareStatement(AppointmentDaoMySqlQueries.UPDATE_APPOINTMENT_NOTIFICATION)) {
                for (Appointment appointment : appointments) {
                    stmt.setBoolean(1, appointment.isNotified());
                    stmt.setInt(2, appointment.getId());
                    stmt.addBatch();
                }
                stmt.executeBatch();
            }
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                logger.log(Level.SEVERE, "Error rolling back transaction", rollbackEx);
            }
            logger.log(Level.SEVERE, "Error updating appointment notification statuses", e);
            throw new DAOException("Error updating appointment notification statuses: " + e.getMessage(), e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException resetEx) {
                logger.log(Level.WARNING, "Error resetting auto-commit", resetEx);
            }
        }

        for (Appointment appointment : appointments) {
            notifyObservers(DaoOperation.UPDATE, "Appointment", String.valueOf(appointment.getId()), appointment);
        }
    }

    @Override
    public void deleteAppointment(int appointmentId) throws DAOException {
        Connection connection = getConnection();
        try (PreparedStatement stmt = connection.prepareStatement(AppointmentDaoMySqlQueries.DELETE_APPOINTMENT)) {
            stmt.setInt(1, appointmentId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new DAOException("Appointment not found: " + appointmentId);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error deleting appointment", e);
            throw new DAOException("Error deleting appointment: " + e.getMessage(), e);
        }
        notifyObservers(DaoOperation.DELETE, "Appointment", String.valueOf(appointmentId), null);
    }

    @Override
    public boolean appointmentExists(int appointmentId) throws DAOException {
        Connection connection = getConnection();
        try (PreparedStatement stmt = connection.prepareStatement(AppointmentDaoMySqlQueries.CHECK_APPOINTMENT_EXISTS)) {
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

    @Override
    public int getNextAppointmentId() throws DAOException {
        Connection connection = getConnection();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(AppointmentDaoMySqlQueries.GET_MAX_APPOINTMENT_ID)) {
            if (rs.next()) {
                return rs.getInt(1) + 1;
            }
            return 1;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error getting next appointment ID", e);
            throw new DAOException("Error getting next appointment ID: " + e.getMessage(), e);
        }
    }

    private Appointment extractAppointmentFromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        LocalDate date = rs.getDate("date").toLocalDate();
        LocalTime time = rs.getTime("time").toLocalTime();
        String description = rs.getString("description");
        boolean notified = rs.getBoolean("notified");
        return new Appointment(id, date, time, description, notified);
    }
}