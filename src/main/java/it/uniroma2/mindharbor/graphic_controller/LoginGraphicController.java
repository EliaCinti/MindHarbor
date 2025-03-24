package it.uniroma2.mindharbor.graphic_controller;

import it.uniroma2.mindharbor.app_controller.LoginController;
import it.uniroma2.mindharbor.beans.CredentialsBean;
import it.uniroma2.mindharbor.exception.DAOException;
import it.uniroma2.mindharbor.exception.UserSessionException;
import it.uniroma2.mindharbor.model.Patient;
import it.uniroma2.mindharbor.model.Psychologist;
import it.uniroma2.mindharbor.model.User;
import it.uniroma2.mindharbor.utilities.LabelDuration;
import it.uniroma2.mindharbor.utilities.NavigatorSingleton;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * LoginGraphicController manages the graphical user interface for the login process.
 * It handles user interactions, validates credentials, and navigates to the appropriate home screen based on a user type.
 * <p>
 * This controller uses the {@link LoginController} for backend authentication and the {@link NavigatorSingleton} for navigating between scenes.
 */
public class LoginGraphicController {
    @FXML
    public Label backButton;
    @FXML
    private Label msgLbl;
    @FXML
    private TextField usernameTextField;
    @FXML
    private TextField passwordTextField;
    @FXML
    private Button loginButton;

    private final LoginController loginController = new LoginController();
    private final NavigatorSingleton navigatorSingleton = NavigatorSingleton.getInstance();
    private static final Logger logger = Logger.getLogger(LoginGraphicController.class.getName());

    /**
     * Initializes the controller after the FXML fields have been bound.
     * This method sets up initial text values and configurations.
     */
    public void initialize() {
        msgLbl.setText("Welcome to MindHarbor");
    }

    /**
     * Handles the login button click event.
     * Validates user input, authenticates credentials, and navigates to the respective user home screen.
     */
    @FXML
    private void onSignInClick() {
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            msgLbl.setText("Please enter your username and password");
            return;
        }
        try {
            CredentialsBean credentials = new CredentialsBean.Builder<>()
                    .username(username)
                    .password(password)
                    .build();

            User loggedUser = loginController.login(credentials);
            if (loggedUser == null) {
                new LabelDuration().duration(msgLbl, "Invalid credentials");
            } else {
                if (loggedUser instanceof Patient) {
                    homePatient();
                } else if (loggedUser instanceof Psychologist) {
                    homePsychologist();
                }
            }
        } catch (DAOException e) {
            logger.log(Level.SEVERE, e, () -> String.format("Error while logging in %s", username));
            new LabelDuration().duration(msgLbl, "Login failed");
        } catch (UserSessionException e) {
            logger.log(Level.INFO, e, () -> String.format("User %s already logged in", username));
            new LabelDuration().duration(msgLbl, "User already logged in");
        }
    }

    /**
     * Navigates to the start screen.
     */
    @FXML
    private void onBackButtonClicked() {
        try {
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.close();
            navigatorSingleton.gotoPage("/it/uniroma2/mindharbor/fxml/StartScreen.fxml");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Unable to load patient home", e);
        }
    }

    /**
     * Navigates to the patient's home screen.
     */
    private void homePatient() {
        try {
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.close();
            navigatorSingleton.gotoPage("/it/uniroma2/mindharbor/fxml/HomePatient.fxml");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Unable to load patient home", e);
        }
    }

    /**
     * Navigates to the psychologist's home screen.
     */
    private void homePsychologist() {
        try {
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.close();
            navigatorSingleton.gotoPage("/it/uniroma2/mindharbor/fxml/HomePsychologist.fxml");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Unable to load psychologist home", e);
        }
    }
}
