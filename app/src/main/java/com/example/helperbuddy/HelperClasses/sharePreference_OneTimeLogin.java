package com.example.helperbuddy.HelperClasses;  //package

/*
Here is the shared preferences class
which will store user session data
and manage until user will not logout
 */

import android.content.Context;
import android.content.SharedPreferences;

public class sharePreference_OneTimeLogin {  //class

    //to create sharePreference file
    SharedPreferences sharedPreferences;   //Shared Preferences object

    //declared all attributes

    private String userEnteredUsername;
   private String name,username,email,phoneNo;

   //method remove user
   private String type;


    public String getType() {
        type=sharedPreferences.getString("userdata","");
        return type;
    }

    public void setType(String type) {
        this.type = type;
        sharedPreferences.edit().putString("userdata",type).commit();
    }
    public void removeUser(){
        sharedPreferences.edit().clear().commit();
    }


    //getter and setter

    public  String getUsername() {
        userEnteredUsername=sharedPreferences.getString("userdata","");
        return userEnteredUsername;
    }

    public void setUsername(String userEnteredUsername) {
        this.userEnteredUsername = userEnteredUsername;
        sharedPreferences.edit().putString("userdata",userEnteredUsername).commit();
    }

    public String getName() {
        name=sharedPreferences.getString("userdata","");
        return name;
    }

    public void setName(String name) {
        sharedPreferences.edit().putString("userdata",name).commit();
        this.name = name;
    }

    public String getUserrname() {
        username=sharedPreferences.getString("userdata","");
        return username;
    }

    public void setUserrname(String username) {
        sharedPreferences.edit().putString("userdata",username).commit();
        this.username = username;
    }

    public String getEmail() {
        email=sharedPreferences.getString("userdata","");
        return email;
    }

    public void setEmail(String email) {
        sharedPreferences.edit().putString("userdata",email).commit();
        this.email = email;
    }

    public String getPhoneNo() {
        phoneNo=sharedPreferences.getString("userdata","");
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        sharedPreferences.edit().putString("userdata",phoneNo).commit();
        this.phoneNo = phoneNo;
    }

    //context pass the preference to another class
   public Context context;
    //creating constructor to pass memory at runtime to the sharedfile
    //for one time login
    public sharePreference_OneTimeLogin( Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("userInfo",context.MODE_PRIVATE);

    }  //method

}  //class
