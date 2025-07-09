package it.uniroma2.mindharbor.graphic_controller;

import it.uniroma2.mindharbor.app_controller.HomePatientController;
import it.uniroma2.mindharbor.model.Patient;
import it.uniroma2.mindharbor.utilities.NavigatorSingleton;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Graphic controller for the patient's home page in MindHarbor.
 * <p>
 * This controller handles UI interactions and delegates business logic
 * to the {@link HomePatientController}. It follows the MVC pattern by
 * focusing solely on View concerns and delegating Model operations to
 * the appropriate application controller.
 * </p>
 */
public class HomePatientGraphicController {
    @FXML
    private Label labelNomePaziente;
    @FXML
    private Label bookAppointment;
    @FXML
    private Label appointmentList;
    @FXML
    private Label diary;
    @FXML
    private HBox logout;
    @FXML
    private Circle notificaDot; // Il pallino verde di notifica

    private final NavigatorSingleton navigatorSingleton = NavigatorSingleton.getInstance();
    private static final Logger logger = Logger.getLogger(HomePatientGraphicController.class.getName());
    private final HomePatientController homeController = new HomePatientController();

    /**
     * Initializes the controller after all FXML elements have been injected.
     * <p>
     * This method is automatically called after the FXML file has been loaded.
     * It delegates to the application controller to retrieve necessary data
     * and then updates the UI accordingly.
     * </p>
     */
    @FXML
    public void initialize() {
        Patient currentPatient = homeController.getCurrentPatient();

        if (currentPatient != null) {
            labelNomePaziente.setText(currentPatient.getName() + " " + currentPatient.getSurname());

            boolean hasAppointments = homeController.hasAppointments();
            updateNotificationIndicator(hasAppointments);
        } else {
            logger.log(Level.SEVERE, "Session error: no patient logged in");
            redirectToLogin();
        }
    }

    /**
     * Updates the notification indicator based on whether appointments exist.
     *
     * @param hasAppointments true if there are appointments, false otherwise
     */
    private void updateNotificationIndicator(boolean hasAppointments) {
        notificaDot.setVisible(hasAppointments);
    }

    /**
     * Handles click on the appointment booking button.
     */
    @FXML
    public void clickLabelPrenotazione() {
        try {
            Stage stage = (Stage) bookAppointment.getScene().getWindow();
            stage.close();
            navigatorSingleton.gotoPage("/it/uniroma2/mindharbor/fxml/BookAppointment.fxml");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Unable to load appointment booking page", e);
        }
    }

    /**
     * Handles click on the appointment list button.
     */
    @FXML
    public void clickLabelAppuntamenti() {
        try {
            // Prima aggiorna i dati del paziente dal database per assicurarti
            // di avere le informazioni più recenti sugli appuntamenti
            boolean b = homeController.refreshPatientData();
            if(!b) {
                // Error message
                logger.log(Level.SEVERE, "Unable to load appointment list page");
            }
            Stage stage = (Stage) appointmentList.getScene().getWindow();
            stage.close();
            navigatorSingleton.gotoPage("/it/uniroma2/mindharbor/fxml/AppointmentList.fxml");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Unable to load appointment list page", e);
        }
    }

    /**
     * Handles click on the diary button.
     */
    @FXML
    public void clickDiary() {
        try {
            Stage stage = (Stage) diary.getScene().getWindow();
            stage.close();
            navigatorSingleton.gotoPage("/it/uniroma2/mindharbor/fxml/PatientDiary.fxml");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Unable to load diary page", e);
        }
    }

    /**
     * Handles click on the logout button.
     */
    @FXML
    public void onLogoutClicked() {
        // Delega l'operazione di logout al controller applicativo
        homeController.logout();
        redirectToLogin();
    }

    /**
     * Redirects to the login screen.
     */
    private void redirectToLogin() {
        try {
            Stage stage = (Stage) logout.getScene().getWindow();
            stage.close();
            navigatorSingleton.gotoPage("/it/uniroma2/mindharbor/fxml/Login.fxml");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Unable to load login page", e);
        }
    }
}
