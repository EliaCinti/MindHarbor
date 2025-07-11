package it.uniroma2.mindharbor.patterns.facade;

import it.uniroma2.mindharbor.dao.AppointmentDao;
import it.uniroma2.mindharbor.dao.PatientDao;
import it.uniroma2.mindharbor.dao.PsychologistDao;
import it.uniroma2.mindharbor.dao.UserDao;
import it.uniroma2.mindharbor.patterns.factory.AppointmentDaoFactory;
import it.uniroma2.mindharbor.patterns.factory.PatientDaoFactory;
import it.uniroma2.mindharbor.patterns.factory.PsychologistDaoFactory;
import it.uniroma2.mindharbor.patterns.factory.UserDaoFactory;
import it.uniroma2.mindharbor.patterns.observer.ObservableDao;
import it.uniroma2.mindharbor.sync.CrossPersistenceSyncObserver;

/**
 * Facade that provides a unified interface to all DAO factories and manages cross-persistence synchronization.
 * <p>
 * This class implements both the Singleton and Facade patterns to:
 * <ul>
 *   <li>Provide a single point of access to all DAO factories</li>
 *   <li>Hide the complexity of DAO creation and observer management</li>
 *   <li>Ensure consistent persistence type configuration across the application</li>
 *   <li>Manage automatic cross-persistence synchronization observers</li>
 * </ul>
 * </p>
 * <p>
 * The facade automatically configures the appropriate synchronization observers
 * based on the current persistence type:
 * <ul>
 *   <li>When using <strong>MySQL</strong> as primary: syncs changes to CSV</li>
 *   <li>When using <strong>CSV</strong> as primary: syncs changes to MySQL</li>
 * </ul>
 * </p>
 * <p>
 * DAOs are cached after first creation and cleared when persistence type changes,
 * ensuring optimal performance while maintaining consistency.
 * </p>
 *
 * @see it.uniroma2.mindharbor.patterns.factory Factory classes for DAO creation
 * @see it.uniroma2.mindharbor.sync.CrossPersistenceSyncObserver for synchronization
 * @see PersistenceType for supported persistence mechanisms
 */
public class DaoFactoryFacade {
    private static DaoFactoryFacade instance;

    private PersistenceType persistenceType;
    private UserDao userDao;
    private PatientDao patientDao;
    private PsychologistDao psychologistDao;
    private AppointmentDao appointmentDao;

    // Observer per le due direzioni di sync
    private final CrossPersistenceSyncObserver mysqlToCsvObserver = new CrossPersistenceSyncObserver(PersistenceType.MYSQL);
    private final CrossPersistenceSyncObserver csvToMysqlObserver = new CrossPersistenceSyncObserver(PersistenceType.CSV);

    /**
     * Private constructor to enforce Singleton pattern.
     */
    private DaoFactoryFacade() {
        /* no  instance */
    }

    /**
     * Returns the singleton instance of the DAO factory facade.
     * <p>
     * This method ensures that only one instance of the facade exists
     * throughout the application lifecycle, providing consistent access
     * to DAO factories and synchronization configuration.
     * </p>
     *
     * @return The singleton DaoFactoryFacade instance
     */
    public static synchronized DaoFactoryFacade getInstance() {
        if (instance == null) {
            instance = new DaoFactoryFacade();
        }
        return instance;
    }

    /**
     * Gets the currently configured persistence type.
     *
     * @return The current persistence type (MYSQL or CSV)
     */
    public PersistenceType getPersistenceType() {
        return persistenceType;
    }

    /**
     * Sets the persistence type and clears the DAO cache if the type changes.
     * <p>
     * When the persistence type changes, all cached DAO instances are cleared
     * to ensure that subsequent requests create DAOs of the correct type with
     * appropriate observer configurations.
     * </p>
     *
     * @param persistenceType The new persistence type to use
     */
    public void setPersistenceType(PersistenceType persistenceType) {
        if (this.persistenceType != persistenceType) {
            this.persistenceType = persistenceType;
            // Pulisci la cache dei DAO quando cambi tipo
            this.userDao = null;
            this.patientDao = null;
            this.psychologistDao = null;
            this.appointmentDao = null;
        }
    }

    /**
     * Gets a UserDao instance configured for the current persistence type.
     * <p>
     * This method implements lazy initialization and caching. If no UserDao
     * exists, it creates one using the appropriate factory and configures
     * the cross-persistence synchronization observer.
     * </p>
     *
     * @return A UserDao instance with synchronization capabilities
     */
    public UserDao getUserDao() {
        if (userDao == null) {
            userDao = new UserDaoFactory().getUserDao(this.persistenceType);
            // Collega l'observer giusto al momento della creazione
            if (this.persistenceType == PersistenceType.MYSQL) {
                ((ObservableDao) userDao).addObserver(mysqlToCsvObserver);
            } else {
                ((ObservableDao) userDao).addObserver(csvToMysqlObserver);
            }
        }
        return userDao;
    }

    /**
            * Gets a PatientDao instance configured for the current persistence type.
            * <p>
     * This method implements lazy initialization and caching. If no PatientDao
     * exists, it creates one using the appropriate factory and configures
     * the cross-persistence synchronization observer.
     * </p>
            *
            * @return A PatientDao instance with synchronization capabilities
     */
    public PatientDao getPatientDao() {
        if (patientDao == null) {
            patientDao = new PatientDaoFactory().getPatientDao(this.persistenceType);
            if (this.persistenceType == PersistenceType.MYSQL) {
                ((ObservableDao) patientDao).addObserver(mysqlToCsvObserver);
            } else {
                ((ObservableDao) patientDao).addObserver(csvToMysqlObserver);
            }
        }
        return patientDao;
    }

    /**
     * Gets a PsychologistDao instance configured for the current persistence type.
     * <p>
     * This method implements lazy initialization and caching. If no PsychologistDao
     * exists, it creates one using the appropriate factory and configures
     * the cross-persistence synchronization observer.
     * </p>
     *
     * @return A PsychologistDao instance with synchronization capabilities
     */
    public PsychologistDao getPsychologistDao() {
        if (psychologistDao == null) {
            psychologistDao = new PsychologistDaoFactory().getPsychologistDao(this.persistenceType);
            if (this.persistenceType == PersistenceType.MYSQL) {
                ((ObservableDao) psychologistDao).addObserver(mysqlToCsvObserver);
            } else {
                ((ObservableDao) psychologistDao).addObserver(csvToMysqlObserver);
            }
        }
        return psychologistDao;
    }

    /**
     * Gets an AppointmentDao instance configured for the current persistence type.
     * <p>
     * This method implements lazy initialization and caching. If no AppointmentDao
     * exists, it creates one using the appropriate factory and configures
     * the cross-persistence synchronization observer.
     * </p>
     *
     * @return An AppointmentDao instance with synchronization capabilities
     */
    public AppointmentDao getAppointmentDao() {
        if (appointmentDao == null) {
            appointmentDao = new AppointmentDaoFactory().getAppointmentDao(this.persistenceType);
            if (this.persistenceType == PersistenceType.MYSQL) {
                ((ObservableDao) appointmentDao).addObserver(mysqlToCsvObserver);
            } else {
                ((ObservableDao) appointmentDao).addObserver(csvToMysqlObserver);
            }
        }
        return appointmentDao;
    }
}