package it.uniroma2.mindharbor.dao;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Connection pool manager for database operations.
 * <p>
 * This class implements a connection pool using HikariCP, a high-performance JDBC connection pool.
 * It provides a singleton instance that manages database connections efficiently,
 * reducing overhead while maintaining robustness.
 * </p>
 */
public class ConnectionPoolManager {
    private static final Logger logger = Logger.getLogger(ConnectionPoolManager.class.getName());
    private static HikariDataSource dataSource;
    private static ConnectionPoolManager instance;

    /**
     * Private constructor that initializes the connection pool from configuration.
     *
     * @throws RuntimeException if there is an error initializing the connection pool
     */
    private ConnectionPoolManager() {
        try (InputStream input = ConnectionPoolManager.class.getClassLoader().getResourceAsStream("config.properties")) {
            Properties properties = new Properties();
            if (input == null) {
                throw new RuntimeException("Unable to find config.properties");
            }
            properties.load(input);

            // Configure HikariCP
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(properties.getProperty("jdbcURL"));
            config.setUsername(properties.getProperty("jdbcUsername"));
            config.setPassword(properties.getProperty("jdbcPassword"));

            // Connection pool settings
            config.setMaximumPoolSize(10); // Maximum number of connections in the pool
            config.setMinimumIdle(5);      // Minimum number of idle connections
            config.setIdleTimeout(30000);  // Max time a connection can be idle (30 seconds)
            config.setMaxLifetime(1800000); // Max lifetime of a connection (30 minutes)

            // Connection testing
            config.setConnectionTestQuery("SELECT 1");
            config.setValidationTimeout(5000); // Validation timeout (5 seconds)

            // Create the connection pool
            dataSource = new HikariDataSource(config);

        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error loading database configuration", e);
            throw new RuntimeException("Error initializing connection pool", e);
        }
    }

    /**
     * Gets the singleton instance of the ConnectionPoolManager.
     *
     * @return the singleton instance
     */
    public static synchronized ConnectionPoolManager getInstance() {
        if (instance == null) {
            instance = new ConnectionPoolManager();
        }
        return instance;
    }

    /**
     * Gets a connection from the pool.
     * The connection should be closed when no longer needed to return it to the pool.
     *
     * @return a database connection from the pool
     * @throws SQLException if a connection cannot be obtained
     */
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    /**
     * Closes the connection pool.
     * This should be called when the application is shut down.
     */
    public void closePool() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }
}