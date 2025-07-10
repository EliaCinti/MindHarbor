package it.uniroma2.mindharbor.dao.csv;

import it.uniroma2.mindharbor.beans.CredentialsBean;
import it.uniroma2.mindharbor.beans.UserBean;
import it.uniroma2.mindharbor.dao.AbstractObservableDao;
import it.uniroma2.mindharbor.dao.UserDao;
import it.uniroma2.mindharbor.dao.csv.constants.UserDaoCsvConstants;
import it.uniroma2.mindharbor.exception.DAOException;
import it.uniroma2.mindharbor.patterns.observer.DaoOperation;
import it.uniroma2.mindharbor.utilities.CsvUtilities;
import it.uniroma2.mindharbor.utilities.PasswordUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UserDaoCsv extends AbstractObservableDao implements UserDao {
    private static final File fd = new File(UserDaoCsvConstants.PATH_NAME_USER);

    @Override
    public void validateUser(CredentialsBean credentials) throws DAOException {
        String[] userRecord = retrieveUser(credentials.getUsername());
        if (userRecord != null) {
            String storedHashedPassword = userRecord[UserDaoCsvConstants.USER_INDEX_PASSWORD];
            if (PasswordUtils.checkPassword(credentials.getPassword(), storedHashedPassword)) {
                credentials.setType(userRecord[UserDaoCsvConstants.USER_INDEX_TYPE]);
            }
        }
    }

    @Override
    public void saveUser(UserBean user) throws DAOException {
        if (isUsernameTaken(user.getUsername())) {
            throw new DAOException(UserDaoCsvConstants.USER_EXIST);
        }
        String hashedPassword = PasswordUtils.hashPassword(user.getPassword());

        String[] userRecord = new String[UserDaoCsvConstants.HEADER.length];
        userRecord[UserDaoCsvConstants.USER_INDEX_USERNAME] = user.getUsername();
        userRecord[UserDaoCsvConstants.USER_INDEX_PASSWORD] = hashedPassword;
        userRecord[UserDaoCsvConstants.USER_INDEX_FIRST_NAME] = user.getName();
        userRecord[UserDaoCsvConstants.USER_INDEX_LAST_NAME] = user.getSurname();
        userRecord[UserDaoCsvConstants.USER_INDEX_TYPE] = user.getType();
        userRecord[UserDaoCsvConstants.USER_INDEX_GENDER] = user.getGender();

        CsvUtilities.writeFile(fd, userRecord);
        notifyObservers(DaoOperation.INSERT, "User", user.getUsername(), user);
    }

    @Override
    public String[] retrieveUser(String username) throws DAOException {
        List<String[]> userTable = CsvUtilities.readAll(fd);
        if (!userTable.isEmpty() && "Username".equals(userTable.getFirst()[0])) {
            userTable.removeFirst();
        }
        for (String[] userRecord : userTable) {
            if (userRecord.length > UserDaoCsvConstants.USER_INDEX_USERNAME && username.equals(userRecord[UserDaoCsvConstants.USER_INDEX_USERNAME])) {
                return userRecord;
            }
        }
        return null;
    }

    @Override
    public List<UserBean> retrieveAllUsers() throws DAOException {
        List<UserBean> users = new ArrayList<>();
        List<String[]> records = CsvUtilities.readAll(fd);
        if (!records.isEmpty() && "Username".equals(records.getFirst()[0])) {
            records.removeFirst(); // Rimuovi header
        }

        for (String[] record : records) {
            // Costruisci il bean senza password in chiaro per sicurezza
            UserBean user = new UserBean.Builder<>()
                    .username(record[UserDaoCsvConstants.USER_INDEX_USERNAME])
                    .password(record[UserDaoCsvConstants.USER_INDEX_PASSWORD]) // La password nel DB è già hashata
                    .name(record[UserDaoCsvConstants.USER_INDEX_FIRST_NAME])
                    .surname(record[UserDaoCsvConstants.USER_INDEX_LAST_NAME])
                    .type(record[UserDaoCsvConstants.USER_INDEX_TYPE])
                    .gender(record[UserDaoCsvConstants.USER_INDEX_GENDER])
                    .build();
            users.add(user);
        }
        return users;
    }

    @Override
    public boolean isUsernameTaken(String username) throws DAOException {
        if (!fd.exists() || fd.length() == 0) {
            return false;
        }
        List<String[]> userTable = CsvUtilities.readAll(fd);
        if (!userTable.isEmpty()) {
            userTable.removeFirst();
        }
        for (String[] recordUser : userTable) {
            if (recordUser.length > UserDaoCsvConstants.USER_INDEX_USERNAME &&
                    username.equals(recordUser[UserDaoCsvConstants.USER_INDEX_USERNAME])) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void updateUser(UserBean user) throws DAOException {
        List<String[]> userTable = CsvUtilities.readAll(fd);
        String[] header = userTable.removeFirst();
        boolean found = false;
        for (String[] recordUser : userTable) {
            if (recordUser[UserDaoCsvConstants.USER_INDEX_USERNAME].equals(user.getUsername())) {
                recordUser[UserDaoCsvConstants.USER_INDEX_PASSWORD] = PasswordUtils.hashPassword(user.getPassword()); // Riapplica l'hash
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
        CsvUtilities.updateFile(fd, header, userTable);
        notifyObservers(DaoOperation.UPDATE, "User", user.getUsername(), user);
    }

    @Override
    public void deleteUser(String username) throws DAOException {
        List<String[]> userTable = CsvUtilities.readAll(fd);
        String[] header = userTable.removeFirst();
        boolean removed = userTable.removeIf(recordUser -> recordUser[UserDaoCsvConstants.USER_INDEX_USERNAME].equals(username));
        if (!removed) {
            throw new DAOException(UserDaoCsvConstants.USER_NOT_FOUND + username);
        }
        CsvUtilities.updateFile(fd, header, userTable);
        notifyObservers(DaoOperation.DELETE, "User", username, null);
    }
}
