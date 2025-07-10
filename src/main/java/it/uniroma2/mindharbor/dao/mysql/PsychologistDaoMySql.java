package it.uniroma2.mindharbor.dao.mysql;

import it.uniroma2.mindharbor.beans.PsychologistBean;
import it.uniroma2.mindharbor.beans.UserBean;
import it.uniroma2.mindharbor.dao.AbstractObservableDao;
import it.uniroma2.mindharbor.dao.ConnectionFactory;
import it.uniroma2.mindharbor.dao.PatientDao;
import it.uniroma2.mindharbor.dao.PsychologistDao;
import it.uniroma2.mindharbor.dao.UserDao;
import it.uniroma2.mindharbor.dao.mysql.constants.PsychologistDaoMySqlConstants;
import it.uniroma2.mindharbor.dao.mysql.constants.PsychologistDaoMySqlQueries;
import it.uniroma2.mindharbor.exception.DAOException;
import it.uniroma2.mindharbor.model.Patient;
import it.uniroma2.mindharbor.model.Psychologist;
import it.uniroma2.mindharbor.patterns.facade.DaoFactoryFacade;
import it.uniroma2.mindharbor.patterns.observer.DaoOperation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PsychologistDaoMySql extends AbstractObservableDao implements PsychologistDao {
    private static final Logger logger = Logger.getLogger(PsychologistDaoMySql.class.getName());

    private Connection getConnection() throws DAOException {
        try {
            return ConnectionFactory.getConnection();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Failed to get database connection", e);
            throw new DAOException("Error obtaining database connection: " + e.getMessage(), e);
        }
    }

    @Override
    public void savePsychologist(PsychologistBean psychologist) throws DAOException {
        UserDao userDao = DaoFactoryFacade.getInstance().getUserDao();
        userDao.saveUser(psychologist);

        Connection connection = getConnection();
        try (PreparedStatement stmt = connection.prepareStatement(PsychologistDaoMySqlQueries.INSERT_PSYCHOLOGIST)) {
            stmt.setString(1, psychologist.getUsername());
            stmt.setString(2, psychologist.getOffice());
            stmt.setDouble(3, psychologist.getHourlyCost());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new DAOException(PsychologistDaoMySqlConstants.FAILED_TO_SAVE_PSYCHOLOGIST);
            }
        } catch (SQLException e) {
            throw new DAOException(PsychologistDaoMySqlConstants.ERROR_SAVING_PSYCHOLOGIST + e.getMessage(), e);
        }
        notifyObservers(DaoOperation.INSERT, "Psychologist", psychologist.getUsername(), psychologist);
    }

    @Override
    public Psychologist retrievePsychologist(String username) throws DAOException {
        Connection connection = getConnection();
        try (PreparedStatement stmt = connection.prepareStatement(PsychologistDaoMySqlQueries.SELECT_PSYCHOLOGIST_BY_USERNAME)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractPsychologistFromResultSet(rs);
                }
            }
            return null;
        } catch (SQLException e) {
            throw new DAOException(PsychologistDaoMySqlConstants.ERROR_RETRIEVING_PSYCHOLOGIST + e.getMessage(), e);
        }
    }

    @Override
    public List<Psychologist> retrieveAllPsychologists() throws DAOException {
        List<Psychologist> psychologists = new ArrayList<>();
        Connection connection = getConnection();
        try (PreparedStatement stmt = connection.prepareStatement(PsychologistDaoMySqlQueries.SELECT_ALL_PSYCHOLOGISTS);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                psychologists.add(extractPsychologistFromResultSet(rs));
            }
        } catch (SQLException e) {
            throw new DAOException("Error retrieving all psychologists: " + e.getMessage(), e);
        }
        return psychologists;
    }

    @Override
    public void updatePsychologist(Psychologist psychologist, UserBean bean) throws DAOException {
        UserDao userDao = DaoFactoryFacade.getInstance().getUserDao();
        userDao.updateUser(bean);

        Connection connection = getConnection();
        try (PreparedStatement stmt = connection.prepareStatement(PsychologistDaoMySqlQueries.UPDATE_PSYCHOLOGIST)) {
            stmt.setString(1, psychologist.getOffice());
            stmt.setString(2, psychologist.getHourlyCost());
            stmt.setString(3, psychologist.getUsername());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new DAOException(PsychologistDaoMySqlConstants.PSYCHOLOGIST_NOT_FOUND + psychologist.getUsername());
            }
        } catch (SQLException e) {
            throw new DAOException(PsychologistDaoMySqlConstants.ERROR_UPDATING_PSYCHOLOGIST + e.getMessage(), e);
        }
        notifyObservers(DaoOperation.UPDATE, "Psychologist", psychologist.getUsername(), psychologist);
    }

    @Override
    public void deletePsychologist(String username) throws DAOException {
        Connection connection = getConnection();
        try (PreparedStatement stmt = connection.prepareStatement(PsychologistDaoMySqlQueries.DELETE_PSYCHOLOGIST)) {
            stmt.setString(1, username);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new DAOException(PsychologistDaoMySqlConstants.PSYCHOLOGIST_NOT_FOUND + username);
            }
        } catch (SQLException e) {
            throw new DAOException(PsychologistDaoMySqlConstants.ERROR_DELETING_PSYCHOLOGIST + e.getMessage(), e);
        }

        UserDao userDao = DaoFactoryFacade.getInstance().getUserDao();
        userDao.deleteUser(username);

        notifyObservers(DaoOperation.DELETE, "Psychologist", username, null);
    }

    @Override
    public List<Patient> getPatients(Psychologist psychologist) throws DAOException {
        PatientDao patientDao = DaoFactoryFacade.getInstance().getPatientDao();
        return patientDao.retrievePatientsByPsychologist(psychologist);
    }

    private Psychologist extractPsychologistFromResultSet(ResultSet rs) throws SQLException {
        String username = rs.getString(PsychologistDaoMySqlConstants.COLUMN_USERNAME);
        String firstName = rs.getString(PsychologistDaoMySqlConstants.COLUMN_FIRSTNAME);
        String lastName = rs.getString(PsychologistDaoMySqlConstants.COLUMN_LASTNAME);
        String gender = rs.getString(PsychologistDaoMySqlConstants.COLUMN_GENDER);
        String office = rs.getString(PsychologistDaoMySqlConstants.COLUMN_OFFICE);
        String hourlyCost = String.valueOf(rs.getDouble(PsychologistDaoMySqlConstants.COLUMN_HOURLY_COST));
        return new Psychologist(username, firstName, lastName, gender, office, hourlyCost);
    }
}