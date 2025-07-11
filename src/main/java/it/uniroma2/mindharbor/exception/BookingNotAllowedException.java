package it.uniroma2.mindharbor.exception;

/**
 * Exception thrown when appointment booking operations are not allowed.
 * <p>
 * This exception is used to indicate that a requested appointment booking
 * cannot be completed due to business rule violations or system constraints.
 * It serves as a way to enforce the application's booking policies and
 * prevent invalid appointment scheduling scenarios.
 * </p>
 * <p>
 * Common scenarios that trigger this exception:
 * <ul>
 *   <li>Attempting to book an appointment with a psychologist who is not assigned to the patient</li>
 *   <li>Trying to schedule an appointment at a time slot that's already occupied</li>
 *   <li>Booking duplicate appointments for the same patient on the same date</li>
 *   <li>Attempting to confirm appointments for patients not under the psychologist's care</li>
 *   <li>Scheduling conflicts with existing appointments in the system</li>
 *   <li>Violating appointment booking policies or constraints</li>
 * </ul>
 * </p>
 * <p>
 * This exception is typically caught by controllers and UI components to
 * provide meaningful feedback to users about why their booking request
 * could not be processed, allowing them to make appropriate adjustments.
 * </p>
 * <p>
 * Note: This exception extends {@code Exception} rather than {@code RuntimeException},
 * requiring explicit handling by calling code to ensure that booking failures
 * are properly addressed rather than causing unexpected application termination.
 * </p>
 *
 * @see it.uniroma2.mindharbor.model.Patient#addAppointment(it.uniroma2.mindharbor.model.Appointment)
 * @see it.uniroma2.mindharbor.model.Psychologist#confirmAppointment(it.uniroma2.mindharbor.model.Appointment, it.uniroma2.mindharbor.model.Patient)
 * @see it.uniroma2.mindharbor.model.Appointment for appointment management
 */
public class BookingNotAllowedException extends Exception {

    /**
     * Constructs a new BookingNotAllowedException with no detail message.
     * <p>
     * This constructor creates a basic exception without any specific
     * error information. The lack of a message indicates that the calling
     * code is expected to handle the exception based on the context in
     * which it was thrown, such as displaying a generic "booking not allowed"
     * message to the user.
     * </p>
     * <p>
     * This design choice keeps the exception lightweight while allowing
     * the UI layer to provide context-appropriate error messages based
     * on the specific operation that failed.
     * </p>
     */
    public BookingNotAllowedException() {
        super();
    }
}
