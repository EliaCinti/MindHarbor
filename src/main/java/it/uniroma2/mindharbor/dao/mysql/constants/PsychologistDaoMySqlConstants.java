package it.uniroma2.mindharbor.dao.mysql.constants;

/**
 * Constants used by PsychologistDaoMySql for managing database operations.
 * <p>
 * This class centralizes all column names and error message strings to improve
 * code maintainability and reduce duplication across the application.
 * </p>
 */
public class PsychologistDaoMySqlConstants {
    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private PsychologistDaoMySqlConstants() {
        // Prevent instantiation
    }

    // Database column names
    public static final String COLUMN_USERNAME = "Username";
    public static final String COLUMN_FIRSTNAME = "Firstname";
    public static final String COLUMN_LASTNAME = "Lastname";
    public static final String COLUMN_GENDER = "Gender";
    public static final String COLUMN_OFFICE = "Office";
    public static final String COLUMN_HOURLY_COST = "HourlyCost";

    // Error messages
    public static final String PSYCHOLOGIST_NOT_FOUND = "Psychologist not found: ";
    public static final String ERROR_SAVING_PSYCHOLOGIST = "Error saving psychologist: ";
    public static final String ERROR_RETRIEVING_PSYCHOLOGIST = "Error retrieving psychologist: ";
    public static final String ERROR_UPDATING_PSYCHOLOGIST = "Error updating psychologist: ";
    public static final String ERROR_DELETING_PSYCHOLOGIST = "Error deleting psychologist: ";
    public static final String FAILED_TO_SAVE_PSYCHOLOGIST = "Failed to save psychologist, no rows affected.";
    public static final String ERROR_RETRIEVING_PATIENTS = "Error retrieving patients for psychologist: ";
    public static final String ERROR_VALIDATING_PSYCHOLOGIST_DATA = "Invalid psychologist data: ";
}
