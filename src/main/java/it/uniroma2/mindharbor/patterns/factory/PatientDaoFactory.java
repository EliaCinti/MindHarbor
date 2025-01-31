package it.uniroma2.mindharbor.patterns.factory;

import it.uniroma2.mindharbor.dao.PatientDao;
import it.uniroma2.mindharbor.dao.csv.PatientDaoCsv;
import it.uniroma2.mindharbor.patterns.facade.PersistenceType;

/**
 * Factory class for creating instances of {@link PatientDao} based on the persistence type.
 * <p>
 * This class follows the Factory pattern to abstract the creation of DAOs for different
 * persistence mechanisms (e.g., CSV, MySQL).
 * </p>
 */
public class PatientDaoFactory {

    /**
     * Retrieves a {@link PatientDao} instance based on the specified persistence type.
     *
     * @param persistenceType The type of persistence (e.g., CSV, MySQL).
     * @return A {@link PatientDao} instance corresponding to the specified persistence type.
     */
    public PatientDao getPatientDao(PersistenceType persistenceType) {
        return switch (persistenceType) {
            case CSV -> createPatientDaoCsv();
            case MYSQL -> createPatientDaoMySql();
        };
    }

    /**
     * Creates a {@link PatientDao} instance for CSV-based persistence.
     *
     * @return A new instance of {@link PatientDaoCsv}.
     */
    public PatientDao createPatientDaoCsv() {
        return new PatientDaoCsv();
    }

    /**
     * Creates a {@link PatientDao} instance for MySQL-based persistence.
     * <p>
     * This method currently returns a {@link PatientDaoCsv} instance as a placeholder.
     * In a real implementation, it should return a MySQL-specific DAO.
     * </p>
     *
     * @return A new instance of {@link PatientDao}.
     */
    public PatientDao createPatientDaoMySql() {
        return new PatientDaoCsv();
    }
}
