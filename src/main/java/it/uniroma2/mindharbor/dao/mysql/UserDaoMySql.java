package it.uniroma2.mindharbor.dao.mysql;

import it.uniroma2.mindharbor.beans.CredentialsBean;
import it.uniroma2.mindharbor.beans.UserBean;
import it.uniroma2.mindharbor.dao.AbstractObservableDao;
import it.uniroma2.mindharbor.dao.ConnectionFactory;
import it.uniroma2.mindharbor.dao.UserDao;
import it.uniroma2.mindharbor.dao.mysql.constants.UserDaoMySqlConstants;
import it.uniroma2.mindharbor.dao.mysql.constants.UserDaoMySqlQueries;
import it.uniroma2.mindharbor.exception.DAOException;
import it.uniroma2.mindharbor.patterns.observer.DaoOperation;
import it.uniroma2.mindharbor.utilities.PasswordUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDaoMySql extends AbstractObservableDao implements UserDao {

    private static final Logger logger = Logger.getLogger(UserDaoMySql.class.getName());

    private Connection getConnection() throws DAOException {
        try {
            return ConnectionFactory.getConnection();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Failed to get database connection", e);
            throw new DAOException("Error obtaining database connection: " + e.getMessage(), e);
        }
    }

    @Override
    public void validateUser(CredentialsBean credentials) throws DAOException {
        String[] user = retrieveUser(credentials.getUsername());

        if (user != null) {
            String storedPassword = user[1]; // Password is at index 1
            if (PasswordUtils.checkPassword(credentials.getPassword(), storedPassword)) {
                credentials.setType(user[4]); // Type is at index 4
            }
        }
    }

    @Override
    public void saveUser(UserBean user) throws DAOException {
        if (isUsernameTaken(user.getUsername())) {
            throw new DAOException(UserDaoMySqlConstants.USERNAME_ALREADY_EXISTS + user.getUsername());
        }

        Connection connection = getConnection();
        try (PreparedStatement stmt = connection.prepareStatement(UserDaoMySqlQueries.INSERT_USER)) {
            String hashedPassword = PasswordUtils.hashPassword(user.getPassword());
            stmt.setString(1, user.getUsername());
            stmt.setString(2, hashedPassword);
            stmt.setString(3, user.getName());
            stmt.setString(4, user.getSurname());
            stmt.setString(5, user.getType());
            stmt.setString(6, user.getGender());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new DAOException(UserDaoMySqlConstants.FAILED_TO_SAVE_USER);
            }
        } catch (SQLException e) {
            throw new DAOException(UserDaoMySqlConstants.ERROR_SAVING_USER + e.getMessage(), e);
        }
        notifyObservers(DaoOperation.INSERT, "User", user.getUsername(), user);
    }

    @Override
    public String[] retrieveUser(String username) throws DAOException {
        Connection connection = getConnection();
        try (PreparedStatement stmt = connection.prepareStatement(UserDaoMySqlQueries.SELECT_USER_BY_USERNAME)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String[] userDetails = new String[6];
                    userDetails[0] = rs.getString(UserDaoMySqlConstants.COLUMN_USERNAME);
                    userDetails[1] = rs.getString(UserDaoMySqlConstants.COLUMN_PASSWORD);
                    userDetails[2] = rs.getString(UserDaoMySqlConstants.COLUMN_FIRSTNAME);
                    userDetails[3] = rs.getString(UserDaoMySqlConstants.COLUMN_LASTNAME);
                    userDetails[4] = rs.getString(UserDaoMySqlConstants.COLUMN_TYPE);
                    userDetails[5] = rs.getString(UserDaoMySqlConstants.COLUMN_GENDER);
                    return userDetails;
                }
            }
            return null;
        } catch (SQLException e) {
            throw new DAOException(UserDaoMySqlConstants.ERROR_RETRIEVING_USER + e.getMessage(), e);
        }
    }

    @Override
    public boolean isUsernameTaken(String username) throws DAOException {
        Connection connection = getConnection();
        try (PreparedStatement stmt = connection.prepareStatement(UserDaoMySqlQueries.CHECK_USERNAME_EXISTS)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            return false;
        } catch (SQLException e) {
            throw new DAOException(UserDaoMySqlConstants.ERROR_CHECKING_USERNAME + e.getMessage(), e);
        }
    }

    @Override
    public void updateUser(UserBean user) throws DAOException {
        String[] currentUser = retrieveUser(user.getUsername());
        if (currentUser == null) {
            throw new DAOException(UserDaoMySqlConstants.USER_NOT_FOUND + user.getUsername());
        }

        Connection connection = getConnection();
        try (PreparedStatement stmt = connection.prepareStatement(UserDaoMySqlQueries.UPDATE_USER)) {
            String passwordToStore;
            if (user.getPassword() != null && !user.getPassword().isEmpty() && !PasswordUtils.checkPassword(user.getPassword(), currentUser[1])) {
                passwordToStore = PasswordUtils.hashPassword(user.getPassword());
            } else {
                passwordToStore = currentUser[1];
            }

            stmt.setString(1, passwordToStore);
            stmt.setString(2, user.getName());
            stmt.setString(3, user.getSurname());
            stmt.setString(4, user.getType());
            stmt.setString(5, user.getGender());
            stmt.setString(6, user.getUsername());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new DAOException(UserDaoMySqlConstants.USER_NOT_FOUND + user.getUsername());
            }
        } catch (SQLException e) {
            throw new DAOException(UserDaoMySqlConstants.ERROR_UPDATING_USER + e.getMessage(), e);
        }
        notifyObservers(DaoOperation.UPDATE, "User", user.getUsername(), user);
    }

    @Override
    public void deleteUser(String username) throws DAOException {
        Connection connection = getConnection();
        try (PreparedStatement stmt = connection.prepareStatement(UserDaoMySqlQueries.DELETE_USER)) {
            stmt.setString(1, username);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new DAOException(UserDaoMySqlConstants.USER_NOT_FOUND + username);
            }
        } catch (SQLException e) {
            throw new DAOException(UserDaoMySqlConstants.ERROR_DELETING_USER + e.getMessage(), e);
        }
        notifyObservers(DaoOperation.DELETE, "User", username, null);
    }
}
