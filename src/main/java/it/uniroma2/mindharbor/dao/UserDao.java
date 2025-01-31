package it.uniroma2.mindharbor.dao;

import it.uniroma2.mindharbor.beans.CredentialsBean;
import it.uniroma2.mindharbor.beans.UserBean;
import it.uniroma2.mindharbor.exception.DAOException;

public interface UserDao {
    void validateUser(CredentialsBean credentials) throws DAOException;
    void saveUser(UserBean user) throws DAOException;
    String[] retrieveUser(String username) throws DAOException;
}
