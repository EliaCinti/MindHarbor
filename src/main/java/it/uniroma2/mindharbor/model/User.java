package it.uniroma2.mindharbor.model;

import java.util.Objects;

/**
 * Represents a generic user in the system.
 * This class provides a base model for user details commonly required across various types of users.
 */
public class User {
    private String username;
    private String name;
    private String surname;
    private String gender;

    /**
     * Constructs a new user with the specified details.
     *
     * @param username the user's unique identifier
     * @param name     the user's first name
     * @param surname  the user's last name
     * @param gender   the user's gender
     */
    public User(String username, String name, String surname, String gender) {
        setUsername(username);
        setName(name);
        setSurname(surname);
        setGender(gender);
    }

    /**
     * Gets the username of the user.
     *
     * @return the username as a String
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of the user.
     *
     * @param username the new username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the first name of the user.
     *
     * @return the user's first name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the first name of the user.
     *
     * @param name the new first name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the last name of the user.
     *
     * @return the user's last name
     */
    public String getSurname() {
        return surname;
    }

    /**
     * Sets the last name of the user.
     *
     * @param surname the new last name to set
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }

    /**
     * Gets the gender of the user.
     *
     * @return the user's gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * Sets the gender of the user.
     *
     * @param gender the new gender to set
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * Compares this user with another object for equality, based on the username.
     * Only another User object with the same username will be considered equal to this one.
     *
     * @param o the object to compare this user against
     * @return true if the given object represents a User equivalent to this user, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username);
    }

    /**
     * Returns a hash code value for the user, which is based on the user's username.
     *
     * @return a hash code value for this user
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(username);
    }

}
