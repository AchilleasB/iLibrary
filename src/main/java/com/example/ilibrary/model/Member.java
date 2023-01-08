package com.example.ilibrary.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Member implements Serializable {

    private int identifier;
    private String firstname;
    private String lastname;
    private LocalDate dateOfBirth;

    public List<Item> borrowedItems;

    public int getIdentifier() {
        return identifier;
    }

    public void setIdentifier(int identifier) {
        this.identifier = identifier;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Member(int identifier, String firstname, String lastname, LocalDate dateOfBirth) {
        this.identifier = identifier;
        this.firstname = firstname;
        this.lastname = lastname;
        this.dateOfBirth = dateOfBirth;
        this.borrowedItems = new ArrayList<>();
    }

    public void addItem(Item item){
        borrowedItems.add(item);
    }

//    public void removeItem(Item item){
//        borrowedItems.remove(item);
//    }
}
