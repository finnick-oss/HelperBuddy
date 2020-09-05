package com.example.helperbuddy.HelperClasses;  //package

/*
Here is the shared preferences class
which will store user session data
and manage until user will not logout
 */
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;

import com.firebase.geofire.GeoLocation;

import java.util.Map;

public class sharePreference_OneTimeLogin {  //class

    //to create sharePreference file
    SharedPreferences sharedPreferences;   //Shared Preferences object

    //declared all attributes

  //  private String userEnteredUsername;
  // private String name,username,email,phoneNo;

   //method remove user
   private String type;
   private String latitude;
   private String longitude;


    public String getType() {
        type=sharedPreferences.getString("userdata","");
        return type;
    }
    //getter and setter
    public void setType(String type) {
        sharedPreferences.edit().putString("userdata",type).apply();
        this.type = type;

    }

    public String getLatitude() {
        latitude=sharedPreferences.getString("Latitude","");
        return latitude;
    }

    public void setLatitude(String latitude) {
        sharedPreferences.edit().putString("Latitude",latitude).apply();
        this.latitude = latitude;
    }

    public String getLongitude() {
        longitude=sharedPreferences.getString("Longitude","");
        return longitude;
    }

    public void setLongitude(String longitude) {
        sharedPreferences.edit().putString("Longitude",longitude).apply();
        this.longitude = longitude;
    }


    public void removeUser(){
        sharedPreferences.edit().clear();
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
