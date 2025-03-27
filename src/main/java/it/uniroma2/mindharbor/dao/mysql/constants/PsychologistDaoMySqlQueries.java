package it.uniroma2.mindharbor.dao.mysql.constants;

/**
 * Constant class containing all SQL queries used by the PsychologistDaoMySql implementation.
 * <p>
 * This class centralizes all SQL statements related to Psychologist data operations
 * to improve maintainability and consistency across the data access layer.
 * </p>
 */
public class PsychologistDaoMySqlQueries {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private PsychologistDaoMySqlQueries() {
        // Prevent instantiation
    }

    /**
     * SQL query to insert a new psychologist.
     */
    public static final String INSERT_PSYCHOLOGIST =
            "INSERT INTO Psychologists (Username, Office, HourlyCost) VALUES (?, ?, ?)";

    /**
     * SQL query to select a psychologist by username, joining with Users table.
     */
    public static final String SELECT_PSYCHOLOGIST_BY_USERNAME =
            "SELECT p.Username, u.Firstname, u.Lastname, u.Gender, p.Office, p.HourlyCost " +
                    "FROM Psychologists p " +
                    "JOIN Users u ON p.Username = u.Username " +
                    "WHERE p.Username = ?";

    /**
     * SQL query to update a psychologist's information.
     */
    public static final String UPDATE_PSYCHOLOGIST =
            "UPDATE Psychologists SET Office = ?, HourlyCost = ? WHERE Username = ?";

    /**
     * SQL query to delete a psychologist.
     */
    public static final String DELETE_PSYCHOLOGIST =
            "DELETE FROM Psychologists WHERE Username = ?";

    /**
     * SQL query to select all patients assigned to a specific psychologist.
     */
    public static final String SELECT_PATIENTS_BY_PSYCHOLOGIST =
            "SELECT p.Username, u.Firstname, u.Lastname, u.Gender, p.Psychologist, p.BirthDate " +
                    "FROM Patients p " +
                    "JOIN Users u ON p.Username = u.Username " +
                    "WHERE p.Psychologist = ?";
}
