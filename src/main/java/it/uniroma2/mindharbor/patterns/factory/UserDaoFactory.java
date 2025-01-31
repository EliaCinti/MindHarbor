package it.uniroma2.mindharbor.patterns.factory;

import it.uniroma2.mindharbor.dao.UserDao;
import it.uniroma2.mindharbor.dao.csv.UserDaoCsv;
import it.uniroma2.mindharbor.dao.mysql.UserDaoMySql;
import it.uniroma2.mindharbor.patterns.facade.PersistenceType;

public class UserDaoFactory {
    public UserDao getUserDao(PersistenceType persistenceType) {
        return switch (persistenceType) {
            case CSV -> createUserDaoCsv();
            case MYSQL -> createUserDaoMySql();
        };
    }

    public UserDao createUserDaoCsv() {
        return new UserDaoCsv();
    }

    public UserDao createUserDaoMySql() {
        return new UserDaoMySql();
    }
}
