package it.uniroma2.mindharbor.patterns.observer;

/**
 * Defines the contract for observable Data Access Objects in the Observer pattern implementation.
 * <p>
 * This interface enables DAO implementations to notify interested observers about
 * data changes (insertions, updates, deletions). It's a key component in the
 * cross-persistence synchronization system that keeps CSV and MySQL data stores
 * in sync automatically.
 * </p>
 * <p>
 * Classes implementing this interface can register multiple observers and
 * notify them when data operations occur, enabling real-time synchronization
 * between different persistence mechanisms.
 * </p>
 *
 * @see DaoObserver for the observer interface
 * @see it.uniroma2.mindharbor.sync.CrossPersistenceSyncObserver for concrete observer implementation
 * @see it.uniroma2.mindharbor.dao.AbstractObservableDao for base implementation
 */
public interface ObservableDao {

    /**
     * Registers an observer to receive notifications about data changes.
     * <p>
     * The observer will be notified whenever insert, update, or delete
     * operations are performed on this DAO. Duplicate observers are
     * typically ignored by implementations.
     * </p>
     *
     * @param observer The observer to register for notifications
     */
    void addObserver(DaoObserver observer);

    /**
     * Unregisters an observer from receiving notifications.
     * <p>
     * After removal, the observer will no longer receive notifications
     * about data changes in this DAO.
     * </p>
     *
     * @param observer The observer to remove from notifications
     */
    void removeObserver(DaoObserver observer);

    /**
     * Notifies all registered observers about a data operation.
     * <p>
     * This method is typically called by DAO implementations after
     * successfully completing a data operation to inform observers
     * about the change.
     * </p>
     *
     * @param operation The type of operation performed (INSERT, UPDATE, DELETE)
     * @param entityType The type of entity that was modified (e.g., "User", "Patient")
     * @param entityId The unique identifier of the affected entity
     * @param entity The entity object (for INSERT/UPDATE) or null (for DELETE)
     */
    void notifyObservers(DaoOperation operation, String entityType, String entityId, Object entity);
}
