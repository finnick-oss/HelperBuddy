package com.example.helperbuddy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.example.helperbuddy.HelperClasses.WorkerSignupHelper;
import com.example.helperbuddy.HelperClasses.sharePreference_OneTimeLogin;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.concurrent.TimeUnit;

public class VerificationWorkerActivity extends AppCompatActivity {

    PinView otp;
    Button otpsubmit;
    private String codeBySystem, number;
    private FirebaseAuth mAuth;
    StorageReference storageReference;//variable for storage
    private TextView resend,textPhone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_worker);
        otp = findViewById(R.id.OTP);
        otpsubmit = findViewById(R.id.otp_submit);
        resend = findViewById(R.id.resend);
        textPhone =findViewById(R.id.text_phone);
        storageReference= FirebaseStorage.getInstance().getReference().child("worker");

        mAuth = FirebaseAuth.getInstance();

        number ="+"+getIntent().getStringExtra("countryCode")+getIntent().getStringExtra("phone_no");


        //setting the mobile no in text field of verificationVisitor
        textPhone.setText(number);


        sendVerificationCode();//calling the function

        otpsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(otp.getText().toString())){
                    Toast.makeText(VerificationWorkerActivity.this, "Enter Otp", Toast.LENGTH_SHORT).show();
                }
                else if(otp.getText().toString().replace(" ","").length()!=6){
                    Toast.makeText(VerificationWorkerActivity.this, "Enter right otp", Toast.LENGTH_SHORT).show();
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
                        VerificationWorkerActivity.this.codeBySystem = id;
                        Toast.makeText(VerificationWorkerActivity.this, "sending OTP", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(VerificationWorkerActivity.this, "Invalid OTP"+e, Toast.LENGTH_SHORT).show();
                    }
                });        // OnVerificationStateChangedCallbacks


    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(VerificationWorkerActivity.this, "Verification Successful", Toast.LENGTH_SHORT).show();

                            String type="worker";
                            sharePreference_OneTimeLogin shared= new sharePreference_OneTimeLogin(VerificationWorkerActivity.this);
                            shared.setType(type);

                            Intent intent = new Intent(VerificationWorkerActivity.this, WorkerSigup.class);
                            intent.putExtra("number",number);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(VerificationWorkerActivity.this, "Verification Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }





    }



