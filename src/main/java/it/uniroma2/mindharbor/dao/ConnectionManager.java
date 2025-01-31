package it.uniroma2.mindharbor.dao;

import it.uniroma2.mindharbor.exception.DAOException;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionManager {

    private static Connection connection;

    private ConnectionManager() throws DAOException, IOException {
        try (InputStream input = ConnectionManager.class.getClassLoader().getResourceAsStream("config.properties")) {
            Properties properties = new Properties();
            if (input == null){
                throw new DAOException("Impossibile recuperare le credenziali del DB");
            }
            properties.load(input);

            // get delle proprietà
            String jdbcURL = properties.getProperty("jdbcURL");
            String jdbcUsername = properties.getProperty("jdbcUsername");
            String jdbcPassword = properties.getProperty("jdbcPassword");


            connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);

        } catch (SQLException e) {
            throw new DAOException(e.getMessage());
        }
    }

    public static Connection getConnection() {
        return connection;
    }
}

