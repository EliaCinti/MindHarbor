package it.uniroma2.mindharbor.patterns.factory;

import it.uniroma2.mindharbor.dao.PatientDao;
import it.uniroma2.mindharbor.dao.csv.PatientDaoCsv;
import it.uniroma2.mindharbor.dao.mysql.PatientDaoMySql;
import it.uniroma2.mindharbor.patterns.facade.PersistenceType;

/**
 * Factory class for creating instances of {@link PatientDao} according to the specified persistence type.
 * Using the Factory pattern, this class abstracts the instantiation of PatientDao objects, allowing the rest of the application
 * to remain decoupled from the specifics of data storage mechanisms, such as CSV files or MySQL databases.
 * <p>
 * This abstraction aids in the flexibility and scalability of the application by facilitating easy switches between different storage types.
 * </p>
 */
public class PatientDaoFactory {

    /**
     * Retrieves a {@link PatientDao} instance based on the specified persistence type.
     * This factory method delegates the instantiation to more specific methods based on the storage type,
     * thus encapsulating the creation logic and enhancing maintainability.
     *
     * @param persistenceType The type of persistence (e.g., CSV, MySQL), directing which DAO implementation to use.
     * @return A {@link PatientDao} instance that is appropriate for the specified persistence type.
     */
    public PatientDao getPatientDao(PersistenceType persistenceType) {
        return switch (persistenceType) {
            case CSV -> createPatientDaoCsv();
            case MYSQL -> createPatientDaoMySql();
        };
    }

    /**
     * Creates and returns a new instance of {@link PatientDaoCsv}, specifically configured for handling patient data in CSV format.
     * This method provides a concrete implementation of {@link PatientDao} optimized for file-based data storage.
     *
     * @return A newly created instance of {@link PatientDaoCsv}.
     */
    private PatientDao createPatientDaoCsv() {
        return new PatientDaoCsv();
    }


    /**
     * Creates and returns a new instance of a {@link PatientDao} for MySQL database storage.
     * Currently, this method returns a {@link PatientDaoCsv} instance as a placeholder due to the lack of a MySQL implementation.
     * This should be replaced with a MySQL-specific DAO once implemented.
     *
     * @return A {@link PatientDao} instance appropriate for MySQL database operations.
     */
    private PatientDao createPatientDaoMySql() {
        return new PatientDaoMySql();
    }
}
