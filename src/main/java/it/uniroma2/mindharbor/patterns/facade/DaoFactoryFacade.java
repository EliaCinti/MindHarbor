package it.uniroma2.mindharbor.patterns.facade;

import it.uniroma2.mindharbor.dao.UserDao;
import it.uniroma2.mindharbor.patterns.factory.UserDaoFactory;

public class DaoFactoryFacade {
    private static DaoFactoryFacade instance;
    private PersistenceType persistenceType;
    private UserDao userDao;

    private DaoFactoryFacade() {
        /* Empty constructor: no instance */
    }

    public static synchronized DaoFactoryFacade getInstance() {
        if(instance == null) {
            instance = new DaoFactoryFacade();
        }
        return instance;
    }

    public void setPersistenceType(PersistenceType persistenceType) {
        this.persistenceType = persistenceType;
    }

    public UserDao getUserDao() {
        if(userDao == null) {
            UserDaoFactory userDaoFactory = new UserDaoFactory();
            userDao = userDaoFactory.getUserDao(persistenceType);
        }
        return userDao;
    }
}
