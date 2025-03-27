package it.uniroma2.mindharbor.dao.mysql.constants;

/**
 * Constant class containing all SQL queries used by the UserDaoMySql implementation.
 * <p>
 * This class centralizes all SQL statements to improve maintainability and consistency
 * across the data access layer.
 * </p>
 */
public class UserDaoMySqlQueries {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private UserDaoMySqlQueries() {
        // Prevent instantiation
    }

    /**
     * SQL query to validate user credentials.
     */
    public static final String VALIDATE_USER =
            "SELECT Type FROM Users WHERE Username = ? AND Password = ?";

    /**
     * SQL query to insert a new user.
     */
    public static final String INSERT_USER =
            "INSERT INTO Users (Username, Password, Firstname, Lastname, Type, Gender) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";

    /**
     * SQL query to select a user by username.
     */
    public static final String SELECT_USER_BY_USERNAME =
            "SELECT Username, Password, Firstname, Lastname, Type, Gender FROM Users WHERE Username = ?";

    /**
     * SQL query to check if a username already exists.
     */
    public static final String CHECK_USERNAME_EXISTS =
            "SELECT COUNT(*) FROM Users WHERE Username = ?";

    /**
     * SQL query to update an existing user.
     */
    public static final String UPDATE_USER =
            "UPDATE Users SET Password = ?, Firstname = ?, Lastname = ?, Type = ?, Gender = ? WHERE Username = ?";

    /**
     * SQL query to delete a user.
     */
    public static final String DELETE_USER =
            "DELETE FROM Users WHERE Username = ?";
}