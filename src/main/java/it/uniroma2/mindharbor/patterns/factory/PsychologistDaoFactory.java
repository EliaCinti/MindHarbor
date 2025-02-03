package it.uniroma2.mindharbor.patterns.factory;

import it.uniroma2.mindharbor.dao.PsychologistDao;
import it.uniroma2.mindharbor.dao.csv.PsychologistDaoCsv;
import it.uniroma2.mindharbor.dao.mysql.PsychologistDaoMySql;
import it.uniroma2.mindharbor.patterns.facade.PersistenceType;

/**
 * Factory class for creating instances of {@link PsychologistDao} based on the specified persistence type.
 * This class implements the Factory pattern to encapsulate the instantiation of PsychologistDao objects,
 * allowing for flexible data storage options via CSV or MySQL databases depending on runtime decisions.
 * <p>
 * Usage of this factory ensures that the rest of the application remains decoupled from the specifics
 * of data persistence mechanism implementations.
 * </p>
 */
public class PsychologistDaoFactory {
    /**
     * Retrieves a {@link PsychologistDao} instance tailored to the specified persistence type.
     * This method uses the Factory pattern to instantiate DAOs dynamically based on the persistence
     * mechanism specified, which can be either CSV or MySQL. This method simplifies the switch between different
     * data storage strategies without modifying the consuming code.
     *
     * @param persistenceType The type of persistence to use (e.g., CSV, MySQL), dictated by the needs of the application context.
     * @return A {@link PsychologistDao} instance appropriate for the specified persistence type.
     */
    public PsychologistDao getPsychologistDao(PersistenceType persistenceType) {
        return switch (persistenceType) {
            case CSV -> createPsychologistDaoCsv();
            case MYSQL -> createPsychologistDaoMySql();
        };
    }

    /**
     * Creates and returns a new instance of {@link PsychologistDaoCsv}, which handles data operations
     * for psychologists in a CSV file format. This method provides a specific implementation of {@link PsychologistDao}
     * that is optimized for CSV data manipulation.
     *
     * @return A newly instantiated {@link PsychologistDaoCsv}.
     */
    private PsychologistDao createPsychologistDaoCsv() {
        return new PsychologistDaoCsv();
    }


    /**
     * Creates and returns a new instance of {@link PsychologistDaoMySql}, which manages data operations
     * for psychologists using a MySQL database. This method provides a specific implementation of {@link PsychologistDao}
     * that leverages SQL for efficient data retrieval and storage.
     *
     * @return A newly instantiated {@link PsychologistDaoMySql}.
     * @todo Implement and return a MySQL-specific DAO for psychologist data management.
     */
    private PsychologistDao createPsychologistDaoMySql() {
        return new PsychologistDaoMySql();
    }
}
