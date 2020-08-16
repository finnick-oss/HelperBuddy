package com.example.helperbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.helperbuddy.HelperClasses.SectionsPagerAdapter;
import com.example.helperbuddy.HelperClasses.sharePreference_OneTimeLogin;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

public class MainDashboard extends AppCompatActivity {

    private static final String TAG = "MainDasboard";

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;
    ImageButton imageButton;
    TextView searchText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_dashboard);



        /*
        If network is available then condition will
        show type of user want to choose
         */



            imageButton = findViewById(R.id.search_btn);
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainDashboard.this, Search.class);
                    startActivity(intent);
                }
            });

            searchText = findViewById(R.id.search_field);
            searchText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainDashboard.this, Search.class);
                    startActivity(intent);
                }
            });

            mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

            mViewPager = (ViewPager) findViewById(R.id.container);
            setupViewPager(mViewPager);

            TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(mViewPager);

            tabLayout.getTabAt(0).setText("Home");
            tabLayout.getTabAt(1).setText("History");


            BottomNavigationView bottomNav = findViewById(R.id.bottomNavView_bar);
            bottomNav.setSelectedItemId(R.id.ic_home);
            bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.ic_help:
                            startActivity(new Intent(getApplicationContext(), HelpThem.class));
                            overridePendingTransition(0, 0);
                            return true;
                        case R.id.ic_home:
                            return true;
                        case R.id.ic_profile:
                            startActivity(new Intent(getApplicationContext(), UserProfile.class));
                            overridePendingTransition(0, 0);
                            return true;
                    }

                    return false;
                }
            });


    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void setupViewPager(ViewPager viewPager) {
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Dashboard());
        adapter.addFragment(new History());
        viewPager.setAdapter(adapter);
    }

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

    }
}