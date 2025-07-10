package it.uniroma2.mindharbor.sync;

import it.uniroma2.mindharbor.beans.PatientBean;
import it.uniroma2.mindharbor.beans.PsychologistBean;
import it.uniroma2.mindharbor.dao.UserDao;
import it.uniroma2.mindharbor.exception.DAOException;
import it.uniroma2.mindharbor.model.Appointment;
import it.uniroma2.mindharbor.model.Patient;
import it.uniroma2.mindharbor.model.Psychologist;
import it.uniroma2.mindharbor.patterns.facade.DaoFactoryFacade;
import it.uniroma2.mindharbor.patterns.facade.PersistenceType;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InitialSyncManager {

    private static final Logger logger = Logger.getLogger(InitialSyncManager.class.getName());

    public void performInitialSync(PersistenceType primaryType) {
        logger.info("Inizio della sincronizzazione iniziale...");
        SyncContext.startSync();
        try {
            PersistenceType secondaryType = (primaryType == PersistenceType.MYSQL) ? PersistenceType.CSV : PersistenceType.MYSQL;

            // Sincronizza le entità
            List<Patient> syncedPatients = syncPatients(primaryType, secondaryType);
            syncPsychologists(primaryType, secondaryType);
            syncAppointments(syncedPatients, primaryType, secondaryType);

            logger.info("Sincronizzazione iniziale completata.");
        } catch (DAOException e) {
            logger.log(Level.SEVERE, "La sincronizzazione iniziale è fallita.", e);
        } finally {
            SyncContext.endSync();
            logger.info("Observer per la sincronizzazione real-time riattivati.");
        }
    }

    private List<Patient> syncPatients(PersistenceType primary, PersistenceType secondary) throws DAOException {
        logger.info("Sincronizzazione Pazienti...");
        DaoFactoryFacade factory = DaoFactoryFacade.getInstance();

        factory.setPersistenceType(primary);
        Map<String, Patient> primaryMap = listToMap(factory.getPatientDao().retrieveAllPatients());

        factory.setPersistenceType(secondary);
        Map<String, Patient> secondaryMap = listToMap(factory.getPatientDao().retrieveAllPatients());

        Set<String> allKeys = new HashSet<>(primaryMap.keySet());
        allKeys.addAll(secondaryMap.keySet());

        for (String key : allKeys) {
            Patient primaryPatient = primaryMap.get(key);
            Patient secondaryPatient = secondaryMap.get(key);

            if (primaryPatient != null && secondaryPatient == null) {
                logger.info("Sync: Copio Paziente " + key + " da " + primary + " a " + secondary);
                PatientBean beanToSave = createPatientBeanFromModel(primaryPatient, factory, primary);
                factory.setPersistenceType(secondary);
                factory.getPatientDao().savePatient(beanToSave);
            } else if (primaryPatient == null && secondaryPatient != null) {
                logger.info("Sync: Copio Paziente " + key + " da " + secondary + " a " + primary);
                PatientBean beanToSave = createPatientBeanFromModel(secondaryPatient, factory, secondary);
                factory.setPersistenceType(primary);
                factory.getPatientDao().savePatient(beanToSave);
            } else if (primaryPatient != null && !primaryPatient.isDataEquivalent(secondaryPatient)) {
                logger.info("Sync Conflitto: Dati diversi per paziente " + key + ". La sorgente " + primary + " vince.");
                PatientBean beanToUpdate = createPatientBeanFromModel(primaryPatient, factory, primary);
                factory.setPersistenceType(secondary); // Assicurati di scrivere sulla destinazione secondaria
                factory.getPatientDao().updatePatient(primaryPatient, beanToUpdate);
            }
        }

        factory.setPersistenceType(primary);
        return factory.getPatientDao().retrieveAllPatients();
    }

    private void syncPsychologists(PersistenceType primary, PersistenceType secondary) throws DAOException {
        logger.info("Sincronizzazione Psicologi...");
        DaoFactoryFacade factory = DaoFactoryFacade.getInstance();

        factory.setPersistenceType(primary);
        Map<String, Psychologist> primaryMap = listToMapPsychologist(factory.getPsychologistDao().retrieveAllPsychologists());

        factory.setPersistenceType(secondary);
        Map<String, Psychologist> secondaryMap = listToMapPsychologist(factory.getPsychologistDao().retrieveAllPsychologists());

        Set<String> allKeys = new HashSet<>(primaryMap.keySet());
        allKeys.addAll(secondaryMap.keySet());

        for (String key : allKeys) {
            Psychologist primaryPsy = primaryMap.get(key);
            Psychologist secondaryPsy = secondaryMap.get(key);
            if (primaryPsy != null && secondaryPsy == null) {
                PsychologistBean bean = createPsychologistBeanFromModel(primaryPsy, factory, primary);
                factory.setPersistenceType(secondary);
                factory.getPsychologistDao().savePsychologist(bean);
            } else if (primaryPsy == null && secondaryPsy != null) {
                PsychologistBean bean = createPsychologistBeanFromModel(secondaryPsy, factory, secondary);
                factory.setPersistenceType(primary);
                factory.getPsychologistDao().savePsychologist(bean);
            } else if (primaryPsy != null && !primaryPsy.isDataEquivalent(secondaryPsy)) {
                logger.info("Sync Conflitto: Dati diversi per psicologo " + key + ". La sorgente " + primary + " vince.");
                PsychologistBean beanToUpdate = createPsychologistBeanFromModel(primaryPsy, factory, primary);
                factory.setPersistenceType(secondary);
                factory.getPsychologistDao().updatePsychologist(primaryPsy, beanToUpdate);
            }
        }
    }

    private void syncAppointments(List<Patient> syncedPatients, PersistenceType primary, PersistenceType secondary) throws DAOException {
        logger.info("Sincronizzazione Appuntamenti...");
        DaoFactoryFacade factory = DaoFactoryFacade.getInstance();

        for (Patient patient : syncedPatients) {
            String username = patient.getUsername();

            factory.setPersistenceType(primary);
            Map<Integer, Appointment> primaryMap = listToMapAppointments(factory.getAppointmentDao().retrieveAppointmentsByPatient(username));

            factory.setPersistenceType(secondary);
            Map<Integer, Appointment> secondaryMap = listToMapAppointments(factory.getAppointmentDao().retrieveAppointmentsByPatient(username));

            Set<Integer> allIds = new HashSet<>(primaryMap.keySet());
            allIds.addAll(secondaryMap.keySet());

            for (Integer id : allIds) {
                Appointment primaryApp = primaryMap.get(id);
                Appointment secondaryApp = secondaryMap.get(id);

                if (primaryApp != null && secondaryApp == null) {
                    logger.info("Sync: Copio Appuntamento " + id + " da " + primary + " a " + secondary);
                    factory.setPersistenceType(secondary);
                    factory.getAppointmentDao().saveAppointment(primaryApp, username);
                } else if (primaryApp == null && secondaryApp != null) {
                    logger.info("Sync: Copio Appuntamento " + id + " da " + secondary + " a " + primary);
                    factory.setPersistenceType(primary);
                    factory.getAppointmentDao().saveAppointment(secondaryApp, username);
                } else if (primaryApp != null && !primaryApp.isDataEquivalent(secondaryApp)) {
                    logger.info("Sync Conflitto: Dati diversi per appuntamento " + id + ". La sorgente " + primary + " vince.");
                    factory.setPersistenceType(secondary);
                    factory.getAppointmentDao().updateAppointment(primaryApp);
                }
            }
        }
    }

    // Metodi Helper
    private Map<String, Patient> listToMap(List<Patient> list) {
        Map<String, Patient> map = new HashMap<>();
        for (Patient p : list) {
            map.put(p.getUsername(), p);
        }
        return map;
    }

    private Map<String, Psychologist> listToMapPsychologist(List<Psychologist> list) {
        Map<String, Psychologist> map = new HashMap<>();
        for (Psychologist p : list) {
            map.put(p.getUsername(), p);
        }
        return map;
    }

    private Map<Integer, Appointment> listToMapAppointments(List<Appointment> list) {
        Map<Integer, Appointment> map = new HashMap<>();
        for (Appointment a : list) {
            map.put(a.getId(), a);
        }
        return map;
    }

    private PatientBean createPatientBeanFromModel(Patient patient, DaoFactoryFacade factory, PersistenceType sourcePersistence) throws DAOException {
        PersistenceType originalType = factory.getPersistenceType();
        try {
            factory.setPersistenceType(sourcePersistence);
            UserDao userDao = factory.getUserDao();
            String[] userInfo = userDao.retrieveUser(patient.getUsername());
            String hashedPassword = (userInfo != null && userInfo.length > 1) ? userInfo[1] : "";

            return new PatientBean.Builder()
                    .username(patient.getUsername()).password(hashedPassword).name(patient.getName())
                    .surname(patient.getSurname()).gender(patient.getGender()).birthDate(patient.getBirthday())
                    .type("PATIENT").build();
        } finally {
            factory.setPersistenceType(originalType);
        }
    }

    private PsychologistBean createPsychologistBeanFromModel(Psychologist psy, DaoFactoryFacade factory, PersistenceType sourcePersistence) throws DAOException {
        PersistenceType originalType = factory.getPersistenceType();
        try {
            factory.setPersistenceType(sourcePersistence);
            UserDao userDao = factory.getUserDao();
            String[] userInfo = userDao.retrieveUser(psy.getUsername());
            String hashedPassword = (userInfo != null && userInfo.length > 1) ? userInfo[1] : "";

            return new PsychologistBean.Builder()
                    .username(psy.getUsername()).password(hashedPassword).name(psy.getName())
                    .surname(psy.getSurname()).gender(psy.getGender()).office(psy.getOffice())
                    .hourlyCost(Double.parseDouble(psy.getHourlyCost())).type("PSYCHOLOGIST").build();
        } finally {
            factory.setPersistenceType(originalType);
        }
    }
}