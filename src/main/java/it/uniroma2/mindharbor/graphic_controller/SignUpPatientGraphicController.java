package it.uniroma2.mindharbor.graphic_controller;

import it.uniroma2.mindharbor.app_controller.SignUpController;
import it.uniroma2.mindharbor.beans.PatientBean;
import it.uniroma2.mindharbor.exception.DAOException;
import it.uniroma2.mindharbor.exception.UserSessionException;
import it.uniroma2.mindharbor.model.User;
import it.uniroma2.mindharbor.utilities.LabelDuration;
import it.uniroma2.mindharbor.utilities.NavigatorSingleton;
import it.uniroma2.mindharbor.utilities.SignUpDataSingleton;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * SignUpPatientGraphicController manages the graphical user interface for the patient registration process.
 * This controller collects specific information for patients such as birthdate and completes the registration process.
 * <p>
 * It uses {@link SignUpController} for registration logic and {@link NavigatorSingleton} for navigation between screens.
 * </p>
 */
public class SignUpPatientGraphicController {
    @FXML
    private Label msgLbl;
    @FXML
    private DatePicker datePicker;
    @FXML
    private Button signUpButton;
    @FXML
    private Label backButton;

    private final SignUpController signUpController = new SignUpController();
    private final NavigatorSingleton navigatorSingleton = NavigatorSingleton.getInstance();
    private static final Logger logger = Logger.getLogger(SignUpPatientGraphicController.class.getName());

    // Fields to store user data
    private String username;
    private String password;
    private String name;
    private String surname;
    private String gender;

    /**
     * Initializes the controller after the FXML fields have been bound.
     * This method sets up initial values and configurations for the screen and retrieves previously entered user data.
     */
    @FXML
    public void initialize() {
        msgLbl.setText("Complete your patient profile");

        // Set minimum date (e.g., don't allow future dates or too old dates)
        datePicker.setValue(LocalDate.now().minusYears(18)); // Default to 18 years old

        // Retrieve data from singleton
        SignUpDataSingleton data = SignUpDataSingleton.getInstance();
        username = data.getUsername();
        password = data.getPassword();
        name = data.getName();
        surname = data.getSurname();
        gender = data.getGender();
    }

    /**
     * Handles the sign-up button click event.
     * Completes the patient registration process by creating a new account.
     */
    @FXML
    private void onSignUpClick() {
        LocalDate birthDate = datePicker.getValue();

        if (birthDate == null) {
            new LabelDuration().duration(msgLbl, "Please select your birth date");
            return;
        }

        // Check that birthdate is not in the future
        if (birthDate.isAfter(LocalDate.now())) {
            new LabelDuration().duration(msgLbl, "Birth date cannot be in the future");
            return;
        }

        try {
            // Create PatientBean object with all necessary data
            PatientBean patientBean = new PatientBean.Builder()
                    .username(username)
                    .password(password)
                    .name(name)
                    .surname(surname)
                    .gender(gender)
                    .type("PATIENT")
                    .birthDate(birthDate)
                    .build();

            // Register patient through application controller
            User registeredUser = signUpController.registerPatient(patientBean);
            if (registeredUser != null) {
                // Clear data from singleton after successful registration
                SignUpDataSingleton.getInstance().clearUserData();
                // Navigate to patient home after registration
                navigateToPatientHome();
            } else {
                new LabelDuration().duration(msgLbl, "Registration failed");
            }
        } catch (DAOException e) {
            logger.log(Level.SEVERE, "Error during patient registration", e);
            if (e.getMessage().contains("already exists")) {
                new LabelDuration().duration(msgLbl, "Username already exists");
            } else {
                new LabelDuration().duration(msgLbl, "Registration error: " + e.getMessage());
            }
        } catch (UserSessionException e) {
            logger.log(Level.SEVERE, "Error creating user session", e);
            new LabelDuration().duration(msgLbl, "Session error: " + e.getMessage());
        }
    }

    /**
     * Handles the back button click to return to the main registration form.
     */
    @FXML
    private void onBackButtonClicked() {
        try {
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.close();
            navigatorSingleton.gotoPage("/it/uniroma2/mindharbor/fxml/SignUp.fxml");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Unable to navigate back to signup page", e);
        }
    }

    /**
     * Navigates to the patient home screen after successful registration.
     */
    private void navigateToPatientHome() {
        try {
            Stage stage = (Stage) signUpButton.getScene().getWindow();
            stage.close();
            navigatorSingleton.gotoPage("/it/uniroma2/mindharbor/fxml/HomePatient.fxml");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Unable to load patient home", e);
        }
    }
}