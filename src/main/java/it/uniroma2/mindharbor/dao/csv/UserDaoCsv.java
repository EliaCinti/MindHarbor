package it.uniroma2.mindharbor.dao.csv;

import it.uniroma2.mindharbor.beans.CredentialsBean;
import it.uniroma2.mindharbor.beans.UserBean;
import it.uniroma2.mindharbor.dao.UserDao;
import it.uniroma2.mindharbor.dao.csv.constants.UserDaoCsvConstants;
import it.uniroma2.mindharbor.exception.DAOException;
import it.uniroma2.mindharbor.utilities.CsvUtilities;

import java.io.File;
import java.util.List;

public class UserDaoCsv implements UserDao {
    private static final File fd = new File(UserDaoCsvConstants.PATH_NAME_USER);

    @Override
    public void validateUser(CredentialsBean credentials) throws DAOException {
        String[] userRecord = retrieveUser(credentials.getUsername());
        if (userRecord != null && credentials.getPassword().equals(userRecord[UserDaoCsvConstants.USER_INDEX_PASSWORD])) {
            credentials.setType(userRecord[UserDaoCsvConstants.USER_INDEX_TYPE]);
        }
    }

    @Override
    public void saveUser(UserBean user) throws DAOException {
        if (retrieveUser(user.getUsername()) != null) {
            // esiste uno username uguale a quello che sto cercando di registrare
            throw new DAOException(UserDaoCsvConstants.USER_EXIST);
        }
        // non esiste, posso procedere
        String[] userRecord = new String[6];
        userRecord[UserDaoCsvConstants.USER_INDEX_USERNAME] = user.getUsername();
        userRecord[UserDaoCsvConstants.USER_INDEX_PASSWORD] = user.getPassword();
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
}
