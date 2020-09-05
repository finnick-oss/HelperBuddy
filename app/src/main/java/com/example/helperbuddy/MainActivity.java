package com.example.helperbuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.helperbuddy.HelperClasses.sharePreference_OneTimeLogin;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {


    //in this main activity we are just checking the user
    // already exist or not

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Timer timer=new Timer();


        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                //if it will exist then it will go same page

                if(FirebaseAuth.getInstance().getCurrentUser()==null){
                    startActivity(new Intent(MainActivity.this,UserType.class));
                    finish();
                }
                else {
                    startActivity(new Intent(MainActivity.this,MainDashboard.class));
                    finish();
                }

            }
        },1500);

    }  //method
}  //class
