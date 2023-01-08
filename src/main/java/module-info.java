module com.example.ilibrary {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.ilibrary to javafx.fxml;
    opens com.example.ilibrary.controller to javafx.fxml;
    opens com.example.ilibrary.model to javafx.fxml;

    exports com.example.ilibrary;
    exports com.example.ilibrary.controller;
    exports com.example.ilibrary.model;
}