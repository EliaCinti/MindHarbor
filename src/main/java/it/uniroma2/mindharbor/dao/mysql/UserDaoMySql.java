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
 * MySQL's implementation of the {@link UserDao} interface.
 * <p>
 * This class provides MySQL-specific implementations for user-related data
 * access operations, such as validating a user, saving user information,
 * retrieving user details, updating user data, and deleting users.
 * </p>
 *
 * @TODO Implement all database operations using MySQL queries.
 */
public class UserDaoMySql implements UserDao {

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

    @Override
    public void saveUser(UserBean user) throws DAOException {
        // TODO: Implement the SQL INSERT query to save user data.
    }

    @Override
    public String[] retrieveUser(String username) throws DAOException {
        // TODO: Implement the SQL SELECT query to retrieve user details.
        return new String[0];
    }

    @Override
    public boolean isUsernameTaken(String username) throws DAOException {
        try {
            Connection connection = ConnectionManager.getConnection();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM Users WHERE Username = ?");
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            return rs.next(); // true se esiste una riga con quell'username
        } catch (SQLException e) {
            throw new DAOException(e.getMessage());
        }
    }

    @Override
    public void updateUser(UserBean user) throws DAOException {
        // TODO: Implement the SQL UPDATE query to update user details.
    }

    @Override
    public void deleteUser(String username) throws DAOException {
        // TODO: Implement the SQL DELETE query to remove a user from the database.
    }
}
