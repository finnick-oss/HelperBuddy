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
import android.widget.Toast;

//Imported shared preferences package to store data

import com.example.helperbuddy.HelperClasses.ConnectivityCheck;
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

        ConnectivityCheck connectivityCheck = new ConnectivityCheck();


        /*
        If network is available then condition will
        show type of user want to choose
         */

       {

            DisplayDetail.setVisibility(View.VISIBLE);
            internetCheck.setVisibility(View.GONE);

            //Hook
            visitor = findViewById(R.id.visitor);

            //onClickListener for to perform the task of button

            visitor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(connectivityCheck.isNetworkAvailable(UserType.this)){
                        String type="users";
                        sharePreference_OneTimeLogin shared= new sharePreference_OneTimeLogin(UserType.this);
                        shared.setType(type);

                        //Intent for redirection
                        Intent intent = new Intent(UserType.this, Login.class);
                        intent.putExtra("type","users");
                        startActivity(intent);

                    }
                    else{
                        Toast.makeText(UserType.this, "(No Internet Connection !!!)", Toast.LENGTH_SHORT).show();
                    }

                }
            });

            service= findViewById(R.id.service);

            //onClickListener for to perform the task of button

            service.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(connectivityCheck.isNetworkAvailable(UserType.this)){
                        String type="worker";
                        sharePreference_OneTimeLogin shared= new sharePreference_OneTimeLogin(UserType.this);
                        shared.setType(type);

                        //Intent for redirection

                        Intent intent = new Intent(UserType.this, Login.class);
                        intent.putExtra("type","worker");
                        startActivity(intent);
                    }
                   else {
                        Toast.makeText(UserType.this, "(No Internet Connection !!!)", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }

    }

    /*

    Method wil check Network is available or not
    if available then it will return true else false

     */




} //class


