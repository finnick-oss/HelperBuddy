package com.example.helperbuddy;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import com.chaos.view.PinView;
import com.example.helperbuddy.HelperClasses.UserHelperClass;


import androidx.annotation.NonNull;

import android.content.Intent;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.helperbuddy.HelperClasses.sharePreference_OneTimeLogin;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;


public class VerificationVisitorActivity extends AppCompatActivity {

    PinView otp;
    Button otpsubmit;

    private String number, codeBySystem ;
    private FirebaseAuth mAuth;
    StorageReference storageReference;
    private TextView resend,textPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_visitor);

        otp = findViewById(R.id.OTP);
        otpsubmit = findViewById(R.id.otp_submit);
        resend = findViewById(R.id.resend);
        textPhone =findViewById(R.id.text_phone);

        storageReference = FirebaseStorage.getInstance().getReference().child("users");
        mAuth = FirebaseAuth.getInstance();


        number ="+"+getIntent().getStringExtra("countryCode")+getIntent().getStringExtra("phone_no");//getting the no. from phoneno activity

        textPhone.setText(number);//setting the mobile no in text field of verificationVisitor that is automatically detected

        sendVerificationCode();

       otpsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(otp.getText().toString())){
                    Toast.makeText(VerificationVisitorActivity.this, "Enter Otp", Toast.LENGTH_SHORT).show();
                }
                else if(otp.getText().toString().replace(" ","").length()!=6){
                    Toast.makeText(VerificationVisitorActivity.this, "Enter right otp", Toast.LENGTH_SHORT).show();
                }
                else {
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeBySystem, otp.getText().toString().replace(" ",""));
                    signInWithPhoneAuthCredential(credential);
                }
            }
        });

        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendVerificationCode();
                finish();
            }
        });



    }

    private void sendVerificationCode() {

        new CountDownTimer(60000,1000){
            @Override
            public void onTick(long l) {
                resend.setText(""+l/1000);
                resend.setEnabled(false);

            }

            @Override
            public void onFinish() {
                resend.setText(" Resend");
                resend.setEnabled(true);

            }
        }.start();


        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,        // Phone number to verify
                120,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {


                    @Override
                    public void onCodeSent(@NonNull String id, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        VerificationVisitorActivity.this.codeBySystem = id;
                        Toast.makeText(VerificationVisitorActivity.this, "sending OTP", Toast.LENGTH_SHORT).show();
                        codeBySystem=id;
                    }

                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        signInWithPhoneAuthCredential(phoneAuthCredential);
                        String code=phoneAuthCredential.getSmsCode();
                        if(code!=null){
                            otp.setText(code);
                        }
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Toast.makeText(VerificationVisitorActivity.this, "Invalid OTP"+e, Toast.LENGTH_SHORT).show();
                    }
                });        // OnVerificationStateChangedCallbacks


    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {


                            Toast.makeText(VerificationVisitorActivity.this, "Verification Successful", Toast.LENGTH_SHORT).show();


                            String type="users";
                            sharePreference_OneTimeLogin shared= new sharePreference_OneTimeLogin(VerificationVisitorActivity.this);
                            shared.setType(type);
                            Intent intent = new Intent(VerificationVisitorActivity.this, SignUp.class);
                            intent.putExtra("number",number);//passing the no. to signUp activity
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(VerificationVisitorActivity.this, "Verification Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}


