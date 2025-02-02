package it.uniroma2.mindharbor.dao.mysql;

import it.uniroma2.mindharbor.beans.PatientBean;
import it.uniroma2.mindharbor.beans.PsychologistBean;
import it.uniroma2.mindharbor.beans.UserBean;
import it.uniroma2.mindharbor.dao.PatientDao;
import it.uniroma2.mindharbor.dao.PsychologistDao;
import it.uniroma2.mindharbor.exception.DAOException;
import it.uniroma2.mindharbor.model.Patient;

public class PatientDaoMySql implements PatientDao {

    @Override
    public void savePatient(PatientBean patient) throws DAOException {

    }

    @Override
    public Patient retrievePatient(String username) throws DAOException {
        return null;
    }

    @Override
    public void updatePatient(Patient patient, UserBean user) throws DAOException {

    }

    @Override
    public void deletePatient(String username) throws DAOException {

    }
}
