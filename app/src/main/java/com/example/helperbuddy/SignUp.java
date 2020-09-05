package com.example.helperbuddy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.helperbuddy.HelperClasses.UserHelperClass;
import com.example.helperbuddy.HelperClasses.WorkerSignupHelper;
import com.example.helperbuddy.HelperClasses.sharePreference_OneTimeLogin;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hbb20.CountryCodePicker;

import java.io.ByteArrayOutputStream;

public class SignUp extends AppCompatActivity {

    TextInputLayout regName, regUsername, regEmail,  regPassword, regPhoneNo;

    Button regBtn, regToLoginBtn;
    ImageView profilePic;

    private Uri imageUri;          //uri type variable to upload image
    private FirebaseAuth mAuth;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    //firebase reference
    DatabaseReference storage;// variable for realtime database
    StorageReference storageReference;
    String number;
    @Override
        protected void onCreate (Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_sign_up);

        //firebaseStorage references
        storage = FirebaseDatabase.getInstance().getReference().child("users");
        mAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference().child("users");
        number = getIntent().getStringExtra("number");

        //Hooks to all xml elements in activity_sign_up.xml
            regName = findViewById(R.id.reg_name);
            regUsername = findViewById(R.id.reg_username);
            regEmail = findViewById(R.id.reg_email);

            regPassword = findViewById(R.id.reg_password);
            regBtn = findViewById(R.id.reg_btn);



        profilePic = findViewById(R.id.profileImage);




        regToLoginBtn = findViewById(R.id.reg_login_btn);

        regToLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SignUp.this, Login.class);
                startActivity(intent);
            }
        });



        regBtn.setOnClickListener(new View.OnClickListener() {    //By clicking on register button data will be send to server(firebase)
                //This function will execute when user click on Register Button
                private Boolean validateName() {
                    String val = regName.getEditText().getText().toString();

                    if (val.isEmpty()) {
                        regName.setError("Field cannot be empty");
                        return false;
                    } else {
                        regName.setError(null);
                        regName.setErrorEnabled(false);
                        return true;
                    }
                }

                private Boolean validateUsername() {
                    String val = regUsername.getEditText().getText().toString();
                    String noWhiteSpace = "\\A\\w{4,20}\\z";

                    if (val.isEmpty()) {
                        regUsername.setError("Field cannot be empty");
                        return false;
                    } else if (val.length() >= 15) {
                        regUsername.setError("Username too long");
                        return false;
                    } else if (!val.matches(noWhiteSpace)) {
                        regUsername.setError("White Spaces are not allowed");
                        return false;
                    } else {
                        regUsername.setError(null);
                        regUsername.setErrorEnabled(false);
                        return true;
                    }
                }

                private Boolean validateEmail() {
                    String val = regEmail.getEditText().getText().toString();
                    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                    if (val.isEmpty()) {
                        regEmail.setError("Field cannot be empty");
                        return false;
                    } else if (!val.matches(emailPattern)) {
                        regEmail.setError("Invalid email address");
                        return false;
                    } else {
                        regEmail.setError(null);
                        regEmail.setErrorEnabled(false);
                        return true;
                    }
                }



                private Boolean validatePassword() {
                    String val = regPassword.getEditText().getText().toString();
                    String passwordVal = "^" +
                            //"(?=.*[0-9])" +         //at least 1 digit
                            //"(?=.*[a-z])" +         //at least 1 lower case letter
                            //"(?=.*[A-Z])" +         //at least 1 upper case letter
                            "(?=.*[a-zA-Z])" +      //any letter
                            "(?=.*[@#$%^&+=])" +    //at least 1 special character
                            "(?=\\S+$)" +           //no white spaces
                            ".{4,}" +               //at least 4 characters
                            "$";

                    if (val.isEmpty()) {
                        regPassword.setError("Field cannot be empty");
                        return false;
                    } else if (!val.matches(passwordVal)) {
                        regPassword.setError("Password is too weak");
                        return false;
                    } else {
                        regPassword.setError(null);
                        regPassword.setErrorEnabled(false);
                        return true;
                    }
                }

                //This function will execute when user click on Register Button
                public void onClick(View view) {//verify all the thing
                    if (!validateName() | !validatePassword() | !validateEmail() | !validateUsername()) {
                        return;
                    }
                  uploadPicture();  //calling method which will upload picture

                }
            });

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePicture();   //method by which user will upload his picture
            }
        });
        }//onCreate Method End

    public void choosePicture() {

        Intent intent = new Intent();

        intent.setType("image/*");  //inserting all images inside this Image folder

        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();

            profilePic.setImageURI(imageUri);
        }
    }
    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }


    private void uploadPicture() {   //method to upload picture


        if (imageUri == null) {
            Toast.makeText(SignUp.this, "Image is Required", Toast.LENGTH_SHORT).show();
        } else {

            final ProgressDialog pd = new ProgressDialog(this);  //object by which showing how much percentage is uploaded
            pd.setTitle("Uploading Image...");
            pd.show();

            final String userName = regUsername.getEditText().getText().toString();

            StorageReference riversRef = storageReference.child(userName + "." + getFileExtension(imageUri));  //adding image with current user detail

            riversRef.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            pd.dismiss();
                            storageReference.child(userName + "." + getFileExtension(imageUri)).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    String name = regName.getEditText().getText().toString();
                                    String username = regUsername.getEditText().getText().toString();
                                    String email = regEmail.getEditText().getText().toString();
                                    String password = regPassword.getEditText().getText().toString();

                                    String images = uri.toString();



                                    rootNode = FirebaseDatabase.getInstance();
                                    reference = FirebaseDatabase.getInstance().getReference("users").child(mAuth.getUid());
                                    //  reference = rootNode.getReference(mAuth.getUid());

                                   UserHelperClass user = new UserHelperClass(name, username,email, number, password, images);
                                    reference.setValue(user);


                                }
                            });


                            Snackbar.make(findViewById(android.R.id.content), "Image Uploaded", Snackbar.LENGTH_LONG).show();
                            Intent intent=new Intent(SignUp.this, MainDashboard.class);
                            startActivity(intent);
                            finish();



                        }


                    })


                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            pd.dismiss();
                            Toast.makeText(getApplicationContext(), "Failed to upload", Toast.LENGTH_SHORT).show();
                        }
                    })

                    //for showing how much progress is going on
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {

                            double progressPercentage = (100.00 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());  //using task snapshot to get the amount of byte transferred
                            pd.setMessage("Percentage : " + (int) progressPercentage + "%");
                        }
                    });

        }  //uploadPicture


    }

    }
