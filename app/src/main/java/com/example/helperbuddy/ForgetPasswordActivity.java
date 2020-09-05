package com.example.helperbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.helperbuddy.HelperClasses.sharePreference_OneTimeLogin;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

public class ForgetPasswordActivity extends AppCompatActivity {

    //declaring instances

    ImageView back;
    Button submit;
    CountryCodePicker Code_Picker;
    TextInputLayout regPhoneNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        Code_Picker = findViewById(R.id.cppForget);
        regPhoneNo =findViewById(R.id.forgetPhone);
        submit= findViewById(R.id.submitPhoneNo);
        back=findViewById(R.id.back);



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ForgetPasswordActivity.this,Login.class);
                startActivity(intent);
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            private Boolean validatePhoneNo() {
                String val = regPhoneNo.getEditText().getText().toString();
                Toast.makeText(ForgetPasswordActivity.this, "phone="+val, Toast.LENGTH_LONG).show();
                if (val.isEmpty() || regPhoneNo.getEditText().getText().toString().length()!=10 ) {
                    regPhoneNo.setError("Field cannot be empty");
                    Toast.makeText(ForgetPasswordActivity.this, "Enter valid no", Toast.LENGTH_SHORT).show();
                    return false;
                }
                else {
                    regPhoneNo.setError(null);
                    regPhoneNo.setEnabled(false);
                    return true;
                }
            }
            @Override
            public void onClick(View view) {
                    Toast.makeText(ForgetPasswordActivity.this, "Sending Details", Toast.LENGTH_LONG).show();
                    if ( !validatePhoneNo()) {
                        return;
                    } else {
                        final sharePreference_OneTimeLogin shared = new sharePreference_OneTimeLogin(ForgetPasswordActivity.this);
                        final String code = Code_Picker.getSelectedCountryCode();
                        final String PhoneNo = regPhoneNo.getEditText().getText().toString();
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference(""+shared.getType());
                            Query checkUser = reference.orderByChild("phoneNo").equalTo("+"+code+PhoneNo);

                        Toast.makeText(ForgetPasswordActivity.this, "phone="+"+"+code+PhoneNo, Toast.LENGTH_LONG).show();
                            checkUser.addListenerForSingleValueEvent(new ValueEventListener() {

                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    if (dataSnapshot.exists()) {
                                        Toast.makeText(ForgetPasswordActivity.this, "Inside", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(ForgetPasswordActivity.this, VerifyOTPLogin.class);
                                        intent.putExtra("countryCode", code);
                                        intent.putExtra("phone_no", PhoneNo);
                                        intent.putExtra("LoginType", "ForgetPass");

                                        startActivity(intent);

                                    } else {

                                        regPhoneNo.setError("No not registered.");
                                        regPhoneNo.requestFocus();

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });




                    }



        }
    });


    }
}