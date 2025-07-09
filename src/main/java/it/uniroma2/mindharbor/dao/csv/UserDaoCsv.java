package it.uniroma2.mindharbor.dao.csv;

import it.uniroma2.mindharbor.beans.CredentialsBean;
import it.uniroma2.mindharbor.beans.UserBean;
import it.uniroma2.mindharbor.dao.UserDao;
import it.uniroma2.mindharbor.dao.csv.constants.UserDaoCsvConstants;
import it.uniroma2.mindharbor.exception.DAOException;
import it.uniroma2.mindharbor.utilities.CsvUtilities;
import it.uniroma2.mindharbor.utilities.PasswordUtils;
import java.io.File;
import java.util.List;

/**
 * The {@code UserDaoCsv} class implements the {@code UserDao} interface to handle
 * user data stored in a CSV file. It provides methods for user validation,
 * retrieval, and saving.
 */
public class UserDaoCsv implements UserDao {
    private static final File fd = new File(UserDaoCsvConstants.PATH_NAME_USER);

    @Override
    public void validateUser(CredentialsBean credentials) throws DAOException {
        String[] userRecord = retrieveUser(credentials.getUsername());
        if (userRecord != null) {
            String storedHashedPassword = userRecord[UserDaoCsvConstants.USER_INDEX_PASSWORD];

            // check the password by BCrypt
            if(PasswordUtils.checkPassword(credentials.getPassword(), storedHashedPassword)){
                // Password OK
                credentials.setType(userRecord[UserDaoCsvConstants.USER_INDEX_TYPE]);
            }
        }
    }

    @Override
    public void saveUser(UserBean user) throws DAOException {
        if (isUsernameTaken(user.getUsername())) {
            // esiste uno username uguale a quello che sto cercando di registrare
            throw new DAOException(UserDaoCsvConstants.USER_EXIST);
        }
        // Hashing della password
        String hashedPassword = PasswordUtils.hashPassword(user.getPassword());

        // non esiste, posso procedere
        String[] userRecord = new String[UserDaoCsvConstants.HEADER.length];
        userRecord[UserDaoCsvConstants.USER_INDEX_USERNAME] = user.getUsername();
        userRecord[UserDaoCsvConstants.USER_INDEX_PASSWORD] = hashedPassword;
        userRecord[UserDaoCsvConstants.USER_INDEX_FIRST_NAME] = user.getName();
        userRecord[UserDaoCsvConstants.USER_INDEX_LAST_NAME] = user.getSurname();
        userRecord[UserDaoCsvConstants.USER_INDEX_TYPE] = user.getType();
        userRecord[UserDaoCsvConstants.USER_INDEX_GENDER] = user.getGender();

        CsvUtilities.writeFile(fd, userRecord);
    }

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

    @Override
    public boolean isUsernameTaken(String username) throws DAOException {
        // Verifica se il file esiste
        if (!fd.exists() || fd.length() == 0) {
            return false; // Se il file non esiste o è vuoto, nessun username è preso
        }
        List<String[]> userTable = CsvUtilities.readAll(fd);

        // Salta l'intestazione se presente
        if (!userTable.isEmpty()) {
            userTable.removeFirst();
        }

        // Cerca l'username nelle righe
        for (String[] recordUser : userTable) {
            if (recordUser.length > UserDaoCsvConstants.USER_INDEX_USERNAME &&
                    username.equals(recordUser[UserDaoCsvConstants.USER_INDEX_USERNAME])) {
                return true; // Username trovato, quindi è già preso
            }
        }
        return false; // Username non trovato, è disponibile
    }

    @Override
    public void updateUser(UserBean user) throws DAOException {
        // Read all user records from the CSV file
        List<String[]> userTable = CsvUtilities.readAll(fd);
        // Find and update the user record
        boolean found = false;
        for (String[] recordUser : userTable) {
            if (recordUser[UserDaoCsvConstants.USER_INDEX_USERNAME].equals(user.getUsername())) {
                recordUser[UserDaoCsvConstants.USER_INDEX_PASSWORD] = user.getPassword();
                recordUser[UserDaoCsvConstants.USER_INDEX_FIRST_NAME] = user.getName();
                recordUser[UserDaoCsvConstants.USER_INDEX_LAST_NAME] = user.getSurname();
                recordUser[UserDaoCsvConstants.USER_INDEX_TYPE] = user.getType();
                recordUser[UserDaoCsvConstants.USER_INDEX_GENDER] = user.getGender();
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

    @Override
    public void deleteUser(String username) throws DAOException {
        // Read all user records from the CSV file
        List<String[]> userTable = CsvUtilities.readAll(fd);
        // Remove the record corresponding to the given username
        boolean removed = userTable.removeIf(recordUser -> recordUser[UserDaoCsvConstants.USER_INDEX_USERNAME].equals(username));
        if (!removed) {
            throw new DAOException(UserDaoCsvConstants.USER_NOT_FOUND + username);
        }
        // Update the file with the modified data
        CsvUtilities.updateFile(fd, UserDaoCsvConstants.HEADER, userTable);
    }
}
