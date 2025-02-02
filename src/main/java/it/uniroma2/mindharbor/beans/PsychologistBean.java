package it.uniroma2.mindharbor.beans;

/**
 * The {@code PsychologistBean} class represents a psychologist in the system.
 * It extends the {@code UserBean} class and includes additional details
 * such as office location and hourly cost.
 */
public class PsychologistBean extends UserBean {
    private String office;
    private double hourlyCost;

    /**
     * Constructs a {@code PsychologistBean} instance.
     *
     * @param username   The psychologist's username.
     * @param password   The psychologist's password.
     * @param type       The user type (e.g., "psychologist").
     * @param name       The psychologist's first name.
     * @param surname    The psychologist's last name.
     * @param gender     The psychologist's gender.
     * @param hourlyCost The psychologist's hourly consultation cost.
     * @param office     The office location of the psychologist.
     */
    public PsychologistBean(String username, String password, String type, String name, String surname, String gender, double hourlyCost, String office) {
        super(username, password, type, name, surname, gender);
        this.office = office;
        this.hourlyCost = hourlyCost;
    }

    /**
     * Gets the psychologist's office location.
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
     * Gets the psychologist's hourly consultation cost.
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
