package it.uniroma2.mindharbor.utilities;

public enum UserType {
    PATIENT("PATIENT"),
    PSYCHOLOGIST("PSYCHOLOGIST");

    private final String type;

    UserType(String type) {this.type = type;}

    public String getType() {return type;}
}
