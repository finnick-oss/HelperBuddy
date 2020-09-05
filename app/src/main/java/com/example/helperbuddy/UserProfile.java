package com.example.helperbuddy;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.helperbuddy.Database.Database;
import com.example.helperbuddy.HelperClasses.ConnectivityCheck;
import com.example.helperbuddy.HelperClasses.Order;
import com.example.helperbuddy.HelperClasses.UserHelperClass;
import com.example.helperbuddy.HelperClasses.WorkerSignupHelper;
import com.example.helperbuddy.HelperClasses.sharePreference_OneTimeLogin;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.example.helperbuddy.HelperClasses.sharePreference_OneTimeLogin;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;


public class UserProfile extends AppCompatActivity {

    private LinearLayout camera, gallery;
    private Uri imageUri, cameraImgUri, downloadUrl;
    private TextView ImageUser;
    private String currentPhotoPath;
    private ImageView userFullProfile;
    private ImageView userProfile1, showImage;
    private StorageReference mStorageRef;
    private FirebaseDatabase database;
    private DatabaseReference myRef;


    TextView name, phoneNo, password;
    TextView occupation, fullName;
    TextView logout;
    CircleImageView imageView;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;

    String userName;
    FloatingActionButton floating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        final sharePreference_OneTimeLogin shared = new sharePreference_OneTimeLogin(UserProfile.this);
        floating=findViewById(R.id.fab);
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavView_bar);
        bottomNav.setSelectedItemId(R.id.ic_profile);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.ic_home:
                        startActivity(new Intent(getApplicationContext(), MainDashboard.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.ic_help:
                        startActivity(new Intent(getApplicationContext(), HelpThem.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.ic_profile:
                        return true;
                }

                return false;
            }
        });


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        ConnectivityCheck connectivityCheck = new ConnectivityCheck();
        DatabaseReference databaseReference = firebaseDatabase.getReference("" + shared.getType()).child(firebaseAuth.getUid());

        name = findViewById(R.id.name);
        phoneNo = findViewById(R.id.phone);
        password = findViewById(R.id.password);
        imageView = findViewById(R.id.profile_image1);
        occupation = findViewById(R.id.profile_occupation);
        fullName = findViewById(R.id.profile_name);

        floating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intialize place field list
                Intent intent = new Intent(UserProfile.this, HelpThem.class);
                startActivity(intent);
            }
        });
        logout = findViewById(R.id.profile_logout);
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
        if(connectivityCheck.isNetworkAvailable(UserProfile.this)){
            database = FirebaseDatabase.getInstance();
            myRef = database.getReference("" + shared.getType()).child(firebaseAuth.getUid());
            ImageUser = findViewById(R.id.profile_update_text);
            //showImage = findViewById(R.id.profile_image);

            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.

                    downloadUrl = Uri.parse(dataSnapshot.child("images").getValue(String.class));
                    userProfile1 = findViewById(R.id.profile_image1);

                    Glide
                            .with(UserProfile.this)
                            .load(downloadUrl)
                            .into(userProfile1);

                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Toast.makeText(UserProfile.this, "Unable to fetch data from firebase", Toast.LENGTH_SHORT).show();
                }
            });

            //-------------------------------------------------------------------------

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    displayUserProfile();
                }
            });

            //-------------------------------------------------------------------------



            //---------------------------------------------------------------------------

            //Task for update image

            ImageUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Permission for camera

                    if (ContextCompat.checkSelfPermission(UserProfile.this,
                            Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions(UserProfile.this,
                                new String[]{
                                        Manifest.permission.CAMERA
                                },
                                100);
                    }
                    else {
                        showBottomSheet();

                    }}
            });
        }
        else {
            Toast.makeText(UserProfile.this, "(No Internet Connection !!!)", Toast.LENGTH_SHORT).show();
        }
        }







    //***************************************OUTSIDE OF ON CREATE METHOD***********************************

    //---------------------------------------------------------------------------------------------

    //Method for displaying profile

    public void displayUserProfile() {


        Dialog dialog = new Dialog(UserProfile.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.showprofile);
        dialog.show();

        userFullProfile = dialog.findViewById(R.id.userFullProfileImage);

        Glide
                .with(this)
                .load(downloadUrl)
                .into(userFullProfile);


    }

    //----------------------------------------------------------------------------------------------

    //Method to show bottom sheet

    public void showBottomSheet(){


        //Showing Bottom sheet Dialogue

        final BottomSheetDialog bottomSheetDialog= new BottomSheetDialog(UserProfile.this,R.style.bottomTheme);

        View bottomSheetView = LayoutInflater.from(getApplicationContext())
                .inflate(

                        R.layout.bottom_sheet,
                        (LinearLayout) findViewById(R.id.bottomContainer)
                );

        //Hooks of Bottom sheet

        camera = bottomSheetView.findViewById(R.id.cameraUpload);
        gallery = bottomSheetView.findViewById(R.id.gallery);

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();  //Show method will show bottom sheet on Activity

                 /*
                When user want to capture image and upload.
                User will click on this layout and this task will perform
                 */
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cameraPhoto(); //calling method will take picture from camera
                bottomSheetDialog.dismiss();  //for hiding the bottom sheet

            }
        });


                /*
                When user want to upload image from
                gallery.
                User will click on this layout and this task will perform
                 */

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                choosePicture(); //calling method

                bottomSheetDialog.dismiss();  //for hiding the bottom sheet

            }

        });
    }   //BottomSheet


    //----------------------------------------------------------------------------------------------

    //Method for taking picture from camera

    public void cameraPhoto(){

        String fileName = "photo";

        File storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        try {
            File imageFile= File.createTempFile(fileName,".jpeg",storageDirectory);
            currentPhotoPath = imageFile.getAbsolutePath();
            Uri imgUri = FileProvider.getUriForFile(UserProfile.this,"com.example.helperbuddy.fileprovider",imageFile);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT,imgUri);
            startActivityForResult(intent, 100);

        } catch (IOException e) {
            e.printStackTrace();
        }

    } //Camera Photo

    //-------------------------------------------------------------------------------------------

    //Method for choosing the picture from gallery


    public void choosePicture() {

        Intent intent = new Intent();
        intent.setType("image/*");  //inserting all images inside this Image folder
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);

    } //Choose Picture

    //--------------------------------------------------------------------------------------------

//When User will inside the gallery folder

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100 ){

            cameraImgUri=(Uri.fromFile(new File(currentPhotoPath)));

            CropImage.activity(cameraImgUri)  //cropping the image

                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setCropShape(CropImageView.CropShape.RECTANGLE)
                    .setFixAspectRatio(true)
                    .start(this);

        }

        else
        if (requestCode == 1  && resultCode == RESULT_OK && data != null && data.getData() != null) {

            //Getting the uri from gallery

            imageUri = data.getData();



            CropImage.activity(imageUri)  //cropping the image

                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setCropShape(CropImageView.CropShape.RECTANGLE)
                    .setFixAspectRatio(true)
                    .start(this);
        }

        //After image will crop again taking the image uri

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();


                userProfile1.setImageURI(Uri.parse(""));
           //    userFullProfile.setImageURI(resultUri);
                UpdateImage(resultUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(this, "Error While Getting uri", Toast.LENGTH_SHORT).show();
            }
        }


    }  //method

//--------------------------------------------------------------------------------------------------

    //Update Image
    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }//for writing file extension with photo like jpg
    private  void UpdateImage(Uri profileUri) {

        //Firebase Storage Reference
        final sharePreference_OneTimeLogin shared = new sharePreference_OneTimeLogin(UserProfile.this);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("" + shared.getType()).child(firebaseAuth.getUid());
        mStorageRef = FirebaseStorage.getInstance().getReference(shared.getType());
        final StorageReference riversRef = mStorageRef.child(userName + "." + getFileExtension(profileUri));

        riversRef.putFile(profileUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        mStorageRef.child(userName + "." + getFileExtension( profileUri)).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                //Setting the url to database

                                myRef.child("images").setValue(uri.toString());
                                Toast.makeText(UserProfile.this, "Image Updated Successfully", Toast.LENGTH_SHORT).show();

                            }

                        });
                    }

                });

    }  //UpdateImage




}




