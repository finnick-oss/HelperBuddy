package com.example.helperbuddy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.helperbuddy.HelperClasses.sharePreference_OneTimeLogin;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class ManualyChangeLocation extends AppCompatActivity {
    FusedLocationProviderClient fusedLocationProviderClient;
    EditText EditLocation;
    TextView CurrentLocation;
    public Place place;
    public LatLng queriedLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manualy_change_location);
        EditLocation=findViewById(R.id.LocationManually);
        CurrentLocation=findViewById(R.id.CurrentLocation);
        location();
        Places.initialize(getApplicationContext(),"AIzaSyBhuQLWGqMssuofXgMstMWu_URVrT7Q-cw");

        EditLocation.setFocusable(false);
        EditLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intialize place field list
                List<Place.Field> fieldList= Arrays.asList(Place.Field.ADDRESS,Place.Field.LAT_LNG,Place.Field.NAME);

                Intent intent=new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY,fieldList).build(ManualyChangeLocation.this);
                startActivityForResult(intent,100);
            }
        });
        CurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(ManualyChangeLocation.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ManualyChangeLocation.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
                            Geocoder geocoder = new Geocoder(ManualyChangeLocation.this, Locale.getDefault());
                            try {
                                List<Address> addresses = geocoder.getFromLocation(
                                        location.getLatitude(), location.getLongitude(), 1

                                );
                                EditLocation.setText(addresses.get(0).getSubLocality() + "," + (addresses.get(0).getLocality()));
                                sharePreference_OneTimeLogin shared = new sharePreference_OneTimeLogin(ManualyChangeLocation.this);
                                shared.setLatitude(Double.toString( location.getLatitude()));//storing Latitude in  sharePreference
                                shared.setLongitude(Double.toString(location.getLongitude()));
                                Intent intent = new Intent(ManualyChangeLocation.this, MainDashboard.class);
                                startActivity(intent);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                });
                //Intialize place field list

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

         if(requestCode==100 && resultCode==RESULT_OK){
            //when Success
            place=Autocomplete.getPlaceFromIntent(data);
            EditLocation.setText(place.getName());
            queriedLocation = place.getLatLng();
            Log.v("Latitude is", "" + queriedLocation.latitude);
            Log.v("Longitude is", "" + queriedLocation.longitude);
             sharePreference_OneTimeLogin shared = new sharePreference_OneTimeLogin(ManualyChangeLocation.this);
             shared.setLatitude(Double.toString(queriedLocation.latitude));//storing Latitude in  sharePreference
             shared.setLongitude(Double.toString(queriedLocation.longitude));

             Intent intent=new Intent(ManualyChangeLocation.this,MainDashboard.class);
             startActivity(intent);
        }
        else if(resultCode== AutocompleteActivity.RESULT_ERROR){
            Status status=Autocomplete.getStatusFromIntent(data);
            Toast.makeText(getApplicationContext(),status.getStatusCode(),Toast.LENGTH_SHORT).show();
        }

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
                    Geocoder geocoder = new Geocoder(ManualyChangeLocation.this, Locale.getDefault());
                    try {
                        List<Address> addresses = geocoder.getFromLocation(
                                location.getLatitude(), location.getLongitude(), 1

                        );
                        CurrentLocation.setText("Current Location = "+addresses.get(0).getSubLocality() + "," + (addresses.get(0).getLocality()));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }

}