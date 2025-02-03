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
 * access operations, such as validating a user, saving user information,
 * retrieving user details, updating user data, and deleting users.
 * </p>
 *
 * @TODO Implement all database operations using MySQL queries.
 */
public class UserDaoMySql implements UserDao {

    /**
     * Validates a user's credentials against the database.
     * <p>
     * This method checks whether the provided username and password exist in the database.
     * If validation succeeds, it sets the user's type (e.g., admin, psychologist, patient).
     * </p>
     *
     * @param credentials The {@link CredentialsBean} containing the user's username and password.
     * @throws DAOException If a database access error occurs or if the user is not found.
     * @TODO Implement the SQL query for user validation.
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
     * This method inserts a new user record into the database.
     * </p>
     *
     * @param user The {@link UserBean} containing the user's details.
     * @throws DAOException If a database access error occurs during the save operation.
     * @TODO Implement the SQL INSERT query to save a new user.
     */
    @Override
    public void saveUser(UserBean user) throws DAOException {
        // TODO: Implement the SQL INSERT query to save user data.
    }


    /**
     * Retrieves a user's information from the database based on their username.
     * <p>
     * This method fetches the user details (such as name, surname, and type) from the database.
     * </p>
     *
     * @param username The username of the user to retrieve.
     * @return A string array containing the user's details.
     * @throws DAOException If a database access error occurs during the retrieval operation.
     * @TODO Implement the SQL SELECT query to retrieve user details.
     */
    @Override
    public String[] retrieveUser(String username) throws DAOException {
        // TODO: Implement the SQL SELECT query to retrieve user details.
        return new String[0];
    }

    /**
     * Updates an existing user's details in the database.
     * <p>
     * This method modifies an existing user record based on the provided {@link UserBean}.
     * </p>
     *
     * @param user The {@link UserBean} object containing updated user details.
     * @throws DAOException If a database access error occurs during the update operation.
     * @TODO Implement the SQL UPDATE query to modify user data.
     */
    @Override
    public void updateUser(UserBean user) throws DAOException {
        // TODO: Implement the SQL UPDATE query to update user details.
    }

    /**
     * Deletes a user from the database.
     * <p>
     * This method removes a user record based on the specified username.
     * </p>
     *
     * @param username The username of the user to delete.
     * @throws DAOException If a database access error occurs during the delete operation.
     * @TODO Implement the SQL DELETE query to remove a user.
     */
    @Override
    public void deleteUser(String username) throws DAOException {
        // TODO: Implement the SQL DELETE query to remove a user from the database.
    }
}
