package it.uniroma2.mindharbor.dao.mysql;

import it.uniroma2.mindharbor.beans.PsychologistBean;
import it.uniroma2.mindharbor.dao.PsychologistDao;
import it.uniroma2.mindharbor.exception.DAOException;

/**
 * MySQL implementation of the {@link PsychologistDao} interface.
 * <p>
 * This class provides MySQL-specific functionality for managing psychologist data in the database.
 * Currently, it includes a placeholder method for saving psychologist data.
 * </p>
 */
public class PsychologistDaoMySql implements PsychologistDao {

    /**
     * Saves the details of a psychologist in the database.
     * <p>
     * This method is a placeholder for inserting psychologist data into the database.
     * It should include logic for persisting the {@link PsychologistBean} object,
     * such as the psychologist's username, office location, and hourly cost.
     * </p>
     *
     * @param psychologist The {@link PsychologistBean} object containing the psychologist's details.
     * @throws DAOException If a database access error occurs during the save operation.
     */
    @Override
    public void savePsychologist(PsychologistBean psychologist) throws DAOException {
        // Implementation placeholder
    }
}
