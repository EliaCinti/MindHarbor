package it.uniroma2.mindharbor.exception;

public class DAOException extends Exception {

    public DAOException() {
        super();
    }

    public DAOException(String messaggio, Throwable causa) {
        super(messaggio, causa);
    }

    public DAOException(String messaggio) {
        super(messaggio);
    }
}
