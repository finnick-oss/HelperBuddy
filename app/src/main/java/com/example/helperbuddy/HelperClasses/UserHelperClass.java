package com.example.helperbuddy.HelperClasses;   //package

/*
Created a user helper class which is simple POJO class
for getting and setting all the details of Visitors
 */

public class UserHelperClass {  //class

    //declaring all the attributes

    String name,username,email,phoneNo,password,images;

    public UserHelperClass() {     } //Constructor default


    //constructor using fields
    public UserHelperClass(String name, String username, String email, String phoneNo, String password,String images) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.phoneNo = phoneNo;
        this.password = password;
        this.images = images;
    }


    /*
    Here are the getters and setter for
    getting the values or setting the
    values
     */

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

}  //class
