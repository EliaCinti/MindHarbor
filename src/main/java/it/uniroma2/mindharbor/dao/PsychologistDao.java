package it.uniroma2.mindharbor.dao;

import it.uniroma2.mindharbor.beans.PsychologistBean;
import it.uniroma2.mindharbor.exception.DAOException;

/**
 * Interface for data access operations related to psychologists.
 * <p>
 * This interface defines the contract for saving psychologist data
 * in the persistence layer. Implementations may vary based on the
 * type of persistence (e.g., CSV, MySQL).
 * </p>
 */
public interface PsychologistDao {

    /**
     * Saves the details of a psychologist in the persistence layer.
     * <p>
     * The method accepts a {@link PsychologistBean} object containing
     * the psychologist's details and saves it to the appropriate
     * data store. If an error occurs during the save operation, a
     * {@link DAOException} is thrown.
     * </p>
     *
     * @param psychologist The {@link PsychologistBean} object representing
     *                     the psychologist to be saved.
     * @throws DAOException If an error occurs while saving the psychologist's data.
     */
    public void savePsychologist(PsychologistBean psychologist) throws DAOException;
}
