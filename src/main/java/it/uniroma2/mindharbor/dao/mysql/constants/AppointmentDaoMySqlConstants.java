package it.uniroma2.mindharbor.dao.mysql.constants;

/**
 * Constants used by AppointmentDaoMySql for managing database operations.
 * <p>
 * This class centralizes all column names and error message strings to improve
 * code maintainability and reduce duplication across the application.
 * </p>
 */
public class AppointmentDaoMySqlConstants {
    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private AppointmentDaoMySqlConstants() {
        // Prevent instantiation
    }

    // Database column names
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_NOTIFIED = "notified";

    // Error messages
    public static final String APPOINTMENT_NOT_FOUND = "Appointment not found: ";
    public static final String ERROR_SAVING_APPOINTMENT = "Error saving appointment: ";
    public static final String ERROR_RETRIEVING_APPOINTMENT = "Error retrieving appointment: ";
    public static final String ERROR_UPDATING_APPOINTMENT = "Error updating appointment: ";
    public static final String ERROR_DELETING_APPOINTMENT = "Error deleting appointment: ";
    public static final String FAILED_TO_SAVE_APPOINTMENT = "Failed to save appointment, no rows affected ";
    public static final String ERROR_RETRIEVING_APPOINTMENTS_BY_PATIENT = "Error retrieving appointments by patient ";
    public static final String ERROR_RETRIEVING_APPOINTMENTS_BY_PSYCHOLOGIST = "Error retrieving appointments by psychologist ";
    public static final String ERROR_RETRIEVING_APPOINTMENTS_BY_DATE = "Error retrieving appointments by date ";
    public static final String ERROR_RETRIEVING_UNNOTIFIED_APPOINTMENTS = "Error retrieving unnoticed appointments ";
    public static final String ERROR_UPDATING_APPOINTMENT_NOTIFICATION_STATUS = "Error updating appointment notification status ";
    public static final String ERROR_ROLLING_BACK = "Error rolling back transaction ";
    public static final String ERROR_UPDATING_APPOINTMENT_NOTIFICATION_STATUSES = "Error updating appointment notification statuses ";
    public static final String ERROR_RESETTING_AUTO_COMMIT = "Error resetting auto commit ";
    public static final String ERROR_CHECKING_IF_APPOINTMENT_EXISTS = "Error checking if appointment exists ";
    public static final String ERROR_GETTING_NEXT_APPOINTMENT = "Error getting next appointment ID ";
}