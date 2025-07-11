package it.uniroma2.mindharbor.patterns.factory;

import it.uniroma2.mindharbor.dao.AppointmentDao;
import it.uniroma2.mindharbor.dao.csv.AppointmentDaoCsv;
import it.uniroma2.mindharbor.dao.mysql.AppointmentDaoMySql;
import it.uniroma2.mindharbor.patterns.facade.PersistenceType;

/**
 * Factory class for creating AppointmentDao instances based on persistence type.
 * <p>
 * This factory implementation follows the Factory Method pattern to abstract
 * the creation of AppointmentDao objects. It allows the application to work
 * with different persistence mechanisms (CSV files or MySQL database) without
 * coupling the client code to specific implementations.
 * </p>
 * <p>
 * The factory supports two persistence strategies:
 * <ul>
 *   <li><strong>CSV</strong>: File-based storage using CSV format</li>
 *   <li><strong>MySQL</strong>: Relational database storage</li>
 * </ul>
 * </p>
 *
 * @see AppointmentDao for the common interface
 * @see it.uniroma2.mindharbor.dao.csv.AppointmentDaoCsv for CSV implementation
 * @see it.uniroma2.mindharbor.dao.mysql.AppointmentDaoMySql for MySQL implementation
 */
public class AppointmentDaoFactory {

    /**
     * Creates an AppointmentDao instance appropriate for the specified persistence type.
     * <p>
     * This method encapsulates the instantiation logic and returns the correct
     * implementation based on the persistence strategy. The returned DAO
     * implements the Observer pattern for cross-persistence synchronization.
     * </p>
     *
     * @param persistenceType The type of persistence mechanism to use
     * @return An AppointmentDao implementation suitable for the specified persistence type
     * @throws IllegalArgumentException if the persistence type is not supported
     */
    public AppointmentDao getAppointmentDao(PersistenceType persistenceType) {
        return switch (persistenceType) {
            case CSV -> createAppointmentDaoCsv();
            case MYSQL -> createAppointmentDaoMySql();
        };
    }

    /**
     * Creates a new instance of the CSV-based AppointmentDao implementation.
     * <p>
     * This method provides a concrete AppointmentDao that stores appointment
     * data in CSV files. The implementation includes observer capabilities
     * for cross-persistence synchronization.
     * </p>
     *
     * @return A new AppointmentDaoCsv instance
     */
    private AppointmentDao createAppointmentDaoCsv() {
        return new AppointmentDaoCsv();
    }

    /**
     * Creates a new instance of the MySQL-based AppointmentDao implementation.
     * <p>
     * This method provides a concrete AppointmentDao that stores appointment
     * data in a MySQL database. The implementation includes observer capabilities
     * for cross-persistence synchronization.
     * </p>
     *
     * @return A new AppointmentDaoMySql instance
     */
    private AppointmentDao createAppointmentDaoMySql() {
        return new AppointmentDaoMySql();
    }
}
