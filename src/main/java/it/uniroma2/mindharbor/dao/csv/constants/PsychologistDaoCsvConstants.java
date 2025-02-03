package it.uniroma2.mindharbor.dao.csv.constants;

/**
 * Constants used by PsychologistDaoCsv for managing CSV file operations for psychologists.
 * This class defines the standard structure and access points for the psychologist CSV storage,
 * ensuring that data is consistently handled and retrieved.
 */
public class PsychologistDaoCsvConstants {
    /**
     * Private constructor to prevent instantiation.
     */
    private PsychologistDaoCsvConstants() {
        // Prevent instantiation
    }

    /**
     * Headers used in the psychologist CSV file. These correspond to each column
     * in the psychologist data file and are used to ensure correct data parsing.
     */
    public static final String[] HEADER = {"Username", "Office", "Hourly Cost"};

    /**
     * The file path to the CSV where psychologist data is stored.
     */
    public static final String PATH_NAME_PSYCHOLOGIST = "db/csv/psychologist_db.csv";
    /**
     * Index for the username column in the psychologist CSV.
     * This is used to access the username information of the psychologist.
     */
    public static final int PSYCHOLOGIST_INDEX_USERNAME = 0;
    /**
     * Index for the office location column in the psychologist CSV.
     * This is used to access the office location information of the psychologist.
     */
    public static final int PSYCHOLOGIST_INDEX_OFFICE = 1;
    /**
     * Index for the hourly cost column in the psychologist CSV.
     * This is used to access the hourly cost information of the psychologist.
     */
    public static final int PSYCHOLOGIST_INDEX_HOURLY_COST = 2;
    /**
     * Message prefix used when a psychologist cannot be found in the database.
     */
    public static final String PSYCHOLOGIST_NOT_FOUND = "Psychologist not found: ";
}
