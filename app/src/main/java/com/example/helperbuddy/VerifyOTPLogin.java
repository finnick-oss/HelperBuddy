package com.example.helperbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.example.helperbuddy.HelperClasses.UserHelperClass;
import com.example.helperbuddy.HelperClasses.sharePreference_OneTimeLogin;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class VerifyOTPLogin extends AppCompatActivity {

    PinView otp;
    Button otpsubmit;
    private String number1,codeBySystem1,forgotPassword;
    private TextView resend,textPhone;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_o_t_p_login);
        mAuth = FirebaseAuth.getInstance();

        otp = findViewById(R.id.OTP1);
        otpsubmit = findViewById(R.id.otp_submit1);
        resend = findViewById(R.id.resend1);
        textPhone =findViewById(R.id.text_phone1);

        forgotPassword=getIntent().getStringExtra("LoginType");


        number1 ="+"+getIntent().getStringExtra("countryCode")+getIntent().getStringExtra("phone_no");
        textPhone.setText(number1);
        sendVerificationCode();
        otpsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(otp.getText().toString())){
                    Toast.makeText(VerifyOTPLogin.this, "Enter Otp", Toast.LENGTH_SHORT).show();
                }
                else if(otp.getText().toString().replace(" ","").length()!=6){
                    Toast.makeText(VerifyOTPLogin.this, "Enter right otp", Toast.LENGTH_SHORT).show();
                }
                else {
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeBySystem1, otp.getText().toString().replace(" ",""));
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
                number1,        // Phone number to verify
                120,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {


                    @Override
                    public void onCodeSent(@NonNull String id, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        VerifyOTPLogin.this.codeBySystem1= id;
                        Toast.makeText(VerifyOTPLogin.this, "sending OTP", Toast.LENGTH_SHORT).show();
                        codeBySystem1=id;
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
                        Toast.makeText(VerifyOTPLogin.this, "Invalid OTP"+e, Toast.LENGTH_SHORT).show();
                    }
                });        // OnVerificationStateChangedCallbacks


    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            if(forgotPassword.equals("ForgetPass"))
                            {

                                Toast.makeText(VerifyOTPLogin.this, "Verification Successful", Toast.LENGTH_SHORT).show();

                                FirebaseUser user = task.getResult().getUser();
                                startActivity(new Intent(VerifyOTPLogin.this, ChangePassword.class));
                                finish();

                            }else {
                                Toast.makeText(VerifyOTPLogin.this, "Verification Successful", Toast.LENGTH_SHORT).show();

                                FirebaseUser user = task.getResult().getUser();
                                startActivity(new Intent(VerifyOTPLogin.this, GetLocation.class));
                                finish();
                            }
                        } else {
                            Toast.makeText(VerifyOTPLogin.this, "Verification Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}