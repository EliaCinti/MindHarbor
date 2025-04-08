package it.uniroma2.mindharbor.dao.mysql.constants;

/**
 * Constants used by PatientDaoMySql for managing database operations.
 * <p>
 * This class centralizes all column names and error message strings to improve
 * code maintainability and reduce duplication across the application.
 * </p>
 */
public class PatientDaoMySqlConstants {
    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private PatientDaoMySqlConstants() {
        // Prevent instantiation
    }

    // Database column names
    public static final String COLUMN_USERNAME = "Username";
    public static final String COLUMN_FIRSTNAME = "Firstname";
    public static final String COLUMN_LASTNAME = "Lastname";
    public static final String COLUMN_GENDER = "Gender";
    public static final String COLUMN_PSYCHOLOGIST = "Psychologist";
    public static final String COLUMN_BIRTH_DATE = "BirthDate";

    // Error messages
    public static final String PATIENT_NOT_FOUND = "Patient not found: ";
    public static final String USERNAME_CANNOT_BE_NULL_OR_EMPTY = "Username cannot be null or empty";
    public static final String PATIENT_BEAN_CANNOT_BE_NULL = "Patient bean cannot be null";
    public static final String PATIENT_CANNOT_BE_NULL = "Patient cannot be null";
    public static final String USER_BEAN_CANNOT_BE_NULL = "User bean cannot be null";
    public static final String PSYCHOLOGIST_CANNOT_BE_NULL = "Psychologist cannot be null";
    public static final String BIRTH_DATE_CANNOT_BE_NULL = "Birth date cannot be null";
    public static final String ERROR_SAVING_PATIENT = "Error saving patient: ";
    public static final String ERROR_RETRIEVING_PATIENT = "Error retrieving patient: ";
    public static final String ERROR_UPDATING_PATIENT = "Error updating patient: ";
    public static final String ERROR_DELETING_PATIENT = "Error deleting patient: ";
    public static final String FAILED_TO_SAVE_PATIENT = "Failed to save patient, no rows affected.";
    public static final String ERROR_RETRIEVING_PATIENTS_BY_PSYCHOLOGIST = "Error retrieving patients by psychologist: ";
    public static final String ERROR_VALIDATING_PATIENT_DATA = "Invalid patient data: ";
}
