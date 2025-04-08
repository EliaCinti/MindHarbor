package it.uniroma2.mindharbor;

import it.uniroma2.mindharbor.dao.ConnectionFactory;
import it.uniroma2.mindharbor.patterns.facade.DaoFactoryFacade;
import it.uniroma2.mindharbor.patterns.facade.PersistenceType;
import it.uniroma2.mindharbor.utilities.NavigatorSingleton;
import javafx.application.Application;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Main class serves as the entry point for the MindHarbor application.
 * It initializes and displays the primary user interface.
 * <p>
 * This class extends {@link javafx.application.Application} to create a JavaFX application.
 * It sets up the primary stage, utilizes {@link NavigatorSingleton} to manage navigation,
 * loads the login view from an FXML file, and sets the scene on the primary stage.
 */
public class Main extends Application {
    private static final Logger logger = Logger.getLogger(Main.class.getName());

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
        navigator.gotoPage("/it/uniroma2/mindharbor/fxml/StartScreen.fxml");
    }

    /**
     * Called when the application is stopping.
     * This method ensures proper cleanup of resources, including closing the database connection if MySQL is being used.
     */
    @Override
    public void stop() throws Exception {
        if (DaoFactoryFacade.getInstance().getPersistenceType() == PersistenceType.MYSQL) {
            try {
                ConnectionFactory.closeConnection();
                logger.info("Database connection closed successfully");
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Error closing database connection", e);
            }
        }

        logger.info("Application shutdown complete");
        super.stop();
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

        logger.info("Starting MindHarbor with persistence: " + persistenceType + ", interface: " + interfaceType);

        if ("mysql".equals(persistenceType)) {
            daoFactoryFacade.setPersistenceType(PersistenceType.MYSQL);
            try {
                boolean connectionOk = ConnectionFactory.testConnection();
                if (!connectionOk) {
                    logger.warning("Database connection test failed. Switching to CSV persistence.");
                    daoFactoryFacade.setPersistenceType(PersistenceType.CSV);
                } else {
                    logger.info("Database connection test successful");
                }
            } catch (Exception e) {
                logger.log(Level.WARNING, "Error testing database connection: " + e.getMessage());
                logger.info("Switching to CSV persistence due to connection error");
                daoFactoryFacade.setPersistenceType(PersistenceType.CSV);
            }
        } else {
            logger.info("Using CSV persistence as specified");
            daoFactoryFacade.setPersistenceType(PersistenceType.CSV);
        }

        if ("gui".equals(interfaceType)) {
            logger.info("Launching GUI interface");
            launch(args);
        } else {
            logger.info("Command-line interface requested, but not yet implemented");
            // @TODO Placeholder for CLI logic
        }
    }
}