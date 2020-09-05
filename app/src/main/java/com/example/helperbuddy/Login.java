package com.example.helperbuddy;

/*
Activity is for login both user and visitor
 */


//imported all the important classes and all the interfaces or packages


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.helperbuddy.HelperClasses.UserHelperClass;
import com.example.helperbuddy.HelperClasses.sharePreference_OneTimeLogin;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

public class Login extends AppCompatActivity {  //class

    //all the views and button

    Button callSignUp, login_btn,forgetPass;
    ImageView image;
    TextView logoText, sloganText;
    TextInputLayout password;
    TextInputLayout  regPhoneNo;


    CountryCodePicker Code_Picker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); //This Line will hide the status bar from the screen

        setContentView(R.layout.activity_login);

        //Hooks for getting the views

        callSignUp = findViewById(R.id.signup_screen);
        image = findViewById(R.id.image);
        logoText = findViewById(R.id.text);
        sloganText = findViewById(R.id.slogan_name);
        regPhoneNo = findViewById(R.id.reg_phoneNo);
        Code_Picker=findViewById(R.id.cpp1);
        login_btn = findViewById(R.id.login_btn);
        password = findViewById(R.id.password);
        forgetPass=findViewById(R.id.forgetPassword);

        //----------------------------------------------
        final sharePreference_OneTimeLogin shared = new sharePreference_OneTimeLogin(Login.this);

        Toast.makeText(this, "Welcome " +shared.getType(), Toast.LENGTH_SHORT).show();

        //on clicking on sign up button it will redirect the user to sign up page
        callSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(Login.this, Phoneno.class);
                startActivity(intent);

            }

        });


        //By clicking on register button data will be send to server(firebase)

        login_btn.setOnClickListener(new View.OnClickListener() {


            //This function will execute when user click on Register Button


            //validating all the necessary things

            private Boolean validatePhoneNo() {
                String val = regPhoneNo.getEditText().getText().toString();

                if (val.isEmpty() || regPhoneNo.getEditText().getText().toString().length()!=10 ) {
                    regPhoneNo.setError("Field cannot be empty");
                    Toast.makeText(Login.this, "Enter valid no", Toast.LENGTH_SHORT).show();
                    return false;
                }
                else {
                    regPhoneNo.setError(null);
                    regPhoneNo.setEnabled(false);
                    return true;
                }
            }
            private Boolean validatePassword() {
                String val = password.getEditText().getText().toString();
                if (val.isEmpty()) {
                    password.setError("Field cannot be empty");
                    return false;
                } else {
                    password.setError(null);
                    password.setErrorEnabled(false);
                    return true;
                }
            }

            public void onClick(View view) {
                //Validate Login Info
                Toast.makeText(Login.this, "Sending Details", Toast.LENGTH_LONG).show();
                if ( !validatePhoneNo()| !validatePassword()) {
                    return;
                } else {
                    isUser();
                }
            }

            private void isUser() {  //method is to test the user credentials is correct or not

                final String phoneNo = regPhoneNo.getEditText().getText().toString().trim();
                String countrycode=Code_Picker.getSelectedCountryCode().toString();
                final String userEnteredPhone="+"+countrycode+phoneNo;
                final String userEnteredPassword = password.getEditText().getText().toString().trim();

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("" +shared.getType());
                Query checkUser = reference.orderByChild("phoneNo").equalTo(userEnteredPhone);

                checkUser.addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()) {
                            regPhoneNo.setError(null);
                            regPhoneNo.setErrorEnabled(false);
                            // String passwordFromDB = dataSnapshot.child("password").getValue(String.class);
                            for (DataSnapshot user : dataSnapshot.getChildren()) {
                                UserHelperClass usersbean = user.getValue(UserHelperClass.class);

                                //Here we are testing the user password and username is valid or not

                                if (usersbean.getPassword().equals(userEnteredPassword)) {
                                    regPhoneNo.setError(null);
                                    regPhoneNo.setErrorEnabled(false);

                                    sharePreference_OneTimeLogin shared= new sharePreference_OneTimeLogin(Login.this);
                                    shared.setType( shared.getType());

                                    String phoneNo1 = regPhoneNo.getEditText().getText().toString();
                                    String countrycode1=Code_Picker.getSelectedCountryCode().toString();

                                    //if it is valid the we are sending the user phone no to the other activity

                                    Intent intent=new Intent(Login.this, VerifyOTPLogin.class);

                                    intent.putExtra("countryCode",countrycode1);
                                    intent.putExtra("phone_no",phoneNo1);
                                    intent.putExtra("LoginType","Login");
                                    startActivity(intent);
                                    finish();


                                } else {
                                    Toast.makeText(Login.this, "Wrong password", Toast.LENGTH_LONG).show();
                                    password.setError("Wrong Password");
                                    password.requestFocus();
                                    forgetPass.setVisibility(View.VISIBLE);
                                }
                            }

                        } else {

                            regPhoneNo.setError("No such User exist");
                            regPhoneNo.requestFocus();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

        });

        //When click on forget password button

        forgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //intent will redirect to ForgetPassword activity

                Intent intent=new Intent(Login.this,ForgetPasswordActivity.class);
                startActivity(intent);

            }
        });


    }  //method

}  //class