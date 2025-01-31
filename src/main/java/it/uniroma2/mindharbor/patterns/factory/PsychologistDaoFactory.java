package it.uniroma2.mindharbor.patterns.factory;

import it.uniroma2.mindharbor.dao.PsychologistDao;
import it.uniroma2.mindharbor.dao.csv.PsychologistDaoCsv;
import it.uniroma2.mindharbor.dao.mysql.PsychologistDaoMySql;
import it.uniroma2.mindharbor.patterns.facade.PersistenceType;

/**
 * Factory class for creating instances of {@link PsychologistDao} based on the persistence type.
 * <p>
 * This class follows the Factory pattern to abstract the creation of DAOs for psychologists.
 * Depending on the specified {@link PersistenceType}, it creates a CSV-based or MySQL-based DAO.
 * </p>
 */
public class PsychologistDaoFactory {
    /**
     * Retrieves a {@link PsychologistDao} instance based on the specified persistence type.
     *
     * @param persistenceType The type of persistence to use (e.g., CSV, MySQL).
     * @return A {@link PsychologistDao} instance corresponding to the specified persistence type.
     */
    public PsychologistDao getPsychologistDao(PersistenceType persistenceType) {
        return switch (persistenceType) {
            case CSV -> createPsychologistDaoCsv();
            case MYSQL -> createPsychologistDaoMySql();
        };
    }

    /**
     * Creates a {@link PsychologistDao} instance for CSV-based persistence.
     *
     * @return A new instance of {@link PsychologistDaoCsv}.
     */
    public PsychologistDao createPsychologistDaoCsv() {
        return new PsychologistDaoCsv();
    }

    /**
     * Creates a {@link PsychologistDao} instance for MySQL-based persistence.
     *
     * @return A new instance of {@link PsychologistDaoMySql}.
     */
    public PsychologistDao createPsychologistDaoMySql() {
        return new PsychologistDaoMySql();
    }
}
