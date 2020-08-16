package com.example.helperbuddy;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

//Imported shared preferences package to store data

import com.example.helperbuddy.HelperClasses.sharePreference_OneTimeLogin;
import com.example.helperbuddy.Login;
import com.example.helperbuddy.R;

//class UserType extends AppCompatActivity

public class UserType extends AppCompatActivity {

    Button visitor;
    Button service;
    Button  tryAgain;
    LinearLayout internetCheck,DisplayDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {  //onCreate method
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_type);


        internetCheck=(LinearLayout) findViewById(R.id.No_internet);
        DisplayDetail=(LinearLayout) findViewById(R.id.user_type);


        tryAgain = findViewById(R.id.try_again);

        //if condition to test the network Availability

        if(!isNetworkAvailable()==true)
        {
            /*
            If network is not available then this  will show
            no internet connection.
             */

            internetCheck.setVisibility(View.VISIBLE);

            tryAgain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(UserType.this,UserType.class);
                    startActivity(intent);
                }
            });

        }

        /*
        If network is available then condition will
        show type of user want to choose
         */

        else if(isNetworkAvailable()==true)  //else if
        {

            DisplayDetail.setVisibility(View.VISIBLE);

            //Hook
            visitor = findViewById(R.id.visitor);

            //onClickListener for to perform the task of button

            visitor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String type="users";
                    sharePreference_OneTimeLogin shared= new sharePreference_OneTimeLogin(UserType.this);
                    shared.setType(type);

                    //Intent for redirection

                    Intent intent = new Intent(UserType.this, Login.class);
                    intent.putExtra("type","users");
                    startActivity(intent);


                }
            });

            service= findViewById(R.id.service);

            //onClickListener for to perform the task of button

            service.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String type="worker";
                    sharePreference_OneTimeLogin shared= new sharePreference_OneTimeLogin(UserType.this);
                    shared.setType(type);

                    //Intent for redirection

                    Intent intent = new Intent(UserType.this, Login.class);
                    intent.putExtra("type","worker");
                    startActivity(intent);

                }
            });
        }

    }

    /*

    Method wil check Network is available or not
    if available then it will return true else false

     */

    public boolean isNetworkAvailable() {     //method

        //ConnectivityManager object

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {


            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());

                //Testing all the type of network availabilities

                if (capabilities != null) {
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {

                        return true;
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {

                        return true;
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {

                        return true;
                    }
                }
            }
        }

        return false;

    }  //method



} //class


