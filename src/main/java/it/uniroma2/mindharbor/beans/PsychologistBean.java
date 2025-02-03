package it.uniroma2.mindharbor.beans;

/**
 * The {@code PsychologistBean} class represents a psychologist in the system.
 * It extends the {@code UserBean} class and includes additional details
 * such as office location and hourly consultation cost.
 * <p>
 * Instances of this class are immutable and must be created using the {@link Builder}.
 * </p>
 */
public class PsychologistBean extends UserBean {
    private String office;
    private double hourlyCost;


    /**
     * Protected constructor used by the {@link Builder} to create an instance of {@code PsychologistBean}.
     *
     * @param builder The builder instance containing the required parameters.
     */
    protected PsychologistBean(Builder builder) {
        super(builder);
        this.office = builder.office;
        this.hourlyCost = builder.hourlyCost;
    }


    /**
     * Builder class for {@code PsychologistBean}.
     * <p>
     * This class extends {@link UserBean.Builder} and allows incremental and flexible object creation.
     * It provides additional methods to set the psychologist's office and hourly cost.
     * </p>
     */
    public static class Builder extends UserBean.Builder<Builder> {
        private String office;
        private double hourlyCost;

        /**
         * Default constructor for {@code PsychologistBean.Builder}.
         */
        public Builder() {
            super();
        }

        /**
         * Sets the office location of the psychologist.
         *
         * @param office The office location.
         * @return The builder instance for method chaining.
         */
        public Builder office(String office) {
            this.office = office;
            return self();
        }

        /**
         * Sets the hourly consultation cost of the psychologist.
         *
         * @param hourlyCost The hourly cost.
         * @return The builder instance for method chaining.
         */
        public Builder hourlyCost(double hourlyCost) {
            this.hourlyCost = hourlyCost;
            return self();
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

        /**
         * Builds and returns an instance of {@code PsychologistBean}.
         *
         * @return A new {@code PsychologistBean} instance with the set properties.
         */
        @Override
        public PsychologistBean build() {
            return new PsychologistBean(this);
        }
    }

    /**
     * Retrieves the psychologist's office location.
     *
     * @return The office location.
     */
    public String getOffice() {
        return office;
    }

    /**
     * Sets the psychologist's office location.
     *
     * @param office The new office location.
     */
    public void setOffice(String office) {
        this.office = office;
    }

    /**
     * Retrieves the psychologist's hourly consultation cost.
     *
     * @return The hourly cost.
     */
    public double getHourlyCost() {
        return hourlyCost;
    }

    /**
     * Sets the psychologist's hourly consultation cost.
     *
     * @param hourlyCost The new hourly cost.
     */
    public void setHourlyCost(double hourlyCost) {
        this.hourlyCost = hourlyCost;
    }
}