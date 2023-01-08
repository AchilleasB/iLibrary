package com.example.ilibrary.model;


import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class Item implements Serializable {

    private int itemCode;
    private String title;
    private String author;
    private boolean available;
    private String availability;
    private LocalDate lentDate;

    public Item(int itemCode, String title, String author) {
        this.itemCode = itemCode;
        this.title = title;
        this.author = author;
        this.available = true;
        this.availability = "Yes";
        this.lentDate = null;
    }

    public int getItemCode() {
        return itemCode;
    }

    public void setItemCode(int itemCode) {
        this.itemCode = itemCode;
    }


    public boolean isAvailable() {
        return available;
    }

    public void setAvailability(boolean available) {
        this.available = available;
        if (available){
            this.availability = "Yes";
        }else {
            this.availability = "No";
        }
    }

    public String getAvailability() {
        return availability;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public LocalDate getLentDate() {
        return lentDate;
    }

    public void setLentDate(LocalDate lentDate) {
        this.lentDate = lentDate;
    }

    public void clearLentDate() {
        this.lentDate = null;
    }

}
