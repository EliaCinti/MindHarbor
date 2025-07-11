package it.uniroma2.mindharbor.graphic_controller;

import it.uniroma2.mindharbor.app_controller.SignUpController;
import it.uniroma2.mindharbor.exception.DAOException;
import it.uniroma2.mindharbor.utilities.LabelDuration;
import it.uniroma2.mindharbor.utilities.NavigatorSingleton;
import it.uniroma2.mindharbor.utilities.SignUpDataSingleton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * SignUpGraphicController manages the graphical user interface for the initial registration process.
 * This controller collects general user information such as name, surname, username, password, gender,
 * and user type (patient or psychologist) and directs the user to the specific registration form based on the chosen type.
 * <p>
 * It uses {@link NavigatorSingleton} for navigation between different screens of the application.
 * </p>
 */
public class SignUpGraphicController {
    @FXML
    private Label msgLbl;
    @FXML
    private TextField usernameTextField;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField surnameTextField;
    @FXML
    private PasswordField passwordTextField;
    @FXML
    private ComboBox<String> gender;
    @FXML
    private ComboBox<String> type;
    @FXML
    private Button loginButton;
    @FXML
    private Label backButton;

    private final SignUpController signUpController = new SignUpController();
    private final NavigatorSingleton navigatorSingleton = NavigatorSingleton.getInstance();
    private static final Logger logger = Logger.getLogger(SignUpGraphicController.class.getName());

    /**
     * Initializes the controller after the FXML fields have been bound.
     * This method sets up initial values and configurations for comboboxes and other UI elements.
     */
    @FXML
    public void initialize() {
        msgLbl.setText("Create your MindHarbor account");

        // Initialize gender ComboBox
        ObservableList<String> genderOptions = FXCollections.observableArrayList(
                "Male", "Female", "Other"
        );
        gender.setItems(genderOptions);
        gender.setPromptText("Gender");

        // Initialize user type ComboBox
        ObservableList<String> typeOptions = FXCollections.observableArrayList(
                "Patient", "Psychologist"
        );
        type.setItems(typeOptions);
        type.setPromptText("User Type");
    }

    /**
     * Handles the sign-up button click event.
     * Validates the input data and navigates to the appropriate form based on the selected user type.
     */
    @FXML
    private void onSignUpClick() {
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();
        String name = nameTextField.getText();
        String surname = surnameTextField.getText();
        String selectedGender = gender.getValue();
        String selectedType = type.getValue();

        // Check if all fields are filled
        if (username.isEmpty() || password.isEmpty() || name.isEmpty() || surname.isEmpty() ||
                selectedGender == null || selectedType == null) {
            new LabelDuration().duration(msgLbl, "Please fill in all fields");
            return;
        }

        if (!signUpController.isValidPassword(password)) {
            new LabelDuration().duration(msgLbl, "Password must be at least 8 characters with uppercase and number");
            return;
        }

        try {
            if (!signUpController.isUsernameAvailable(username)) {
                new LabelDuration().duration(msgLbl, "Username already in use, please choose another one");
                return;
            }
            // Save data in singleton to make it available to subsequent screens
            SignUpDataSingleton.getInstance().setUserData(
                    username, password, name, surname, selectedGender, selectedType);

            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.close();
            // Navigate to the specific form based on user type
            if ("PATIENT".equalsIgnoreCase(selectedType)) {

                navigatorSingleton.gotoPage("/it/uniroma2/mindharbor/fxml/SignUpPatient.fxml");
            } else if ("PSYCHOLOGIST".equalsIgnoreCase(selectedType)) {
                navigatorSingleton.gotoPage("/it/uniroma2/mindharbor/fxml/SignUpPsychologist.fxml");
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Unable to navigate to specific signup form", e);
            new LabelDuration().duration(msgLbl, "Error in navigation");
        } catch (DAOException e) {
            logger.log(Level.SEVERE, "Error verifying username", e);
            new LabelDuration().duration(msgLbl, "Error verifying username");
        }
    }

    /**
     * Handles the back button click to return to the start screen.
     */
    @FXML
    private void onBackButtonClicked() {
        try {
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.close();
            navigatorSingleton.gotoPage("/it/uniroma2/mindharbor/fxml/StartScreen.fxml");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Unable to navigate back to start screen", e);
        }
    }
}
