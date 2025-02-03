package it.uniroma2.mindharbor.dao.csv;

import it.uniroma2.mindharbor.beans.PatientBean;
import it.uniroma2.mindharbor.beans.PsychologistBean;
import it.uniroma2.mindharbor.beans.UserBean;
import it.uniroma2.mindharbor.dao.PsychologistDao;
import it.uniroma2.mindharbor.dao.UserDao;
import it.uniroma2.mindharbor.dao.csv.constants.PsychologistDaoCsvConstants;
import it.uniroma2.mindharbor.dao.csv.constants.UserDaoCsvConstants;
import it.uniroma2.mindharbor.exception.DAOException;
import it.uniroma2.mindharbor.model.Psychologist;
import it.uniroma2.mindharbor.patterns.facade.DaoFactoryFacade;
import it.uniroma2.mindharbor.utilities.CsvUtilities;

import java.io.File;
import java.util.List;

/**
 * CSV file implementation of the {@link PsychologistDao} for managing psychologist data.
 * This class provides the necessary operations to manage psychologists' records in a CSV format,
 * integrating with a CSV utility class for file operations.
 */
public class PsychologistDaoCsv implements PsychologistDao {

    private static final File fd = new File(PsychologistDaoCsvConstants.PATH_NAME_PSYCHOLOGIST);

    /**
     * Saves a new psychologist record to a CSV file.
     *
     * @param psychologist The psychologist data to save.
     * @throws DAOException if there is an error during the write operation.
     */
    @Override
    public void savePsychologist(PsychologistBean psychologist) throws DAOException {
        UserDao userDao = DaoFactoryFacade.getInstance().getUserDao();
        userDao.saveUser(psychologist);
        String[] psychologistRecord = new String[PsychologistDaoCsvConstants.HEADER.length];
        psychologistRecord[PsychologistDaoCsvConstants.PSYCHOLOGIST_INDEX_USERNAME] = psychologist.getUsername();
        psychologistRecord[PsychologistDaoCsvConstants.PSYCHOLOGIST_INDEX_OFFICE] = psychologist.getOffice();
        psychologistRecord[PsychologistDaoCsvConstants.PSYCHOLOGIST_INDEX_HOURLY_COST] = String.valueOf(psychologist.getHourlyCost());
        CsvUtilities.writeFile(fd, psychologistRecord);
    }

    /**
     * Retrieves a psychologist record from a CSV file based on username.
     *
     * @param username The username of the psychologist to retrieve.
     * @return A {@link Psychologist} object containing the psychologist's details.
     * @throws DAOException if the psychologist cannot be found or an error occurs during file reading.
     */
    @Override
    public Psychologist retrievePsychologist(String username) throws DAOException {
        UserDao userDao = DaoFactoryFacade.getInstance().getUserDao();
        String[] userInfo = userDao.retrieveUser(username);
        String[] psychologistInfo = retrievePsychologistRecord(username);
        return new Psychologist(
                userInfo[UserDaoCsvConstants.USER_INDEX_USERNAME],
                userInfo[UserDaoCsvConstants.USER_INDEX_FIRST_NAME],
                userInfo[UserDaoCsvConstants.USER_INDEX_LAST_NAME],
                userInfo[UserDaoCsvConstants.USER_INDEX_GENDER],
                psychologistInfo[PsychologistDaoCsvConstants.PSYCHOLOGIST_INDEX_OFFICE],
                psychologistInfo[PsychologistDaoCsvConstants.PSYCHOLOGIST_INDEX_HOURLY_COST]
        );
    }

    /**
     * Updates an existing psychologist record in a CSV file.
     *
     * @param psychologist The updated psychologist data.
     * @param bean         The user bean containing updated user data.
     * @throws DAOException if the psychologist does not exist or an error occurs during the update.
     */
    @Override
    public void updatePsychologist(Psychologist psychologist, UserBean bean) throws DAOException {
        UserDao userDao = DaoFactoryFacade.getInstance().getUserDao();
        userDao.updateUser(bean);
        List<String[]> psychologistTable = CsvUtilities.readAll(fd);
        boolean found = false;
        for (String[] record : psychologistTable) {
            if (record[PsychologistDaoCsvConstants.PSYCHOLOGIST_INDEX_USERNAME].equals(psychologist.getUsername())) {
                record[PsychologistDaoCsvConstants.PSYCHOLOGIST_INDEX_OFFICE] = psychologist.getOffice();
                record[PsychologistDaoCsvConstants.PSYCHOLOGIST_INDEX_HOURLY_COST] = String.valueOf(psychologist.getHourlyCost());
                found = true;
                break;
            }
        }
        if (!found) {
            throw new DAOException(PsychologistDaoCsvConstants.PSYCHOLOGIST_NOT_FOUND + psychologist.getUsername());
        }
        CsvUtilities.updateFile(fd, PsychologistDaoCsvConstants.HEADER, psychologistTable);
    }

    /**
     * Deletes a psychologist record from a CSV file based on username.
     *
     * @param username The username of the psychologist to delete.
     * @throws DAOException if the psychologist cannot be found or an error occurs during the deletion.
     */
    @Override
    public void deletePsychologist(String username) throws DAOException {
        UserDao userDao = DaoFactoryFacade.getInstance().getUserDao();
        userDao.deleteUser(username);
        List<String[]> psychologistTable = CsvUtilities.readAll(fd);
        boolean removed = psychologistTable.removeIf(record -> record[PsychologistDaoCsvConstants.PSYCHOLOGIST_INDEX_USERNAME].equals(username));
        if (!removed) {
            throw new DAOException(PsychologistDaoCsvConstants.PSYCHOLOGIST_NOT_FOUND + username);
        }
        CsvUtilities.updateFile(fd, PsychologistDaoCsvConstants.HEADER, psychologistTable);
    }

    /**
     * Retrieves all patients assigned to a specific psychologist by username.
     * This method is not yet implemented.
     *
     * @param psychologistUsername The username of the psychologist whose patients to retrieve.
     * @return A list of {@link PatientBean} objects.
     * @throws DAOException if the operation cannot be performed.
     */
    @Override
    public List<PatientBean> getPatients(String psychologistUsername) throws DAOException {
        // DA IMPLEMENTARE
        return List.of();
    }

    /**
     * Retrieves a specific psychologist record from the CSV file.
     *
     * @param username The username of the psychologist to find.
     * @return An array of strings representing the psychologist record.
     * @throws DAOException if the psychologist cannot be found.
     */
    private String[] retrievePsychologistRecord(String username) throws DAOException {
        List<String[]> psychologistTable = CsvUtilities.readAll(fd);
        String[] psychologistRecord = null;
        while (!psychologistTable.isEmpty()) {
            psychologistRecord = psychologistTable.removeFirst();
            if (username.equals(psychologistRecord[PsychologistDaoCsvConstants.PSYCHOLOGIST_INDEX_USERNAME])) {
                return psychologistRecord;
            }
        }
        return psychologistRecord;
    }
}
