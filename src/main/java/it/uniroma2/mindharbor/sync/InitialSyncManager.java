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

/**
 * Manages the initial synchronization process between different persistence types.
 * <p>
 * This class is responsible for ensuring data consistency when the application starts
 * by comparing and synchronizing data between CSV and MySQL storage systems.
 * It handles conflict resolution by giving priority to the primary persistence type.
 * </p>
 * <p>
 * The synchronization process includes:
 * <ul>
 *   <li>Users and patient/psychologist profiles</li>
 *   <li>Appointment records for all patients</li>
 *   <li>Conflict detection and resolution</li>
 * </ul>
 * </p>
 *
 * @see CrossPersistenceSyncObserver for real-time synchronization
 * @see SyncContext for preventing synchronization loops
 */
public class InitialSyncManager {

    private static final Logger logger = Logger.getLogger(InitialSyncManager.class.getName());

    /**
     * Performs a complete initial synchronization between primary and secondary persistence types.
     * <p>
     * This method coordinates the synchronization of all entities in the correct order:
     * patients first (since they can exist independently), then psychologists,
     * and finally appointments (which depend on patients).
     * </p>
     * <p>
     * The synchronization is performed within a {@link SyncContext} to prevent
     * observer notifications that could cause infinite loops.
     * </p>
     *
     * @param primaryType The primary persistence type that takes precedence in conflict resolution
     */
    public void performInitialSync(PersistenceType primaryType) {
        logger.info("Starting initial synchronization...");
        SyncContext.startSync();
        try {
            PersistenceType secondaryType = (primaryType == PersistenceType.MYSQL) ? PersistenceType.CSV : PersistenceType.MYSQL;

            // Synchronize entities in dependency order
            List<Patient> syncedPatients = syncPatients(primaryType, secondaryType);
            syncPsychologists(primaryType, secondaryType);
            syncAppointments(syncedPatients, primaryType, secondaryType);

            logger.info("Initial synchronization completed successfully.");
        } catch (DAOException e) {
            logger.log(Level.SEVERE, "Initial synchronization failed.", e);
        } finally {
            SyncContext.endSync();
            logger.info("Real-time synchronization observers reactivated.");
        }
    }

    /**
     * Synchronizes patient data between primary and secondary persistence types.
     * <p>
     * This method compares patient records from both storage systems and:
     * <ul>
     *   <li>Copies missing patients from one system to another</li>
     *   <li>Resolves conflicts by prioritizing the primary persistence type</li>
     *   <li>Ensures data consistency across both systems</li>
     * </ul>
     * </p>
     *
     * @param primary The primary persistence type
     * @param secondary The secondary persistence type
     * @return List of synchronized patients from the primary persistence
     * @throws DAOException if patient data access or synchronization fails
     */
    private List<Patient> syncPatients(PersistenceType primary, PersistenceType secondary) throws DAOException {
        logger.info("Synchronizing patients...");
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
                logger.info("Sync: Copying patient " + key + " from " + primary + " to " + secondary);
                PatientBean beanToSave = createPatientBeanFromModel(primaryPatient, factory, primary);
                factory.setPersistenceType(secondary);
                factory.getPatientDao().savePatient(beanToSave);
            } else if (primaryPatient == null && secondaryPatient != null) {
                logger.info("Sync: Copying patient " + key + " from " + secondary + " to " + primary);
                PatientBean beanToSave = createPatientBeanFromModel(secondaryPatient, factory, secondary);
                factory.setPersistenceType(primary);
                factory.getPatientDao().savePatient(beanToSave);
            } else if (primaryPatient != null && !primaryPatient.isDataEquivalent(secondaryPatient)) {
                logger.info("Sync Conflict: Different data for patient " + key + ". Primary source " + primary + " takes precedence.");
                PatientBean beanToUpdate = createPatientBeanFromModel(primaryPatient, factory, primary);
                factory.setPersistenceType(secondary); // Si scrive sulla destinazione secondaria
                factory.getPatientDao().updatePatient(primaryPatient, beanToUpdate);
            }
        }

        factory.setPersistenceType(primary);
        return factory.getPatientDao().retrieveAllPatients();
    }

    /**
     * Synchronizes psychologist data between primary and secondary persistence types.
     * <p>
     * Similar to patient synchronization, this method ensures that psychologist
     * records are consistent across both storage systems, with conflict resolution
     * favoring the primary persistence type.
     * </p>
     *
     * @param primary The primary persistence type
     * @param secondary The secondary persistence type
     * @throws DAOException if psychologist data access or synchronization fails
     */
    private void syncPsychologists(PersistenceType primary, PersistenceType secondary) throws DAOException {
        logger.info("Synchronizing psychologists...");
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
                logger.info("Sync: Copying psychologist " + key + " from " + primary + " to " + secondary);
                PsychologistBean bean = createPsychologistBeanFromModel(primaryPsy, factory, primary);
                factory.setPersistenceType(secondary);
                factory.getPsychologistDao().savePsychologist(bean);
            } else if (primaryPsy == null && secondaryPsy != null) {
                logger.info("Sync: Copying psychologist " + key + " from " + secondary + " to " + primary);
                PsychologistBean bean = createPsychologistBeanFromModel(secondaryPsy, factory, secondary);
                factory.setPersistenceType(primary);
                factory.getPsychologistDao().savePsychologist(bean);
            } else if (primaryPsy != null && !primaryPsy.isDataEquivalent(secondaryPsy)) {
                logger.info("Sync Conflict: Different data for psychologist " + key + ". Primary source " + primary + " takes precedence.");                PsychologistBean beanToUpdate = createPsychologistBeanFromModel(primaryPsy, factory, primary);
                factory.setPersistenceType(secondary);
                factory.getPsychologistDao().updatePsychologist(primaryPsy, beanToUpdate);
            }
        }
    }

    /**
     * Synchronizes appointment data for all patients between persistence types.
     * <p>
     * This method processes appointments for each patient individually, ensuring
     * that appointment schedules are consistent across both storage systems.
     * Since appointments are patient-specific, this method requires the list
     * of synchronized patients to process.
     * </p>
     *
     * @param syncedPatients List of patients whose appointments should be synchronized
     * @param primary The primary persistence type
     * @param secondary The secondary persistence type
     * @throws DAOException if appointment data access or synchronization fails
     */
    private void syncAppointments(List<Patient> syncedPatients, PersistenceType primary, PersistenceType secondary) throws DAOException {
        logger.info("Synchronizing appointments...");
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
                    logger.info("Sync: Copying appointment " + id + " from " + primary + " to " + secondary);                    factory.setPersistenceType(secondary);
                    factory.getAppointmentDao().saveAppointment(primaryApp, username);
                } else if (primaryApp == null && secondaryApp != null) {
                    logger.info("Sync: Copying appointment " + id + " from " + secondary + " to " + primary);
                    factory.setPersistenceType(primary);
                    factory.getAppointmentDao().saveAppointment(secondaryApp, username);
                } else if (primaryApp != null && !primaryApp.isDataEquivalent(secondaryApp)) {
                    logger.info("Sync Conflict: Different data for appointment " + id + ". Primary source " + primary + " takes precedence.");
                    factory.setPersistenceType(secondary);
                    factory.getAppointmentDao().updateAppointment(primaryApp);
                }
            }
        }
    }

    // Helper Methods

    /**
     * Converts a list of patients to a map with username as key.
     * <p>
     * This helper method facilitates efficient lookup and comparison
     * of patient records during synchronization.
     * </p>
     *
     * @param list List of patients to convert
     * @return Map with username as key and Patient object as value
     */
    private Map<String, Patient> listToMap(List<Patient> list) {
        Map<String, Patient> map = new HashMap<>();
        for (Patient p : list) {
            map.put(p.getUsername(), p);
        }
        return map;
    }

    /**
     * Converts a list of psychologists to a map with username as key.
     * <p>
     * This helper method facilitates efficient lookup and comparison
     * of psychologist records during synchronization.
     * </p>
     *
     * @param list List of psychologists to convert
     * @return Map with username as key and Psychologist object as value
     */
    private Map<String, Psychologist> listToMapPsychologist(List<Psychologist> list) {
        Map<String, Psychologist> map = new HashMap<>();
        for (Psychologist p : list) {
            map.put(p.getUsername(), p);
        }
        return map;
    }

    /**
     * Converts a list of appointments to a map with appointment ID as key.
     * <p>
     * This helper method facilitates efficient lookup and comparison
     * of appointment records during synchronization.
     * </p>
     *
     * @param list List of appointments to convert
     * @return Map with appointment ID as key and Appointment object as value
     */
    private Map<Integer, Appointment> listToMapAppointments(List<Appointment> list) {
        Map<Integer, Appointment> map = new HashMap<>();
        for (Appointment a : list) {
            map.put(a.getId(), a);
        }
        return map;
    }

    /**
     * Creates a PatientBean from a Patient model object for persistence operations.
     * <p>
     * This method retrieves the complete user information (including hashed password)
     * from the source persistence and creates a properly formatted bean for saving
     * or updating in the destination persistence.
     * </p>
     *
     * @param patient The patient model object to convert
     * @param factory The DAO factory facade for accessing persistence
     * @param sourcePersistence The persistence type to retrieve user data from
     * @return A PatientBean with complete information for persistence operations
     * @throws DAOException if user data retrieval fails
     */
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

    /**
     * Creates a PsychologistBean from a Psychologist model object for persistence operations.
     * <p>
     * This method retrieves the complete user information (including hashed password)
     * from the source persistence and creates a properly formatted bean for saving
     * or updating in the destination persistence.
     * </p>
     *
     * @param psy The psychologist model object to convert
     * @param factory The DAO factory facade for accessing persistence
     * @param sourcePersistence The persistence type to retrieve user data from
     * @return A PsychologistBean with complete information for persistence operations
     * @throws DAOException if user data retrieval fails
     */
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