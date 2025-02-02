package it.uniroma2.mindharbor.beans;

/**
 * Represents a user's credentials including username, password, and user type.
 * This class provides constructors for different use cases such as fetching user information,
 * validating login credentials, and creating a new user.
 */
public class CredentialsBean {
    private String username;
    private String password;
    private String type;

    /**
     * Constructor to initialize a CredentialsBean with only a username.
     * This can be used when retrieving user information.
     *
     * @param username the username of the user
     */
    public CredentialsBean(String username) {
        // quando voglio informazioni sullo user
        this.username = username;
    }

    /**
     * Constructor to initialize a CredentialsBean with a username and password.
     * This is typically used for login validation.
     *
     * @param username the username of the user
     * @param password the password of the user
     */
    public CredentialsBean(String username, String password) {
        // validate login
        this(username);
        this.password = password;
    }

    /**
     * Constructor to initialize a CredentialsBean with a username, password, and user type.
     * This is typically used when saving a new user.
     *
     * @param username the username of the user
     * @param password the password of the user
     * @param type     the type of the user (e.g., admin, regular user, etc.)
     */
    public CredentialsBean(String username, String password, String type) {
        // save new user
        this(username, password);
        this.type = type;
    }

    /**
     * Retrieves the username of the user.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Retrieves the password of the user.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Retrieves the user type.
     *
     * @return the user type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the user type.
     *
     * @param type the new user type
     */
    public void setType(String type) {
        this.type = type;
    }
}
