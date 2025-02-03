package it.uniroma2.mindharbor.beans;

import java.time.LocalDate;

/**
 * The {@code PatientBean} class represents a patient in the system.
 * It extends the {@code UserBean} class and includes additional information
 * about the patient's birthdate.
 * <p>
 * Instances of this class are immutable and must be created using the {@link Builder}.
 * </p>
 */
public class PatientBean extends UserBean {
    private LocalDate birthDate;

    /**
     * Private constructor used by the {@link Builder} to create an instance of {@code PatientBean}.
     *
     * @param builder The builder instance containing the required parameters.
     */
    private PatientBean(Builder builder) {
        super(builder);
        this.birthDate = builder.birthDate;
    }

    /**
     * Builder class for {@code PatientBean}.
     * <p>
     * This class extends {@link UserBean.Builder} and allows incremental and flexible object creation.
     * It provides an additional method to set the patient's birthdate.
     * </p>
     */
    public static class Builder extends UserBean.Builder<Builder> {
        private LocalDate birthDate;

        /**
         * Default constructor for {@code PatientBean.Builder}.
         */
        public Builder() {
            super();
        }

        /**
         * Sets the patient's birthdate.
         *
         * @param birthDate The birthdate of the patient.
         * @return The builder instance for method chaining.
         */
        public Builder birthDate(LocalDate birthDate) {
            this.birthDate = birthDate;
            return this;
        }

        /**
         * Builds and returns an instance of {@code PatientBean}.
         *
         * @return A new {@code PatientBean} instance with the set properties.
         */
        @Override
        public PatientBean build() {
            return new PatientBean(this);
        }

        /**
         * Returns the correct Builder instance, allowing method chaining.
         *
         * @return The builder instance of type {@code Builder}.
         */
        @Override
        protected Builder self() {
            return this;
        }
    }

    /**
     * Retrieves the patient's birthdate.
     *
     * @return The birthdate of the patient.
     */
    public LocalDate getBirthDate() {
        return birthDate;
    }

    /**
     * Sets the patient's birthdate.
     *
     * @param birthDate The new birthdate of the patient.
     */
    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
}
