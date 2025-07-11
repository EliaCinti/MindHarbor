package it.uniroma2.mindharbor.patterns.observer;

/**
 * Enumeration of database operations that can be observed in the DAO layer.
 * <p>
 * This enum defines the three fundamental CRUD operations that trigger
 * observer notifications in the cross-persistence synchronization system.
 * Each operation type may carry different data and require different
 * handling by observers.
 * </p>
 * <p>
 * Operation characteristics:
 * <ul>
 *   <li><strong>INSERT</strong>: Creates new entities, typically with complete Bean objects</li>
 *   <li><strong>UPDATE</strong>: Modifies existing entities, typically with Model objects</li>
 *   <li><strong>DELETE</strong>: Removes entities, requires only entity identifier</li>
 * </ul>
 * </p>
 *
 * @see ObservableDao#notifyObservers(DaoOperation, String, String, Object)
 * @see DaoObserver for methods handling each operation type
 */
public enum DaoOperation {
    /**
     * Represents an entity insertion operation.
     * <p>
     * Triggered when new entities are created in the persistence layer.
     * Observers typically receive complete Bean objects containing all
     * necessary data for replication.
     * </p>
     */
    INSERT,

    /**
     * Represents an entity update operation.
     * <p>
     * Triggered when existing entities are modified in the persistence layer.
     * Observers typically receive Model objects with updated field values.
     * </p>
     */
    UPDATE,

    /**
     * Represents an entity deletion operation.
     * <p>
     * Triggered when entities are removed from the persistence layer.
     * Observers receive entity identifiers but no entity objects, as the
     * entity no longer exists.
     * </p>
     */
    DELETE
}
