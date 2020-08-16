package com.example.helperbuddy.HelperClasses;

public class Order {
    private String name;
    private String occupation;
    private String address;
    private String description;
    private String phoneNo;
    private String images;

    public Order() {
    }

    public Order(String name, String occupation, String address, String description, String phoneNo,String images) {
        this.name = name;
        this.occupation = occupation;
        this.address = address;
        this.description = description;
        this.phoneNo = phoneNo;
        this.images=images;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }
}
