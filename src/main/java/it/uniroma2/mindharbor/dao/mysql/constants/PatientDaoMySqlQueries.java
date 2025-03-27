package it.uniroma2.mindharbor.dao.mysql.constants;

/**
 * Constant class containing all SQL queries used by the PatientDaoMySql implementation.
 * <p>
 * This class centralizes all SQL statements related to Patient data operations
 * to improve maintainability and consistency across the data access layer.
 * </p>
 */
public class PatientDaoMySqlQueries {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private PatientDaoMySqlQueries() {
        // Prevent instantiation
    }

    /**
     * SQL query to insert a new patient.
     */
    public static final String INSERT_PATIENT =
            "INSERT INTO Patients (Username, BirthDate, Psychologist) VALUES (?, ?, ?)";

    /**
     * SQL query to select a patient by username, joining with Users table.
     */
    public static final String SELECT_PATIENT_BY_USERNAME =
            "SELECT p.Username, u.Firstname, u.Lastname, u.Gender, p.Psychologist, p.BirthDate " +
                    "FROM Patients p " +
                    "JOIN Users u ON p.Username = u.Username " +
                    "WHERE p.Username = ?";

    /**
     * SQL query to select all patients assigned to a specific psychologist.
     */
    public static final String SELECT_PATIENTS_BY_PSYCHOLOGIST =
            "SELECT p.Username, u.Firstname, u.Lastname, u.Gender, p.Psychologist, p.BirthDate " +
                    "FROM Patients p " +
                    "JOIN Users u ON p.Username = u.Username " +
                    "WHERE p.Psychologist = ?";

    /**
     * SQL query to update a patient's information.
     */
    public static final String UPDATE_PATIENT =
            "UPDATE Patients SET BirthDate = ?, Psychologist = ? WHERE Username = ?";

    /**
     * SQL query to delete a patient.
     */
    public static final String DELETE_PATIENT =
            "DELETE FROM Patients WHERE Username = ?";
}
