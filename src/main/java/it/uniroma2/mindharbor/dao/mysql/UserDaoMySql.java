package it.uniroma2.mindharbor.dao.mysql;

import it.uniroma2.mindharbor.beans.CredentialsBean;
import it.uniroma2.mindharbor.beans.UserBean;
import it.uniroma2.mindharbor.dao.ConnectionPoolManager;
import it.uniroma2.mindharbor.dao.UserDao;
import it.uniroma2.mindharbor.dao.mysql.constants.UserDaoMySqlQueries;
import it.uniroma2.mindharbor.exception.DAOException;
import it.uniroma2.mindharbor.utilities.PasswordUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * MySQL's implementation of the {@link UserDao} interface.
 * <p>
 * This class provides MySQL-specific implementations for user-related data
 * access operations, such as validating a user, saving user information,
 * retrieving user details, updating user data, and deleting users.
 * </p>
 * <p>
 * This implementation uses connection pooling through {@link ConnectionPoolManager}
 * to improve performance while maintaining robustness.
 * </p>
 */
public class UserDaoMySql implements UserDao {

    private static final Logger logger = Logger.getLogger(UserDaoMySql.class.getName());

    /**
     * Validates a user by checking if the provided username and password match
     * an existing record in the database.
     * <p>
     * If a match is found, the user's type is retrieved and set in the
     * {@link CredentialsBean}.
     * Otherwise, the {@code type} remains {@code null}.
     * </p>
     *
     * @param credentials The {@link CredentialsBean} containing the username and password to validate.
     * @throws DAOException If an error occurs while accessing the data storage.
     */
    @Override
    public void validateUser(CredentialsBean credentials) throws DAOException {
        try (Connection connection = ConnectionPoolManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(UserDaoMySqlQueries.VALIDATE_USER)) {

            stmt.setString(1, credentials.getUsername());

            // When validating, we want to compare with the hashed password
            // Since we can't decrypt the hashed password, we need to check differently
            // to First get the user by username
            String[] user = retrieveUser(credentials.getUsername());

            if (user != null) {
                // Check if the provided password matches the stored hash
                String storedPassword = user[1]; // Password is at index 1

                if (PasswordUtils.checkPassword(credentials.getPassword(), storedPassword)) {
                    // Password matches, set the user type
                    credentials.setType(user[4]); // Type is at index 4
                }
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error validating user credentials", e);
            throw new DAOException("Error validating user: " + e.getMessage(), e);
        }
    }

    /**
     * Saves a new user in the database.
     * <p>
     * If a user with the same username already exists, a {@link DAOException} is thrown.
     * Otherwise, the user details are stored.
     * </p>
     *
     * @param user The {@link UserBean} object containing the user's details.
     * @throws DAOException If an error occurs while saving the user or if the user already exists.
     */
    @Override
    public void saveUser(UserBean user) throws DAOException {
        // First check if the username is already taken
        if (isUsernameTaken(user.getUsername())) {
            throw new DAOException("Username already exists: " + user.getUsername());
        }

        try (Connection connection = ConnectionPoolManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(UserDaoMySqlQueries.INSERT_USER)) {

            // Hash the password before storing
            String hashedPassword = PasswordUtils.hashPassword(user.getPassword());

            stmt.setString(1, user.getUsername());
            stmt.setString(2, hashedPassword);
            stmt.setString(3, user.getName());
            stmt.setString(4, user.getSurname());
            stmt.setString(5, user.getType());
            stmt.setString(6, user.getGender());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new DAOException("Failed to save user, no rows affected.");
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error saving user", e);
            throw new DAOException("Error saving user: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves user details from the database based on the username.
     * <p>
     * The method searches for a user with the specified username and returns an array of strings
     * containing the user's details.
     * If no match is found, {@code null} is returned.
     * </p>
     *
     * @param username The username of the user to retrieve.
     * @return A {@code String[]} containing the user's details if found, otherwise {@code null}.
     * @throws DAOException If an error occurs while accessing the data storage.
     */
    @Override
    public String[] retrieveUser(String username) throws DAOException {
        try (Connection connection = ConnectionPoolManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(UserDaoMySqlQueries.SELECT_USER_BY_USERNAME)) {

            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String[] userDetails = new String[6];
                    userDetails[0] = rs.getString("Username");
                    userDetails[1] = rs.getString("Password");
                    userDetails[2] = rs.getString("Firstname");
                    userDetails[3] = rs.getString("Lastname");
                    userDetails[4] = rs.getString("Type");
                    userDetails[5] = rs.getString("Gender");
                    return userDetails;
                }
            }

            return null; // User not found

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error retrieving user", e);
            throw new DAOException("Error retrieving user: " + e.getMessage(), e);
        }
    }

    /**
     * Checks if a username is already taken in the database.
     *
     * @param username The username to check.
     * @return true if the username is already in use, false otherwise.
     * @throws DAOException If an error occurs while accessing the data storage.
     */
    @Override
    public boolean isUsernameTaken(String username) throws DAOException {
        try (Connection connection = ConnectionPoolManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(UserDaoMySqlQueries.CHECK_USERNAME_EXISTS)) {

            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

            return false;

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error checking if username is taken", e);
            throw new DAOException("Error checking if username is taken: " + e.getMessage(), e);
        }
    }

    /**
     * Updates an existing user's details in the database.
     * <p>
     * If the user does not exist, a {@link DAOException} is thrown.
     * Otherwise, the user's information is updated.
     * </p>
     *
     * @param user The {@link UserBean} object containing the updated details.
     * @throws DAOException If an error occurs while updating the user.
     */
    @Override
    public void updateUser(UserBean user) throws DAOException {
        try (Connection connection = ConnectionPoolManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(UserDaoMySqlQueries.UPDATE_USER)) {

            // Check if the original password has changed and needs to be hashed
            String[] currentUser = retrieveUser(user.getUsername());
            if (currentUser == null) {
                throw new DAOException("User not found: " + user.getUsername());
            }

            String passwordToStore;
            if (currentUser[1].equals(user.getPassword())) {
                // Password unchanged, store as is
                passwordToStore = user.getPassword();
            } else {
                // Password changed, hash the new password
                passwordToStore = PasswordUtils.hashPassword(user.getPassword());
            }

            stmt.setString(1, passwordToStore);
            stmt.setString(2, user.getName());
            stmt.setString(3, user.getSurname());
            stmt.setString(4, user.getType());
            stmt.setString(5, user.getGender());
            stmt.setString(6, user.getUsername()); // WHERE clause parameter

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new DAOException("User not found: " + user.getUsername());
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error updating user", e);
            throw new DAOException("Error updating user: " + e.getMessage(), e);
        }
    }

    /**
     * Deletes a user from the database.
     * <p>
     * If the user does not exist, a {@link DAOException} is thrown.
     * </p>
     *
     * @param username The username of the user to delete.
     * @throws DAOException If an error occurs while deleting the user.
     */
    @Override
    public void deleteUser(String username) throws DAOException {
        try (Connection connection = ConnectionPoolManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(UserDaoMySqlQueries.DELETE_USER)) {

            stmt.setString(1, username);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new DAOException("User not found: " + username);
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error deleting user", e);
            throw new DAOException("Error deleting user: " + e.getMessage(), e);
        }
    }
}
