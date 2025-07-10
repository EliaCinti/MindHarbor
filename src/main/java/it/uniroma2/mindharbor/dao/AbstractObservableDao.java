package it.uniroma2.mindharbor.dao;

import it.uniroma2.mindharbor.patterns.observer.DaoObserver;
import it.uniroma2.mindharbor.patterns.observer.DaoOperation;
import it.uniroma2.mindharbor.patterns.observer.ObservableDao;

import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.List;

public abstract class AbstractObservableDao implements ObservableDao {
    private static final Logger logger = Logger.getLogger(AbstractObservableDao.class.getName());

    private final List<DaoObserver> observers = new ArrayList<>();

    @Override
    public synchronized void addObserver(DaoObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    @Override
    public synchronized void removeObserver(DaoObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(DaoOperation operation, String entityType, String entityId, Object entity) {
        List<DaoObserver> observersCopy = new ArrayList<>(observers);

        for (DaoObserver observer : observersCopy) {
            try {
                switch (operation) {
                    case INSERT -> observer.onAfterInsert(entityType, entityId, entity);
                    case UPDATE -> observer.onAfterUpdate(entityType, entityId, entity);
                    case DELETE -> observer.onAfterDelete(entityType, entityId);
                }
            } catch (Exception e) {
                logger.severe("Observer notification failed: " + e.getMessage());
            }
        }
    }
}
