package com.example.ilibrary.controller;

import com.example.ilibrary.model.Item;
import com.example.ilibrary.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AddItemDialogController implements Initializable {

    @FXML
    TextField titleTextField;
    @FXML
    TextField authorTextField;
    @FXML
    RadioButton availableButton;

    @FXML
    Label warningLabel;

    private Item item;

    public Item getItem() {
        return item;
    }

    private List<Item> items;

    private MainController mainController;

    public AddItemDialogController(List<Item> items, User user) {
        this.items = items;
        mainController = new MainController(user);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    // event handler to process the addition of a new item in the library collection
    @FXML
    public void onAddItemClick(ActionEvent event) {

        try {
            // check if all fields have a value
            if (titleTextField.getText().isEmpty() || authorTextField.getText().isEmpty()) {
                warningLabel.setText("Fill in all the fields!");
                mainController.pauseLabel(warningLabel);
            } else {
                // create the new item according to the data provided and then close the stage
                item = new Item(itemCode(), titleTextField.getText(), authorTextField.getText());
                item.setAvailability(availableButton.isSelected());
                Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                stage.close();
            }

        } catch (Exception e) {
            warningLabel.setText("Try again!");
            mainController.pauseLabel(warningLabel);
        }
    }

    // close the add item mode
    @FXML
    public void onCancelClick(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
    }

    // return a number, by one greater than the highest item code in the items list
    public Integer itemCode() {
        int highest = 1;
        for (Item i : items) {
            if (i.getItemCode() > highest) {
                highest = i.getItemCode();
            }
        }
        return highest + 1;
    }

}
