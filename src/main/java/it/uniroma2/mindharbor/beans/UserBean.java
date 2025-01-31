package it.uniroma2.mindharbor.beans;

public class UserBean extends CredentialsBean {
    private String name;
    private String surname;
    private String gender;

    public UserBean(String username, String password, String type, String name, String surname, String gender) {
        super(username, password, type);
        this.name = name;
        this.surname = surname;
        this.gender = gender;
    }

    public UserBean (String username, String password, String type) {
        super(username, password, type);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
