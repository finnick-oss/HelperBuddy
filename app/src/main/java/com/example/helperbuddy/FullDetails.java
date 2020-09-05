
package com.example.helperbuddy;  //package

/*
Activity is for displaying the search
employee profile with all the important details
 */

//imported all the important classes and all the interfaces or packages


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.helperbuddy.Database.Database;
import com.example.helperbuddy.HelperClasses.ConnectivityCheck;
import com.example.helperbuddy.HelperClasses.Order;
import com.example.helperbuddy.HelperClasses.Request;
import com.example.helperbuddy.HelperClasses.UserHelperClass;
import com.example.helperbuddy.HelperClasses.WorkerSignupHelper;
import com.example.helperbuddy.HelperClasses.sharePreference_OneTimeLogin;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class FullDetails extends AppCompatActivity {  //class

    //declaring all the Views attributes

    TextView worker_occupation,worker_name,worker_address,worker_phone,worker_description;
    ImageView worker_image;
    CollapsingToolbarLayout collapsingToolbarLayout;
    Button call;
    String detailId="";
    String searchId="";

    ConnectivityCheck connectivityCheck = new ConnectivityCheck();

    //instance of firebaseDatabase

    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseDatabase rootNode;
    DatabaseReference reference1 ,reference2,reference3;

    DatabaseReference requests;

    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    WorkerSignupHelper currentUser;
    List<WorkerSignupHelper> cart=new ArrayList<>();

    //Instance for Feedback

    TextView ratingCount;
    RatingBar bar;
    float ratingValue;
    Float totalRatings;
    int totalUsers;
    Button SubmitRating;
    EditText UserFeedback;

   String occupation;
    //Taking Linear Layouts

    LinearLayout ThanksLayout,SubmitLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_details);

        //getting the instance of firebase database

        database=FirebaseDatabase.getInstance();
        //and  getting the reference of workers

        firebaseAuth=FirebaseAuth.getInstance();
        reference=database.getReference("worker");


        //creating hooks

        worker_name=(TextView) findViewById(R.id.worker_name);
        worker_address=(TextView)findViewById(R.id.worker_address);
        worker_phone=(TextView)findViewById(R.id.worker_phone);
        worker_description=(TextView)findViewById(R.id.worker_description);
        worker_image=(ImageView)findViewById(R.id.worker_image);
        call=findViewById(R.id.call_btn);
        collapsingToolbarLayout=findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);


        //For Feedback-------------------------------------------------

        ratingCount=findViewById(R.id.count_rating);
        bar=findViewById(R.id.rating_bar);
        SubmitRating=findViewById(R.id.submitRating);
        UserFeedback = findViewById(R.id.FeedbackSeccection);


        //DOUBT TO GET WORKER NAME PLEASE CHECK IF WORKER NAME WILL GET OR NOT IF NOT THEN SOLVE
        //AND TAKE THE WORKER NAME IN 'name' variable




        SubmitLayout=findViewById(R.id.take_feedback);
        ThanksLayout = findViewById(R.id.thanks_feedback);

        //--------------------------------------------------------

        //getting detail id from Intent

        if(getIntent()!=null)
            detailId=getIntent().getStringExtra("DetailId");
        if( detailId!=null  && !detailId.isEmpty()  ){
            getAllDetails(detailId);
        }

        else{
            if(getIntent()!=null)
                searchId=getIntent().getStringExtra("SearchId");
            if(searchId!=null && !searchId.isEmpty()){
                getAllDetailsSearch(searchId);
            }}


        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();

        //Setting Rating value

        bar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float value, boolean b) {

                ratingValue=value;
                ratingCount.setText("Value : " + value);

            }
        });


        //Firebase reference for inserting value




        //Method for submit rating

        SubmitRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(connectivityCheck.isNetworkAvailable(FullDetails.this)){
                    if(UploadRatings()){

                        ThanksLayout.setVisibility(View.VISIBLE);
                        SubmitLayout.setVisibility(View.GONE);

                    }
                }
                else {
                    Toast.makeText(FullDetails.this, "(No Internet Connection !!!)", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    //Method for to submit rating
    private boolean UploadRatings() {


        totalRatings =totalRatings+ratingValue;
        totalUsers=totalUsers+1;


        //feedback testing is remaining


        final String Feedback=UserFeedback.getText().toString();

        reference1.child("total_ratings").setValue(Float.toString(totalRatings));
        reference1.child("total_users").setValue(Integer.toString(totalUsers));


        if(Feedback.length()>1){

            /*HERE YOU HAVE TO GET THE NAME FROM SHARED PREFERENCE
            AND REMOVE THE "name" VARIABLE AND CREATE NEW STRING
            VARIABLE AND INSERT IT INSIDE CHILD
             */
            final sharePreference_OneTimeLogin shared = new sharePreference_OneTimeLogin(FullDetails.this);
            DatabaseReference databaseReference=firebaseDatabase.getReference("" +shared.getType()).child(firebaseAuth.getUid());
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    UserHelperClass userHelperClass=snapshot.getValue(UserHelperClass.class);
                    reference2.child(userHelperClass.getName()).setValue(Feedback);//username



                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }

        return true;

    }




    //Method to get All details of user

    private void getAllDetails(final String detailId) {//this method is to go from DetailsRecycler view to full details
        reference.child(detailId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currentUser = snapshot.getValue(WorkerSignupHelper.class);
                Picasso.get().load(currentUser.getImages()).into(worker_image);//getting image from firebase
                collapsingToolbarLayout.setTitle(currentUser.getOccupation());//getting text from firebase
                worker_name.setText(currentUser.getName());
                worker_address.setText(currentUser.getAddress());
                worker_phone.setText(currentUser.getPhoneNo());
                worker_description.setText(currentUser.getDescription());

                occupation=currentUser.getOccupation();
                rootNode = FirebaseDatabase.getInstance();

                reference1 = FirebaseDatabase.getInstance().getReference("worker/"+ detailId);
                reference2 = rootNode.getReference("worker/"+ detailId+"/UsersFeedback");


                reference1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try{
                        if((dataSnapshot.child("total_ratings").getValue(String.class))==null && (dataSnapshot.child("total_users").getValue(String.class))==null )
                        {
                            totalRatings=0.0f;
                            totalUsers=0;


                        }
                        else{
                            totalRatings = Float.parseFloat(dataSnapshot.child("total_ratings").getValue(String.class));
                            totalUsers = Integer.parseInt(dataSnapshot.child("total_users").getValue(String.class));
                        }
                        }
                        catch (Exception e){

                        }


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(FullDetails.this, "The read failed: " + databaseError.getCode(), Toast.LENGTH_SHORT).show();

                    }
                });

                /*
                When user will click on the call buttor
                then it will automatically redirected the visitor
                to dial pad and automatically number will written on dial pad
                 */
                call.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent= new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:"+ currentUser.getPhoneNo()));
                        startActivity(intent);
                        final sharePreference_OneTimeLogin shared = new sharePreference_OneTimeLogin(FullDetails.this);
                        DatabaseReference databaseReference=firebaseDatabase.getReference("" +shared.getType()).child(firebaseAuth.getUid());
                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                UserHelperClass userHelperClass=snapshot.getValue(UserHelperClass.class);

                                String time= new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());//getting current time
                                String date=new SimpleDateFormat("yyyy-MMM-dd", Locale.getDefault()).format(new Date());//getting current date


                                String workerName=currentUser.getName();//taking all the details of worker from firebase
                                String workerPhone=currentUser.getPhoneNo();
                                String workerOccupation=currentUser.getOccupation();
                                String workerAddress=currentUser.getAddress();


                                requests = database.getReference("Requests/"+userHelperClass.getName()+"("+userHelperClass.getPhoneNo()+")"+"/"+date+"/"+"("+workerName+")"+time);
                                Request request=new Request(workerName,workerPhone,workerOccupation,workerAddress);
                                requests.setValue(request);//getting the request in firebase

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        new Database(getBaseContext()).addToCart(new Order(
                                currentUser.getName(),
                                currentUser.getOccupation(),
                                currentUser.getAddress(),
                                currentUser.getDescription(),
                                currentUser.getPhoneNo(),
                                currentUser.getImages()

                        ));
                        Toast.makeText(FullDetails.this, "Added In History ", Toast.LENGTH_SHORT).show();

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // when the database error occured
            }


        });
    }



    private void getAllDetailsSearch(String searchId) {//this method is to go from searchRecycler view to full details
        reference.child(searchId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final WorkerSignupHelper details = snapshot.getValue(WorkerSignupHelper.class);

                //Getting all the details from firebase by useing id

                Picasso.get().load(details.getImages()).into(worker_image);//getting image from firebase
                collapsingToolbarLayout.setTitle(details.getOccupation());//getting text from firebase
                worker_name.setText(details.getName());
                worker_address.setText(details.getAddress());
                worker_phone.setText(details.getPhoneNo());
                worker_description.setText(details.getDescription());

                call.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {//to print phone number on dial pad when user click on call button
                        Intent intent= new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:"+details.getPhoneNo()));
                        startActivity(intent);
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // when the database error occured
            }
        });
    }
}