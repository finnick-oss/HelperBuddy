package com.example.helperbuddy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


import android.app.ProgressDialog;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.helperbuddy.HelperClasses.WorkerSignupHelper;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
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

import java.util.Arrays;
import java.util.List;


public class WorkerSigup extends AppCompatActivity {

    //variables


    TextInputLayout regName, regUsername, regoccupation, regPhoneNo, regPassword, regdescription;
    Button register, regToLoginBtn;
    private ImageView profilePic;
    CountryCodePicker Code_Picker;
    EditText raddress;

    private Uri imageUri;          //uri type variable to upload image
    //firebase reference
    DatabaseReference storage;// variable for realtime database
    StorageReference storageReference;//variable for storage


    private FirebaseAuth mAuth;
    FirebaseDatabase rootNode;
    DatabaseReference reference;

   public Place place;
   public LatLng queriedLocation;

    //Creating a string array for autoComplete

    private static final String[] OCCUPATION = new String[]{
            "Labour", "Tailor", "Electrician", "Farmer", "Carpenter", "Maid", "Painter",
            "Plumber", "Security_Guard"
    };

    private AutoCompleteTextView occupationauto;
    String number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_sigup);



        //firebaseStorage references
        storage = FirebaseDatabase.getInstance().getReference().child("worker");
        //getting instance of firebase
        storageReference = FirebaseStorage.getInstance().getReference().child("worker");
        number = getIntent().getStringExtra("number");

        mAuth = FirebaseAuth.getInstance();

        //created hooks
        regName = findViewById(R.id.reg1_name);
        regUsername = findViewById(R.id.reg1_username);
        regdescription = findViewById(R.id.reg1_description);
        regPassword = findViewById(R.id.reg1_password);
        register = findViewById(R.id.reg1_btn);
        profilePic = findViewById(R.id.imageProfile);
        regToLoginBtn = findViewById(R.id.reg1_login_btn);
        raddress=findViewById(R.id.editText);

        //initalizeing  places
        Places.initialize(getApplicationContext(),"AIzaSyBhuQLWGqMssuofXgMstMWu_URVrT7Q-cw");

        raddress.setFocusable(false);
        raddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intialize place field list
                List<Place.Field> fieldList= Arrays.asList(Place.Field.ADDRESS,Place.Field.LAT_LNG,Place.Field.NAME);

                Intent intent=new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY,fieldList).build(WorkerSigup.this);
                startActivityForResult(intent,100);
            }
        });

        occupationauto = findViewById(R.id.occupationauto);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, OCCUPATION);
        occupationauto.setAdapter(adapter);

        regToLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WorkerSigup.this, Login.class);
               startActivity(intent);

            }
        });

        register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                Boolean result = false;

                String input = occupationauto.getText().toString().trim();
                String[] OccupationArray = input.split("\\s*,\\s*");

                String name = regName.getEditText().getText().toString();
                String username = regUsername.getEditText().getText().toString();
                //  String  occupation = OccupationArray.toString();
                String address = raddress.getText().toString();
                String description = regdescription.getEditText().getText().toString();
            //    String phoneNo = regPhoneNo.getEditText().getText().toString();
                String password = regPassword.getEditText().getText().toString();


                if (input.isEmpty() || name.isEmpty() || username.isEmpty() || address.isEmpty() || description.isEmpty()  || password.isEmpty() || imageUri == null) {
                    regName.setError("Field cannot be empty");
                    regUsername.setError("Field cannot be empty");
                    raddress.setError("Field cannot be empty");
                    regdescription.setError("Field cannot be empty");
                    occupationauto.setError("Field cannot be empty");
                    regPassword.setError("Field cannot be empty");


                }
                if(imageUri == null) {
                    Toast.makeText(WorkerSigup.this, "Image is Required", Toast.LENGTH_SHORT).show();
                }
                else {
                    result = true;
                }


                //registering all the data in firebase
                uploadPicture();   //calling method which will upload picture


            }
        });

        profilePic = findViewById(R.id.imageProfile);
        //Creating method which will allow to user to upload profile picture.

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePicture();   //method by which user will upload his picture
            }
        });


    }   //onCreate

    //method by which user will upload his profile pic
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
        else if(requestCode==100 && resultCode==RESULT_OK){
            //when Success
              place=Autocomplete.getPlaceFromIntent(data);
             raddress.setText(place.getAddress());
             queriedLocation = place.getLatLng();
             Log.v("Latitude is", "" + queriedLocation.latitude);
             Log.v("Longitude is", "" + queriedLocation.longitude);



        }
        else if(resultCode== AutocompleteActivity.RESULT_ERROR){
            Status status=Autocomplete.getStatusFromIntent(data);
            Toast.makeText(getApplicationContext(),status.getStatusCode(),Toast.LENGTH_SHORT).show();
        }

    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }//for writing file extension with photo like jpg


    private void uploadPicture() {   //method to upload picture


        if (imageUri == null) {
            Toast.makeText(WorkerSigup.this, "Image is Required", Toast.LENGTH_SHORT).show();
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

                                    String OccupationArray = occupationauto.getText().toString().trim();
                                    String name = regName.getEditText().getText().toString();
                                    String username = regUsername.getEditText().getText().toString();
                                    String  address = raddress.getText().toString();
                                    String  description = regdescription.getEditText().getText().toString();

                                    String password = regPassword.getEditText().getText().toString();
                                    String images = uri.toString();

                                     String Total_ratings="0";
                                     String Total_users="0";

                                    Double latitude=queriedLocation.latitude;
                                    Double longitude=queriedLocation.longitude;

                                    rootNode = FirebaseDatabase.getInstance();
                                    reference = FirebaseDatabase.getInstance().getReference("worker").child(mAuth.getUid());
                                    //  reference = rootNode.getReference(mAuth.getUid());

                                    WorkerSignupHelper user = new WorkerSignupHelper(name, username, OccupationArray, address, description, number, password, images,Total_ratings,Total_users,latitude,longitude);
                                    reference.setValue(user);
                                    DatabaseReference ref=FirebaseDatabase.getInstance().getReference("geofire").child(OccupationArray);
                                    GeoFire geoFire=new GeoFire(ref);
                                    geoFire.setLocation(mAuth.getUid(),new GeoLocation(queriedLocation.latitude,queriedLocation.longitude));



                                }
                            });


                            Snackbar.make(findViewById(android.R.id.content), "Image Uploaded", Snackbar.LENGTH_LONG).show();

                            Intent intent=new Intent(WorkerSigup.this, MainDashboard.class);

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