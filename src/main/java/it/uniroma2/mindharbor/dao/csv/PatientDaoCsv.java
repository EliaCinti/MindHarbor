package it.uniroma2.mindharbor.dao.csv;

import it.uniroma2.mindharbor.beans.PatientBean;
import it.uniroma2.mindharbor.beans.UserBean;
import it.uniroma2.mindharbor.dao.PatientDao;
import it.uniroma2.mindharbor.dao.UserDao;
import it.uniroma2.mindharbor.dao.csv.constants.PatientDaoCsvConstants;
import it.uniroma2.mindharbor.dao.csv.constants.UserDaoCsvConstants;
import it.uniroma2.mindharbor.exception.DAOException;
import it.uniroma2.mindharbor.model.Patient;
import it.uniroma2.mindharbor.model.Psychologist;
import it.uniroma2.mindharbor.patterns.facade.DaoFactoryFacade;
import it.uniroma2.mindharbor.utilities.CsvUtilities;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the PatientDao interface for CSV-based persistence.
 * This class manages patient data stored in a CSV file.
 */
public class PatientDaoCsv implements PatientDao {

    private static final File fd = new File(it.uniroma2.mindharbor.dao.csv.constants.PatientDaoCsvConstants.PATH_NAME_PATIENTS);

    @Override
    public void savePatient(PatientBean patient) throws DAOException {
        UserDao userDao = DaoFactoryFacade.getInstance().getUserDao();
        userDao.saveUser(patient);
        String[] patientRecord = new String[PatientDaoCsvConstants.HEADER.length];
        patientRecord[PatientDaoCsvConstants.PATIENT_INDEX_USERNAME] = patient.getUsername();
        patientRecord[PatientDaoCsvConstants.PATIENT_INDEX_BIRTHDATE] = patient.getBirthDate().toString();
        CsvUtilities.writeFile(fd, patientRecord);
    }

    @Override
    public Patient retrievePatient(String username) throws DAOException {
        UserDao userDao = DaoFactoryFacade.getInstance().getUserDao();
        String[] userInfo = userDao.retrieveUser(username);
        String[] patientInfo = retrievePatientRecord(username);
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
    public List<Patient> retrievePatientsByPsychologist(Psychologist psychologist) throws DAOException {
        UserDao userDao = DaoFactoryFacade.getInstance().getUserDao();
        List<String[]> patientRecord = CsvUtilities.readAll(fd);
        List<Patient> patients = new ArrayList<>();
        for (String[] recordPatient : patientRecord) {
            if (recordPatient[PatientDaoCsvConstants.PATIENT_INDEX_PSYCOLOGIST].equals(psychologist.getUsername())) {
                String[] userInfo = userDao.retrieveUser(recordPatient[PatientDaoCsvConstants.PATIENT_INDEX_USERNAME]);
                patients.add(new Patient(recordPatient[PatientDaoCsvConstants.PATIENT_INDEX_USERNAME],
                        userInfo[UserDaoCsvConstants.USER_INDEX_FIRST_NAME],
                        userInfo[UserDaoCsvConstants.USER_INDEX_LAST_NAME],
                        userInfo[UserDaoCsvConstants.USER_INDEX_GENDER],
                        recordPatient[PatientDaoCsvConstants.PATIENT_INDEX_PSYCOLOGIST],
                        LocalDate.parse(recordPatient[PatientDaoCsvConstants.PATIENT_INDEX_BIRTHDATE])
                ));
            }
        }
        return patients;
    }

    @Override
    public void updatePatient(Patient patient, UserBean user) throws DAOException {
        UserDao userDao = DaoFactoryFacade.getInstance().getUserDao();
        userDao.updateUser(user);
        List<String[]> patientTable = CsvUtilities.readAll(fd);
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
        CsvUtilities.updateFile(fd, PatientDaoCsvConstants.HEADER, patientTable);
    }

    @Override
    public void deletePatient(String username) throws DAOException {
        UserDao userDao = DaoFactoryFacade.getInstance().getUserDao();
        userDao.deleteUser(username);
        List<String[]> patientTable = CsvUtilities.readAll(fd);
        boolean removed = patientTable.removeIf(recordPatient -> recordPatient[PatientDaoCsvConstants.PATIENT_INDEX_USERNAME].equals(username));
        if (!removed) {
            throw new DAOException(PatientDaoCsvConstants.PATIENT_NOT_FOUND + username);
        }
        CsvUtilities.updateFile(fd, PatientDaoCsvConstants.HEADER, patientTable);
    }

    /**
     * Retrieves a specific patient's record from the CSV file based on their username.
     * Iterates through all records in the CSV file until it finds a matching username.
     *
     * @param username The username of the patient whose record is to be retrieved.
     * @return A string array representing the patient's record, or null if no record is found.
     * @throws DAOException If an error occurs while reading the CSV file.
     */
    private String[] retrievePatientRecord(String username) throws DAOException {
        List<String[]> patientTable = CsvUtilities.readAll(fd);
        String[] patientRecord = null;
        while (!patientTable.isEmpty()) {
            patientRecord = patientTable.removeFirst();
            if (username.equals(patientRecord[PatientDaoCsvConstants.PATIENT_INDEX_USERNAME])) {
                return patientRecord;
            }
        }
        return patientRecord;
    }
}
