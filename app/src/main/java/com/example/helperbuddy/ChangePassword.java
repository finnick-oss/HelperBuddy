package com.example.helperbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.helperbuddy.HelperClasses.UserHelperClass;
import com.example.helperbuddy.HelperClasses.sharePreference_OneTimeLogin;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChangePassword extends AppCompatActivity {  //class

    //instances

    EditText newPass,confirmPass;
    Button changePass;

    FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {  //method
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);


        //hooks

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();

        newPass=findViewById(R.id.newpassword);
        confirmPass=findViewById(R.id.confirmpassword);
        changePass=findViewById(R.id.changepassword);


        //when click on change button this will start


        changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String pass1=newPass.getText().toString();
                final String pass2=confirmPass.getText().toString();

                Toast.makeText(ChangePassword.this, "PASS"+pass1, Toast.LENGTH_LONG).show();
                Toast.makeText(ChangePassword.this, "PAss2"+pass2, Toast.LENGTH_LONG).show();

                if (pass1!=null && pass2!=null) {  //testing password is not null


                    if(pass1.equals(pass2)){  //testing both password is same or not

                      //  if(pass1.contains("@")||pass1.contains("#")||pass1.contains("(")||pass1.contains(")")){  //testing password is strong or not




                            //sharedPreference object

                            final sharePreference_OneTimeLogin shared = new sharePreference_OneTimeLogin(ChangePassword.this);

                            //DatabaseReference for getting node

                            final DatabaseReference databaseReference=firebaseDatabase.getReference("" +shared.getType()).child(firebaseAuth.getUid());
                            databaseReference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {


                                /*
                                If condition will check the password which you have written  already exist or not
                                if yes then throw toast of already exist else will change.
                                 */


                                    if(pass1.equals(snapshot.child("password").getValue(String.class))){

                                        Toast.makeText(ChangePassword.this, "Password is same as previous.", Toast.LENGTH_SHORT).show();

                                    }else{

                                        //updating the password

                                        databaseReference.child("password").setValue(pass1);


                                        Toast.makeText(ChangePassword.this, "Successfully updated", Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(ChangePassword.this, Login.class);
                                        startActivity(intent);
                                        finish();  //finishing the activity
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });




                    }else{

                        Toast.makeText(ChangePassword.this, "Password not matches.", Toast.LENGTH_SHORT).show();

                    }

                }
                else{
                    Toast.makeText(ChangePassword.this, "Enter password.", Toast.LENGTH_SHORT).show();
                }

            }//onclick method

        });  //onClickListener method



    }  //onCreate method
    @Override
    protected void onStop() {
        FirebaseAuth.getInstance().signOut();
        super.onStop();
    }
}  //class