package it.uniroma2.mindharbor.beans;

/**
 * The {@code UserBean} class represents a general user in the system.
 * It extends the {@code CredentialsBean} class by adding personal details
 * such as name, surname, and gender.
 * <p>
 * Instances of this class are immutable and must be created using the {@link Builder}.
 * </p>
 */
public class UserBean extends CredentialsBean {
    private String name;
    private String surname;
    private String gender;

    /**
     * Protected constructor used by the {@link Builder} to create an instance of {@code UserBean}.
     *
     * @param builder The builder instance containing the required parameters.
     */
    protected UserBean(Builder<?> builder) {
        super(builder);
        this.name = builder.name;
        this.surname = builder.surname;
        this.gender = builder.gender;
    }

    /**
     * Builder class for {@code UserBean}.
     * <p>
     * This class extends {@link CredentialsBean.Builder} and allows incremental and flexible object creation.
     * It provides additional methods to set user-specific details.
     * </p>
     *
     * @param <T> The type of the Builder, enabling method chaining in subclasses.
     */
    public static class Builder<T extends Builder<T>> extends CredentialsBean.Builder<T> {
        private String name;
        private String surname;
        private String gender;

        /**
         * Default constructor for {@code UserBean.Builder}.
         */
        public Builder() {
            super();
        }

        /**
         * Sets the user's first name.
         *
         * @param name The first name of the user.
         * @return The builder instance for method chaining.
         */
        public T name(String name) {
            this.name = name;
            return self();
        }

        /**
         * Sets the user's last name.
         *
         * @param surname The last name of the user.
         * @return The builder instance for method chaining.
         */
        public T surname(String surname) {
            this.surname = surname;
            return self();
        }

        /**
         * Sets the user's gender.
         *
         * @param gender The gender of the user.
         * @return The builder instance for method chaining.
         */
        public T gender(String gender) {
            this.gender = gender;
            return self();
        }

        /**
         * Returns the correct Builder instance, allowing subclasses to override this method
         * for correct method chaining.
         *
         * @return The builder instance of type {@code T}.
         */
        @SuppressWarnings("unchecked")
        @Override
        protected T self() {
            return (T) this;
        }

        /**
         * Builds and returns an instance of {@code UserBean}.
         *
         * @return A new {@code UserBean} instance with the set properties.
         */
        public UserBean build() {
            return new UserBean(this);
        }
    }

    /**
     * Retrieves the user's first name.
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
     * Retrieves the user's last name.
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
     * Retrieves the user's gender.
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
