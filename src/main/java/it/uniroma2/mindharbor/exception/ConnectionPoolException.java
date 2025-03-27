package it.uniroma2.mindharbor.exception;

/**
 * Exception thrown for errors related to the database connection pool.
 * This exception indicates problems with creating, using, or managing the connection pool.
 */
public class ConnectionPoolException extends Exception {

    /**
     * Constructs a new ConnectionPoolException with null as its detail message.
     */
    public ConnectionPoolException() {
        super();
    }

    /**
     * Constructs a new ConnectionPoolException with the specified detail message.
     *
     * @param message the detail message
     */
    public ConnectionPoolException(String message) {
        super(message);
    }

    /**
     * Constructs a new ConnectionPoolException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause the cause
     */
    public ConnectionPoolException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new ConnectionPoolException with the specified cause.
     *
     * @param cause the cause
     */
    public ConnectionPoolException(Throwable cause) {
        super(cause);
    }
}
