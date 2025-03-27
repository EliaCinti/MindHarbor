package it.uniroma2.mindharbor.dao.csv;

import it.uniroma2.mindharbor.dao.AppointmentDao;
import it.uniroma2.mindharbor.dao.PatientDao;
import it.uniroma2.mindharbor.dao.PsychologistDao;
import it.uniroma2.mindharbor.dao.csv.constants.AppointmentDaoCsvConstants;
import it.uniroma2.mindharbor.exception.DAOException;
import it.uniroma2.mindharbor.model.Appointment;
import it.uniroma2.mindharbor.model.Patient;
import it.uniroma2.mindharbor.patterns.facade.DaoFactoryFacade;
import it.uniroma2.mindharbor.utilities.CsvUtilities;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * CSV file implementation of the {@link AppointmentDao} interface.
 * <p>
 * This class manages appointment data stored in a CSV file, providing methods
 * to create, retrieve, update, and delete appointment records.
 * </p>
 */
public class AppointmentDaoCsv implements AppointmentDao {

    private static final Logger logger = Logger.getLogger(AppointmentDaoCsv.class.getName());
    private static final File appointmentFile = new File(AppointmentDaoCsvConstants.PATH_NAME_APPOINTMENTS);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_TIME;

    /**
     * Saves a new appointment in the CSV file.
     *
     * @param appointment     The appointment to be saved
     * @param patientUsername The username of the patient this appointment belongs to
     * @throws DAOException   If there is an error writing to the CSV file
     */
    @Override
    public void saveAppointment(Appointment appointment, String patientUsername) throws DAOException {
        // Create a new record for the CSV file
        String[] appointmentRecord = new String[AppointmentDaoCsvConstants.HEADER.length];
        appointmentRecord[AppointmentDaoCsvConstants.APPOINTMENT_INDEX_ID] = String.valueOf(appointment.getId());
        appointmentRecord[AppointmentDaoCsvConstants.APPOINTMENT_INDEX_DATE] = appointment.getDate().format(DATE_FORMATTER);
        appointmentRecord[AppointmentDaoCsvConstants.APPOINTMENT_INDEX_TIME] = appointment.getTime().format(TIME_FORMATTER);
        appointmentRecord[AppointmentDaoCsvConstants.APPOINTMENT_INDEX_DESCRIPTION] = appointment.getDescription();
        appointmentRecord[AppointmentDaoCsvConstants.APPOINTMENT_INDEX_NOTIFIED] = String.valueOf(appointment.isNotified());
        appointmentRecord[AppointmentDaoCsvConstants.APPOINTMENT_INDEX_PATIENT_USERNAME] = patientUsername;

        // Write the record to the CSV file
        CsvUtilities.writeFile(appointmentFile, appointmentRecord);
    }

    /**
     * Retrieves an appointment by its ID from the CSV file.
     *
     * @param appointmentId The ID of the appointment to retrieve
     * @return The appointment with the specified ID, or null if not found
     * @throws DAOException If there is an error reading from the CSV file
     */
    @Override
    public Appointment retrieveAppointment(int appointmentId) throws DAOException {
        List<String[]> appointmentRecords = CsvUtilities.readAll(appointmentFile);

        // Skip the header if it exists
        if (!appointmentRecords.isEmpty() && appointmentRecords.getFirst()[0].equals("ID")) {
            appointmentRecords.removeFirst();
        }

        // Search for the appointment with the specified ID
        for (String[] record : appointmentRecords) {
            if (Integer.parseInt(record[AppointmentDaoCsvConstants.APPOINTMENT_INDEX_ID]) == appointmentId) {
                return convertRecordToAppointment(record);
            }
        }

        return null; // Appointment not found
    }

    /**
     * Retrieves all appointments for a specific patient from the CSV file.
     *
     * @param patientUsername The username of the patient
     * @return A list of appointments for the patient, empty list if none found
     * @throws DAOException If there is an error reading from the CSV file
     */
    @Override
    public List<Appointment> retrieveAppointmentsByPatient(String patientUsername) throws DAOException {
        List<String[]> appointmentRecords = CsvUtilities.readAll(appointmentFile);
        List<Appointment> patientAppointments = new ArrayList<>();

        // Skip the header if it exists
        if (!appointmentRecords.isEmpty() && appointmentRecords.getFirst()[0].equals("ID")) {
            appointmentRecords.removeFirst();
        }

        // Filter appointments by patient username
        for (String[] record : appointmentRecords) {
            if (record[AppointmentDaoCsvConstants.APPOINTMENT_INDEX_PATIENT_USERNAME].equals(patientUsername)) {
                patientAppointments.add(convertRecordToAppointment(record));
            }
        }

        return patientAppointments;
    }

    /**
     * Retrieves all appointments for a specific psychologist from the CSV file.
     * <p>
     * This requires first retrieving all patients assigned to the psychologist,
     * then collecting all appointments for those patients.
     * </p>
     *
     * @param psychologistUsername The username of the psychologist
     * @return A list of appointments for the psychologist, empty list if none found
     * @throws DAOException If there is an error reading from the CSV file
     */
    @Override
    public List<Appointment> retrieveAppointmentsByPsychologist(String psychologistUsername) throws DAOException {
        List<Appointment> psychologistAppointments = new ArrayList<>();
        DaoFactoryFacade daoFactoryFacade = DaoFactoryFacade.getInstance();

        try {
            // Get all patients assigned to this psychologist
            PsychologistDao psychologistDao = daoFactoryFacade.getPsychologistDao();
            PatientDao patientDao = daoFactoryFacade.getPatientDao();

            List<Patient> patients = patientDao.retrievePatientsByPsychologist(
                    psychologistDao.retrievePsychologist(psychologistUsername));

            // Collect all appointments for each patient
            for (Patient patient : patients) {
                psychologistAppointments.addAll(retrieveAppointmentsByPatient(patient.getUsername()));
            }

            return psychologistAppointments;

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error retrieving appointments by psychologist", e);
            throw new DAOException("Error retrieving appointments by psychologist: " + e.getMessage());
        }
    }

    /**
     * Retrieves all appointments scheduled for a specific date from the CSV file.
     *
     * @param date The date for which appointments should be retrieved
     * @return A list of appointments on the specified date, empty list if none found
     * @throws DAOException If there is an error reading from the CSV file
     */
    @Override
    public List<Appointment> retrieveAppointmentsByDate(LocalDate date) throws DAOException {
        List<String[]> appointmentRecords = CsvUtilities.readAll(appointmentFile);
        List<Appointment> dateAppointments = new ArrayList<>();

        // Skip the header if it exists
        if (!appointmentRecords.isEmpty() && appointmentRecords.getFirst()[0].equals("ID")) {
            appointmentRecords.removeFirst();
        }

        String dateString = date.format(DATE_FORMATTER);

        // Filter appointments by date
        for (String[] record : appointmentRecords) {
            if (record[AppointmentDaoCsvConstants.APPOINTMENT_INDEX_DATE].equals(dateString)) {
                dateAppointments.add(convertRecordToAppointment(record));
            }
        }

        return dateAppointments;
    }

    /**
     * Retrieves all appointments that have not been notified to the patient yet.
     *
     * @param patientUsername The username of the patient
     * @return A list of unnotified appointments, empty list if none found
     * @throws DAOException If there is an error reading from the CSV file
     */
    @Override
    public List<Appointment> retrieveUnnotifiedAppointments(String patientUsername) throws DAOException {
        List<String[]> appointmentRecords = CsvUtilities.readAll(appointmentFile);
        List<Appointment> unnotifiedAppointments = new ArrayList<>();

        // Skip the header if it exists
        if (!appointmentRecords.isEmpty() && appointmentRecords.getFirst()[0].equals("ID")) {
            appointmentRecords.removeFirst();
        }

        // Filter appointments by patient username and notification status
        for (String[] record : appointmentRecords) {
            if (record[AppointmentDaoCsvConstants.APPOINTMENT_INDEX_PATIENT_USERNAME].equals(patientUsername) &&
                    record[AppointmentDaoCsvConstants.APPOINTMENT_INDEX_NOTIFIED].equals("false")) {
                unnotifiedAppointments.add(convertRecordToAppointment(record));
            }
        }

        return unnotifiedAppointments;
    }

    /**
     * Updates an existing appointment in the CSV file.
     *
     * @param appointment The appointment with updated information
     * @throws DAOException If there is an error updating the CSV file or if the appointment doesn't exist
     */
    @Override
    public void updateAppointment(Appointment appointment) throws DAOException {
        List<String[]> appointmentRecords = CsvUtilities.readAll(appointmentFile);
        boolean found = false;

        // Skip the header
        String[] header = appointmentRecords.isEmpty() ? AppointmentDaoCsvConstants.HEADER : appointmentRecords.removeFirst();

        // Update the appointment record
        for (String[] record : appointmentRecords) {
            if (Integer.parseInt(record[AppointmentDaoCsvConstants.APPOINTMENT_INDEX_ID]) == appointment.getId()) {
                record[AppointmentDaoCsvConstants.APPOINTMENT_INDEX_DATE] = appointment.getDate().format(DATE_FORMATTER);
                record[AppointmentDaoCsvConstants.APPOINTMENT_INDEX_TIME] = appointment.getTime().format(TIME_FORMATTER);
                record[AppointmentDaoCsvConstants.APPOINTMENT_INDEX_DESCRIPTION] = appointment.getDescription();
                record[AppointmentDaoCsvConstants.APPOINTMENT_INDEX_NOTIFIED] = String.valueOf(appointment.isNotified());
                found = true;
                break;
            }
        }

        if (!found) {
            throw new DAOException("Appointment not found: " + appointment.getId());
        }

        // Update the CSV file
        CsvUtilities.updateFile(appointmentFile, header, appointmentRecords);
    }

    /**
     * Updates the notification status of a specific appointment.
     *
     * @param appointmentId The ID of the appointment to update
     * @param notified The new notification status
     * @throws DAOException If there is an error updating the CSV file or if the appointment doesn't exist
     */
    @Override
    public void updateAppointmentNotificationStatus(int appointmentId, boolean notified) throws DAOException {
        List<String[]> appointmentRecords = CsvUtilities.readAll(appointmentFile);
        boolean found = false;

        // Skip the header
        String[] header = appointmentRecords.isEmpty() ? AppointmentDaoCsvConstants.HEADER : appointmentRecords.removeFirst();

        // Update the notification status
        for (String[] record : appointmentRecords) {
            if (Integer.parseInt(record[AppointmentDaoCsvConstants.APPOINTMENT_INDEX_ID]) == appointmentId) {
                record[AppointmentDaoCsvConstants.APPOINTMENT_INDEX_NOTIFIED] = String.valueOf(notified);
                found = true;
                break;
            }
        }

        if (!found) {
            throw new DAOException("Appointment not found: " + appointmentId);
        }

        // Update the CSV file
        CsvUtilities.updateFile(appointmentFile, header, appointmentRecords);
    }

    /**
     * Updates the notification status for a list of appointments in a batch operation.
     *
     * @param appointments The list of appointments to update
     * @throws DAOException If there is an error updating the CSV file
     */
    @Override
    public void updateAppointmentsNotificationStatus(List<Appointment> appointments) throws DAOException {
        if (appointments == null || appointments.isEmpty()) {
            return;
        }

        List<String[]> appointmentRecords = CsvUtilities.readAll(appointmentFile);

        // Skip the header
        String[] header = appointmentRecords.isEmpty() ? AppointmentDaoCsvConstants.HEADER : appointmentRecords.removeFirst();

        // Create a map of appointment IDs for faster lookup
        List<Integer> appointmentIds = new ArrayList<>();
        List<Boolean> notifiedValues = new ArrayList<>();

        for (Appointment appointment : appointments) {
            appointmentIds.add(appointment.getId());
            notifiedValues.add(appointment.isNotified());
        }

        // Update each record if its ID is in the list
        for (String[] record : appointmentRecords) {
            int recordId = Integer.parseInt(record[AppointmentDaoCsvConstants.APPOINTMENT_INDEX_ID]);
            int index = appointmentIds.indexOf(recordId);

            if (index != -1) {
                record[AppointmentDaoCsvConstants.APPOINTMENT_INDEX_NOTIFIED] = String.valueOf(notifiedValues.get(index));
            }
        }

        // Update the CSV file
        CsvUtilities.updateFile(appointmentFile, header, appointmentRecords);
    }

    /**
     * Deletes an appointment from the CSV file.
     *
     * @param appointmentId The ID of the appointment to delete
     * @throws DAOException If there is an error updating the CSV file
     */
    @Override
    public void deleteAppointment(int appointmentId) throws DAOException {
        List<String[]> appointmentRecords = CsvUtilities.readAll(appointmentFile);

        // Skip the header
        String[] header = appointmentRecords.isEmpty() ? AppointmentDaoCsvConstants.HEADER : appointmentRecords.removeFirst();

        // Remove the appointment record
        boolean removed = appointmentRecords.removeIf(record ->
                Integer.parseInt(record[AppointmentDaoCsvConstants.APPOINTMENT_INDEX_ID]) == appointmentId);

        if (!removed) {
            throw new DAOException("Appointment not found: " + appointmentId);
        }

        // Update the CSV file
        CsvUtilities.updateFile(appointmentFile, header, appointmentRecords);
    }

    /**
     * Checks if an appointment with the given ID exists in the CSV file.
     *
     * @param appointmentId The ID to check
     * @return true if the appointment exists, false otherwise
     * @throws DAOException If there is an error reading from the CSV file
     */
    @Override
    public boolean appointmentExists(int appointmentId) throws DAOException {
        List<String[]> appointmentRecords = CsvUtilities.readAll(appointmentFile);

        // Skip the header if it exists
        if (!appointmentRecords.isEmpty() && appointmentRecords.getFirst()[0].equals("ID")) {
            appointmentRecords.removeFirst();
        }

        // Check if the appointment exists
        for (String[] record : appointmentRecords) {
            if (Integer.parseInt(record[AppointmentDaoCsvConstants.APPOINTMENT_INDEX_ID]) == appointmentId) {
                return true;
            }
        }

        return false;
    }

    /**
     * Generates the next available appointment ID based on the highest ID currently in the CSV file.
     *
     * @return The next available appointment ID
     * @throws DAOException If there is an error reading from the CSV file
     */
    @Override
    public int getNextAppointmentId() throws DAOException {
        List<String[]> appointmentRecords = CsvUtilities.readAll(appointmentFile);

        // Skip the header if it exists
        if (!appointmentRecords.isEmpty() && appointmentRecords.getFirst()[0].equals("ID")) {
            appointmentRecords.removeFirst();
        }

        int maxId = 0;

        // Find the highest ID
        for (String[] record : appointmentRecords) {
            try {
                int id = Integer.parseInt(record[AppointmentDaoCsvConstants.APPOINTMENT_INDEX_ID]);
                if (id > maxId) {
                    maxId = id;
                }
            } catch (NumberFormatException e) {
                logger.log(Level.WARNING, "Invalid appointment ID in CSV file: " + record[AppointmentDaoCsvConstants.APPOINTMENT_INDEX_ID]);
            }
        }

        return maxId + 1;
    }

    /**
     * Converts a CSV record to an Appointment object.
     *
     * @param record The CSV record as a string array
     * @return The converted Appointment object
     * @throws DAOException If there is an error converting the record
     */
    private Appointment convertRecordToAppointment(String[] record) throws DAOException {
        try {
            int id = Integer.parseInt(record[AppointmentDaoCsvConstants.APPOINTMENT_INDEX_ID]);
            LocalDate date = LocalDate.parse(record[AppointmentDaoCsvConstants.APPOINTMENT_INDEX_DATE], DATE_FORMATTER);
            LocalTime time = LocalTime.parse(record[AppointmentDaoCsvConstants.APPOINTMENT_INDEX_TIME], TIME_FORMATTER);
            String description = record[AppointmentDaoCsvConstants.APPOINTMENT_INDEX_DESCRIPTION];
            boolean notified = Boolean.parseBoolean(record[AppointmentDaoCsvConstants.APPOINTMENT_INDEX_NOTIFIED]);

            return new Appointment(id, date, time, description, notified);
        } catch (DateTimeParseException | NumberFormatException e) {
            throw new DAOException("Error converting CSV record to Appointment: " + e.getMessage());
        }
    }
}
