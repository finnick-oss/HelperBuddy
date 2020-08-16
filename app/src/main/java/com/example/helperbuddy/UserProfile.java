package com.example.helperbuddy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.helperbuddy.Database.Database;
import com.example.helperbuddy.HelperClasses.Order;
import com.example.helperbuddy.HelperClasses.UserHelperClass;
import com.example.helperbuddy.HelperClasses.WorkerSignupHelper;
import com.example.helperbuddy.HelperClasses.sharePreference_OneTimeLogin;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.helperbuddy.HelperClasses.sharePreference_OneTimeLogin;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class UserProfile extends AppCompatActivity {
    TextView name, phoneNo, password;
    TextView occupation,fullName  ;
    TextView  logout;
    CircleImageView imageView;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        BottomNavigationView bottomNav = findViewById(R.id.bottomNavView_bar);
        bottomNav.setSelectedItemId(R.id.ic_profile);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.ic_home:
                        startActivity(new Intent(getApplicationContext(),MainDashboard.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.ic_help:
                        startActivity(new Intent(getApplicationContext(),HelpThem.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.ic_profile:
                        return true;
                }

                return false;
            }
        });
        final sharePreference_OneTimeLogin shared = new sharePreference_OneTimeLogin(UserProfile.this);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();

        DatabaseReference databaseReference=firebaseDatabase.getReference("" +shared.getType()).child(firebaseAuth.getUid());

        name=findViewById(R.id.name);
        phoneNo=findViewById(R.id.phone);
        password=findViewById(R.id.password);
        imageView=findViewById(R.id.profile_image);
        occupation=findViewById(R.id.profile_occupation);
        fullName=findViewById(R.id.profile_name);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserHelperClass userHelperClass=snapshot.getValue(UserHelperClass.class);
                fullName.setText(userHelperClass.getUsername());
                name.setText(userHelperClass.getName());
                phoneNo.setText(userHelperClass.getPhoneNo());
                password.setText(userHelperClass.getPassword());

                Picasso.get().load( userHelperClass.getImages()).into(imageView);

                WorkerSignupHelper WorkerHelperClass=snapshot.getValue(WorkerSignupHelper.class);
                fullName.setText( WorkerHelperClass.getUsername());
                occupation.setText( WorkerHelperClass.getOccupation());
                name.setText( WorkerHelperClass.getName());
                phoneNo.setText( WorkerHelperClass.getPhoneNo());
                password.setText( WorkerHelperClass.getPassword());
                Picasso.get().load( WorkerHelperClass.getImages()).into(imageView);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        logout=findViewById(R.id.profile_logout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Database(getBaseContext()).clearCart(new Order());
                new sharePreference_OneTimeLogin(UserProfile.this).removeUser();
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(UserProfile.this, UserType.class);
                            // set the new task and clear flags
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);//all the previous activity will be cleared
                startActivity(i);
            }
        });
    }}





