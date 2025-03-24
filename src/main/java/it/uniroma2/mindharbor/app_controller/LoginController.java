package it.uniroma2.mindharbor.app_controller;

import it.uniroma2.mindharbor.beans.CredentialsBean;
import it.uniroma2.mindharbor.dao.PatientDao;
import it.uniroma2.mindharbor.dao.PsychologistDao;
import it.uniroma2.mindharbor.dao.UserDao;
import it.uniroma2.mindharbor.exception.DAOException;
import it.uniroma2.mindharbor.exception.UserSessionException;
import it.uniroma2.mindharbor.model.Patient;
import it.uniroma2.mindharbor.model.Psychologist;
import it.uniroma2.mindharbor.model.User;
import it.uniroma2.mindharbor.patterns.facade.DaoFactoryFacade;
import it.uniroma2.mindharbor.session.SessionManager;
import it.uniroma2.mindharbor.utilities.UserType;

/**
 * LoginController manages the authentication process for users trying to log in to the MindHarbor application.
 * This class interacts with various data access objects (DAOs)
 * to validate user credentials and retrieve user information.
 */
public class LoginController extends AbstractController {

    /**
     * Attempts to log in a user using the provided credentials.
     * It determines the user type, validates the credentials, retrieves the corresponding user data,
     * and initiates a session for the user if authentication is successful.
     *
     * @param credentials The credentials provided by the user, containing username, password, and user type.
     * @return A User object representing the logged-in user, or null if authentication fails.
     * @throws DAOException         If there is an issue with data access, such as invalid user type or database errors.
     * @throws UserSessionException If the user is already logged in elsewhere, preventing a new session start.
     */
    public User login(CredentialsBean credentials) throws DAOException, UserSessionException {

        DaoFactoryFacade daoFactoryFacade = DaoFactoryFacade.getInstance();
        UserDao userDao = daoFactoryFacade.getUserDao();
        userDao.validateUser(credentials);

        if (credentials.getType() != null) {
            if (credentials.getType().equalsIgnoreCase(String.valueOf(UserType.PATIENT))) {
                // Handle login for a patient
                PatientDao patientDao = daoFactoryFacade.getPatientDao();
                Patient patient = patientDao.retrievePatient(credentials.getUsername());
                storeUserSession(patient);
                return patient;
            } else {
                // Handle login for a psychologist
                PsychologistDao psychologistDao = daoFactoryFacade.getPsychologistDao();
                Psychologist psychologist = psychologistDao.retrievePsychologist(credentials.getUsername());
                storeUserSession(psychologist);
                return psychologist;
            }
        }
        return null;
    }

    /**
     * Stores user session information upon successful login.
     * This method ensures that the user's session is registered in the system,
     * allowing for session management and tracking.
     *
     * @param user The user for whom the session is to be stored.
     * @throws UserSessionException If there is an error in starting a new session, typically if the user is already logged in.
     */
    @Override
    protected void storeUserSession(User user) throws UserSessionException {
        SessionManager sessionManager = SessionManager.getInstance();
        sessionManager.login(user);
    }
}
