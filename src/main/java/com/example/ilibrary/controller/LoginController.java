package com.example.ilibrary.controller;

import com.example.ilibrary.data.Database;
import com.example.ilibrary.iLibrary;
import com.example.ilibrary.model.User;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.List;

public class LoginController {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;
    @FXML
    private Label messageLabel;

    private User loggedInUser;

    // check login, close login stage and open the main view
    // pass the user object to the main controller
    @FXML
    public void onLoginClick(ActionEvent event) {
        if (checkLogin()) {

            try {
                Stage stage = (Stage) loginButton.getScene().getWindow();
                //stage.close();
                //
                FXMLLoader fxmlLoader = new FXMLLoader(iLibrary.class.getResource("main-view.fxml"));
                // create a controller object to pass the user object
                // and set as controller for the main view
                MainController mainController = new MainController((loggedInUser));
                fxmlLoader.setController(mainController);

                Scene scene = new Scene(fxmlLoader.load());
                // scene.getStylesheets().add(getClass().getResource("css/style.css").toExternalForm());
                stage.setTitle("iLibrary");
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                System.err.printf("Error: %s%n", e.getMessage());
            }
        }
    }

    // check if the username and password belong to a user
    // if they do, log in
    // otherwise display a message to the user
    public boolean checkLogin() {

        Database database = new Database();
        List<User> users = database.getUsers();

        for (User user : users) {
            if (usernameField.getText().equals(user.getUsername()) && passwordField.getText().equals(user.getPassword())) {
                loggedInUser = user;
                return true;
            }
        }

        messageLabel.setText("Invalid username/password combination!");
        pauseLabel(messageLabel);
        usernameField.setText("");
        passwordField.setText("");
        return false;
    }

    public void pauseLabel(Label label) {
        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(e -> label.setText(null));
        pause.play();
    }
}

