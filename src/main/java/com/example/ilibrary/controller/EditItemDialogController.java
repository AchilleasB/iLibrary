package com.example.ilibrary.controller;


import com.example.ilibrary.model.Item;
import com.example.ilibrary.model.User;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class EditItemDialogController implements Initializable {

    @FXML
    Label itemCodeLabel;
    @FXML
    RadioButton availableButton;
    @FXML
    TextField titleTextField;
    @FXML
    TextField authorTextField;
    @FXML
    Label warningLabel;

    public Item getItem() {
        return selectedItem;
    }

    private Item selectedItem;
    private ObservableList<Item> items;

    private MainController mainController;

    public EditItemDialogController(Item selectedItem, ObservableList<Item> items, User user) {
        this.selectedItem = selectedItem;
        this.items = items;
        mainController = new MainController(user);
    }

    // set the text fields with values received from the selected item
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        itemCodeLabel.setText(String.valueOf(selectedItem.getItemCode()));
        titleTextField.setText(selectedItem.getTitle());
        authorTextField.setText(selectedItem.getAuthor());
    }

    // event handler to process the editing of an item
    @FXML
    public void onEditItemClick(ActionEvent event) {

        try {
            if (titleTextField.getText().isEmpty() || authorTextField.getText().isEmpty()) {
                warningLabel.setText("Fill in all the fields");
                mainController.pauseLabel(warningLabel);
            } else {

                selectedItem.setItemCode(Integer.parseInt(itemCodeLabel.getText()));
                selectedItem.setAvailability(availableButton.isSelected());
                selectedItem.setTitle(titleTextField.getText());
                selectedItem.setAuthor(authorTextField.getText());

                Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                stage.close();
            }

        } catch (Exception e) {
            warningLabel.setText("Try again!");
            mainController.pauseLabel(warningLabel);
        }
    }

    // event handler to cancel the editing of an item mode
    @FXML
    public void onCancelClick(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
