module com.example.miniproyecto3 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    opens com.example.miniproyecto3.controller to javafx.fxml;
    opens com.example.miniproyecto3.view to javafx.fxml;

    exports com.example.miniproyecto3;
}