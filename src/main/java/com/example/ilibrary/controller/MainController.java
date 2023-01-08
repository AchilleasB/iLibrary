package com.example.ilibrary.controller;

import com.example.ilibrary.data.Database;
import com.example.ilibrary.model.Item;
import com.example.ilibrary.model.Member;
import com.example.ilibrary.model.User;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private Label nameLabel;
    @FXML
    private TextField itemCodeLendField;
    @FXML
    private TextField memberIdLendField;
    @FXML
    private Button lendItemButton;
    @FXML
    private TextField itemCodeReceiveField;
    @FXML
    private Button receiveItemButton;
    @FXML
    private Label lendMessageLabel;
    @FXML
    private Label delayLabel;
    @FXML
    private Label receiveMessageLabel;

    @FXML
    private TableView<Item> itemView;
    @FXML
    private TableColumn<Item, Integer> itemCodeColumn;
    @FXML
    private TableColumn<Item, String> availableColumn;
    @FXML
    private TableColumn<Item, String> titleColumn;
    @FXML
    private TableColumn<Item, String> authorColumn;
    @FXML
    private TableColumn<Item, LocalDate> returnDateColumn;

    @FXML
    private Button addItemButton;
    @FXML
    private Button editItemButton;
    @FXML
    private Button deleteItemButton;
    @FXML
    private TextField searchField;
    @FXML
    private Button searchButton;
    @FXML
    private Button refreshButton;
    @FXML
    private Label itemMessageLabel;
    @FXML
    private Label memberMessageLabel;
    @FXML
    private TableView<Member> memberView;
    @FXML
    private TableColumn<Member, Integer> identifierColumn;
    @FXML
    private TableColumn<Member, String> firstNameColumn;
    @FXML
    private TableColumn<Member, String> lastNameColumn;
    @FXML
    private TableColumn<Member, LocalDate> dateOfBirthColumn;
    @FXML
    private Button addMemberButton;
    @FXML
    private Button editMemberButton;
    @FXML
    private Button deleteMemberButton;

    @FXML
    private TextField itemMessageTextField;

    private User loggedInUser;
    public Database database;
    private ObservableList<Item> items;
    private ObservableList<Member> members;
    private ObservableList<Item> matchingItems;
    private Item selectedItem;
    private Member selectedMember;

    // constructor, gets user from login, initializes database to access items and members
    public MainController(User user) {
        this.loggedInUser = user;
        database = new Database();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // display the full name of the person who logged in
        nameLabel.setText(loggedInUser.getFullname(loggedInUser.fullname));

        // add items to the observable list of items
        items = FXCollections.observableArrayList(database.getItems());
        // display the item collection in the Collection tab
        loadItemView();

        // add members to the observable list of members
        members = FXCollections.observableArrayList(database.getMembers());
        // display the member collection in the Members tab
        loadMemberView();

    }

    public void loadItemView() {
        setUpItemTableColumns();
        itemView.setItems(items);
    }

    public void loadMemberView() {
        setUpMemberTableColumns();
        memberView.setItems(members);
    }

    public void setUpItemTableColumns() {
        // set columns for the items collection
        itemCodeColumn.setCellValueFactory(new PropertyValueFactory<Item, Integer>("itemCode"));
        availableColumn.setCellValueFactory(new PropertyValueFactory<Item, String>("availability"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<Item, String>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<Item, String>("author"));
    }

    public void setUpMemberTableColumns() {
        // set columns for the members collection
        identifierColumn.setCellValueFactory(new PropertyValueFactory<Member, Integer>("identifier"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<Member, String>("firstname"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<Member, String>("lastname"));
        dateOfBirthColumn.setCellValueFactory(new PropertyValueFactory<Member, LocalDate>("dateOfBirth"));
    }

    /* The following code operates on the Lending/Receiving tab */

    @FXML
    public void onLendItemClick(ActionEvent event) {
        try {
            if (!itemCodeLendField.getText().isEmpty() && !memberIdLendField.getText().isEmpty()){
                int itemCode = Integer.parseInt(itemCodeLendField.getText());
                int memberId = Integer.parseInt(memberIdLendField.getText());

                // get the selected item(to be lent) from the item collection
                selectedItem = itemToBeLend(itemCode);
                // get the member to which the item will be lent
                selectedMember = memberToReceiveItem(memberId);

                if (selectedItem == null || selectedMember == null) {
                    lendMessageLabel.setText("Provide correct item code and member id!");
                } else{
                    if (selectedItem.isAvailable()) {
                        selectedMember.addItem(selectedItem);
                        lendMessageLabel.setText("The item was lent successfully");
                        selectedItem.setAvailability(false);
                        selectedItem.setLentDate(LocalDate.now());
                        itemView.refresh();
                        updateFiles();
                    } else {

                        System.out.println(selectedItem.getLentDate());
                        lendMessageLabel.setText("Item is not available !");
                    }
                }

            }else{
                lendMessageLabel.setText("Fill in the fields!");
            }

            resetLendTextFields();
            pauseLabel(lendMessageLabel);
        } catch (NumberFormatException e) {
            lendMessageLabel.setText("The fields take only numerical values!");
            pauseLabel(lendMessageLabel);
            resetLendTextFields();
        }
    }

    // method to find the item that will be lent in the items list
    public Item itemToBeLend(int itemCode) {

        for (Item i : items) {
            if (i.getItemCode() == itemCode) {
                return i;
            }
        }
        return null;
    }

    // method to find the member that will receive the item
    public Member memberToReceiveItem(int identifier) {

        Member memberToReceiveItem;

        for (Member m : members) {
            if (m.getIdentifier() == identifier) {
                memberToReceiveItem = m;
                return memberToReceiveItem;
            }
        }
        return null;
    }

    // method to pause the label display for 3 seconds
    public void pauseLabel(Label label) {
        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(e -> label.setText(null));
        pause.play();
    }

    // method to reset the text fields in the lend screen
    public void resetLendTextFields() {
        itemCodeLendField.setText("");
        memberIdLendField.setText("");
    }


    // event handler to process the receiving of an item back to the library
    @FXML
    public void onReceiveItemClick(ActionEvent event) {
        try {
            if (!itemCodeReceiveField.getText().isEmpty()) {
                // get the user input
                int code = Integer.parseInt(itemCodeReceiveField.getText());

                //get the selected item from the list of lent items and update the listView
                selectedItem = itemToBeReceived(code);

                if (selectedItem == null) {
                    receiveMessageLabel.setText("No item to receive!");
                } else {
                    //selectedMember.removeItem(selectedItem);
                    receiveMessageLabel.setText("The item was received successfully!");
                    displayDelayedReturn(selectedItem);
                    updateReceivedItemStatus(selectedItem.getItemCode());
                    itemView.refresh();
                    updateFiles();

                    System.out.println(selectedItem.getLentDate() + selectedItem.getTitle() + selectedItem.getItemCode());
                }
            } else {
                receiveMessageLabel.setText("Type in an item code!");
            }

            pauseLabel(receiveMessageLabel);
            itemCodeReceiveField.setText("");
        } catch (NumberFormatException e) {
            receiveMessageLabel.setText("The field takes only numerical values!");
            pauseLabel(receiveMessageLabel);
            receiveMessageLabel.setText("");
        }
    }

    //return the item that is about to be received and remove it from the member's list of borrowed items
    public Item itemToBeReceived(int itemCode) {

        for (Member m : database.getMembers()) {
            for (Item i: m.borrowedItems) {
                if (itemCode == i.getItemCode()){
                    m.borrowedItems.remove(i);
                    return i;
                }
            }
        }
        return null;
    }

    public void updateReceivedItemStatus(int itemCode){
        for (Item i:items) {
            if (i.getItemCode() == itemCode){
                i.setAvailability(true);
                i.clearLentDate();
            }
        }
    }

    public void updateFiles(){
        database.saveItemsToFile(items);
        database.saveMembersToFile(members);
    }

    // check for how long the item was lent out and if there is a delay in returning it
    public int daysOfItemDelay(Item receivingItem) {
        if (receivingItem != null){
            long daysOfLending = ChronoUnit.DAYS.between(receivingItem.getLentDate(), LocalDate.now());
            int daysOfDelay = (int) daysOfLending - 21;
            return daysOfDelay;
        }

        return 0;
    }

    public void displayDelayedReturn(Item item){
        int days = daysOfItemDelay(item);
        if (days > 0){
            delayLabel.setText("The item is " + days + "late.");
        }
    }


    /*  end of Lendind/Receiving tab */

    /* The following code operates on the Collection tab */

    public void onSearchClick(ActionEvent event){
        try{
            String query = searchField.getText();

            // create list of books that match the search query
            ObservableList<Item> matchingItems = FXCollections.observableArrayList();

            for (Item item : items) {
                if (item.getTitle().contains(query) || item.getAuthor().contains(query)) {
                    matchingItems.add(item);
                }
            }

            // update table with matching books
            if (searchField.getText().isEmpty()){
                itemView.setItems(items);
            }else {
                itemView.setItems(matchingItems);
                searchField.setText("");
            }

        } catch (Exception e) {
            itemMessageLabel.setText("Try again!");
        }
    }

    public void onRefreshClick (ActionEvent event){
        itemView.setItems(items);
    }

    // event handler to load a dialog to process the addition of an item to the library collection
    public void onAddItemClick(ActionEvent event) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/ilibrary/addItem-dialog-view.fxml"));
            AddItemDialogController addItemDialogController = new AddItemDialogController(items, loggedInUser);

            loadDialog(fxmlLoader, addItemDialogController, "Add item");

            // get the new item created in the add mode and add to the items list
            if (addItemDialogController.getItem() != null) {
                items.add(addItemDialogController.getItem());
                database.saveItemsToFile(items);
            }
        } catch (Exception e) {
            itemMessageLabel.setText("Try again!");
        }

    }

    // event handler to load a dialog to process the editing of an item in the library collection

    @FXML
    public void onEditItemClick(ActionEvent event) {
        try {
            Item selectedItem = itemView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/ilibrary/editItem-dialog-view.fxml"));
                EditItemDialogController editItemDialogController = new EditItemDialogController(selectedItem, items, loggedInUser);
                items.remove(selectedItem);

                loadDialog(fxmlLoader, editItemDialogController, "Edit item");

                // get the edited item from the edit mode and add it to the items list
                if (editItemDialogController.getItem() != null) {
                    items.add(editItemDialogController.getItem());
                    database.saveItemsToFile(items);
                }
            } else {
                itemMessageLabel.setText("You haven't selected an item!");
                pauseLabel(itemMessageLabel);
            }
        } catch (Exception e) {
            itemMessageLabel.setText("Try again!");
        }
    }

    // remove the selected item from the items list and save the change
    @FXML
    public void onDeleteItemClick(ActionEvent event) {
        selectedItem = itemView.getSelectionModel().getSelectedItem();
        items.remove(selectedItem);
        database.saveItemsToFile(items);
    }

    /*  end of Collection tab */

    /* The following code operates on the Members tab */

    // event handler to load a dialog to process the addition of a member to the members collection
    @FXML
    public void onAddMemberClick(ActionEvent event) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/ilibrary/addMember-dialog-view.fxml"));
            AddMemberDialogController addMemberDialogController = new AddMemberDialogController(members, loggedInUser);

            // load a new window that allows to add a new member
            loadDialog(fxmlLoader, addMemberDialogController, "Add member");

            // get the new member created in the add mode and add to the members list
            if (addMemberDialogController.getMember() != null) {
                members.add(addMemberDialogController.getMember());
                database.saveMembersToFile(members);
            }
        } catch (Exception e) {
            memberMessageLabel.setText("Try again!");
        }

    }

    // event handler to load a dialog to process the editing of a member in the members collection
    @FXML
    public void onEditMemberClick(ActionEvent event) throws IOException {

        try {
            Member selectedMember = memberView.getSelectionModel().getSelectedItem();
            if (selectedMember != null) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/ilibrary/editMember-dialog-view.fxml"));
                EditMemberDialogController editMemberDialogController = new EditMemberDialogController(selectedMember, members, loggedInUser);
                members.remove(selectedMember);

                // load a new window that allows to edit an existing member
                loadDialog(fxmlLoader, editMemberDialogController, "Edit member");

                // get the edited member from the edit mode and add it to the members list
                if (editMemberDialogController.getMember() != null) {
                    members.add(editMemberDialogController.getMember());
                    database.saveMembersToFile(members);
                }
            } else {
                memberMessageLabel.setText("You haven't selected a member!");
                pauseLabel(memberMessageLabel);
            }
        } catch (Exception e) {
            memberMessageLabel.setText("Try again!");
        }

    }

    // remove the selected member from the members list and save the change
    @FXML
    public void onDeleteMemberClick(ActionEvent event) {
        selectedMember = memberView.getSelectionModel().getSelectedItem();
        members.remove(selectedMember);
        database.saveMembersToFile(members);
    }

    // method to load different dialogs
    public void loadDialog(FXMLLoader fxmlLoader, Object object, String title) {
        try {
            fxmlLoader.setController(object);
            Scene scene = new Scene(fxmlLoader.load());
            Stage dialog = new Stage();

            // stage can be created only once
            dialog.initModality(Modality.APPLICATION_MODAL);

            dialog.setScene(scene);
            dialog.setTitle(title);
            dialog.showAndWait();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
