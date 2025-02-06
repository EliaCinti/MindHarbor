package it.uniroma2.mindharbor.beans;

/**
 * Represents a user's credentials including username, password, and user type.
 * This class provides a Builder pattern for constructing instances in a flexible manner.
 * <p>
 * The {@code CredentialsBean} stores fundamental user information such as
 * - Username
 * - Password
 * - User type (e.g., "admin", "patient", "psychologist").
 * </p>
 * <p>
 * Instances of this class are immutable and can be created only using the {@link Builder}.
 * </p>
 */
public class CredentialsBean {
    private final String username;
    private final String password;
    private String type;

    /**
     * Private constructor used by the {@link Builder} to create instances of {@code CredentialsBean}.
     *
     * @param builder The builder instance containing the required parameters.
     */
    protected CredentialsBean(Builder<?> builder) {
        this.username = builder.username;
        this.password = builder.password;
        this.type = builder.type;
    }

    /**
     * Builder class for {@code CredentialsBean}.
     * <p>
     * This class follows the Builder pattern, allowing incremental and flexible object creation.
     * </p>
     *
     * @param <T> The type of the Builder, allowing method chaining in subclasses.
     */
    public static class Builder<T extends Builder<T>> {
        protected String username;
        protected String password;
        protected String type;

        /**
         * Sets the username for the {@code CredentialsBean}.
         *
         * @param username The username of the user.
         * @return The builder instance for method chaining.
         */
        public T username(String username) {
            this.username = username;
            return self();
        }

        /**
         * Sets the password for the {@code CredentialsBean}.
         *
         * @param password The password of the user.
         * @return The builder instance for method chaining.
         */
        public T password(String password) {
            this.password = password;
            return self();
        }

        /**
         * Sets the user type for the {@code CredentialsBean}.
         * <p>
         * Example values: "admin", "patient", "psychologist".
         * </p>
         *
         * @param type The user type.
         * @return The builder instance for method chaining.
         */
        public T type(String type) {
            this.type = type;
            return self();
        }

        /**
         * Returns the correct Builder instance, allowing subclasses to override this method
         * for correct method chaining.
         *
         * @return The builder instance of type {@code T}.
         */
        @SuppressWarnings("unchecked")
        protected T self() {
            return (T) this;
        }

        /**
         * Builds and returns an instance of {@code CredentialsBean}.
         *
         * @return A new {@code CredentialsBean} instance with the set properties.
         */
        public CredentialsBean build() {
            return new CredentialsBean(this);
        }
    }

    /**
     * Retrieves the username of the user.
     *
     * @return The username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Retrieves the password of the user.
     *
     * @return The password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Retrieves the user type.
     *
     * @return The type of the user (e.g., "admin", "patient", "psychologist").
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the user type.
     * <p>
     * This method updates the user type, which can represent roles such as
     * "patient" or "psychologist".
     * </p>
     *
     * @param type The new user type to be assigned.
     */
    public void setType(String type) {
        this.type = type;
    }
}
