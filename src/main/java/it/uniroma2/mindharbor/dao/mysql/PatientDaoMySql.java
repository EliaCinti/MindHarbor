package it.uniroma2.mindharbor.dao.mysql;

import it.uniroma2.mindharbor.beans.PatientBean;
import it.uniroma2.mindharbor.beans.UserBean;
import it.uniroma2.mindharbor.dao.ConnectionFactory;
import it.uniroma2.mindharbor.dao.PatientDao;
import it.uniroma2.mindharbor.dao.UserDao;
import it.uniroma2.mindharbor.dao.mysql.constants.PatientDaoMySqlQueries;
import it.uniroma2.mindharbor.exception.DAOException;
import it.uniroma2.mindharbor.model.Patient;
import it.uniroma2.mindharbor.model.Psychologist;
import it.uniroma2.mindharbor.patterns.facade.DaoFactoryFacade;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * MySQL implementation of the {@link PatientDao} interface.
 * <p>
 * This class provides operations to manage patients in a MySQL database, including saving,
 * retrieving, updating, and deleting patient records.
 * It uses ConnectionFactory to obtain database connections.
 * </p>
 */
public class PatientDaoMySql implements PatientDao {

    private static final Logger logger = Logger.getLogger(PatientDaoMySql.class.getName());

    /**
     * Gets a connection from the connection factory.
     *
     * @return A database connection
     * @throws DAOException If there is an error obtaining a connection
     */
    private Connection getConnection() throws DAOException {
        try {
            return ConnectionFactory.getConnection();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Failed to get database connection", e);
            throw new DAOException("Error obtaining database connection: " + e.getMessage(), e);
        }
    }

    /**
     * Saves a new patient in the database.
     * <p>
     * This method first saves the general user information and then adds the patient-specific
     * details to the Patient table.
     * </p>
     *
     * @param patient The {@link PatientBean} containing the patient's details
     * @throws DAOException If there is an error while saving the patient or if the patient already exists
     */
    @Override
    public void savePatient(PatientBean patient) throws DAOException {
        UserDao userDao = DaoFactoryFacade.getInstance().getUserDao();
        userDao.saveUser(patient);

        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(PatientDaoMySqlQueries.INSERT_PATIENT)) {

            stmt.setString(1, patient.getUsername());
            stmt.setDate(2, Date.valueOf(patient.getBirthDate()));
            stmt.setString(3, null); // No psychologist assigned initially

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new DAOException("Failed to save patient, no rows affected.");
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error saving patient", e);
            throw new DAOException("Error saving patient: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves a patient's details from the database based on the username.
     * <p>
     * This method joins the Patients and Users tables to retrieve complete patient information.
     * </p>
     *
     * @param username The username of the patient to retrieve
     * @return A {@link Patient} object if found, otherwise null
     * @throws DAOException If there is an error accessing the database
     */
    @Override
    public Patient retrievePatient(String username) throws DAOException {
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(PatientDaoMySqlQueries.SELECT_PATIENT_BY_USERNAME)) {

            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractPatientFromResultSet(rs);
                }
            }

            return null; // Patient not found

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error retrieving patient", e);
            throw new DAOException("Error retrieving patient: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves a list of patients assigned to a specific psychologist.
     * <p>
     * This method queries the database for all patients who have the specified
     * psychologist assigned to them.
     * </p>
     *
     * @param psychologist The {@link Psychologist} whose patients are to be retrieved
     * @return A list of {@link Patient} objects, empty list if none found
     * @throws DAOException If there is an error accessing the database
     */
    @Override
    public List<Patient> retrievePatientsByPsychologist(Psychologist psychologist) throws DAOException {
        List<Patient> patients = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(PatientDaoMySqlQueries.SELECT_PATIENTS_BY_PSYCHOLOGIST)) {

            stmt.setString(1, psychologist.getUsername());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    patients.add(extractPatientFromResultSet(rs));
                }
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error retrieving patients by psychologist", e);
            throw new DAOException("Error retrieving patients by psychologist: " + e.getMessage(), e);
        }

        return patients;
    }

    /**
     * Updates an existing patient's details in the database.
     * <p>
     * This method updates both the general user information and the patient-specific
     * details in the respective tables.
     * </p>
     *
     * @param patient The {@link Patient} object containing updated patient-specific details
     * @param user    The {@link UserBean} object containing updated general user details
     * @throws DAOException If the patient does not exist, or if there is an error updating the data
     */
    @Override
    public void updatePatient(Patient patient, UserBean user) throws DAOException {
        UserDao userDao = DaoFactoryFacade.getInstance().getUserDao();
        userDao.updateUser(user);

        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(PatientDaoMySqlQueries.UPDATE_PATIENT)) {

            stmt.setDate(1, Date.valueOf(patient.getBirthday()));
            stmt.setString(2, patient.getPsychologist());
            stmt.setString(3, patient.getUsername());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new DAOException("Patient not found: " + patient.getUsername());
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error updating patient", e);
            throw new DAOException("Error updating patient: " + e.getMessage(), e);
        }
    }

    /**
     * Deletes a patient from the database.
     * <p>
     * This method removes both the patient-specific record and the associated user record.
     * </p>
     *
     * @param username The username of the patient to delete
     * @throws DAOException If the patient does not exist, or if there is an error deleting the data
     */
    @Override
    public void deletePatient(String username) throws DAOException {
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(PatientDaoMySqlQueries.DELETE_PATIENT)) {

            stmt.setString(1, username);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new DAOException("Patient not found: " + username);
            }

            // Delete the associated user record
            UserDao userDao = DaoFactoryFacade.getInstance().getUserDao();
            userDao.deleteUser(username);

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error deleting patient", e);
            throw new DAOException("Error deleting patient: " + e.getMessage(), e);
        }
    }

    /**
     * Extracts patient data from a ResultSet row.
     *
     * @param rs The ResultSet containing patient data
     * @return The constructed Patient object
     * @throws SQLException If there is an error reading from the ResultSet
     */
    private Patient extractPatientFromResultSet(ResultSet rs) throws SQLException {
        String username = rs.getString("Username");
        String firstName = rs.getString("Firstname");
        String lastName = rs.getString("Lastname");
        String gender = rs.getString("Gender");
        String psychologist = rs.getString("Psychologist");
        LocalDate birthDate = rs.getDate("BirthDate").toLocalDate();

        return new Patient(username, firstName, lastName, gender, psychologist, birthDate);
    }
}