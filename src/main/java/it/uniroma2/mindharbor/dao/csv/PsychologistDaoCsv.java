package it.uniroma2.mindharbor.dao.csv;

import it.uniroma2.mindharbor.beans.PsychologistBean;
import it.uniroma2.mindharbor.beans.UserBean;
import it.uniroma2.mindharbor.dao.AbstractObservableDao;
import it.uniroma2.mindharbor.dao.PatientDao;
import it.uniroma2.mindharbor.dao.PsychologistDao;
import it.uniroma2.mindharbor.dao.UserDao;
import it.uniroma2.mindharbor.dao.csv.constants.PsychologistDaoCsvConstants;
import it.uniroma2.mindharbor.dao.csv.constants.UserDaoCsvConstants;
import it.uniroma2.mindharbor.exception.DAOException;
import it.uniroma2.mindharbor.model.Patient;
import it.uniroma2.mindharbor.model.Psychologist;
import it.uniroma2.mindharbor.patterns.facade.DaoFactoryFacade;
import it.uniroma2.mindharbor.patterns.observer.DaoOperation;
import it.uniroma2.mindharbor.utilities.CsvUtilities;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PsychologistDaoCsv extends AbstractObservableDao implements PsychologistDao {

    private static final File fd = new File(PsychologistDaoCsvConstants.PATH_NAME_PSYCHOLOGIST);

    @Override
    public void savePsychologist(PsychologistBean psychologist) throws DAOException {
        UserDao userDao = DaoFactoryFacade.getInstance().getUserDao();
        // Rendi l'operazione robusta per la sincronizzazione
        try {
            userDao.saveUser(psychologist);
        } catch (DAOException e) {
            if (!e.getMessage().contains(UserDaoCsvConstants.USER_EXIST)) {
                throw e;
            }
            // Se l'utente esiste già, va bene.
        }

        String[] psychologistRecord = new String[PsychologistDaoCsvConstants.HEADER.length];
        psychologistRecord[PsychologistDaoCsvConstants.PSYCHOLOGIST_INDEX_USERNAME] = psychologist.getUsername();
        psychologistRecord[PsychologistDaoCsvConstants.PSYCHOLOGIST_INDEX_OFFICE] = psychologist.getOffice();
        psychologistRecord[PsychologistDaoCsvConstants.PSYCHOLOGIST_INDEX_HOURLY_COST] = String.valueOf(psychologist.getHourlyCost());

        CsvUtilities.writeFile(fd, psychologistRecord);
        notifyObservers(DaoOperation.INSERT, "Psychologist", psychologist.getUsername(), psychologist);
    }

    @Override
    public Psychologist retrievePsychologist(String username) throws DAOException {
        UserDao userDao = DaoFactoryFacade.getInstance().getUserDao();
        String[] userInfo = userDao.retrieveUser(username);
        if (userInfo == null) return null;

        String[] psychologistInfo = retrievePsychologistRecord(username);
        if (psychologistInfo == null) return null;

        return new Psychologist(
                userInfo[UserDaoCsvConstants.USER_INDEX_USERNAME],
                userInfo[UserDaoCsvConstants.USER_INDEX_FIRST_NAME],
                userInfo[UserDaoCsvConstants.USER_INDEX_LAST_NAME],
                userInfo[UserDaoCsvConstants.USER_INDEX_GENDER],
                psychologistInfo[PsychologistDaoCsvConstants.PSYCHOLOGIST_INDEX_OFFICE],
                psychologistInfo[PsychologistDaoCsvConstants.PSYCHOLOGIST_INDEX_HOURLY_COST]
        );
    }

    @Override
    public List<Psychologist> retrieveAllPsychologists() throws DAOException {
        List<Psychologist> psychologists = new ArrayList<>();
        List<String[]> records = CsvUtilities.readAll(fd);
        if (!records.isEmpty() && "Username".equals(records.getFirst()[0])) {
            records.removeFirst(); // Rimuovi header
        }

        for (String[] record : records) {
            Psychologist psychologist = retrievePsychologist(record[PsychologistDaoCsvConstants.PSYCHOLOGIST_INDEX_USERNAME]);
            if (psychologist != null) {
                psychologists.add(psychologist);
            }
        }
        return psychologists;
    }

    @Override
    public void updatePsychologist(Psychologist psychologist, UserBean bean) throws DAOException {
        UserDao userDao = DaoFactoryFacade.getInstance().getUserDao();
        userDao.updateUser(bean);

        List<String[]> psychologistTable = CsvUtilities.readAll(fd);
        String[] header = psychologistTable.removeFirst();
        boolean found = false;
        for (String[] recordPsychologist : psychologistTable) {
            if (recordPsychologist[PsychologistDaoCsvConstants.PSYCHOLOGIST_INDEX_USERNAME].equals(psychologist.getUsername())) {
                recordPsychologist[PsychologistDaoCsvConstants.PSYCHOLOGIST_INDEX_OFFICE] = psychologist.getOffice();
                recordPsychologist[PsychologistDaoCsvConstants.PSYCHOLOGIST_INDEX_HOURLY_COST] = String.valueOf(psychologist.getHourlyCost());
                found = true;
                break;
            }
        }
        if (!found) {
            throw new DAOException(PsychologistDaoCsvConstants.PSYCHOLOGIST_NOT_FOUND + psychologist.getUsername());
        }
        CsvUtilities.updateFile(fd, header, psychologistTable);
        notifyObservers(DaoOperation.UPDATE, "Psychologist", psychologist.getUsername(), psychologist);
    }

    @Override
    public void deletePsychologist(String username) throws DAOException {
        UserDao userDao = DaoFactoryFacade.getInstance().getUserDao();
        try {
            userDao.deleteUser(username);
        } catch (DAOException e) {
            if (!e.getMessage().contains(UserDaoCsvConstants.USER_NOT_FOUND)) {
                throw e;
            }
        }

        List<String[]> psychologistTable = CsvUtilities.readAll(fd);
        String[] header = psychologistTable.removeFirst();
        boolean removed = psychologistTable.removeIf(record -> record[PsychologistDaoCsvConstants.PSYCHOLOGIST_INDEX_USERNAME].equals(username));
        if (!removed) {
            throw new DAOException(PsychologistDaoCsvConstants.PSYCHOLOGIST_NOT_FOUND + username);
        }
        CsvUtilities.updateFile(fd, header, psychologistTable);
        notifyObservers(DaoOperation.DELETE, "Psychologist", username, null);
    }

    @Override
    public List<Patient> getPatients(Psychologist psychologist) throws DAOException {
        PatientDao patientDao = DaoFactoryFacade.getInstance().getPatientDao();
        return patientDao.retrievePatientsByPsychologist(psychologist);
    }

    private String[] retrievePsychologistRecord(String username) throws DAOException {
        List<String[]> psychologistTable = CsvUtilities.readAll(fd);
        if (!psychologistTable.isEmpty()) psychologistTable.removeFirst(); // Rimuovi header

        for (String[] record : psychologistTable) {
            if (record.length > PsychologistDaoCsvConstants.PSYCHOLOGIST_INDEX_USERNAME && username.equals(record[PsychologistDaoCsvConstants.PSYCHOLOGIST_INDEX_USERNAME])) {
                return record;
            }
        }
        return null;
    }
}