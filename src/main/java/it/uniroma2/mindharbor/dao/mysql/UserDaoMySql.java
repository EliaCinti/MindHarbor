package it.uniroma2.mindharbor.dao.mysql;

import it.uniroma2.mindharbor.beans.CredentialsBean;
import it.uniroma2.mindharbor.dao.ConnectionManager;
import it.uniroma2.mindharbor.dao.UserDao;
import it.uniroma2.mindharbor.exception.DAOException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
}
