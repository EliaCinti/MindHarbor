package it.uniroma2.mindharbor.app_controller;

import it.uniroma2.mindharbor.exception.UserSessionException;
import it.uniroma2.mindharbor.model.User;

public abstract class AbstractController {
    protected abstract void storeUserSession(User user) throws UserSessionException;
}
