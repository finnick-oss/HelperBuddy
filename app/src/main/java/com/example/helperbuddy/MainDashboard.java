package com.example.helperbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.helperbuddy.HelperClasses.SectionsPagerAdapter;
import com.example.helperbuddy.HelperClasses.sharePreference_OneTimeLogin;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class MainDashboard extends AppCompatActivity {

    private static final String TAG = "MainDasboard";

    private SectionsPagerAdapter mSectionsPagerAdapter;


    private ViewPager mViewPager;

    TextView searchText,Location;
    FusedLocationProviderClient fusedLocationProviderClient;
    LinearLayout ManualLocation;
    Geocoder geocoder;
    FloatingActionButton floating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_dashboard);
        location();
        floating=findViewById(R.id.fab);
        Location =findViewById(R.id.Location);
        ManualLocation=findViewById(R.id.permission);
        /*
        If network is available then condition will
        show type of user want to choose
         */

         floating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intialize place field list
                Intent intent = new Intent(MainDashboard.this, HelpThem.class);
                startActivity(intent);
            }
        });

          ManualLocation.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        //Intialize place field list
        Intent intent = new Intent(MainDashboard.this, ManualyChangeLocation.class);
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


    private void location(){
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getLocation();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location != null) {

                    geocoder = new Geocoder(MainDashboard.this, Locale.getDefault());
                    try {
                        sharePreference_OneTimeLogin shared = new sharePreference_OneTimeLogin(MainDashboard.this);
                      //  sharePreference_OneTimeLogin shared = new sharePreference_OneTimeLogin(ManualyChangeLocation.this);
                        //shared.setLatitude(Double.toString( location.getLatitude()));//storing Latitude in  sharePreference
                        //shared.setLongitude(Double.toString(location.getLongitude()));



                        List<Address> addresses= geocoder.getFromLocation(Double.parseDouble(shared.getLatitude()),Double.parseDouble(shared.getLongitude()),1);
                     //  List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1  );


                        if(addresses.get(0).getSubLocality()==null) {
                            Location.setText( "Unnamed Road," + (addresses.get(0).getLocality()));
                        }
                        else if(addresses.get(0).getLocality()==null) {
                            Location.setText( addresses.get(0).getCountryName());
                        }
                        else{
                            Location.setText(addresses.get(0).getSubLocality() + "," + (addresses.get(0).getLocality()));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
            private String getCityNameByCoordinates(double lat, double lon) throws IOException {
                sharePreference_OneTimeLogin shared = new sharePreference_OneTimeLogin(MainDashboard.this);
                List<Address> addresses = geocoder.getFromLocation(Double.parseDouble(shared.getLatitude()),Double.parseDouble(shared.getLongitude()), 1);
                if (addresses != null && addresses.size() > 0) {
                    Toast.makeText(MainDashboard.this, "State="+addresses.get(0).getLocality(), Toast.LENGTH_SHORT).show();
                    return addresses.get(0).getLocality();
                }
                return null;
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



}