package it.uniroma2.mindharbor.beans;

/**
 * The {@code UserBean} class represents a general user in the system.
 * It extends the {@code CredentialsBean} class by adding personal details
 * such as name, surname, and gender.
 */
public class UserBean extends CredentialsBean {
    private String name;
    private String surname;
    private String gender;

    /**
     * Constructs a {@code UserBean} instance with full user details.
     *
     * @param username The user's username.
     * @param password The user's password.
     * @param type     The user type (e.g., "patient", "psychologist").
     * @param name     The user's first name.
     * @param surname  The user's last name.
     * @param gender   The user's gender.
     */
    public UserBean(String username, String password, String type, String name, String surname, String gender) {
        super(username, password, type);
        this.name = name;
        this.surname = surname;
        this.gender = gender;
    }

    /**
     * Constructs a {@code UserBean} instance with only credentials.
     *
     * @param username The user's username.
     * @param password The user's password.
     * @param type     The user type (e.g., "patient", "psychologist").
     */
    public UserBean(String username, String password, String type) {
        super(username, password, type);
    }

    /**
     * Gets the user's first name.
     *
     * @return The first name of the user.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the user's first name.
     *
     * @param name The new first name of the user.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the user's last name.
     *
     * @return The last name of the user.
     */
    public String getSurname() {
        return surname;
    }

    /**
     * Sets the user's last name.
     *
     * @param surname The new last name of the user.
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }

    /**
     * Gets the user's gender.
     *
     * @return The gender of the user.
     */
    public String getGender() {
        return gender;
    }

    /**
     * Sets the user's gender.
     *
     * @param gender The new gender of the user.
     */
    public void setGender(String gender) {
        this.gender = gender;
    }
}
