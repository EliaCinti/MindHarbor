package it.uniroma2.mindharbor.dao;

import it.uniroma2.mindharbor.beans.PatientBean;
import it.uniroma2.mindharbor.beans.PsychologistBean;
import it.uniroma2.mindharbor.beans.UserBean;
import it.uniroma2.mindharbor.exception.DAOException;
import it.uniroma2.mindharbor.model.Patient;
import it.uniroma2.mindharbor.model.Psychologist;

import java.util.List;

/**
 * Interface for data access operations related to psychologists.
 * <p>
 * This interface defines the contract for managing psychologist data in the persistence layer.
 * Implementations may vary based on the type of persistence (e.g., CSV, MySQL).
 * </p>
 */
public interface PsychologistDao {

    /**
     * Saves a new psychologist's data in the persistence system.
     *
     * @param psychologist the {@link PsychologistBean} to be saved.
     * @throws DAOException if an error occurs during saving or if the psychologist already exists.
     */
    void savePsychologist(PsychologistBean psychologist) throws DAOException;

    /**
     * Retrieves a psychologist's data based on their username.
     *
     * @param username the psychologist's username.
     * @return the corresponding {@link PsychologistBean} if found, otherwise {@code null}.
     * @throws DAOException if an error occurs during data access.
     */
    Psychologist retrievePsychologist(String username) throws DAOException;

    /**
     * Updates an existing psychologist's data.
     *
     * @param psychologist the {@link PsychologistBean} containing the updated data.
     * @throws DAOException if an error occurs during the update.
     */
    void updatePsychologist(Psychologist psychologist, UserBean bean) throws DAOException;

    /**
     * Deletes the psychologist identified by the given username from the persistence system.
     *
     * @param username the username of the psychologist to be deleted.
     * @throws DAOException if an error occurs during the deletion.
     */
    void deletePsychologist(String username) throws DAOException;

    /**
     * Returns a list of patients associated with the psychologist identified by the given username.
     * <p>
     * The implementation of this method may use the {@code PatientDao} to retrieve
     * the complete list of patients and filter those that are associated with the specified psychologist.
     * </p>
     *
     * @param psychologist the {@link Psychologist} that want retrive the patients list
     * @return a list of {@link Patient} containing the associated patients.
     * @throws DAOException if an error occurs during data access.
     */
    List<Patient> getPatients(Psychologist psychologist) throws DAOException;
}