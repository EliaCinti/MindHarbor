package it.uniroma2.mindharbor.dao.mysql;

import it.uniroma2.mindharbor.beans.PatientBean;
import it.uniroma2.mindharbor.beans.UserBean;
import it.uniroma2.mindharbor.dao.PatientDao;
import it.uniroma2.mindharbor.exception.DAOException;
import it.uniroma2.mindharbor.model.Patient;
import it.uniroma2.mindharbor.model.Psychologist;

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

    @Override
    public void savePatient(PatientBean patient) throws DAOException {
        // TODO: Implement the SQL insert query for saving a patient.
    }


    @Override
    public Patient retrievePatient(String username) throws DAOException {
        // TODO: Implement the SQL SELECT query for retrieving a patient.
        return null;
    }


    @Override
    public List<Patient> retrievePatientsByPsychologist(Psychologist psychologist) throws DAOException {
        // TODO: Implement the SQL query to retrieve patients by psychologist.
        return List.of();
    }


    @Override
    public void updatePatient(Patient patient, UserBean user) throws DAOException {
        // TODO: Implement the SQL UPDATE query for updating patient data.
    }

    @Override
    public void deletePatient(String username) throws DAOException {
        // TODO: Implement the SQL DELETE query for removing a patient.
    }
}
