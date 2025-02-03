package it.uniroma2.mindharbor.dao;

import it.uniroma2.mindharbor.beans.PatientBean;
import it.uniroma2.mindharbor.beans.UserBean;
import it.uniroma2.mindharbor.exception.DAOException;
import it.uniroma2.mindharbor.model.Patient;

import java.util.List;

/**
 * The {@code PatientDao} interface defines data access operations for managing patients.
 * <p>
 * Implementations of this interface provide functionality to save, retrieve, update,
 * and delete patient records from the persistence layer.
 * </p>
 */
public interface PatientDao {
    /**
     * Saves a new patient in the persistence layer.
     * <p>
     * If a patient with the same username already exists, a {@link DAOException} is thrown.
     * Otherwise, the patient details are stored.
     * </p>
     *
     * @param patient The {@link PatientBean} object containing the patient's details.
     * @throws DAOException If an error occurs while saving the patient or if the patient already exists.
     */
    public void savePatient(PatientBean patient) throws DAOException;

    /**
     * Retrieves patient details from the persistence layer based on the username.
     * <p>
     * The method searches for a patient with the specified username and returns a
     * {@link PatientBean} object. If no match is found, {@code null} is returned.
     * </p>
     *
     * @param username The username of the patient to retrieve.
     * @return A {@link PatientBean} object if found, otherwise {@code null}.
     * @throws DAOException If an error occurs while accessing the data storage.
     */
    public Patient retrievePatient(String username) throws DAOException;

    /**
     * Retrieves a list of patients assigned to a specific psychologist.
     * <p>
     * This method searches the patient database for records where the psychologist's username
     * matches the specified value. It returns a list of {@link PatientBean} objects
     * representing the patients under the psychologist's care.
     * </p>
     *
     * @param psychologist The username of the psychologist whose patients are to be retrieved.
     * @return A list of {@link PatientBean} objects representing the patients assigned to the psychologist.
     * If no patients are found, the list will be empty.
     * @throws DAOException If an error occurs while accessing the data storage.
     */
    public List<PatientBean> retrivePatientsByPsychologist(String psychologist) throws DAOException;

    /**
     * Updates an existing patient's details in the persistence layer.
     * <p>
     * This method updates both the user's general information and the patient's specific details.
     * First, it updates the associated user data using {@link UserDao}. Then, it searches for
     * the corresponding patient record in the CSV file and updates the assigned psychologist.
     * If the patient does not exist, a {@link DAOException} is thrown.
     * </p>
     *
     * @param patient The {@link Patient} object containing the updated patient-specific details.
     * @param user    The {@link UserBean} object containing the updated general user details.
     * @throws DAOException If the patient does not exist or if an error occurs while updating the data.
     */
    void updatePatient(Patient patient, UserBean user) throws DAOException;

    /**
     * Deletes a patient from the persistence layer.
     * <p>
     * If the patient does not exist, no action is taken.
     * </p>
     *
     * @param username The username of the patient to delete.
     * @throws DAOException If an error occurs while deleting the patient.
     */
    void deletePatient(String username) throws DAOException;
}
