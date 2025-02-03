package it.uniroma2.mindharbor.dao.mysql;

import it.uniroma2.mindharbor.beans.PatientBean;
import it.uniroma2.mindharbor.beans.PsychologistBean;
import it.uniroma2.mindharbor.beans.UserBean;
import it.uniroma2.mindharbor.dao.PsychologistDao;
import it.uniroma2.mindharbor.exception.DAOException;
import it.uniroma2.mindharbor.model.Psychologist;

import java.util.List;

/**
 * MySQL implementation of the {@link PsychologistDao} interface.
 * <p>
 * This class provides MySQL-specific functionality for managing psychologist data in the database.
 * It includes operations for saving, retrieving, updating, and deleting psychologists.
 * </p>
 *
 * @TODO Implement all database operations using MySQL queries.
 */
public class PsychologistDaoMySql implements PsychologistDao {

    /**
     * Saves the details of a psychologist in the database.
     * <p>
     * This method inserts a new psychologist record into the database.
     * </p>
     *
     * @param psychologist The {@link PsychologistBean} object containing the psychologist's details.
     * @throws DAOException If a database access error occurs during the save operation.
     * @TODO Implement the SQL INSERT query to save a psychologist.
     */
    @Override
    public void savePsychologist(PsychologistBean psychologist) throws DAOException {
        // TODO: Implement the SQL INSERT query to save psychologist data.
    }

    /**
     * Retrieves a psychologist's data from the MySQL database based on their username.
     * <p>
     * This method fetches the psychologist's details, including their office location and hourly cost.
     * </p>
     *
     * @param username The username of the psychologist to retrieve.
     * @return A {@link Psychologist} object if found, otherwise {@code null}.
     * @throws DAOException If a database access error occurs during the retrieval operation.
     * @TODO Implement the SQL SELECT query to retrieve psychologist details.
     */
    @Override
    public Psychologist retrievePsychologist(String username) throws DAOException {
        // TODO: Implement the SQL SELECT query to retrieve psychologist details.
        return null;
    }

    /**
     * Updates an existing psychologist's details in the database.
     * <p>
     * This method modifies an existing psychologist record based on the provided {@link Psychologist} and {@link UserBean}.
     * </p>
     *
     * @param psychologist The {@link Psychologist} object containing the updated psychologist-specific details.
     * @param bean         The {@link UserBean} object containing updated general user details.
     * @throws DAOException If a database access error occurs during the update operation.
     * @TODO Implement the SQL UPDATE query to modify psychologist data.
     */
    @Override
    public void updatePsychologist(Psychologist psychologist, UserBean bean) throws DAOException {
        // TODO: Implement the SQL UPDATE query to update psychologist details.
    }

    /**
     * Deletes a psychologist from the MySQL database.
     * <p>
     * This method removes a psychologist record based on the specified username.
     * </p>
     *
     * @param username The username of the psychologist to delete.
     * @throws DAOException If a database access error occurs during the delete operation.
     * @TODO Implement the SQL DELETE query to remove a psychologist.
     */
    @Override
    public void deletePsychologist(String username) throws DAOException {
        // TODO: Implement the SQL DELETE query to remove a psychologist.
    }

    /**
     * Retrieves a list of patients assigned to a specific psychologist.
     * <p>
     * This method should query the database to find all patients linked to the given psychologist.
     * </p>
     *
     * @param psychologistUsername The username of the psychologist whose patients are to be retrieved.
     * @return A list of {@link PatientBean} objects representing the patients assigned to the psychologist.
     * If no patients are found, the list will be empty.
     * @throws DAOException If an error occurs while accessing the database.
     * @TODO Implement the SQL query to fetch patients assigned to a psychologist.
     */
    @Override
    public List<PatientBean> getPatients(String psychologistUsername) throws DAOException {
        // TODO: Implement the SQL query to retrieve patients assigned to a psychologist.
        return List.of();
    }
}
