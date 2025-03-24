module it.uniroma2.mindharbor {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.opencsv;
    requires jbcrypt;


    opens it.uniroma2.mindharbor to javafx.fxml;
    opens it.uniroma2.mindharbor.graphic_controller to javafx.fxml;
    exports it.uniroma2.mindharbor;

}