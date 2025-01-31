package it.uniroma2.mindharbor.beans;

public class PsychologistBean extends UserBean {
    private String office;
    private double hourlyCost;

    public PsychologistBean(String username, String password, String type, String name, String surname, String gender, double hourlyCost, String office) {
        super(username, password, type, name, surname, gender);
        this.office = office;
        this.hourlyCost = hourlyCost;
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public double getHourlyCost() {
        return hourlyCost;
    }

    public void setHourlyCost(double hourlyCost) {
        this.hourlyCost = hourlyCost;
    }
}
