package it.uniroma2.mindharbor.model;

import it.uniroma2.mindharbor.exception.BookingNotAllowedException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a psychologist in the system. Extends the User class by adding
 * psychologist-specific attributes such as office location and hourly consultation cost.
 */
public class Psychologist extends User {
    private String office;
    private String hourlyCost;
    private List<Patient> patients;


    /**
     * Constructs a Psychologist with the specified details.
     *
     * @param username   The psychologist's username.
     * @param name       The psychologist's first name.
     * @param surname    The psychologist's last name.
     * @param gender     The psychologist's gender.
     * @param office     The office location of the psychologist.
     * @param hourlyCost The hourly cost charged by the psychologist.
     */
    public Psychologist(String username, String name, String surname, String gender, String office, String hourlyCost) {
        super(username, name, surname, gender);
        setOffice(office);
        setHourlyCost(hourlyCost);
    }

    /**
     * Returns the office location of the psychologist.
     *
     * @return The office location.
     */
    public String getOffice() {
        return office;
    }

    /**
     * Sets the office location for the psychologist.
     *
     * @param office The new office location.
     */
    public void setOffice(String office) {
        this.office = office;
    }

    /**
     * Returns the hourly consultation cost of the psychologist.
     *
     * @return The hourly cost.
     */
    public String getHourlyCost() {
        return hourlyCost;
    }

    /**
     * Sets the hourly consultation cost for the psychologist.
     *
     * @param hourlyCost The new hourly consultation cost.
     */
    public void setHourlyCost(String hourlyCost) {
        this.hourlyCost = hourlyCost;
    }

    /**
     * Returns a list of patients under the care of this psychologist.
     *
     * @return A list of patients.
     */
    public List<Patient> getPatients() {
        return patients;
    }

    /**
     * Sets the list of patients.
     *
     * @param patients the list of patients to be set
     */
    public void setPatients(List<Patient> patients) {
        this.patients = patients;
    }

    /**
     * Confirms an appointment for a specific patient, ensuring that the appointment does not
     * conflict with existing appointments and that the patient is under the care of this psychologist.
     *
     * @param appointment The appointment to be confirmed.
     * @param patient     The patient for whom the appointment is being confirmed.
     * @throws BookingNotAllowedException If the patient is not under the care of this psychologist,
     *                                    or if there is a time conflict with another appointment.
     */
    public void confirmAppointment(Appointment appointment, Patient patient) throws BookingNotAllowedException {
        if (patients == null || !patients.contains(patient)) {
            throw new BookingNotAllowedException();
        }
        List<Appointment> appointments = getAppointmentsByDate(appointment.getDate());
        for (Appointment a : appointments) {
            if (a.getTime().equals(appointment.getTime()))
                throw new BookingNotAllowedException();
        }
        patient.addAppointment(appointment);
    }

    /**
     * Retrieves all appointments for this psychologist on a specific date.
     *
     * @param date The date for which to retrieve appointments.
     * @return A list of appointments on the specified date.
     */
    public List<Appointment> getAppointmentsByDate(LocalDate date) {
        List<Appointment> appointments = new ArrayList<>();
        for (Patient patient : patients) {
            if (patient.getAppointmentByDate(date) != null) {
                appointments.add(patient.getAppointmentByDate(date));
            }
        }
        return appointments;
    }

    /**
     * Checks if this psychologist is data-equivalent to another object.
     * <p>
     * This method performs a deep comparison of all psychologist-specific data fields
     * to determine if two psychologist objects contain the same information.
     * Unlike the standard {@code equals} method, this method focuses on data
     * equivalence rather than object identity, making it particularly useful
     * for synchronization operations between different persistence systems.
     * </p>
     * <p>
     * The comparison includes:
     * <ul>
     *   <li>Username (inherited from User)</li>
     *   <li>Name and surname (inherited from User)</li>
     *   <li>Gender (inherited from User)</li>
     *   <li>Office location (psychologist-specific)</li>
     *   <li>Hourly cost (psychologist-specific)</li>
     * </ul>
     * </p>
     * <p>
     * Note: This method does not compare the patient list, as it focuses on
     * the psychologist's intrinsic data rather than relational data.
     * </p>
     *
     * @param o The object to compare with this psychologist
     * @return {@code true} if the object is a Psychologist with equivalent data,
     *         {@code false} otherwise
     * @see #equals(Object) for identity-based comparison
     */
    public boolean isDataEquivalent(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Psychologist that = (Psychologist) o;
        return getUsername().equals(that.getUsername()) &&
                getName().equals(that.getName()) &&
                getSurname().equals(that.getSurname()) &&
                getGender().equals(that.getGender()) &&
                java.util.Objects.equals(getOffice(), that.getOffice()) &&
                java.util.Objects.equals(getHourlyCost(), that.getHourlyCost());
    }
}
