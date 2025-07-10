package it.uniroma2.mindharbor.dao.mysql;

import it.uniroma2.mindharbor.beans.PatientBean;
import it.uniroma2.mindharbor.beans.UserBean;
import it.uniroma2.mindharbor.dao.AbstractObservableDao;
import it.uniroma2.mindharbor.dao.ConnectionFactory;
import it.uniroma2.mindharbor.dao.PatientDao;
import it.uniroma2.mindharbor.dao.UserDao;
import it.uniroma2.mindharbor.dao.mysql.constants.PatientDaoMySqlQueries;
import it.uniroma2.mindharbor.exception.DAOException;
import it.uniroma2.mindharbor.model.Patient;
import it.uniroma2.mindharbor.model.Psychologist;
import it.uniroma2.mindharbor.patterns.facade.DaoFactoryFacade;
import it.uniroma2.mindharbor.patterns.observer.DaoOperation;

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

public class PatientDaoMySql extends AbstractObservableDao implements PatientDao {

    private static final Logger logger = Logger.getLogger(PatientDaoMySql.class.getName());

    private Connection getConnection() throws DAOException {
        try {
            return ConnectionFactory.getConnection();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Failed to get database connection", e);
            throw new DAOException("Error obtaining database connection: " + e.getMessage(), e);
        }
    }

    @Override
    public void savePatient(PatientBean patient) throws DAOException {
        UserDao userDao = DaoFactoryFacade.getInstance().getUserDao();
        userDao.saveUser(patient);

        Connection connection = getConnection();
        try (PreparedStatement stmt = connection.prepareStatement(PatientDaoMySqlQueries.INSERT_PATIENT)) {
            stmt.setString(1, patient.getUsername());
            stmt.setDate(2, Date.valueOf(patient.getBirthDate()));
            stmt.setString(3, null);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new DAOException("Failed to save patient, no rows affected.");
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error saving patient", e);
            throw new DAOException("Error saving patient: " + e.getMessage(), e);
        }
        notifyObservers(DaoOperation.INSERT, "Patient", patient.getUsername(), patient);
    }

    @Override
    public Patient retrievePatient(String username) throws DAOException {
        Connection connection = getConnection();
        try (PreparedStatement stmt = connection.prepareStatement(PatientDaoMySqlQueries.SELECT_PATIENT_BY_USERNAME)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractPatientFromResultSet(rs);
                }
            }
            return null;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error retrieving patient", e);
            throw new DAOException("Error retrieving patient: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Patient> retrieveAllPatients() throws DAOException {
        List<Patient> patients = new ArrayList<>();
        Connection connection = getConnection();
        try (PreparedStatement stmt = connection.prepareStatement(PatientDaoMySqlQueries.SELECT_ALL_PATIENTS);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                patients.add(extractPatientFromResultSet(rs));
            }
        } catch (SQLException e) {
            throw new DAOException("Error retrieving all patients: " + e.getMessage(), e);
        }
        return patients;
    }

    @Override
    public List<Patient> retrievePatientsByPsychologist(Psychologist psychologist) throws DAOException {
        List<Patient> patients = new ArrayList<>();
        Connection connection = getConnection();
        try (PreparedStatement stmt = connection.prepareStatement(PatientDaoMySqlQueries.SELECT_PATIENTS_BY_PSYCHOLOGIST)) {
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

    @Override
    public void updatePatient(Patient patient, UserBean user) throws DAOException {
        UserDao userDao = DaoFactoryFacade.getInstance().getUserDao();
        userDao.updateUser(user);

        Connection connection = getConnection();
        try (PreparedStatement stmt = connection.prepareStatement(PatientDaoMySqlQueries.UPDATE_PATIENT)) {
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
        notifyObservers(DaoOperation.UPDATE, "Patient", patient.getUsername(), patient);
    }

    @Override
    public void deletePatient(String username) throws DAOException {
        Connection connection = getConnection();
        try (PreparedStatement stmt = connection.prepareStatement(PatientDaoMySqlQueries.DELETE_PATIENT)) {
            stmt.setString(1, username);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new DAOException("Patient not found: " + username);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error deleting patient", e);
            throw new DAOException("Error deleting patient: " + e.getMessage(), e);
        }

        // Delete the associated user record
        UserDao userDao = DaoFactoryFacade.getInstance().getUserDao();
        userDao.deleteUser(username);

        notifyObservers(DaoOperation.DELETE, "Patient", username, null);
    }

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