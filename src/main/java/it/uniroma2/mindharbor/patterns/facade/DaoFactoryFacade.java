package it.uniroma2.mindharbor.patterns.facade;

import it.uniroma2.mindharbor.dao.PatientDao;
import it.uniroma2.mindharbor.dao.PsychologistDao;
import it.uniroma2.mindharbor.dao.UserDao;
import it.uniroma2.mindharbor.patterns.factory.PatientDaoFactory;
import it.uniroma2.mindharbor.patterns.factory.PsychologistDaoFactory;
import it.uniroma2.mindharbor.patterns.factory.UserDaoFactory;

/**
 * Facade class for managing Data Access Object (DAO) creation through factories.
 * This class acts as a central point for managing DAOs across different persistence
 * types (e.g., CSV, MySQL) using a singleton pattern to ensure a single unified point of access.
 * <p>
 * It simplifies the client's interaction with DAO creation, handling the instantiation
 * of DAOs based on the specified persistence type and ensuring that each DAO type is
 * instantiated only once per runtime.
 * </p>
 */
public class DaoFactoryFacade {
    private static DaoFactoryFacade instance;
    private PersistenceType persistenceType;
    private UserDao userDao;
    private PatientDao patientDao;
    private PsychologistDao psychologistDao;

    /**
     * Private constructor to prevent external instantiation, enforcing the singleton pattern.
     */
    private DaoFactoryFacade() {
        /* Empty constructor */
    }


    /**
     * Provides global access to the single instance of the {@code DaoFactoryFacade},
     * creating it if it does not yet exist. This method is thread-safe to prevent
     * multiple instantiation in a multi-threaded environment.
     *
     * @return The singleton instance of {@code DaoFactoryFacade}.
     */
    public static synchronized DaoFactoryFacade getInstance() {
        if (instance == null) {
            instance = new DaoFactoryFacade();
        }
        return instance;
    }

    /**
     * Sets the type of persistence mechanism that DAOs should use throughout the application.
     * This allows dynamic changes in the persistence strategy at runtime.
     *
     * @param persistenceType The desired persistence type (e.g., CSV, MySQL).
     */
    public void setPersistenceType(PersistenceType persistenceType) {
        this.persistenceType = persistenceType;
    }

    /**
     * Retrieves a singleton instance of {@link UserDao}, creating it through {@link UserDaoFactory}
     * if not previously instantiated. Ensures the DAO is aligned with the current persistence strategy.
     *
     * @return The singleton {@link UserDao} instance.
     */
    public UserDao getUserDao() {
        if (userDao == null) {
            UserDaoFactory userDaoFactory = new UserDaoFactory();
            userDao = userDaoFactory.getUserDao(persistenceType);
        }
        return userDao;
    }


    /**
     * Retrieves a singleton instance of {@link PatientDao}, creating it through {@link PatientDaoFactory}
     * if not previously instantiated. Ensures the DAO is aligned with the current persistence strategy.
     *
     * @return The singleton {@link PatientDao} instance.
     */
    public PatientDao getPatientDao() {
        if (patientDao == null) {
            PatientDaoFactory patientDaoFactory = new PatientDaoFactory();
            patientDao = patientDaoFactory.getPatientDao(persistenceType);
        }
        return patientDao;
    }

    /**
     * Retrieves a singleton instance of {@link PsychologistDao}, creating it through {@link PsychologistDaoFactory}
     * if not previously instantiated. Ensures the DAO is aligned with the current persistence strategy.
     *
     * @return The singleton {@link PsychologistDao} instance.
     */
    public PsychologistDao getPsychologistDao() {
        if (psychologistDao == null) {
            PsychologistDaoFactory psychologistDaoFactory = new PsychologistDaoFactory();
            psychologistDao = psychologistDaoFactory.getPsychologistDao(persistenceType);
        }
        return psychologistDao;
    }
}
