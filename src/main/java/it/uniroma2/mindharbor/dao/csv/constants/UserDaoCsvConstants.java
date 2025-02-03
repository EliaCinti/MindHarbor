package it.uniroma2.mindharbor.dao.csv.constants;

/**
 * Constants used by UserDaoCsv for handling CSV file operations related to users.
 * This class provides static constants to standardize the column indices and other parameters
 * used throughout the UserDaoCsv class, ensuring consistency in the CSV data manipulation.
 */
public class UserDaoCsvConstants {
    /**
     * Private constructor to prevent instantiation.
     */
    private UserDaoCsvConstants() {
        // Prevent instantiation
    }

    /**
     * Headers used in the user CSV file, corresponding to each column in the CSV.
     */
    public static final String[] HEADER = {"Username", "Password", "Firstname", "Lastname", "Type", "Gender"};
    /**
     * Path to the CSV file where user data is stored.
     */
    public static final String PATH_NAME_USER = "db/csv/user_db.csv";
    /**
     * Index for the username column in the CSV.
     */
    public static final int USER_INDEX_USERNAME = 0;
    /**
     * Index for the password column in the CSV.
     */
    public static final int USER_INDEX_PASSWORD = 1;
    /**
     * Index for the first name column in the CSV.
     */
    public static final int USER_INDEX_FIRST_NAME = 2;
    /**
     * Index for the last name column in the CSV.
     */
    public static final int USER_INDEX_LAST_NAME = 3;
    /**
     * Index for the user type column in the CSV.
     */
    public static final int USER_INDEX_TYPE = 4;

    /**
     * Index for the gender column in the CSV.
     */
    public static final int USER_INDEX_GENDER = 5;
    /**
     * Message to indicate a user already exists in the database.
     */
    public static final String USER_EXIST = "User already exists";
    /**
     * Message prefix used when a user cannot be found in the database.
     */
    public static final String USER_NOT_FOUND = "User not found: ";
}
