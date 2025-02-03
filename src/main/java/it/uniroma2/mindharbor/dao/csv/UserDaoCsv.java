package it.uniroma2.mindharbor.dao.csv;

import it.uniroma2.mindharbor.beans.CredentialsBean;
import it.uniroma2.mindharbor.beans.UserBean;
import it.uniroma2.mindharbor.dao.UserDao;
import it.uniroma2.mindharbor.dao.csv.constants.UserDaoCsvConstants;
import it.uniroma2.mindharbor.exception.DAOException;
import it.uniroma2.mindharbor.utilities.CsvUtilities;

import java.io.File;
import java.util.List;

/**
 * The {@code UserDaoCsv} class implements the {@code UserDao} interface to handle
 * user data stored in a CSV file. It provides methods for user validation,
 * retrieval, and saving.
 */
public class UserDaoCsv implements UserDao {
    private static final File fd = new File(UserDaoCsvConstants.PATH_NAME_USER);

    /**
     * Validates a user by checking if the username and password stored in the given
     * {@link CredentialsBean} match an existing record in the persistence layer.
     * <p>
     * If a match is found, the user's actual type is retrieved and set in the
     * {@link CredentialsBean}. Otherwise, the type remains {@code null}.
     * </p>
     *
     * @param credentials The {@link CredentialsBean} containing the username and password to validate.
     * @throws DAOException If an error occurs while accessing the data storage.
     */
    @Override
    public void validateUser(CredentialsBean credentials) throws DAOException {

        String[] userRecord = retrieveUser(credentials.getUsername());
        if (userRecord != null && credentials.getPassword().equals(userRecord[UserDaoCsvConstants.USER_INDEX_PASSWORD])) {
            credentials.setType(userRecord[UserDaoCsvConstants.USER_INDEX_TYPE]);
        }
    }

    /**
     * Saves a new user in the CSV file.
     * <p>
     * If a user with the same username already exists, an exception is thrown.
     * Otherwise, the user details are stored in the CSV file.
     * </p>
     *
     * @param user The {@link UserBean} object containing the user's details.
     * @throws DAOException If an error occurs while writing to the file or if the user already exists.
     */
    @Override
    public void saveUser(UserBean user) throws DAOException {
        if (retrieveUser(user.getUsername()) != null) {
            // esiste uno username uguale a quello che sto cercando di registrare
            throw new DAOException(UserDaoCsvConstants.USER_EXIST);
        }
        // non esiste, posso procedere
        String[] userRecord = new String[UserDaoCsvConstants.HEADER.length];
        userRecord[UserDaoCsvConstants.USER_INDEX_USERNAME] = user.getUsername();
        userRecord[UserDaoCsvConstants.USER_INDEX_PASSWORD] = user.getPassword();
        userRecord[UserDaoCsvConstants.USER_INDEX_FIRST_NAME] = user.getName();
        userRecord[UserDaoCsvConstants.USER_INDEX_LAST_NAME] = user.getSurname();
        userRecord[UserDaoCsvConstants.USER_INDEX_TYPE] = user.getType();
        userRecord[UserDaoCsvConstants.USER_INDEX_GENDER] = user.getGender();
        CsvUtilities.writeFile(fd, userRecord);
    }

    /**
     * Retrieves user details from the CSV file based on the username.
     * <p>
     * The method searches for a user with the specified username in the CSV file.
     * If found, it returns an array of strings containing the user's data.
     * If no match is found, {@code null} is returned.
     * </p>
     *
     * @param username The username of the user to retrieve.
     * @return A {@code String[]} containing the user's details if found, otherwise {@code null}.
     * @throws DAOException If an error occurs while reading from the file.
     */
    @Override
    public String[] retrieveUser(String username) throws DAOException {
        List<String[]> userTable = CsvUtilities.readAll(fd);
        String[] userRecord = null;
        while (!userTable.isEmpty()) {
            userRecord = userTable.removeFirst();
            if (username.equals(userRecord[UserDaoCsvConstants.USER_INDEX_USERNAME])) {
                return userRecord;
            }
        }
        return userRecord;
    }

    /**
     * Updates an existing user's details in the CSV file.
     * <p>
     * If the user does not exist, a {@link DAOException} is thrown.
     * Otherwise, the user's information is updated and saved back to the file.
     * </p>
     *
     * @param user The {@link UserBean} object containing the updated details.
     * @throws DAOException If an error occurs while updating the user or if the user does not exist.
     */
    @Override
    public void updateUser(UserBean user) throws DAOException {
        // Read all user records from the CSV file
        List<String[]> userTable = CsvUtilities.readAll(fd);
        // Find and update the user record
        boolean found = false;
        for (String[] record : userTable) {
            if (record[UserDaoCsvConstants.USER_INDEX_USERNAME].equals(user.getUsername())) {
                record[UserDaoCsvConstants.USER_INDEX_PASSWORD] = user.getPassword();
                record[UserDaoCsvConstants.USER_INDEX_FIRST_NAME] = user.getName();
                record[UserDaoCsvConstants.USER_INDEX_LAST_NAME] = user.getSurname();
                record[UserDaoCsvConstants.USER_INDEX_TYPE] = user.getType();
                record[UserDaoCsvConstants.USER_INDEX_GENDER] = user.getGender();
                found = true;
                break;
            }
        }
        if (!found) {
            throw new DAOException(UserDaoCsvConstants.USER_NOT_FOUND + user.getUsername());
        }
        // Update the file with the modified data
        CsvUtilities.updateFile(fd, UserDaoCsvConstants.HEADER, userTable);
    }

    /**
     * Deletes a user from the CSV file.
     * <p>
     * If the user does not exist, a {@link DAOException} is thrown.
     * Otherwise, the user record is removed from the CSV file.
     * </p>
     *
     * @param username The username of the user to delete.
     * @throws DAOException If an error occurs while deleting the user or if the user does not exist.
     */
    @Override
    public void deleteUser(String username) throws DAOException {
        // Read all user records from the CSV file
        List<String[]> userTable = CsvUtilities.readAll(fd);
        // Remove the record corresponding to the given username
        boolean removed = userTable.removeIf(record -> record[UserDaoCsvConstants.USER_INDEX_USERNAME].equals(username));
        if (!removed) {
            throw new DAOException(UserDaoCsvConstants.USER_NOT_FOUND + username);
        }
        // Update the file with the modified data
        CsvUtilities.updateFile(fd, UserDaoCsvConstants.HEADER, userTable);
    }
}
