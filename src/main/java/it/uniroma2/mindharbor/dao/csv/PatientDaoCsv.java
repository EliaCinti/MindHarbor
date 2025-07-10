package it.uniroma2.mindharbor.dao.csv;

import it.uniroma2.mindharbor.beans.PatientBean;
import it.uniroma2.mindharbor.beans.UserBean;
import it.uniroma2.mindharbor.dao.AbstractObservableDao;
import it.uniroma2.mindharbor.dao.PatientDao;
import it.uniroma2.mindharbor.dao.UserDao;
import it.uniroma2.mindharbor.dao.csv.constants.PatientDaoCsvConstants;
import it.uniroma2.mindharbor.dao.csv.constants.UserDaoCsvConstants;
import it.uniroma2.mindharbor.exception.DAOException;
import it.uniroma2.mindharbor.model.Patient;
import it.uniroma2.mindharbor.model.Psychologist;
import it.uniroma2.mindharbor.patterns.facade.DaoFactoryFacade;
import it.uniroma2.mindharbor.patterns.observer.DaoOperation;
import it.uniroma2.mindharbor.utilities.CsvUtilities;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PatientDaoCsv extends AbstractObservableDao implements PatientDao {

    private static final File fd = new File(PatientDaoCsvConstants.PATH_NAME_PATIENTS);

    @Override
    public void savePatient(PatientBean patient) throws DAOException {
        UserDao userDao = DaoFactoryFacade.getInstance().getUserDao();
        // Rendi l'operazione robusta per la sincronizzazione
        try {
            userDao.saveUser(patient);
        } catch (DAOException e) {
            if (!e.getMessage().contains(UserDaoCsvConstants.USER_EXIST)) {
                throw e;
            }
            // Se l'utente esiste già, va bene, la sincronizzazione può procedere.
        }

        String[] patientRecord = new String[PatientDaoCsvConstants.HEADER.length];
        patientRecord[PatientDaoCsvConstants.PATIENT_INDEX_USERNAME] = patient.getUsername();
        patientRecord[PatientDaoCsvConstants.PATIENT_INDEX_BIRTHDATE] = patient.getBirthDate().toString();
        // Quando si crea un paziente, non ha ancora uno psicologo assegnato
        patientRecord[PatientDaoCsvConstants.PATIENT_INDEX_PSYCOLOGIST] = "";

        CsvUtilities.writeFile(fd, patientRecord);
        notifyObservers(DaoOperation.INSERT, "Patient", patient.getUsername(), patient);
    }

    @Override
    public Patient retrievePatient(String username) throws DAOException {
        UserDao userDao = DaoFactoryFacade.getInstance().getUserDao();
        String[] userInfo = userDao.retrieveUser(username);
        if (userInfo == null) return null; // Se l'utente non esiste, il paziente non può esistere

        String[] patientInfo = retrievePatientRecord(username);
        if (patientInfo == null) return null;

        return new Patient(
                userInfo[UserDaoCsvConstants.USER_INDEX_USERNAME],
                userInfo[UserDaoCsvConstants.USER_INDEX_FIRST_NAME],
                userInfo[UserDaoCsvConstants.USER_INDEX_LAST_NAME],
                userInfo[UserDaoCsvConstants.USER_INDEX_GENDER],
                patientInfo[PatientDaoCsvConstants.PATIENT_INDEX_PSYCOLOGIST],
                LocalDate.parse(patientInfo[PatientDaoCsvConstants.PATIENT_INDEX_BIRTHDATE])
        );
    }

    @Override
    public List<Patient> retrieveAllPatients() throws DAOException {
        List<Patient> patients = new ArrayList<>();
        List<String[]> records = CsvUtilities.readAll(fd);
        if (!records.isEmpty() && "Username".equals(records.getFirst()[0])) {
            records.removeFirst(); // Rimuovi header
        }

        for (String[] record : records) {
            // Per ogni paziente nel CSV, recuperiamo i suoi dati utente completi
            Patient patient = retrievePatient(record[PatientDaoCsvConstants.PATIENT_INDEX_USERNAME]);
            if (patient != null) {
                patients.add(patient);
            }
        }
        return patients;
    }

    @Override
    public List<Patient> retrievePatientsByPsychologist(Psychologist psychologist) throws DAOException {
        UserDao userDao = DaoFactoryFacade.getInstance().getUserDao();
        List<String[]> patientRecords = CsvUtilities.readAll(fd);
        if (!patientRecords.isEmpty()) patientRecords.removeFirst(); // Rimuovi header

        List<Patient> patients = new ArrayList<>();
        for (String[] recordPatient : patientRecords) {
            if (recordPatient.length > PatientDaoCsvConstants.PATIENT_INDEX_PSYCOLOGIST &&
                    psychologist.getUsername().equals(recordPatient[PatientDaoCsvConstants.PATIENT_INDEX_PSYCOLOGIST])) {

                String[] userInfo = userDao.retrieveUser(recordPatient[PatientDaoCsvConstants.PATIENT_INDEX_USERNAME]);
                if (userInfo != null) {
                    patients.add(new Patient(
                            recordPatient[PatientDaoCsvConstants.PATIENT_INDEX_USERNAME],
                            userInfo[UserDaoCsvConstants.USER_INDEX_FIRST_NAME],
                            userInfo[UserDaoCsvConstants.USER_INDEX_LAST_NAME],
                            userInfo[UserDaoCsvConstants.USER_INDEX_GENDER],
                            recordPatient[PatientDaoCsvConstants.PATIENT_INDEX_PSYCOLOGIST],
                            LocalDate.parse(recordPatient[PatientDaoCsvConstants.PATIENT_INDEX_BIRTHDATE])
                    ));
                }
            }
        }
        return patients;
    }

    @Override
    public void updatePatient(Patient patient, UserBean user) throws DAOException {
        UserDao userDao = DaoFactoryFacade.getInstance().getUserDao();
        userDao.updateUser(user);

        List<String[]> patientTable = CsvUtilities.readAll(fd);
        String[] header = patientTable.removeFirst();
        boolean found = false;
        for (String[] recordPatient : patientTable) {
            if (recordPatient[PatientDaoCsvConstants.PATIENT_INDEX_USERNAME].equals(patient.getUsername())) {
                recordPatient[PatientDaoCsvConstants.PATIENT_INDEX_BIRTHDATE] = patient.getBirthday().toString();
                recordPatient[PatientDaoCsvConstants.PATIENT_INDEX_PSYCOLOGIST] = patient.getPsychologist();
                found = true;
                break;
            }
        }
        if (!found) {
            throw new DAOException(PatientDaoCsvConstants.PATIENT_NOT_FOUND + patient.getUsername());
        }
        CsvUtilities.updateFile(fd, header, patientTable);
        notifyObservers(DaoOperation.UPDATE, "Patient", patient.getUsername(), patient);
    }

    @Override
    public void deletePatient(String username) throws DAOException {
        UserDao userDao = DaoFactoryFacade.getInstance().getUserDao();
        try {
            userDao.deleteUser(username);
        } catch (DAOException e) {
            if (!e.getMessage().contains(UserDaoCsvConstants.USER_NOT_FOUND)) {
                throw e;
            }
        }

        List<String[]> patientTable = CsvUtilities.readAll(fd);
        String[] header = patientTable.removeFirst();
        boolean removed = patientTable.removeIf(recordPatient -> recordPatient[PatientDaoCsvConstants.PATIENT_INDEX_USERNAME].equals(username));
        if (!removed) {
            throw new DAOException(PatientDaoCsvConstants.PATIENT_NOT_FOUND + username);
        }
        CsvUtilities.updateFile(fd, header, patientTable);
        notifyObservers(DaoOperation.DELETE, "Patient", username, null);
    }

    private String[] retrievePatientRecord(String username) throws DAOException {
        List<String[]> patientTable = CsvUtilities.readAll(fd);
        if (!patientTable.isEmpty()) patientTable.removeFirst(); // Rimuovi header

        for (String[] patientRecord : patientTable) {
            if (patientRecord.length > PatientDaoCsvConstants.PATIENT_INDEX_USERNAME && username.equals(patientRecord[PatientDaoCsvConstants.PATIENT_INDEX_USERNAME])) {
                return patientRecord;
            }
        }
        return null;
    }
}
