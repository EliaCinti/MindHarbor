package it.uniroma2.mindharbor.dao.mysql;

import it.uniroma2.mindharbor.beans.PatientBean;
import it.uniroma2.mindharbor.beans.UserBean;
import it.uniroma2.mindharbor.dao.PatientDao;
import it.uniroma2.mindharbor.exception.DAOException;
import it.uniroma2.mindharbor.model.Patient;

import java.util.List;

/**
 * Implementation of {@link PatientDao} that interacts with a MySQL database.
 * <p>
 * This class provides operations to manage patients, including saving, retrieving,
 * updating, and deleting patient records in the database.
 * </p>
 * <p>
 * <b>Note:</b> The methods are currently not implemented and require MySQL queries.
 * </p>
 *
 * @TODO Implement all database operations using MySQL queries.
 */
public class PatientDaoMySql implements PatientDao {
    /**
     * Saves a new patient in the MySQL database.
     * <p>
     * This method should insert the patient's details into the database.
     * </p>
     *
     * @param patient The {@link PatientBean} object containing the patient's details.
     * @throws DAOException If an error occurs while saving the patient.
     * @TODO Implement the SQL insert query.
     */
    @Override
    public void savePatient(PatientBean patient) throws DAOException {
        // TODO: Implement the SQL insert query for saving a patient.
    }

    /**
     * Retrieves a patient's data from the MySQL database based on their username.
     * <p>
     * This method should execute a SQL SELECT query to fetch the patient's details.
     * </p>
     *
     * @param username The username of the patient to retrieve.
     * @return A {@link Patient} object if found, otherwise {@code null}.
     * @throws DAOException If an error occurs while retrieving the patient.
     * @TODO Implement the SQL SELECT query.
     */
    @Override
    public Patient retrievePatient(String username) throws DAOException {
        // TODO: Implement the SQL SELECT query for retrieving a patient.
        return null;
    }

    /**
     * Retrieves a list of patients assigned to a specific psychologist from the MySQL database.
     * <p>
     * This method should execute a SQL query to find all patients linked to the given psychologist.
     * </p>
     *
     * @param psychologist The username of the psychologist whose patients are to be retrieved.
     * @return A list of {@link PatientBean} objects representing the patients.
     * If no patients are found, the list will be empty.
     * @throws DAOException If an error occurs while accessing the database.
     * @TODO Implement the SQL query to fetch patient data by psychologist.
     */
    @Override
    public List<PatientBean> retrivePatientsByPsychologist(String psychologist) throws DAOException {
        // TODO: Implement the SQL query to retrieve patients by psychologist.
        return List.of();
    }

    /**
     * Updates an existing patient's details in the MySQL database.
     * <p>
     * This method should execute a SQL UPDATE query to modify patient information.
     * </p>
     *
     * @param patient The {@link Patient} object containing the updated patient-specific details.
     * @param user    The {@link UserBean} object containing the updated general user details.
     * @throws DAOException If an error occurs while updating the patient.
     * @TODO Implement the SQL UPDATE query.
     */
    @Override
    public void updatePatient(Patient patient, UserBean user) throws DAOException {
        // TODO: Implement the SQL UPDATE query for updating patient data.
    }

    /**
     * Deletes a patient from the MySQL database.
     * <p>
     * This method should execute a SQL DELETE query to remove the patient record.
     * </p>
     *
     * @param username The username of the patient to delete.
     * @throws DAOException If an error occurs while deleting the patient.
     * @TODO Implement the SQL DELETE query.
     */
    @Override
    public void deletePatient(String username) throws DAOException {
        // TODO: Implement the SQL DELETE query for removing a patient.
    }
}
