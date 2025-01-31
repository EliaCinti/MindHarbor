package it.uniroma2.mindharbor.dao;

import it.uniroma2.mindharbor.beans.PatientBean;
import it.uniroma2.mindharbor.exception.DAOException;
import it.uniroma2.mindharbor.model.Patient;

public interface PatientDao {
    public void savePatient(PatientBean patient) throws DAOException;

}
