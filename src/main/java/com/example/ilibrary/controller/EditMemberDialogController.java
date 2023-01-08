package com.example.ilibrary.controller;

import com.example.ilibrary.model.Member;
import com.example.ilibrary.model.User;
import javafx.collections.ObservableList;
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
import java.util.Objects;
import java.util.ResourceBundle;

public class EditMemberDialogController implements Initializable {

    @FXML
    Label memberIdentifierLabel;
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

    public Member getMember() {
        return selectedMember;
    }

    private Member selectedMember;
    private ObservableList<Member> members;
    private LocalDate dateOfBirth;
    private MainController mainController;

    public EditMemberDialogController(Member selectedMember, ObservableList<Member> members, User user) {
        this.selectedMember = selectedMember;
        this.members = members;
        mainController = new MainController(user);
    }

    // set the text fields with values received from the selected member
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        memberIdentifierLabel.setText(String.valueOf(selectedMember.getIdentifier()));
        firstNameTextField.setText(selectedMember.getFirstname());
        lastNameTextField.setText(selectedMember.getLastname());

        dateOfBirth = selectedMember.getDateOfBirth();
        dateOfBirthField.setValue(selectedMember.getDateOfBirth());

    }

    // event handler to process the editing of a library member
    @FXML
    public void onEditMemberClick(ActionEvent event) {
        try {
            if (firstNameTextField.getText().isEmpty() || lastNameTextField.getText().isEmpty() ||
                    (dateOfBirthField.getValue() == null && dateOfBirthField.getEditor().getText().isEmpty())) {
                warningLabel.setText("Fill in all the fields");
                mainController.pauseLabel(warningLabel);
            } else {
                updateSelectedMember();

                Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                stage.close();
            }
        } catch (Exception e) {
            warningLabel.setText("Change the date format!");
            exampleLabel.setText("(e.g. 01/01/2001, DD/MM/YYYY )");
            mainController.pauseLabel(warningLabel);
            mainController.pauseLabel(exampleLabel);
        }
    }

    // method to update the selected member in the members' list
    public void updateSelectedMember() {
        selectedMember.setFirstname(firstNameTextField.getText());
        selectedMember.setLastname(lastNameTextField.getText());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
        LocalDate newDateOfBirth = LocalDate.parse(dateOfBirthField.getEditor().getText(), formatter);

        if (isDateOfBirthChanged(dateOfBirth, newDateOfBirth)) {
            selectedMember.setDateOfBirth(dateOfBirthField.getValue());
        } else {
            selectedMember.setDateOfBirth(newDateOfBirth);
        }
    }

    // method to check if the date of birth text on the date picker was changed by the user
    public boolean isDateOfBirthChanged(LocalDate oldDateOfBirth, LocalDate newDateOfBirth) {
        return Objects.equals(oldDateOfBirth, newDateOfBirth);
    }

    // close the dialog
    @FXML
    public void onCancelClick(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
