package it.uniroma2.mindharbor.patterns.observer;

public interface DaoObserver {
    void onAfterInsert(String entityType, String entityId, Object entity);
    void onAfterUpdate(String entityType, String entityId, Object entity);
    void onAfterDelete(String entityType, String entityId);
}
