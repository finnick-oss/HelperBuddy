package com.example.helperbuddy;

/*
Activity is using bottom navigation
and intent for redirection the visitor to
 sign up page of worker
 */

//imported all the important classes and all the interfaces or packages


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

//this is for to use bottom navigation

import com.example.helperbuddy.Database.Database;
import com.example.helperbuddy.HelperClasses.Order;
import com.example.helperbuddy.ViewHolder.HistoryAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class HelpThem extends AppCompatActivity {  //class

          RelativeLayout helpThem;

          @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_them);
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavView_bar);
        bottomNav.setSelectedItemId(R.id.ic_help);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.ic_home:
                        startActivity(new Intent(getApplicationContext(),MainDashboard.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.ic_help:

                        return true;
                    case R.id.ic_profile:
                        startActivity(new Intent(getApplicationContext(),UserProfile.class));
                        overridePendingTransition(0,0);
                        return true;
                }

                return false;
            }
        });
        helpThem = findViewById(R.id.registerThem);

        /*
        when user will click on  bottom navigation
        helpthem button then it will redirect the visitor to
        signup  activity
         */

        helpThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //intent which is similar to redirect

                Intent intent = new Intent(HelpThem.this, WorkerSigup.class);
                intent.putExtra("type","users");
                startActivity(intent);

            }
        });


    }  //method


}  //class