package it.uniroma2.mindharbor.dao.mysql;

import it.uniroma2.mindharbor.beans.PsychologistBean;
import it.uniroma2.mindharbor.beans.UserBean;
import it.uniroma2.mindharbor.dao.PsychologistDao;
import it.uniroma2.mindharbor.exception.DAOException;
import it.uniroma2.mindharbor.model.Patient;
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

    @Override
    public void savePsychologist(PsychologistBean psychologist) throws DAOException {
        // TODO: Implement the SQL INSERT query to save psychologist data.
    }

    @Override
    public Psychologist retrievePsychologist(String username) throws DAOException {
        // TODO: Implement the SQL SELECT query to retrieve psychologist details.
        return null;
    }

    @Override
    public void updatePsychologist(Psychologist psychologist, UserBean bean) throws DAOException {
        // TODO: Implement the SQL UPDATE query to update psychologist details.
    }


    @Override
    public void deletePsychologist(String username) throws DAOException {
        // TODO: Implement the SQL DELETE query to remove a psychologist.
    }


    @Override
    public List<Patient> getPatients(Psychologist psychologist) throws DAOException {
        // TODO: Implement the SQL query to retrieve patients assigned to a psychologist.
        return List.of();
    }
}
