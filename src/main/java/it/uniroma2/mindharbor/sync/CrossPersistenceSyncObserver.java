package it.uniroma2.mindharbor.sync;

import it.uniroma2.mindharbor.beans.PatientBean;
import it.uniroma2.mindharbor.beans.PsychologistBean;
import it.uniroma2.mindharbor.beans.UserBean;
import it.uniroma2.mindharbor.dao.*;
import it.uniroma2.mindharbor.exception.DAOException;
import it.uniroma2.mindharbor.model.Appointment;
import it.uniroma2.mindharbor.model.Patient;
import it.uniroma2.mindharbor.model.Psychologist;
import it.uniroma2.mindharbor.patterns.facade.DaoFactoryFacade;
import it.uniroma2.mindharbor.patterns.facade.PersistenceType;
import it.uniroma2.mindharbor.patterns.observer.DaoObserver;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implements real-time synchronization between different persistence types using the Observer pattern.
 * <p>
 * This class observes data changes in one persistence system (source) and automatically
 * replicates them to another persistence system (target). It supports bidirectional
 * synchronization between CSV files and MySQL database, ensuring data consistency
 * across both storage mechanisms.
 * </p>
 * <p>
 * The observer handles three types of operations:
 * <ul>
 *   <li><strong>INSERT</strong>: Receives Bean objects from the UI layer</li>
 *   <li><strong>UPDATE</strong>: Receives Model objects with updated data</li>
 *   <li><strong>DELETE</strong>: Receives entity identifiers for removal</li>
 * </ul>
 * </p>
 * <p>
 * To prevent infinite synchronization loops, this observer checks {@link SyncContext}
 * before processing any operation and sets the sync flag during execution.
 * </p>
 *
 * @see DaoObserver for the observer interface contract
 * @see SyncContext for synchronization loop prevention
 * @see InitialSyncManager for initial data synchronization
 */
public class CrossPersistenceSyncObserver implements DaoObserver {

    private static final Logger logger = Logger.getLogger(CrossPersistenceSyncObserver.class.getName());
    private final PersistenceType sourceType;

    /**
     * Creates a new cross-persistence synchronization observer.
     *
     * @param sourceType The persistence type that this observer is monitoring for changes
     */
    public CrossPersistenceSyncObserver(PersistenceType sourceType) {
        this.sourceType = sourceType;
    }

    /**
     * Determines the target persistence type for synchronization.
     * <p>
     * The target is always the opposite of the source type:
     * if source is CSV, target is MySQL, and vice versa.
     * </p>
     *
     * @return The target persistence type for replication
     */
    private PersistenceType getTargetType() {
        return sourceType == PersistenceType.CSV ? PersistenceType.MYSQL : PersistenceType.CSV;
    }

    /**
     * Gets a DAO factory configured for the target persistence type.
     * <p>
     * This method configures the singleton factory to use the target persistence
     * type for the current synchronization operation.
     * </p>
     *
     * @return DaoFactoryFacade configured for target persistence
     */
    private DaoFactoryFacade getTargetFactory() {
        DaoFactoryFacade factory = DaoFactoryFacade.getInstance();
        factory.setPersistenceType(getTargetType());
        return factory;
    }

    /**
     * Handles entity insertion synchronization.
     * <p>
     * When a new entity is inserted in the source persistence, this method
     * replicates the insertion to the target persistence. For INSERT operations,
     * the system always receives Bean objects from the UI layer, which contain
     * all necessary data for persistence.
     * </p>
     * <p>
     * Supported entity types:
     * <ul>
     *   <li><strong>Patient</strong>: Receives PatientBean</li>
     *   <li><strong>Psychologist</strong>: Receives PsychologistBean</li>
     *   <li><strong>Appointment</strong>: Receives array with Appointment and patient username</li>
     * </ul>
     * </p>
     *
     * @param entityType The type of entity being inserted (Patient, Psychologist, Appointment)
     * @param entityId The unique identifier of the inserted entity
     * @param entity The entity data (Bean object for insertions)
     */
    @Override
    public void onAfterInsert(String entityType, String entityId, Object entity) {
        if (SyncContext.isSyncing()) return;
        SyncContext.startSync();
        try {
            logger.log(Level.INFO, "SYNC INSERT: Propagating {0} ({1}) from {2} to {3}", new Object[]{entityType, entityId, sourceType, getTargetType()});

            switch (entityType) {
                // per l'INSERT si riceve sempre un ogetto di tipo bean
                case "Patient" -> {
                    PatientDao targetDao = getTargetFactory().getPatientDao();
                    // casto a bean perche riceviamo un bean
                    targetDao.savePatient((PatientBean) entity);
                }
                case "Psychologist" -> {
                    PsychologistDao targetDao = getTargetFactory().getPsychologistDao();
                    // come sopra
                    targetDao.savePsychologist((PsychologistBean) entity);
                }
                case "Appointment" -> {
                    AppointmentDao targetDao = getTargetFactory().getAppointmentDao();
                    Object[] syncPackage = (Object[]) entity;
                    Appointment app = (Appointment) syncPackage[0];
                    String patientUsername = (String) syncPackage[1];
                    targetDao.saveAppointment(app, patientUsername);
                }
                default -> logger.log(Level.WARNING, "Sync INSERT not handled for entity type: {0}", entityType);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Sync INSERT failed", e);
        } finally {
            SyncContext.endSync();
        }
    }

    /**
     * Handles entity update synchronization.
     * <p>
     * When an entity is updated in the source persistence, this method
     * replicates the update to the target persistence. For UPDATE operations,
     * the system always receives Model objects that contain the updated data.
     * </p>
     * <p>
     * Since DAO update methods require UserBean objects for user-related entities,
     * this method constructs appropriate UserBean instances from the Model objects,
     * excluding password updates which are not handled through this synchronization flow.
     * </p>
     *
     * @param entityType The type of entity being updated
     * @param entityId The unique identifier of the updated entity
     * @param entity The entity data (Model object for updates)
     */
    @Override
    public void onAfterUpdate(String entityType, String entityId, Object entity) {
        if (SyncContext.isSyncing()) return;
        SyncContext.startSync();
        try {
            logger.log(Level.INFO, "SYNC UPDATE: Propagating {0} ({1}) from {2} to {3}", new Object[]{entityType, entityId, sourceType, getTargetType()});
            switch (entityType) {
                // Per l'UPDATE, si riceve sempre un oggetto del MODELLO.
                case "Patient" -> {
                    PatientDao targetDao = getTargetFactory().getPatientDao();
                    // Il cast corretto è al Model
                    Patient patient = (Patient) entity;
                    // Il metodo updatePatient richiede un UserBean, quindi lo costruiamo dal modello.
                    UserBean userBean = new UserBean.Builder<>()
                            .username(patient.getUsername())
                            .name(patient.getName())
                            .surname(patient.getSurname())
                            .gender(patient.getGender())
                            .type("PATIENT")
                            .password(null) // La password non viene aggiornata da questo flusso
                            .build();
                    targetDao.updatePatient(patient, userBean);
                }
                case "Psychologist" -> {
                    PsychologistDao targetDao = getTargetFactory().getPsychologistDao();
                    // Il cast corretto è al Model
                    Psychologist psychologist = (Psychologist) entity;
                    UserBean userBean = new UserBean.Builder<>()
                            .username(psychologist.getUsername())
                            .name(psychologist.getName())
                            .surname(psychologist.getSurname())
                            .gender(psychologist.getGender())
                            .type("PSYCHOLOGIST")
                            .password(null)
                            .build();
                    targetDao.updatePsychologist(psychologist, userBean);
                }
                case "Appointment" -> {
                    AppointmentDao targetDao = getTargetFactory().getAppointmentDao();
                    // L'update dell'appuntamento riceve e usa direttamente il modello.
                    targetDao.updateAppointment((Appointment) entity);
                }
                default -> logger.log(Level.WARNING, "Sync UPDATE not handled for entity type: {0}", entityType);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Sync UPDATE failed", e);
        } finally {
            SyncContext.endSync();
        }
    }

    /**
     * Handles entity deletion synchronization.
     * <p>
     * When an entity is deleted from the source persistence, this method
     * replicates the deletion to the target persistence. For DELETE operations,
     * only the entity type and identifier are needed to perform the operation.
     * </p>
     * <p>
     * The method handles different entity types appropriately:
     * <ul>
     *   <li><strong>User, Patient, Psychologist</strong>: Use string identifier (username)</li>
     *   <li><strong>Appointment</strong>: Use integer identifier (appointment ID)</li>
     * </ul>
     * </p>
     *
     * @param entityType The type of entity being deleted
     * @param entityId The unique identifier of the deleted entity
     */
    @Override
    public void onAfterDelete(String entityType, String entityId) {
        if (SyncContext.isSyncing()) return;
        SyncContext.startSync();
        try {
            logger.log(Level.INFO, "SYNC DELETE: Propagating {0} ({1}) from {2} to {3}", new Object[]{entityType, entityId, sourceType, getTargetType()});
            DaoFactoryFacade targetFactory = getTargetFactory();
            switch (entityType) {
                case "User" -> targetFactory.getUserDao().deleteUser(entityId);
                case "Patient" -> targetFactory.getPatientDao().deletePatient(entityId);
                case "Psychologist" -> targetFactory.getPsychologistDao().deletePsychologist(entityId);
                case "Appointment" -> targetFactory.getAppointmentDao().deleteAppointment(Integer.parseInt(entityId));
                default -> logger.log(Level.WARNING, "Sync DELETE not handled for entity type: {0}", entityType);
            }
        } catch (DAOException e) {
            logger.log(Level.SEVERE, "Sync DELETE failed", e);
        } finally {
            SyncContext.endSync();
        }
    }
}
