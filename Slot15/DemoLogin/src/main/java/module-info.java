module com.hsf302.hieutm.demologin {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.hsf302.hieutm.demologin to javafx.fxml;
    exports com.hsf302.hieutm.demologin;
}