package it.uniroma2.mindharbor.beans;

public class CredentialsBean {
    private String username;
    private String password;
    private String type;

    public CredentialsBean(String username) {
        // quando voglio informazioni sullo user
        this.username = username;
    }

    public CredentialsBean(String username, String password) {
        // validate login
        this(username);
        this.password = password;
    }

    public CredentialsBean(String username, String password, String type) {
        // save new user
        this(username, password);
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
