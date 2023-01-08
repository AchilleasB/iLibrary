package com.example.ilibrary.data;

import com.example.ilibrary.model.Item;
import com.example.ilibrary.model.Member;
import com.example.ilibrary.model.User;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Database {

    private List<User> users = new ArrayList<>();

    public List<Item> items = new ArrayList<>();

    private List<Member> members = new ArrayList<>();

    public List<User> getUsers() {
        return users;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(Item item) {
        items.add(item);
    }

    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(Member member) {
        members.add(member);
    }

    File itemFile = new File("item_collection.txt");
    File memberFile = new File("member_collection.txt");
    File userFile = new File("user_collection.txt");

    public Database() {

        // populate the users list with data from file
        if(userFile.exists()){
            readUsersFromFile();
        }
        else {
            users.addAll(List.of(
                    new User("admin", "1234", "John", "Doe"),
                    new User("achil", "1234", "Achilleas", "Ballanos"),
                    new User("maria", "1234", "Maria", "Mak")
            ));
            saveUsersToFile(users);
        }

        // populate the items list with data from file
        if(itemFile.exists()){
            readItemsFromFile();
        }
        else {
            items.addAll(List.of(
                    new Item(11, "The Lord Of The Rings", "J.R.R. Tolkien"),
                    new Item(12,  "The Little Prince", "Antoine de Saint-Exupery"),
                    new Item(13,   "Harry Potter and the Philosopher’s Stone", "J.K. Rowling"),
                    new Item(14,   "The Hobbit", "J.R.R. Tolkien"),
                    new Item(15,   "The Dream Of The Red Chamber", "Cao Xueqin")
            ));
            saveItemsToFile(items);
        }

        // populate the members list with data from file
        if(memberFile.exists()){
            readMembersFromFile();
        }
        else{
            members.addAll(List.of(
                    new Member(101, "Tony", "Stark", LocalDate.of(1972, 3, 9)),
                    new Member(102, "Steve", "Rogers", LocalDate.of(1921, 8, 22)),
                    new Member(103, "Bruce", "Banners", LocalDate.of(1981, 12, 4)),
                    new Member(104, "Peter", "Parker", LocalDate.of(2000, 6, 17)),
                    new Member(105, "Thor", "Odin", LocalDate.of(560, 5, 5))
            ));
            saveMembersToFile(members);
        }

    }

    // the following methods write/save the data from a list to a file accordingly
    //

    private void saveUsersToFile(List<User> users) {
        try (FileOutputStream fos = new FileOutputStream(userFile);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            for (User user : users) {
                oos.writeObject(user);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveItemsToFile(List<Item> items) {

        try (FileOutputStream fos = new FileOutputStream(itemFile);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            for (Item item : items) {
                oos.writeObject(item);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void saveMembersToFile(List<Member> members) {
        try (FileOutputStream fos = new FileOutputStream(memberFile);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            for (Member member : members) {
                oos.writeObject(member);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // the following methods read from file and populate a list accordingly
    //
    private void readUsersFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(userFile))) {
            while (true) {
                try {
                    User user = (User) ois.readObject();
                    users.add(user);
                } catch (EOFException | ClassNotFoundException eofe) {
                    break; // break out of the loop
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void readItemsFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(itemFile))) {
            while (true) {
                try {
                    Item item = (Item) ois.readObject();
                    items.add(item);
                } catch (EOFException | ClassNotFoundException eofe) {
                    break; // break out of the loop
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void readMembersFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(memberFile))) {
            while (true) {
                try {
                    Member member = (Member) ois.readObject();
                    members.add(member);
                } catch (EOFException | ClassNotFoundException eofe) {
                    break; // break out of the loop
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
