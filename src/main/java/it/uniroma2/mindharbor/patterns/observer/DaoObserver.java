package it.uniroma2.mindharbor.patterns.observer;

/**
 * Defines the contract for observers in the DAO Observer pattern implementation.
 * <p>
 * This interface allows objects to receive notifications when data changes
 * occur in observable DAOs. It's primarily used by the cross-persistence
 * synchronization system to maintain data consistency between CSV files
 * and MySQL database.
 * </p>
 * <p>
 * Each method corresponds to a specific type of database operation and
 * receives appropriate data for that operation type:
 * <ul>
 *   <li><strong>Insert</strong>: Complete entity data for replication</li>
 *   <li><strong>Update</strong>: Modified entity data for synchronization</li>
 *   <li><strong>Delete</strong>: Entity identifier for removal</li>
 * </ul>
 * </p>
 *
 * @see ObservableDao for the observable interface
 * @see it.uniroma2.mindharbor.sync.CrossPersistenceSyncObserver for concrete implementation
 * @see DaoOperation for operation types
 */
public interface DaoObserver {

    /**
     * Called after an entity has been successfully inserted into the data store.
     * <p>
     * This method receives the complete entity data, typically as a Bean object
     * from the UI layer. Observers can use this data to replicate the insertion
     * in other persistence systems or perform additional processing.
     * </p>
     *
     * @param entityType The type of entity that was inserted (e.g., "Patient", "Psychologist")
     * @param entityId The unique identifier of the inserted entity
     * @param entity The complete entity data, typically a Bean object
     */
    void onAfterInsert(String entityType, String entityId, Object entity);

    /**
     * Called after an entity has been successfully updated in the data store.
     * <p>
     * This method receives the updated entity data, typically as a Model object
     * containing the new field values. Observers can use this data to replicate
     * the update in other persistence systems.
     * </p>
     *
     * @param entityType The type of entity that was updated
     * @param entityId The unique identifier of the updated entity
     * @param entity The updated entity data, typically a Model object
     */
    void onAfterUpdate(String entityType, String entityId, Object entity);

    /**
     * Called after an entity has been successfully deleted from the data store.
     * <p>
     * This method receives only the entity identifier, as the entity no longer
     * exists. Observers can use the identifier to replicate the deletion in
     * other persistence systems.
     * </p>
     *
     * @param entityType The type of entity that was deleted
     * @param entityId The unique identifier of the deleted entity
     */
    void onAfterDelete(String entityType, String entityId);
}
