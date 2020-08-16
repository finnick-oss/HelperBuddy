package com.example.helperbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.helperbuddy.HelperClasses.UserHelperClass;
import com.example.helperbuddy.HelperClasses.sharePreference_OneTimeLogin;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.hbb20.CountryCodePicker;

public class Phoneno extends AppCompatActivity {

    TextInputLayout  regPhoneNo;
    Button verify, regToLoginBtn;
    CountryCodePicker Code_Picker;

    //firebase reference
    DatabaseReference storage;// variable for realtime database

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phoneno);
        regPhoneNo = findViewById(R.id.reg_phoneNo1);

        Code_Picker=findViewById(R.id.cpp2);
        verify = findViewById(R.id.verify_btn);

        regToLoginBtn = findViewById(R.id.signup_screen);

        regToLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Phoneno.this, Login.class);
                startActivity(intent);
            }
        });

        verify.setOnClickListener(new View.OnClickListener() {
            private Boolean validatePhoneNo() {
                String val = regPhoneNo.getEditText().getText().toString();

                if (val.isEmpty() || regPhoneNo.getEditText().getText().toString().length()!= 10) {
                    regPhoneNo.setError("Field cannot be empty");
                    Toast.makeText(Phoneno.this, "Enter valid no", Toast.LENGTH_SHORT).show();
                    return false;
                } else {
                    regPhoneNo.setError(null);
                    regPhoneNo.setEnabled(false);
                    return true;
                }
            }

            //This function will execute when user click on Register Button
            public void onClick(View view) {//verify all the thing
                if (!validatePhoneNo()) {
                    return;
                }



                final sharePreference_OneTimeLogin shared = new sharePreference_OneTimeLogin(Phoneno.this);
                //registering all the data in firebase
                final String phone = regPhoneNo.getEditText().getText().toString();
                final String countrycode = Code_Picker.getSelectedCountryCode();

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("" +shared.getType());
                Query checkUser = reference.orderByChild("phoneNo").equalTo(phone);

                checkUser.addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()) {

                            regPhoneNo.setError("User Already exist with this no.");
                            regPhoneNo.requestFocus();

                        } else {

                            if (shared.getType().equals("worker")) {

                                Intent intent = new Intent(Phoneno.this, VerificationWorkerActivity.class);
                                intent.putExtra("countryCode", countrycode);
                                intent.putExtra("phone_no", phone);
                                startActivity(intent);
                                finish();

                            } else if (shared.getType().equals("users")) {

                                Intent intent = new Intent(Phoneno.this, VerificationVisitorActivity.class);
                                intent.putExtra("countryCode", countrycode);
                                intent.putExtra("phone_no", phone);
                                startActivity(intent);
                                finish();
                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

        });

    } //onCreate

}  //class