package it.uniroma2.mindharbor.dao;

import it.uniroma2.mindharbor.beans.CredentialsBean;
import it.uniroma2.mindharbor.beans.UserBean;
import it.uniroma2.mindharbor.exception.DAOException;

/**
 * The {@code UserDao} interface defines data access operations for managing users.
 * <p>
 * Implementations of this interface provide functionality to validate user credentials,
 * save new users, and retrieve user data from the persistence layer.
 * </p>
 */
public interface UserDao {
    /**
     * Validates a user by checking if the provided username and password match
     * an existing record in the persistence layer.
     * <p>
     * If a match is found, the user's type is retrieved and set in the
     * {@link CredentialsBean}. Otherwise, the {@code type} remains {@code null}.
     * </p>
     *
     * @param credentials The {@link CredentialsBean} containing the username and password to validate.
     * @throws DAOException If an error occurs while accessing the data storage.
     */
    void validateUser(CredentialsBean credentials) throws DAOException;

    /**
     * Saves a new user in the persistence layer.
     * <p>
     * If a user with the same username already exists, a {@link DAOException} is thrown.
     * Otherwise, the user details are stored.
     * </p>
     *
     * @param user The {@link UserBean} object containing the user's details.
     * @throws DAOException If an error occurs while saving the user or if the user already exists.
     */
    void saveUser(UserBean user) throws DAOException;

    /**
     * Retrieves user details from the persistence layer based on the username.
     * <p>
     * The method searches for a user with the specified username and returns an array of strings
     * containing the user's details. If no match is found, {@code null} is returned.
     * </p>
     *
     * @param username The username of the user to retrieve.
     * @return A {@code String[]} containing the user's details if found, otherwise {@code null}.
     * @throws DAOException If an error occurs while accessing the data storage.
     */
    String[] retrieveUser(String username) throws DAOException;

    /**
     * Updates an existing user's details in the persistence layer.
     * <p>
     * If the user does not exist, a {@link DAOException} is thrown.
     * Otherwise, the user's information is updated.
     * </p>
     *
     * @param user The {@link UserBean} object containing the updated details.
     * @throws DAOException If an error occurs while updating the user.
     */
    void updateUser(UserBean user) throws DAOException;

    /**
     * Deletes a user from the persistence layer.
     * <p>
     * If the user does not exist, no action is taken.
     * </p>
     *
     * @param username The username of the user to delete.
     * @throws DAOException If an error occurs while deleting the user.
     */
    void deleteUser(String username) throws DAOException;
}
