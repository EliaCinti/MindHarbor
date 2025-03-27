package it.uniroma2.mindharbor.dao.csv.constants;

/**
 * Constants used by AppointmentDaoCsv for managing CSV file operations.
 * <p>
 * This class defines standard structure and identifiers for the appointment CSV storage,
 * ensuring consistent data handling across the application.
 * </p>
 */
public class AppointmentDaoCsvConstants {
    /**
     * Private constructor to prevent instantiation of the class.
     */
    private AppointmentDaoCsvConstants() {
        // Prevent instantiation
    }

    /**
     * Headers used in the appointment CSV file, corresponding to each column in the CSV.
     * These headers are used to ensure correct data parsing and writing.
     */
    public static final String[] HEADER = {
            "ID", "Date", "Time", "Description", "Notified", "PatientUsername"
    };

    /**
     * The file path to the CSV where appointment data is stored.
     */
    public static final String PATH_NAME_APPOINTMENTS = "db/csv/appointment_db.csv";

    /**
     * Index for the appointment ID column in the CSV.
     */
    public static final int APPOINTMENT_INDEX_ID = 0;

    /**
     * Index for the date column in the CSV.
     */
    public static final int APPOINTMENT_INDEX_DATE = 1;

    /**
     * Index for the time column in the CSV.
     */
    public static final int APPOINTMENT_INDEX_TIME = 2;

    /**
     * Index for the description column in the CSV.
     */
    public static final int APPOINTMENT_INDEX_DESCRIPTION = 3;

    /**
     * Index for the notified status column in the CSV.
     */
    public static final int APPOINTMENT_INDEX_NOTIFIED = 4;

    /**
     * Index for the patient username column in the CSV.
     */
    public static final int APPOINTMENT_INDEX_PATIENT_USERNAME = 5;

    /**
     * Message prefix used when an appointment cannot be found in the database.
     */
    public static final String APPOINTMENT_NOT_FOUND = "Appointment not found: ";
}