package it.uniroma2.mindharbor.dao.mysql;

import it.uniroma2.mindharbor.beans.PsychologistBean;
import it.uniroma2.mindharbor.beans.UserBean;
import it.uniroma2.mindharbor.dao.ConnectionFactory;
import it.uniroma2.mindharbor.dao.PatientDao;
import it.uniroma2.mindharbor.dao.PsychologistDao;
import it.uniroma2.mindharbor.dao.UserDao;
import it.uniroma2.mindharbor.dao.mysql.constants.PsychologistDaoMySqlQueries;
import it.uniroma2.mindharbor.exception.DAOException;
import it.uniroma2.mindharbor.model.Patient;
import it.uniroma2.mindharbor.model.Psychologist;
import it.uniroma2.mindharbor.patterns.facade.DaoFactoryFacade;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * MySQL implementation of the {@link PsychologistDao} interface.
 * <p>
 * This class provides operations to manage psychologists in a MySQL database, including saving,
 * retrieving, updating, and deleting psychologist records.
 * It uses ConnectionFactory to obtain database connections.
 * </p>
 */
public class PsychologistDaoMySql implements PsychologistDao {

    private static final Logger logger = Logger.getLogger(PsychologistDaoMySql.class.getName());

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
     * Saves a new psychologist in the database.
     * <p>
     * This method first saves the general user information and then adds the psychologist-specific
     * details to the Psychologists table.
     * </p>
     *
     * @param psychologist The {@link PsychologistBean} containing the psychologist's details
     * @throws DAOException If there is an error while saving the psychologist or if the psychologist already exists
     */
    @Override
    public void savePsychologist(PsychologistBean psychologist) throws DAOException {
        UserDao userDao = DaoFactoryFacade.getInstance().getUserDao();
        userDao.saveUser(psychologist);

        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(PsychologistDaoMySqlQueries.INSERT_PSYCHOLOGIST)) {

            stmt.setString(1, psychologist.getUsername());
            stmt.setString(2, psychologist.getOffice());
            stmt.setDouble(3, psychologist.getHourlyCost());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new DAOException("Failed to save psychologist, no rows affected.");
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error saving psychologist", e);
            throw new DAOException("Error saving psychologist: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves a psychologist's details from the database based on the username.
     * <p>
     * This method joins the Psychologists and Users' tables to retrieve complete psychologist information.
     * </p>
     *
     * @param username The username of the psychologist to retrieve
     * @return A {@link Psychologist} object if found, otherwise null
     * @throws DAOException If there is an error accessing the database
     */
    @Override
    public Psychologist retrievePsychologist(String username) throws DAOException {
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(PsychologistDaoMySqlQueries.SELECT_PSYCHOLOGIST_BY_USERNAME)) {

            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractPsychologistFromResultSet(rs);
                }
            }

            return null; // Psychologist not found

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error retrieving psychologist", e);
            throw new DAOException("Error retrieving psychologist: " + e.getMessage(), e);
        }
    }

    /**
     * Updates an existing psychologist's details in the database.
     * <p>
     * This method updates both the general user information and the psychologist-specific
     * details in the respective tables.
     * </p>
     *
     * @param psychologist The {@link Psychologist} object containing updated psychologist-specific details
     * @param bean The {@link UserBean} object containing updated general user details
     * @throws DAOException If the psychologist does not exist, or if there is an error updating the data
     */
    @Override
    public void updatePsychologist(Psychologist psychologist, UserBean bean) throws DAOException {
        UserDao userDao = DaoFactoryFacade.getInstance().getUserDao();
        userDao.updateUser(bean);

        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(PsychologistDaoMySqlQueries.UPDATE_PSYCHOLOGIST)) {

            stmt.setString(1, psychologist.getOffice());
            stmt.setString(2, psychologist.getHourlyCost());
            stmt.setString(3, psychologist.getUsername());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new DAOException("Psychologist not found: " + psychologist.getUsername());
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error updating psychologist", e);
            throw new DAOException("Error updating psychologist: " + e.getMessage(), e);
        }
    }

    /**
     * Deletes a psychologist from the database.
     * <p>
     * This method removes both the psychologist-specific record and the associated user record.
     * </p>
     *
     * @param username The username of the psychologist to delete
     * @throws DAOException If the psychologist does not exist, or if there is an error deleting the data
     */
    @Override
    public void deletePsychologist(String username) throws DAOException {
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(PsychologistDaoMySqlQueries.DELETE_PSYCHOLOGIST)) {

            stmt.setString(1, username);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new DAOException("Psychologist not found: " + username);
            }

            // Delete the associated user record
            UserDao userDao = DaoFactoryFacade.getInstance().getUserDao();
            userDao.deleteUser(username);

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error deleting psychologist", e);
            throw new DAOException("Error deleting psychologist: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves a list of patients assigned to a specific psychologist.
     * <p>
     * This method delegates to the PatientDao to retrieve patients by psychologist.
     * </p>
     *
     * @param psychologist The {@link Psychologist} whose patients are to be retrieved
     * @return A list of {@link Patient} objects, empty list if none found
     * @throws DAOException If there is an error accessing the database
     */
    @Override
    public List<Patient> getPatients(Psychologist psychologist) throws DAOException {
        PatientDao patientDao = DaoFactoryFacade.getInstance().getPatientDao();
        return patientDao.retrievePatientsByPsychologist(psychologist);
    }

    /**
     * Extracts psychologist data from a ResultSet row.
     *
     * @param rs The ResultSet containing psychologist data
     * @return The constructed Psychologist object
     * @throws SQLException If there is an error reading from the ResultSet
     */
    private Psychologist extractPsychologistFromResultSet(ResultSet rs) throws SQLException {
        String username = rs.getString("Username");
        String firstName = rs.getString("Firstname");
        String lastName = rs.getString("Lastname");
        String gender = rs.getString("Gender");
        String office = rs.getString("Office");
        String hourlyCost = String.valueOf(rs.getDouble("HourlyCost"));

        return new Psychologist(username, firstName, lastName, gender, office, hourlyCost);
    }
}