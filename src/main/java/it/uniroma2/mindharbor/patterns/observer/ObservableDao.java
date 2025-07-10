package it.uniroma2.mindharbor.patterns.observer;

public interface ObservableDao {
    void addObserver(DaoObserver observer);
    void removeObserver(DaoObserver observer);
    void notifyObservers(DaoOperation operation, String entityType, String entityId, Object entity);
}
