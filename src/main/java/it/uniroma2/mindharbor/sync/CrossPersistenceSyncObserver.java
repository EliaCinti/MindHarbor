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

public class CrossPersistenceSyncObserver implements DaoObserver {

    private static final Logger logger = Logger.getLogger(CrossPersistenceSyncObserver.class.getName());
    private final PersistenceType sourceType;

    public CrossPersistenceSyncObserver(PersistenceType sourceType) {
        this.sourceType = sourceType;
    }

    private PersistenceType getTargetType() {
        return sourceType == PersistenceType.CSV ? PersistenceType.MYSQL : PersistenceType.CSV;
    }

    private DaoFactoryFacade getTargetFactory() {
        DaoFactoryFacade factory = DaoFactoryFacade.getInstance();
        factory.setPersistenceType(getTargetType());
        return factory;
    }

    @Override
    public void onAfterInsert(String entityType, String entityId, Object entity) {
        if (SyncContext.isSyncing()) return;
        SyncContext.startSync();
        try {
            logger.log(Level.INFO, "SYNC INSERT: Propagating {0} ({1}) from {2} to {3}", new Object[]{entityType, entityId, sourceType, getTargetType()});

            switch (entityType) {
                // Per l'INSERT, riceviamo sempre un BEAN dall'interfaccia grafica.
                case "Patient" -> {
                    PatientDao targetDao = getTargetFactory().getPatientDao();
                    // Il cast corretto è al Bean
                    targetDao.savePatient((PatientBean) entity);
                }
                case "Psychologist" -> {
                    PsychologistDao targetDao = getTargetFactory().getPsychologistDao();
                    // Il cast corretto è al Bean
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

    @Override
    public void onAfterUpdate(String entityType, String entityId, Object entity) {
        if (SyncContext.isSyncing()) return;
        SyncContext.startSync();
        try {
            logger.log(Level.INFO, "SYNC UPDATE: Propagating {0} ({1}) from {2} to {3}", new Object[]{entityType, entityId, sourceType, getTargetType()});
            switch (entityType) {
                // Per l'UPDATE, riceviamo sempre un oggetto del MODELLO.
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
