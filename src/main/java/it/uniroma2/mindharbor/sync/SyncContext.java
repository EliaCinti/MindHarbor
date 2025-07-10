package it.uniroma2.mindharbor.sync;

/**
 * Manages the synchronization context to prevent infinite loops between DAO observers.
 * It uses a ThreadLocal variable to track whether the current thread is already
 * performing a synchronization operation.
 */
public class SyncContext {

    // ThreadLocal a-side: if a thread is syncing, it will have a "true" value
    private static final ThreadLocal<Boolean> isSyncing = ThreadLocal.withInitial(() -> false);

    private SyncContext() {
        // Private constructor to prevent instantiation
    }

    /**
     * Marks the beginning of a synchronization operation for the current thread.
     */
    public static void startSync() {
        isSyncing.set(true);
    }

    /**
     * Marks the end of a synchronization operation for the current thread.
     */
    public static void endSync() {
        isSyncing.remove(); // Clean up the ThreadLocal
    }

    /**
     * Checks if the current thread is already in a synchronization process.
     *
     * @return true if the thread is currently syncing, false otherwise.
     */
    public static boolean isSyncing() {
        return isSyncing.get();
    }
}
