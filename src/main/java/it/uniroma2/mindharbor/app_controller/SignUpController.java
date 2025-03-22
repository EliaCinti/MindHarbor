package it.uniroma2.mindharbor.app_controller;

import it.uniroma2.mindharbor.beans.PatientBean;
import it.uniroma2.mindharbor.beans.PsychologistBean;
import it.uniroma2.mindharbor.dao.PatientDao;
import it.uniroma2.mindharbor.dao.PsychologistDao;
import it.uniroma2.mindharbor.dao.UserDao;
import it.uniroma2.mindharbor.exception.DAOException;
import it.uniroma2.mindharbor.exception.UserSessionException;
import it.uniroma2.mindharbor.model.User;
import it.uniroma2.mindharbor.patterns.facade.DaoFactoryFacade;
import it.uniroma2.mindharbor.session.SessionManager;

/**
 * SignUpController manages the registration process for new users in the MindHarbor application.
 * This class handles the creation of both patient and psychologist accounts by interacting with
 * the appropriate data access objects.
 */
public class SignUpController extends AbstractController {

    /**
     * Registers a new patient in the system.
     *
     * @param patientBean The bean containing the patient's information.
     * @return The newly created patient User object, or null if registration fails.
     * @throws DAOException If there is an issue with data access during registration.
     * @throws UserSessionException If there is an issue with creating a user session.
     */
    public User registerPatient(PatientBean patientBean) throws DAOException, UserSessionException {
        DaoFactoryFacade daoFactoryFacade = DaoFactoryFacade.getInstance();
        PatientDao patientDao = daoFactoryFacade.getPatientDao();

        patientDao.savePatient(patientBean);
        // After successful registration, we retrieve the patient to return it
        User patient = patientDao.retrievePatient(patientBean.getUsername());
        storeUserSession(patient);
        return patient;

    }

    /**
     * Registers a new psychologist in the system.
     *
     * @param psychologistBean The bean containing the psychologist's information.
     * @return The newly created psychologist User object, or null if registration fails.
     * @throws DAOException If there is an issue with data access during registration.
     * @throws UserSessionException If there is an issue with creating a user session.
     */
    public User registerPsychologist(PsychologistBean psychologistBean) throws DAOException, UserSessionException {
        DaoFactoryFacade daoFactoryFacade = DaoFactoryFacade.getInstance();
        PsychologistDao psychologistDao = daoFactoryFacade.getPsychologistDao();

        psychologistDao.savePsychologist(psychologistBean);
        // After successful registration, we retrieve the psychologist to return it
        User psychologist = psychologistDao.retrievePsychologist(psychologistBean.getUsername());
        storeUserSession(psychologist);
        return psychologist;
    }

    /**
     * Verifica se l'username è già presente nel sistema.
     *
     * @param username Lo username da verificare
     * @return true se lo username è disponibile, false se è già in uso
     * @throws DAOException Se si verifica un errore durante l'accesso ai dati
     */
    public boolean isUsernameAvailable(String username) throws DAOException {
        DaoFactoryFacade daoFactoryFacade = DaoFactoryFacade.getInstance();
        UserDao userDao = daoFactoryFacade.getUserDao();
        return userDao.retrieveUser(username) == null;
    }

    /**
     * Stores user session information upon successful registration.
     * This method ensures that the user's session is registered in the system,
     * allowing for immediate login after registration.
     *
     * @param user The user for whom the session is to be stored.
     * @throws UserSessionException If there is an error in starting a new session.
     */
    @Override
    protected void storeUserSession(User user) throws UserSessionException {
        SessionManager sessionManager = SessionManager.getInstance();
        sessionManager.login(user);
    }
}