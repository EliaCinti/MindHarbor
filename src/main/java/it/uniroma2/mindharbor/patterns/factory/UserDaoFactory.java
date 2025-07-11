package it.uniroma2.mindharbor.patterns.factory;

import it.uniroma2.mindharbor.dao.UserDao;
import it.uniroma2.mindharbor.dao.csv.UserDaoCsv;
import it.uniroma2.mindharbor.dao.mysql.UserDaoMySql;
import it.uniroma2.mindharbor.patterns.facade.PersistenceType;

/**
 * Factory class for creating instances of {@link UserDao}.
 * This class provides a method to create UserDao objects based on the specified persistence type.
 */
public class UserDaoFactory {
    /**
     * Retrieves a {@link UserDao} instance based on the specified persistence type.
     * This method determines which type of DAO to instantiate based on the provided
     * {@link PersistenceType}.
     *
     * @param persistenceType The persistence type specifying the storage mechanism (CSV or MySQL).
     * @return An instance of {@link UserDao} suitable for the specified persistence type.
     */
    public UserDao getUserDao(PersistenceType persistenceType) {
        return switch (persistenceType) {
            case CSV -> createUserDaoCsv();
            case MYSQL -> createUserDaoMySql();
        };
    }

    /**
     * Creates a new instance of {@link UserDaoCsv}, which is a specific implementation
     * of {@link UserDao} that handles data persistence in CSV format.
     *
     * @return A new instance of {@link UserDaoCsv}.
     */
    private UserDao createUserDaoCsv() {
        return new UserDaoCsv();
    }

    /**
     * Creates a new instance of {@link UserDaoMySql}, which is a specific implementation
     * of {@link UserDao} that manages data persistence using a MySQL database.
     *
     * @return A new instance of {@link UserDaoMySql}.
     */
    private UserDao createUserDaoMySql() {
        return new UserDaoMySql();
    }
}
