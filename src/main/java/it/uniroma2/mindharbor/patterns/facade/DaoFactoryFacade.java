package it.uniroma2.mindharbor.patterns.facade;

import it.uniroma2.mindharbor.dao.PatientDao;
import it.uniroma2.mindharbor.dao.PsychologistDao;
import it.uniroma2.mindharbor.dao.UserDao;
import it.uniroma2.mindharbor.patterns.factory.PatientDaoFactory;
import it.uniroma2.mindharbor.patterns.factory.PsychologistDaoFactory;
import it.uniroma2.mindharbor.patterns.factory.UserDaoFactory;

/**
 * Facade class for managing Data Access Object (DAO) creation through factories.
 * <p>
 * This class simplifies the creation and management of DAOs for different entities,
 * such as users, patients, and psychologists. It uses a singleton pattern to ensure
 * only one instance of the facade exists during runtime.
 * </p>
 */
public class DaoFactoryFacade {
    private static DaoFactoryFacade instance;
    private PersistenceType persistenceType;
    private UserDao userDao;
    private PatientDao patientDao;
    private PsychologistDao psychologistDao;

    /**
     * Private constructor to prevent direct instantiation.
     * This class follows the singleton pattern.
     */
    private DaoFactoryFacade() {
        /* Empty constructor */
    }

    /**
     * Retrieves the single instance of the {@code DaoFactoryFacade}.
     * If the instance does not exist, it is created.
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
     * Sets the persistence type to be used for creating DAOs.
     *
     * @param persistenceType The persistence type (e.g., CSV, MySQL).
     */
    public void setPersistenceType(PersistenceType persistenceType) {
        this.persistenceType = persistenceType;
    }

    /**
     * Retrieves the {@link UserDao} instance.
     * If the DAO has not been initialized, it is created using the {@link UserDaoFactory}.
     *
     * @return The {@link UserDao} instance.
     */
    public UserDao getUserDao() {
        if (userDao == null) {
            UserDaoFactory userDaoFactory = new UserDaoFactory();
            userDao = userDaoFactory.getUserDao(persistenceType);
        }
        return userDao;
    }

    /**
     * Retrieves the {@link PatientDao} instance.
     * If the DAO has not been initialized, it is created using the {@link PatientDaoFactory}.
     *
     * @return The {@link PatientDao} instance.
     */
    public PatientDao getPatientDao() {
        if (patientDao == null) {
            PatientDaoFactory patientDaoFactory = new PatientDaoFactory();
            patientDao = patientDaoFactory.getPatientDao(persistenceType);
        }
        return patientDao;
    }

    /**
     * Retrieves the {@link PsychologistDao} instance.
     * If the DAO has not been initialized, it is created using the {@link PsychologistDaoFactory}.
     *
     * @return The {@link PsychologistDao} instance.
     */
    public PsychologistDao getPsychologistDao() {
        if (psychologistDao == null) {
            PsychologistDaoFactory psychologistDaoFactory = new PsychologistDaoFactory();
            psychologistDao = psychologistDaoFactory.getPsychologistDao(persistenceType);
        }
        return psychologistDao;
    }
}
