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

    private DaoFactoryFacade() {}

    public static synchronized DaoFactoryFacade getInstance() {
        if (instance == null) {
            instance = new DaoFactoryFacade();
        }
        return instance;
    }

    public PersistenceType getPersistenceType() {
        return persistenceType;
    }

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