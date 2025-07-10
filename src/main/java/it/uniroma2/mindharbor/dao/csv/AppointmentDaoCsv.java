package it.uniroma2.mindharbor.dao.csv;

import it.uniroma2.mindharbor.dao.AbstractObservableDao;
import it.uniroma2.mindharbor.dao.AppointmentDao;
import it.uniroma2.mindharbor.dao.PatientDao;
import it.uniroma2.mindharbor.dao.PsychologistDao;
import it.uniroma2.mindharbor.dao.csv.constants.AppointmentDaoCsvConstants;
import it.uniroma2.mindharbor.exception.DAOException;
import it.uniroma2.mindharbor.model.Appointment;
import it.uniroma2.mindharbor.model.Patient;
import it.uniroma2.mindharbor.model.Psychologist;
import it.uniroma2.mindharbor.patterns.facade.DaoFactoryFacade;
import it.uniroma2.mindharbor.patterns.observer.DaoOperation;
import it.uniroma2.mindharbor.utilities.CsvUtilities;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AppointmentDaoCsv extends AbstractObservableDao implements AppointmentDao {

    private static final Logger logger = Logger.getLogger(AppointmentDaoCsv.class.getName());
    private static final File appointmentFile = new File(AppointmentDaoCsvConstants.PATH_NAME_APPOINTMENTS);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_TIME;

    @Override
    public void saveAppointment(Appointment appointment, String patientUsername) throws DAOException {
        String[] appointmentRecord = new String[AppointmentDaoCsvConstants.HEADER.length];
        appointmentRecord[AppointmentDaoCsvConstants.APPOINTMENT_INDEX_ID] = String.valueOf(appointment.getId());
        appointmentRecord[AppointmentDaoCsvConstants.APPOINTMENT_INDEX_DATE] = appointment.getDate().format(DATE_FORMATTER);
        appointmentRecord[AppointmentDaoCsvConstants.APPOINTMENT_INDEX_TIME] = appointment.getTime().format(TIME_FORMATTER);
        appointmentRecord[AppointmentDaoCsvConstants.APPOINTMENT_INDEX_DESCRIPTION] = appointment.getDescription();
        appointmentRecord[AppointmentDaoCsvConstants.APPOINTMENT_INDEX_NOTIFIED] = String.valueOf(appointment.isNotified());
        appointmentRecord[AppointmentDaoCsvConstants.APPOINTMENT_INDEX_PATIENT_USERNAME] = patientUsername;

        CsvUtilities.writeFile(appointmentFile, appointmentRecord);

        Object[] syncPackage = {appointment, patientUsername};
        notifyObservers(DaoOperation.INSERT, "Appointment", String.valueOf(appointment.getId()), syncPackage);
    }

    @Override
    public Appointment retrieveAppointment(int appointmentId) throws DAOException {
        List<String[]> appointmentRecords = CsvUtilities.readAll(appointmentFile);
        if (!appointmentRecords.isEmpty() && "ID".equals(appointmentRecords.getFirst()[0])) {
            appointmentRecords.removeFirst();
        }

        for (String[] record : appointmentRecords) {
            if (Integer.parseInt(record[AppointmentDaoCsvConstants.APPOINTMENT_INDEX_ID]) == appointmentId) {
                return convertRecordToAppointment(record);
            }
        }
        return null;
    }

    @Override
    public List<Appointment> retrieveAllAppointments() throws DAOException {
        List<Appointment> allAppointments = new ArrayList<>();
        List<String[]> appointmentRecords = CsvUtilities.readAll(appointmentFile);

        if (!appointmentRecords.isEmpty() && "ID".equals(appointmentRecords.getFirst()[0])) {
            appointmentRecords.removeFirst(); // Rimuovi l'header
        }

        for (String[] record : appointmentRecords) {
            allAppointments.add(convertRecordToAppointment(record));
        }
        return allAppointments;
    }

    @Override
    public List<Appointment> retrieveAppointmentsByPatient(String patientUsername) throws DAOException {
        List<String[]> appointmentRecords = CsvUtilities.readAll(appointmentFile);
        List<Appointment> patientAppointments = new ArrayList<>();
        if (!appointmentRecords.isEmpty() && "ID".equals(appointmentRecords.getFirst()[0])) {
            appointmentRecords.removeFirst();
        }

        for (String[] record : appointmentRecords) {
            if (record.length > AppointmentDaoCsvConstants.APPOINTMENT_INDEX_PATIENT_USERNAME &&
                    patientUsername.equals(record[AppointmentDaoCsvConstants.APPOINTMENT_INDEX_PATIENT_USERNAME])) {
                patientAppointments.add(convertRecordToAppointment(record));
            }
        }
        return patientAppointments;
    }

    @Override
    public List<Appointment> retrieveAppointmentsByPsychologist(String psychologistUsername) throws DAOException {
        List<Appointment> psychologistAppointments = new ArrayList<>();
        DaoFactoryFacade daoFactoryFacade = DaoFactoryFacade.getInstance();
        try {
            PsychologistDao psychologistDao = daoFactoryFacade.getPsychologistDao();
            Psychologist psychologist = psychologistDao.retrievePsychologist(psychologistUsername);
            if (psychologist == null) return psychologistAppointments;

            PatientDao patientDao = daoFactoryFacade.getPatientDao();
            List<Patient> patients = patientDao.retrievePatientsByPsychologist(psychologist);

            for (Patient patient : patients) {
                psychologistAppointments.addAll(retrieveAppointmentsByPatient(patient.getUsername()));
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error retrieving appointments by psychologist", e);
            throw new DAOException("Error retrieving appointments by psychologist: " + e.getMessage(), e);
        }
        return psychologistAppointments;
    }

    @Override
    public List<Appointment> retrieveAppointmentsByDate(LocalDate date) throws DAOException {
        List<String[]> appointmentRecords = CsvUtilities.readAll(appointmentFile);
        List<Appointment> dateAppointments = new ArrayList<>();
        if (!appointmentRecords.isEmpty() && "ID".equals(appointmentRecords.getFirst()[0])) {
            appointmentRecords.removeFirst();
        }

        String dateString = date.format(DATE_FORMATTER);
        for (String[] record : appointmentRecords) {
            if (record[AppointmentDaoCsvConstants.APPOINTMENT_INDEX_DATE].equals(dateString)) {
                dateAppointments.add(convertRecordToAppointment(record));
            }
        }
        return dateAppointments;
    }

    @Override
    public List<Appointment> retrieveUnnotifiedAppointments(String patientUsername) throws DAOException {
        List<String[]> appointmentRecords = CsvUtilities.readAll(appointmentFile);
        List<Appointment> unnotifiedAppointments = new ArrayList<>();
        if (!appointmentRecords.isEmpty() && "ID".equals(appointmentRecords.getFirst()[0])) {
            appointmentRecords.removeFirst();
        }

        for (String[] record : appointmentRecords) {
            if (patientUsername.equals(record[AppointmentDaoCsvConstants.APPOINTMENT_INDEX_PATIENT_USERNAME]) &&
                    "false".equals(record[AppointmentDaoCsvConstants.APPOINTMENT_INDEX_NOTIFIED])) {
                unnotifiedAppointments.add(convertRecordToAppointment(record));
            }
        }
        return unnotifiedAppointments;
    }

    @Override
    public void updateAppointment(Appointment appointment) throws DAOException {
        List<String[]> appointmentRecords = CsvUtilities.readAll(appointmentFile);
        String[] header = appointmentRecords.removeFirst();
        boolean found = false;

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
            throw new DAOException(AppointmentDaoCsvConstants.APPOINTMENT_NOT_FOUND + appointment.getId());
        }
        CsvUtilities.updateFile(appointmentFile, header, appointmentRecords);
        notifyObservers(DaoOperation.UPDATE, "Appointment", String.valueOf(appointment.getId()), appointment);
    }

    @Override
    public void updateAppointmentNotificationStatus(int appointmentId, boolean notified) throws DAOException {
        List<String[]> appointmentRecords = CsvUtilities.readAll(appointmentFile);
        String[] header = appointmentRecords.removeFirst();
        boolean found = false;

        for (String[] record : appointmentRecords) {
            if (Integer.parseInt(record[AppointmentDaoCsvConstants.APPOINTMENT_INDEX_ID]) == appointmentId) {
                record[AppointmentDaoCsvConstants.APPOINTMENT_INDEX_NOTIFIED] = String.valueOf(notified);
                found = true;
                break;
            }
        }
        if (!found) {
            throw new DAOException(AppointmentDaoCsvConstants.APPOINTMENT_NOT_FOUND + appointmentId);
        }
        CsvUtilities.updateFile(appointmentFile, header, appointmentRecords);

        Appointment updatedAppointment = retrieveAppointment(appointmentId);
        if (updatedAppointment != null) {
            notifyObservers(DaoOperation.UPDATE, "Appointment", String.valueOf(appointmentId), updatedAppointment);
        }
    }

    @Override
    public void updateAppointmentsNotificationStatus(List<Appointment> appointments) throws DAOException {
        if (appointments == null || appointments.isEmpty()) {
            return;
        }
        List<String[]> appointmentRecords = CsvUtilities.readAll(appointmentFile);
        String[] header = appointmentRecords.removeFirst();

        Map<Integer, Boolean> appointmentsToUpdate = new HashMap<>();
        for (Appointment app : appointments) {
            appointmentsToUpdate.put(app.getId(), app.isNotified());
        }

        for (String[] record : appointmentRecords) {
            int recordId = Integer.parseInt(record[AppointmentDaoCsvConstants.APPOINTMENT_INDEX_ID]);
            if (appointmentsToUpdate.containsKey(recordId)) {
                record[AppointmentDaoCsvConstants.APPOINTMENT_INDEX_NOTIFIED] = String.valueOf(appointmentsToUpdate.get(recordId));
            }
        }
        CsvUtilities.updateFile(appointmentFile, header, appointmentRecords);

        for (Appointment appointment : appointments) {
            notifyObservers(DaoOperation.UPDATE, "Appointment", String.valueOf(appointment.getId()), appointment);
        }
    }

    @Override
    public void deleteAppointment(int appointmentId) throws DAOException {
        List<String[]> appointmentRecords = CsvUtilities.readAll(appointmentFile);
        String[] header = appointmentRecords.removeFirst();
        boolean removed = appointmentRecords.removeIf(record -> Integer.parseInt(record[AppointmentDaoCsvConstants.APPOINTMENT_INDEX_ID]) == appointmentId);

        if (!removed) {
            throw new DAOException(AppointmentDaoCsvConstants.APPOINTMENT_NOT_FOUND + appointmentId);
        }
        CsvUtilities.updateFile(appointmentFile, header, appointmentRecords);
        notifyObservers(DaoOperation.DELETE, "Appointment", String.valueOf(appointmentId), null);
    }

    @Override
    public boolean appointmentExists(int appointmentId) throws DAOException {
        return retrieveAppointment(appointmentId) != null;
    }

    @Override
    public int getNextAppointmentId() throws DAOException {
        List<String[]> appointmentRecords = CsvUtilities.readAll(appointmentFile);
        if (appointmentRecords.size() <= 1) { // Solo header o vuoto
            return 1;
        }
        appointmentRecords.removeFirst(); // Rimuovi header

        int maxId = 0;
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

    private Appointment convertRecordToAppointment(String[] record) throws DAOException {
        try {
            int id = Integer.parseInt(record[AppointmentDaoCsvConstants.APPOINTMENT_INDEX_ID]);
            LocalDate date = LocalDate.parse(record[AppointmentDaoCsvConstants.APPOINTMENT_INDEX_DATE], DATE_FORMATTER);
            LocalTime time = LocalTime.parse(record[AppointmentDaoCsvConstants.APPOINTMENT_INDEX_TIME], TIME_FORMATTER);
            String description = record[AppointmentDaoCsvConstants.APPOINTMENT_INDEX_DESCRIPTION];
            boolean notified = Boolean.parseBoolean(record[AppointmentDaoCsvConstants.APPOINTMENT_INDEX_NOTIFIED]);
            return new Appointment(id, date, time, description, notified);
        } catch (DateTimeParseException | NumberFormatException e) {
            throw new DAOException("Error converting CSV record to Appointment: " + e.getMessage(), e);
        }
    }
}