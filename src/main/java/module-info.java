module it.uniroma2.mindharbor {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.slf4j;
    requires java.sql;
    requires com.opencsv;


    opens it.uniroma2.mindharbor to javafx.fxml;
    exports it.uniroma2.mindharbor;
}