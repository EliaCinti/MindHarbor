package it.uniroma2.mindharbor.dao.csv;

import it.uniroma2.mindharbor.beans.PatientBean;
import it.uniroma2.mindharbor.dao.PatientDao;
import it.uniroma2.mindharbor.dao.UserDao;
import it.uniroma2.mindharbor.dao.csv.constants.PatientDaoCsvConstants;
import it.uniroma2.mindharbor.dao.csv.constants.UserDaoCsvConstants;
import it.uniroma2.mindharbor.exception.DAOException;
import it.uniroma2.mindharbor.model.Patient;
import it.uniroma2.mindharbor.patterns.facade.DaoFactoryFacade;
import it.uniroma2.mindharbor.utilities.CsvUtilities;

import java.io.File;
import java.time.LocalDate;
import java.util.List;

/**
 * Implementation of the PatientDao interface for CSV-based persistence.
 * This class manages patient data stored in a CSV file.
 */
public class PatientDaoCsv implements PatientDao {

    private static final File fd = new File(it.uniroma2.mindharbor.dao.csv.constants.PatientDaoCsvConstants.PATH_NAME_PATIENTS);

    /**
     * Saves a patient's data to the CSV file.
     * This method first saves the user's basic information using UserDao,
     * then appends patient-specific data (e.g., birthdate) to the CSV file.
     *
     * @param patient The PatientBean object containing the patient's data.
     * @throws DAOException If an error occurs while saving the data to the CSV file.
     */
    @Override
    public void savePatient(PatientBean patient) throws DAOException {
        UserDao userDao = DaoFactoryFacade.getInstance().getUserDao();
        userDao.saveUser(patient);
        String[] patientRecord = new String[3];
        patientRecord[PatientDaoCsvConstants.PATIENT_INDEX_USERNAME] = patient.getUsername();
        patientRecord[PatientDaoCsvConstants.PATIENT_INDEX_BIRTHDATE] = patient.getBirthDate().toString();
        CsvUtilities.writeFile(fd, patientRecord);
    }

    /**
     * Retrieves a patient's data from the CSV file based on their username.
     * Combines user information from UserDao with patient-specific information from the CSV file.
     *
     * @param username The username of the patient to retrieve.
     * @return A Patient object containing the combined user and patient data.
     * @throws DAOException If an error occurs while retrieving the data.
     */
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
