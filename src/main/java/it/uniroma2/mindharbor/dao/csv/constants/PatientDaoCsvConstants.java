package it.uniroma2.mindharbor.dao.csv.constants;

/**
 * Constants used by PatientDaoCsv for managing CSV file operations for patients.
 * This class defines standard structure and identifiers for the patient CSV storage,
 * ensuring consistent data handling across the application.
 */
public class PatientDaoCsvConstants {
    /**
     * Private constructor to prevent instantiation of the class.
     */
    private PatientDaoCsvConstants() {
        // Prevent instantiation
    }

    /**
     * Headers used in the patient CSV file, corresponding to each column in the CSV.
     * These headers are utilized to ensure correct data parsing and writing.
     */
    public static final String[] HEADER = {"Username", "Birth Date", "Psychologist"};
    /**
     * The file path to the CSV where patient data is stored.
     */
    public static final String PATH_NAME_PATIENTS = "db/csv/patient_db.csv";
    /**
     * Index for the username column in the patient CSV.
     * This index is used to access the username information of the patient.
     */
    public static final int PATIENT_INDEX_USERNAME = 0;
    /**
     * Index for the birth date column in the patient CSV.
     * This index is used to access the birth date information of the patient.
     */
    public static final int PATIENT_INDEX_BIRTHDATE = 1;

    /**
     * Index for the psychologist column in the patient CSV.
     * This index is used to access the assigned psychologist's information.
     */
    public static final int PATIENT_INDEX_PSYCOLOGIST = 2;
    /**
     * Message prefix used when a patient cannot be found in the database.
     * This is utilized to construct error messages for better error handling.
     */
    public static final String PATIENT_NOT_FOUND = "Patient not found: ";
}
