package it.uniroma2.mindharbor.dao.csv;

import it.uniroma2.mindharbor.beans.PatientBean;
import it.uniroma2.mindharbor.beans.UserBean;
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
import java.util.ArrayList;
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
        String[] patientRecord = new String[PatientDaoCsvConstants.HEADER.length];
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
    public List<PatientBean> retrivePatientsByPsychologist(String psychologist) throws DAOException {
        List<String[]> patientRecord = CsvUtilities.readAll(fd);
        List<PatientBean> patients = new ArrayList<PatientBean>();
        for (String[] record : patientRecord) {
            if (record[PatientDaoCsvConstants.PATIENT_INDEX_PSYCOLOGIST].equals(psychologist)) {
                patients.add(new PatientBean.Builder()
                        .username(record[PatientDaoCsvConstants.PATIENT_INDEX_USERNAME])
                        .birthDate(LocalDate.parse(record[PatientDaoCsvConstants.PATIENT_INDEX_BIRTHDATE]))
                        .build());
            }
        }
        return patients;
    }

    /**
     * Updates an existing patient's details in the persistence layer.
     * <p>
     * This method first updates the associated user data using {@link UserDao}.
     * Then, it searches for the corresponding patient record in the CSV file and updates
     * the psychologist assigned to the patient. If the patient is not found, a
     * {@link DAOException} is thrown.
     * </p>
     *
     * @param patient The {@link Patient} object containing the updated patient details.
     * @param user    The {@link UserBean} object containing the updated user details.
     * @throws DAOException If the patient does not exist or if an error occurs while updating the data.
     */
    @Override
    public void updatePatient(Patient patient, UserBean user) throws DAOException {
        UserDao userDao = DaoFactoryFacade.getInstance().getUserDao();
        userDao.updateUser(user);
        List<String[]> patientTable = CsvUtilities.readAll(fd);
        boolean found = false;
        for (String[] record : patientTable) {
            if (record[PatientDaoCsvConstants.PATIENT_INDEX_USERNAME].equals(patient.getUsername())) {
                record[PatientDaoCsvConstants.PATIENT_INDEX_BIRTHDATE] = patient.getBirthday().toString();
                record[PatientDaoCsvConstants.PATIENT_INDEX_PSYCOLOGIST] = patient.getPsychologist();
                found = true;
                break;
            }
        }
        if (!found) {
            throw new DAOException(PatientDaoCsvConstants.PATIENT_NOT_FOUND + patient.getUsername());
        }
        CsvUtilities.updateFile(fd, PatientDaoCsvConstants.HEADER, patientTable);
    }

    /**
     * Deletes a patient from the persistence layer.
     * <p>
     * This method first deletes the associated user data using {@link UserDao}.
     * Then, it removes the corresponding patient record from the CSV file.
     * If the patient does not exist, a {@link DAOException} is thrown.
     * </p>
     *
     * @param username The username of the patient to delete.
     * @throws DAOException If the patient does not exist or if an error occurs while deleting the patient.
     */
    @Override
    public void deletePatient(String username) throws DAOException {
        UserDao userDao = DaoFactoryFacade.getInstance().getUserDao();
        userDao.deleteUser(username);
        List<String[]> patientTable = CsvUtilities.readAll(fd);
        boolean removed = patientTable.removeIf(record -> record[PatientDaoCsvConstants.PATIENT_INDEX_USERNAME].equals(username));
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
