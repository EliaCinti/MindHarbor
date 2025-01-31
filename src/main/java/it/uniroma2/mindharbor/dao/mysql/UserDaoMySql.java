package it.uniroma2.mindharbor.dao.mysql;

import it.uniroma2.mindharbor.beans.CredentialsBean;
import it.uniroma2.mindharbor.beans.UserBean;
import it.uniroma2.mindharbor.dao.ConnectionManager;
import it.uniroma2.mindharbor.dao.UserDao;
import it.uniroma2.mindharbor.exception.DAOException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * MySQL implementation of the {@link UserDao} interface.
 * <p>
 * This class provides MySQL-specific implementations for user-related data
 * access operations, such as validating a user, saving user information, and
 * retrieving user details.
 * </p>
 */
public class UserDaoMySql implements UserDao {

    /**
     * Validates a user's credentials against the database.
     * <p>
     * This method checks whether the provided username and password exist in the database
     * and sets the user's type (e.g., admin, psychologist, patient) if validation succeeds.
     * </p>
     *
     * @param credentials The {@link CredentialsBean} containing the user's username and password.
     * @throws DAOException If a database access error occurs or if the user is not found.
     */
    @Override
    public void validateUser(CredentialsBean credentials) throws DAOException {
        try {
            Connection connection = ConnectionManager.getConnection();
            PreparedStatement ps = connection.prepareStatement("");
            ps.setString(1, credentials.getUsername());
            ps.setString(2, credentials.getPassword());
            ResultSet rs = ps.executeQuery();
            credentials.setType(rs.getString("Categoria"));
        } catch (SQLException e) {
            throw new DAOException(e.getMessage());
        }
    }

    /**
     * Saves a user's information in the database.
     * <p>
     * This method is a placeholder for inserting user data into the database.
     * </p>
     *
     * @param user The {@link UserBean} containing the user's details.
     * @throws DAOException If a database access error occurs during the save operation.
     */
    @Override
    public void saveUser(UserBean user) throws DAOException {
        // Implementation placeholder
    }

    /**
     * Retrieves a user's information from the database based on their username.
     * <p>
     * This method is a placeholder for fetching user details from the database.
     * </p>
     *
     * @param username The username of the user to retrieve.
     * @return A string array containing the user's details.
     * @throws DAOException If a database access error occurs during the retrieval operation.
     */
    @Override
    public String[] retrieveUser(String username) throws DAOException {
        return new String[0]; // Implementation placeholder
    }
}
