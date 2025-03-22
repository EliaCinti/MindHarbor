package it.uniroma2.mindharbor.graphic_controller;

import it.uniroma2.mindharbor.app_controller.SignUpController;
import it.uniroma2.mindharbor.beans.PsychologistBean;
import it.uniroma2.mindharbor.exception.DAOException;
import it.uniroma2.mindharbor.exception.UserSessionException;
import it.uniroma2.mindharbor.model.User;
import it.uniroma2.mindharbor.utilities.LabelDuration;
import it.uniroma2.mindharbor.utilities.NavigatorSingleton;
import it.uniroma2.mindharbor.utilities.SignUpDataSingleton;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * SignUpPsychologistGraphicController manages the graphical user interface for the psychologist registration process.
 * This controller collects specific information for psychologists such as office location and hourly consultation cost
 * and completes the registration process.
 * <p>
 * It uses {@link SignUpController} for registration logic and {@link NavigatorSingleton} for navigation between screens.
 * </p>
 */
public class SignUpPsychologistGraphicController {
    @FXML
    private Label msgLbl;
    @FXML
    private PasswordField officeTextField;
    @FXML
    private PasswordField hourlyCostTextField1;
    @FXML
    private Button signUpButton;
    @FXML
    private Label backButton;

    private final SignUpController signUpController = new SignUpController();
    private final NavigatorSingleton navigatorSingleton = NavigatorSingleton.getInstance();
    private static final Logger logger = Logger.getLogger(SignUpPsychologistGraphicController.class.getName());

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
        msgLbl.setText("Complete your psychologist profile");

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
     * Completes the psychologist registration process by creating a new account.
     */
    @FXML
    private void onSignUpClick() {
        String office = officeTextField.getText();
        String hourlyCostStr = hourlyCostTextField1.getText();

        if (office.isEmpty() || hourlyCostStr.isEmpty()) {
            new LabelDuration().duration(msgLbl, "Please fill in all fields");
            return;
        }

        // Validate hourly cost (must be a positive number)
        double hourlyCost;
        try {
            hourlyCost = Double.parseDouble(hourlyCostStr);
            if (hourlyCost <= 0) {
                new LabelDuration().duration(msgLbl, "Hourly cost must be a positive number");
                return;
            }
        } catch (NumberFormatException e) {
            new LabelDuration().duration(msgLbl, "Hourly cost must be a valid number");
            return;
        }

        try {
            // Create PsychologistBean object with all necessary data
            PsychologistBean psychologistBean = new PsychologistBean.Builder()
                    .username(username)
                    .password(password)
                    .name(name)
                    .surname(surname)
                    .gender(gender)
                    .type("PSYCHOLOGIST")
                    .office(office)
                    .hourlyCost(hourlyCost)
                    .build();

            // Register psychologist through application controller
            User registeredUser = signUpController.registerPsychologist(psychologistBean);

            if (registeredUser != null) {
                // Clear data from singleton after successful registration
                SignUpDataSingleton.getInstance().clearUserData();
                // Navigate to psychologist home after registration
                navigateToPsychologistHome();
            } else {
                new LabelDuration().duration(msgLbl, "Registration failed");
            }
        } catch (DAOException e) {
            logger.log(Level.SEVERE, "Error during psychologist registration", e);
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
     * Navigates to the psychologist home screen after successful registration.
     */
    private void navigateToPsychologistHome() {
        try {
            Stage stage = (Stage) signUpButton.getScene().getWindow();
            stage.close();
            navigatorSingleton.gotoPage("/it/uniroma2/mindharbor/fxml/HomePsychologist.fxml");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Unable to load psychologist home", e);
        }
    }
}