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

public class LoginController extends AbstractController {

    /**
     * Effettua il login dell'utente.
     *
     * @param credentials Le credenziali fornite dall'utente.
     * @return L'oggetto User con informazioni complete se il login ha successo; altrimenti, null.
     * @throws DAOException            Se si verifica un errore durante l'accesso ai dati.
     * @throws UserSessionException Se l'utente risulta già loggato.
     */
    public User login(CredentialsBean credentials) throws DAOException, UserSessionException {

        UserType userType;
        try {
            userType = UserType.valueOf(credentials.getType().toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new DAOException("Invalid user type provided: " + credentials.getType(), e);
        }

        DaoFactoryFacade daoFactoryFacade = DaoFactoryFacade.getInstance();
        UserDao userDao = daoFactoryFacade.getUserDao();
        userDao.validateUser(credentials);

        if(credentials.getType() != null) {
            if(userType == UserType.PATIENT) {
                // The user is a patient
                PatientDao patientDao = daoFactoryFacade.getPatientDao();
                Patient patient = patientDao.retrievePatient(credentials.getUsername());
                storeUserSession(patient);
                return patient;
            }else {
                // The user is a psychologist
                PsychologistDao psychologistDao = daoFactoryFacade.getPsychologistDao();
                Psychologist psychologist = psychologistDao.retrievePsychologist(credentials.getUsername());
                storeUserSession(psychologist);
                return psychologist;
            }
        }
        return null;
    }

    @Override
    protected void storeUserSession(User user) throws UserSessionException {
        SessionManager sessionManager = SessionManager.getInstance();
        sessionManager.login(user);
    }
}
