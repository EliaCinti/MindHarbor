package it.uniroma2.mindharbor.dao.csv;

import it.uniroma2.mindharbor.beans.CredentialsBean;
import it.uniroma2.mindharbor.beans.UserBean;
import it.uniroma2.mindharbor.dao.UserDao;
import it.uniroma2.mindharbor.exception.DAOException;
import it.uniroma2.mindharbor.utilities.CsvUtilities;

import java.io.File;
import java.util.List;

public class UserDaoCsv implements UserDao {
    private static final File fd = new File("db/csv/user_db.csv");

    @Override
    public void validateUser(CredentialsBean credentials) throws DAOException {
        String[] userRecord = retrieveUser(credentials.getUsername());
        if (userRecord != null && credentials.getPassword().equals(userRecord[UserIndex.USER_INDEX_PASSWORD])) {
            credentials.setType(userRecord[UserIndex.USER_INDEX_TYPE]);
        }
    }

    @Override
    public void saveUser(UserBean user) throws DAOException {
        if (retrieveUser(user.getUsername()) != null) {
            // esiste uno username uguale a quello che sto cercando di registrare
            throw new DAOException("User already exists");
        }
        // non esiste, posso procedere
        String[] userRecord = new String[6];
        userRecord[UserIndex.USER_INDEX_USERNAME] = user.getUsername();
        userRecord[UserIndex.USER_INDEX_PASSWORD] = user.getPassword();
        userRecord[UserIndex.USER_INDEX_FIRST_NAME] = user.getName();
        userRecord[UserIndex.USER_INDEX_LAST_NAME] = user.getSurname();
        userRecord[UserIndex.USER_INDEX_TYPE] = user.getType();
        userRecord[UserIndex.USER_INDEX_GENDER] = user.getGender();
        CsvUtilities.writeFile(fd, userRecord);
    }

    @Override
    public String[] retrieveUser(String username) throws DAOException {
        List<String[]> userTable = CsvUtilities.readAll(fd);
        String[] userRecord = null;
        while (!userTable.isEmpty()) {
            userRecord = userTable.removeFirst();
            if (username.equals(userRecord[UserIndex.USER_INDEX_USERNAME])) {
                return userRecord;
            }
        }
        return userRecord;
    }


    private static class UserIndex {
        static final int USER_INDEX_USERNAME = 0;
        static final int USER_INDEX_PASSWORD = 1;
        static final int USER_INDEX_FIRST_NAME = 2;
        static final int USER_INDEX_LAST_NAME = 3;
        static final int USER_INDEX_TYPE = 4;
        static final int USER_INDEX_GENDER = 5;
    }
}
