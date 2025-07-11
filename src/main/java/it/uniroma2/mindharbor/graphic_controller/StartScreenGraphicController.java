package it.uniroma2.mindharbor.graphic_controller;

import it.uniroma2.mindharbor.utilities.NavigatorSingleton;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Graphic controller for the application's start screen.
 * <p>
 * This controller manages the initial welcome screen where users can choose
 * between signing in (for existing users) or signing up (for new user registration).
 * It serves as the entry point to the MindHarbor application's authentication flow.
 * </p>
 * <p>
 * The controller handles:
 * <ul>
 *   <li>Navigation to the login screen for existing users</li>
 *   <li>Navigation to the registration screen for new users</li>
 *   <li>Proper window management during navigation</li>
 * </ul>
 * </p>
 *
 * @see LoginGraphicController for user authentication
 * @see SignUpGraphicController for user registration
 * @see NavigatorSingleton for screen navigation management
 */
public class StartScreenGraphicController {

    private static final Logger logger = Logger.getLogger(StartScreenGraphicController.class.getName());
    private final NavigatorSingleton navigator = NavigatorSingleton.getInstance();

    @FXML
    private Button signInButton;
    @FXML
    private Button signUpButton;

    /**
     * Handles the sign-in button click event.
     * <p>
     * This method is triggered when the user clicks the "Sign In" button.
     * It navigates to the login screen where existing users can authenticate
     * with their credentials.
     * </p>
     */
    @FXML
    public void onSignInButtonClicked() {
        signIn();
    }

    /**
     * Handles the sign-up button click event.
     * <p>
     * This method is triggered when the user clicks the "Sign Up" button.
     * It navigates to the registration screen where new users can create
     * their MindHarbor accounts.
     * </p>
     */
    @FXML
    public void onSignUpButtonClicked() {
        signUp();
    }

    /**
     * Navigates to the sign-in (login) screen.
     * <p>
     * This method closes the current start screen window and opens the login
     * interface where users can enter their credentials to access the application.
     * If navigation fails, an error is logged for debugging purposes.
     * </p>
     */
    private void signIn() {
        try {
            Stage currentStage = (Stage) signInButton.getScene().getWindow();
            currentStage.close();
            navigator.gotoPage("/it/uniroma2/mindharbor/fxml/Login.fxml");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Unable to load sign-in page", e);
        }
    }

    /**
     * Navigates to the sign-up (registration) screen.
     * <p>
     * This method closes the current start screen window and opens the registration
     * interface where new users can create their accounts by providing necessary
     * information and choosing their user type (patient or psychologist).
     * If navigation fails, an error is logged for debugging purposes.
     * </p>
     */
    private void signUp() {
        try {
            Stage currentStage = (Stage) signUpButton.getScene().getWindow();
            currentStage.close();
            navigator.gotoPage("/it/uniroma2/mindharbor/fxml/SignUp.fxml");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Unable to load sign-up page", e);
        }
    }
}