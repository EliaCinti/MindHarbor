package it.uniroma2.mindharbor.exception;

import java.io.Serial;

public class UserSessionException extends Exception {

    @Serial
    private static final long serialVersionUID = 3L;

    public UserSessionException() {super("The user hasn't been defined yet");}

    public UserSessionException(String message) {
        super(message);
    }
}
