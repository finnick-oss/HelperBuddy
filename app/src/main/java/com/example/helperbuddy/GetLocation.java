package com.example.helperbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.helperbuddy.Common.Common;
import com.example.helperbuddy.HelperClasses.sharePreference_OneTimeLogin;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class GetLocation extends AppCompatActivity {
    FusedLocationProviderClient fusedLocationProviderClient;
    public GeoLocation CURRENT_LOCATION1;
    LinearLayout LocationPermission;
    TextView ManualLocation;
    int PERMISSION_ALL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_location);

        ManualLocation = findViewById(R.id.ManualLocation);
        ManualLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GetLocation.this, ManualyChangeLocation.class);
                startActivity(intent);

            }
        });
        LocationPermission = (LinearLayout) findViewById(R.id.permission);
        LocationPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String[] PERMISSIONS = new String[0];
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    PERMISSIONS = new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION};
                }

                fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(GetLocation.this);

                if (ActivityCompat.checkSelfPermission(GetLocation.this, Arrays.toString(PERMISSIONS)) == PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(GetLocation.this, PERMISSIONS, PERMISSION_ALL);
                    getLocation();

                } else {
                    ActivityCompat.requestPermissions(GetLocation.this, PERMISSIONS, PERMISSION_ALL);

                }

            }

        });

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Thanks for enabling the permission", Toast.LENGTH_SHORT).show();
                getLocation();
                Intent intent = new Intent(GetLocation.this,MainDashboard.class);
                startActivity(intent);
                //do something permission is allowed here....

            } else {
                Intent intent = new Intent(GetLocation.this,ManualyChangeLocation.class);
                startActivity(intent);
                Toast.makeText(this, "Please allow the Permission", Toast.LENGTH_SHORT).show();


            }

        }


    }

    public void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //   ActivityCompat.requestPermissions(GetLocation.this,PackageManager.PERMISSION_GRANTED,);
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
                    Geocoder geocoder = new Geocoder(GetLocation.this, Locale.getDefault());
                    try {
                        List<Address> addresses = geocoder.getFromLocation(
                                location.getLatitude(), location.getLongitude(), 1
                        );
                        sharePreference_OneTimeLogin shared = new sharePreference_OneTimeLogin(GetLocation.this);
                        shared.setLatitude(Double.toString(location.getLatitude()));//storing Latitude in  sharePreference
                        shared.setLongitude(Double.toString(location.getLongitude()));

                        CURRENT_LOCATION1 = new GeoLocation(+addresses.get(0).getLatitude(), +addresses.get(0).getLongitude());


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

    }}

