package it.uniroma2.mindharbor.graphic_controller;

import it.uniroma2.mindharbor.utilities.NavigatorSingleton;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StartScreenGraphicController {

    private static final Logger logger = Logger.getLogger(StartScreenGraphicController.class.getName());
    private final NavigatorSingleton navigator = NavigatorSingleton.getInstance();

    @FXML
    private Button signInButton;
    @FXML
    private Button signUpButton;

    @FXML
    public void onSignInButtonClicked() {
        signIn();
    }

    @FXML
    public void onSignUpButtonClicked() {
        signUp();
    }

    private void signIn() {
        try {
            Stage loginstage = (Stage) signInButton.getScene().getWindow();
            loginstage.close();
            navigator.gotoPage("/it/uniroma2/mindharbor/fxml/Login.fxml");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Unable to load sign-in page", e);
        }
    }

    private void signUp() {
        try {
            Stage loginstage = (Stage) signUpButton.getScene().getWindow();
            loginstage.close();
            navigator.gotoPage("/it/uniroma2/mindharbor/fxml/SignUp.fxml");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Unable to load sign-up page", e);
        }
    }
}
