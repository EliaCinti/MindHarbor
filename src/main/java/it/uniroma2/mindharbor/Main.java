package it.uniroma2.mindharbor;

import it.uniroma2.mindharbor.utilities.NavigatorSingleton;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;


/**
 * Main class serves as the entry point for the MindHarbor application.
 * It initializes and displays the primary user interface.
 * <p>
 * This class extends {@link javafx.application.Application} to create a JavaFX application.
 * It sets up the primary stage, utilizes {@link NavigatorSingleton} to manage navigation,
 * loads the login view from an FXML file, and sets the scene on the primary stage.
 */
public class Main extends Application {
    /**
     * Starts the primary stage of the application and sets the initial scene.
     *
     * @param primaryStage The primary stage for this application, onto which
     *                     the application scene can be set.
     *                     The primary stage is configured and
     *                     displayed by this method.
     * @throws IOException if there is an error loading the FXML file for the login scene.
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        NavigatorSingleton navigator = NavigatorSingleton.getInstance(primaryStage);
        navigator.gotoPage("/it/uniroma2/mindharbor/fxml/Login.fxml");
    }

    /**
     * The main method that launches the application.
     * This method is called when the application starts from the command line.
     *
     * @param args the command line arguments passed to the application.
     * It is not used within this application.
     */
    public static void main(String[] args) {
        launch(args);
    }
}