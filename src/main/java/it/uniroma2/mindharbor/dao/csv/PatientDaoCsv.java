package it.uniroma2.mindharbor.dao.csv;

import it.uniroma2.mindharbor.beans.PatientBean;
import it.uniroma2.mindharbor.dao.PatientDao;
import it.uniroma2.mindharbor.dao.UserDao;
import it.uniroma2.mindharbor.exception.DAOException;
import it.uniroma2.mindharbor.model.Patient;
import it.uniroma2.mindharbor.patterns.facade.DaoFactoryFacade;
import it.uniroma2.mindharbor.utilities.CsvUtilities;

import java.io.File;
import java.time.LocalDate;
import java.util.List;

public class PatientDaoCsv implements PatientDao {

    private static final File fd = new File("db/csv/patient_db.csv");

    @Override
    public void savePatient(PatientBean patient) throws DAOException {
        UserDao userDao = DaoFactoryFacade.getInstance().getUserDao();
        userDao.saveUser(patient);
        String[] patientRecord = new String[3];
        patientRecord[PatientIndex.PATIENT_INDEX_USERNAME] = patient.getUsername();
        patientRecord[PatientIndex.PATIENT_INDEX_BIRTHDATE] = patient.getBirthDate().toString();
        CsvUtilities.writeFile(fd, patientRecord);
    }

    public Patient retrievePatient(String username) throws DAOException {
        UserDao userDao = DaoFactoryFacade.getInstance().getUserDao();
        String[] userInfo = userDao.retrieveUser(username);
        String[] patientInfo = retrievePatientRecord(username);
        return new Patient(
                userInfo[0],
                userInfo[2],
                userInfo[3],
                userInfo[5],
                patientInfo[PatientIndex.PATIENT_INDEX_PSYCOLOGIST],
                LocalDate.parse(patientInfo[PatientIndex.PATIENT_INDEX_BIRTHDATE])
                );
    }

    private String[] retrievePatientRecord(String username) throws DAOException {
            List<String[]> patientTable = CsvUtilities.readAll(fd);
            String[] patientRecord = null;
            while (!patientTable.isEmpty()) {
                patientRecord = patientTable.removeFirst();
                if (username.equals(patientRecord[PatientIndex.PATIENT_INDEX_USERNAME])) {
                    return patientRecord;
                }
            }
            return patientRecord;
    }

    private static class PatientIndex {
        static final int PATIENT_INDEX_USERNAME = 0;
        static final int PATIENT_INDEX_BIRTHDATE = 1;
        static final int PATIENT_INDEX_PSYCOLOGIST = 2;
    }
}
