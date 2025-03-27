package it.uniroma2.mindharbor.patterns.factory;

import it.uniroma2.mindharbor.dao.AppointmentDao;
import it.uniroma2.mindharbor.dao.csv.AppointmentDaoCsv;
import it.uniroma2.mindharbor.dao.mysql.AppointmentDaoMySql;
import it.uniroma2.mindharbor.patterns.facade.PersistenceType;

public class AppointmentDaoFactory {

    public AppointmentDao getAppointmentDao(PersistenceType persistenceType) {
        return switch (persistenceType) {
            case CSV -> createAppointmentDaoCsv();
            case MYSQL -> createAppointmentDaoMySql();
        };
    }

    private AppointmentDao createAppointmentDaoCsv() {
        return new AppointmentDaoCsv();
    }

    private AppointmentDao createAppointmentDaoMySql() {
        return new AppointmentDaoMySql();
    }
}
