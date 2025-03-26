package it.uniroma2.mindharbor.app_controller;

import it.uniroma2.mindharbor.dao.PatientDao;
import it.uniroma2.mindharbor.exception.DAOException;
import it.uniroma2.mindharbor.model.Patient;
import it.uniroma2.mindharbor.patterns.facade.DaoFactoryFacade;
import it.uniroma2.mindharbor.session.SessionManager;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Application controller for the patient's home screen.
 * <p>
 * This controller handles business logic related to the patient's home screen,
 * including retrieving patient information and appointment data. It follows the MVC
 * pattern by separating business logic from UI concerns.
 * </p>
 */
public class HomePatientController {
    private static final Logger logger = Logger.getLogger(HomePatientController.class.getName());

    /**
     * Retrieves the current patient from the session.
     *
     * @return The current patient or null if no patient is logged in.
     */
    public Patient getCurrentPatient() {
        SessionManager sessionManager = SessionManager.getInstance();
        if (sessionManager.isLoggedIn() && sessionManager.getCurrentUser() instanceof Patient) {
            return (Patient) sessionManager.getCurrentUser();
        }
        return null;
    }

    /**
     * Checks if the current patient has any appointments.
     *
     * @return true if the patient has at least one appointment, false otherwise.
     */
    public boolean hasAppointments() {
        Patient patient = getCurrentPatient();
        return patient != null &&
                patient.getAppointmentList() != null &&
                !patient.getAppointmentList().isEmpty();
    }

    /**
     * Gets the number of appointments for the current patient.
     *
     * @return The number of appointments or 0 if there are none.
     */
    public int getAppointmentCount() {
        Patient patient = getCurrentPatient();
        if (patient != null && patient.getAppointmentList() != null) {
            return patient.getAppointmentList().size();
        }
        return 0;
    }

    /**
     * Refreshes the patient's data from the database, including appointments.
     * <p>
     * This method is useful when the patient's appointment list might have been
     * updated by other parts of the application or other users.
     * </p>
     *
     * @return true if the refresh was successful, false otherwise.
     */
    public boolean refreshPatientData() {
        try {
            Patient currentPatient = getCurrentPatient();
            if (currentPatient != null) {
                DaoFactoryFacade daoFactoryFacade = DaoFactoryFacade.getInstance();
                PatientDao patientDao = daoFactoryFacade.getPatientDao();

                // Retrieve fresh patient data from the database
                Patient refreshedPatient = patientDao.retrievePatient(currentPatient.getUsername());

                // Update the session with refreshed data
                SessionManager sessionManager = SessionManager.getInstance();
                sessionManager.logout(); // Clear current session
                sessionManager.login(refreshedPatient); // Set refreshed patient

                return true;
            }
            return false;
        } catch (DAOException e) {
            logger.log(Level.SEVERE, "Error refreshing patient data", e);
            return false;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error refreshing patient data", e);
            return false;
        }
    }

    /**
     * Performs logout action by clearing the current session.
     */
    public void logout() {
        SessionManager.getInstance().logout();
    }
}
