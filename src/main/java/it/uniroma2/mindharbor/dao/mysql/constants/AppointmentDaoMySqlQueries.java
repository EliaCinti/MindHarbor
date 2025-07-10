package it.uniroma2.mindharbor.dao.mysql.constants;

/**
 * Constant class containing all SQL queries used by the AppointmentDaoMySql implementation.
 * <p>
 * This class centralizes all SQL statements to improve maintainability and consistency
 * across the data access layer. It follows the same pattern as the CSV constants classes.
 * </p>
 */
public class AppointmentDaoMySqlQueries {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private AppointmentDaoMySqlQueries() {
        // Prevent instantiation
    }

    /**
     * SQL query to insert a new appointment.
     */
    public static final String INSERT_APPOINTMENT =
            "INSERT INTO Appointments (id, date, time, description, notified, patient_username) VALUES (?, ?, ?, ?, ?, ?)";

    /**
     * SQL query to select an appointment by its ID.
     */
    public static final String SELECT_APPOINTMENT_BY_ID =
            "SELECT * FROM Appointments WHERE id = ?";

    public static final String SELECT_ALL_APPOINTMENTS = "SELECT * FROM Appointments";

    /**
     * SQL query to select all appointments for a specific patient.
     */
    public static final String SELECT_APPOINTMENTS_BY_PATIENT =
            "SELECT * FROM Appointments WHERE patient_username = ? ORDER BY date, time";

    /**
     * SQL query to select all appointments for a specific psychologist.
     * <p>
     * This joins the Appointments table with the Patients table to find appointments
     * for patients that are assigned to the specified psychologist.
     * </p>
     */
    public static final String SELECT_APPOINTMENTS_BY_PSYCHOLOGIST =
            "SELECT a.* FROM Appointments a JOIN Patients p ON a.patient_username = p.username " +
                    "WHERE p.psychologist = ? ORDER BY a.date, a.time";

    /**
     * SQL query to select all appointments for a specific date.
     */
    public static final String SELECT_APPOINTMENTS_BY_DATE =
            "SELECT * FROM Appointments WHERE date = ? ORDER BY time";

    /**
     * SQL query to select all unnoticed appointments for a specific patient.
     */
    public static final String SELECT_UNNOTIFIED_APPOINTMENTS =
            "SELECT * FROM Appointments WHERE patient_username = ? AND notified = false ORDER BY date, time";

    /**
     * SQL query to update an appointment's information.
     */
    public static final String UPDATE_APPOINTMENT =
            "UPDATE Appointments SET date = ?, time = ?, description = ?, notified = ? WHERE id = ?";

    /**
     * SQL query to update only the notification status of an appointment.
     */
    public static final String UPDATE_APPOINTMENT_NOTIFICATION =
            "UPDATE Appointments SET notified = ? WHERE id = ?";

    /**
     * SQL query to delete an appointment by its ID.
     */
    public static final String DELETE_APPOINTMENT =
            "DELETE FROM Appointments WHERE id = ?";

    /**
     * SQL query to check if an appointment exists.
     */
    public static final String CHECK_APPOINTMENT_EXISTS =
            "SELECT COUNT(*) FROM Appointments WHERE id = ?";

    /**
     * SQL query to get the maximum appointment ID currently in the database.
     */
    public static final String GET_MAX_APPOINTMENT_ID =
            "SELECT COALESCE(MAX(id), 0) FROM Appointments";
}