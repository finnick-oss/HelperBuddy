package com.example.helperbuddy.HelperClasses;  //package

/*
Created a WorkerSignupHelper class which is simple POJO class
for getting and setting all the details of Workers
*/

import com.google.firebase.database.Exclude;

public class WorkerSignupHelper {   //This class is use to get all the details of Worker and used in WorkerSignup class

    //created all the attributes
    String name,username,occupation,address,description,phoneNo,password,images, Total_ratings,Total_users;
    Double latitude,longitude;
    @Exclude String id;


    public WorkerSignupHelper() {
    }

    public WorkerSignupHelper(String name, String username, String occupation, String address, String description, String phoneNo, String password, String images, String total_ratings, String total_users, Double latitude, Double longitude) {
        this.name = name;
        this.username = username;
        this.occupation = occupation;
        this.address = address;
        this.description = description;
        this.phoneNo = phoneNo;
        this.password = password;
        this.images = images;
        Total_ratings = total_ratings;
        Total_users = total_users;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getTotal_ratings() {
        return Total_ratings;
    }

    public void setTotal_ratings(String total_ratings) {
        Total_ratings = total_ratings;
    }

    public String getTotal_users() {
        return Total_users;
    }

    public void setTotal_users(String total_users) {
        Total_users = total_users;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}  //class