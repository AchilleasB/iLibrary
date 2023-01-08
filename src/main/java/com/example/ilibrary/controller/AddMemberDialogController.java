package com.example.ilibrary.controller;


import com.example.ilibrary.model.Member;
import com.example.ilibrary.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class AddMemberDialogController implements Initializable {

    @FXML
    TextField firstNameTextField;
    @FXML
    TextField lastNameTextField;
    @FXML
    DatePicker dateOfBirthField;
    @FXML
    Label warningLabel;
    @FXML
    Label exampleLabel;

    private Member member;

    public Member getMember() {
        return member;
    }

    List<Member> members;

    private MainController mainController;

    public AddMemberDialogController(List<Member> members, User user) {
        this.members = members;
        mainController = new MainController(user);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    // add a new member in the member-TableView
    @FXML
    public void onAddMemberClick(ActionEvent event) {
        try {
            // check if all fields have a value
            if (firstNameTextField.getText().isEmpty() || lastNameTextField.getText().isEmpty() ||
                    (dateOfBirthField.getValue() == null && dateOfBirthField.getEditor().getText().isEmpty())) {
                warningLabel.setText("Fill in all the fields!");
                mainController.pauseLabel(warningLabel);
            } else {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
                LocalDate dateOfBirth;
                // create the new member according to the data provided by the user
                if (dateOfBirthField.getValue() != null) {

                    member = new Member(memberIdentifier(), firstNameTextField.getText(), lastNameTextField.getText(), dateOfBirthField.getValue());
                } else {
                    dateOfBirth = LocalDate.parse(dateOfBirthField.getEditor().getText(), formatter);
                    member = new Member(memberIdentifier(), firstNameTextField.getText(), lastNameTextField.getText(), dateOfBirth);
                }
                Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                stage.close();
            }
        } catch (Exception e) {
            warningLabel.setText("Change the date format!");
            exampleLabel.setText(" (e.g. 01/01/2001, DD/MM/YYYY ) ");
            mainController.pauseLabel(warningLabel);
            mainController.pauseLabel(exampleLabel);
        }
    }

    // close the dialog
    @FXML
    public void onCancelClick(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
    }

    // return a number greater than the highest member identifier in the members list
    public Integer memberIdentifier() {
        int highest = 1;
        for (Member m : members) {
            if (m.getIdentifier() > highest) {
                highest = m.getIdentifier();
            }
        }
        return highest + 1;
    }

}
