package it.uniroma2.mindharbor.dao.mysql.constants;

/**
 * Constants used by UserDaoMySql for managing database operations.
 * <p>
 * This class centralizes all column names and error message strings to improve
 * code maintainability and reduce duplication across the application.
 * </p>
 */
public class UserDaoMySqlConstants {
    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private UserDaoMySqlConstants() {
        // Prevent instantiation
    }

    // Database column names
    public static final String COLUMN_USERNAME = "Username";
    public static final String COLUMN_PASSWORD = "Password";
    public static final String COLUMN_FIRSTNAME = "Firstname";
    public static final String COLUMN_LASTNAME = "Lastname";
    public static final String COLUMN_TYPE = "Type";
    public static final String COLUMN_GENDER = "Gender";

    // Error messages
    public static final String USER_NOT_FOUND = "User not found: ";
    public static final String ERROR_SAVING_USER = "Error saving user: ";
    public static final String ERROR_RETRIEVING_USER = "Error retrieving user: ";
    public static final String ERROR_UPDATING_USER = "Error updating user: ";
    public static final String ERROR_DELETING_USER = "Error deleting user: ";
    public static final String ERROR_VALIDATING_USER = "Error validating user: ";
    public static final String FAILED_TO_SAVE_USER = "Failed to save user, no rows affected.";
    public static final String USERNAME_ALREADY_EXISTS = "Username already exists: ";
    public static final String ERROR_CHECKING_USERNAME = "Error checking if username exists: ";
}
