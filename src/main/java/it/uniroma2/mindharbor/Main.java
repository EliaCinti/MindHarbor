package it.uniroma2.mindharbor;

import it.uniroma2.mindharbor.patterns.facade.DaoFactoryFacade;
import it.uniroma2.mindharbor.patterns.facade.PersistenceType;
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
     * Main method to configure the application settings based on command-line arguments and launch the application.
     * This method determines the persistence type and interface type based on the arguments provided.
     *
     * @param args Command-line arguments to configure the application:
     *             args[0]: Optional.
     *             Specify the persistence type.
     *             Default is "mysql".
     *             Args[1]: Optional.
     *             Specify the interface type.
     *             Default is "gui".
     */
    public static void main(String[] args) {
        DaoFactoryFacade daoFactoryFacade = DaoFactoryFacade.getInstance();

        String persistenceType = args.length > 0 ? args[0].toLowerCase() : "mysql";
        String interfaceType = args.length > 1 ? args[1].toLowerCase() : "gui";

        if ("mysql".equals(persistenceType)) {
            daoFactoryFacade.setPersistenceType(PersistenceType.MYSQL);
        } else {
            daoFactoryFacade.setPersistenceType(PersistenceType.CSV);
        }

        if ("gui".equals(interfaceType)) {
            launch(args);
        } else {
            // @TODO Placeholder for CLI logic
        }
    }
}