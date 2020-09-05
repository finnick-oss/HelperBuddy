package com.example.helperbuddy;  //package

/*
Here in this class
all the thing which will implement on
main dashboard  is working in this class
 */

//Imported all the important class and packages

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.content.Context;


import com.example.helperbuddy.HelperClasses.Features;
import com.example.helperbuddy.Interface.ItemClickListener;
import com.example.helperbuddy.ViewHolder.MyViewHolder;

//imported firebase classes

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

//this for downloading powerful high quality images

import com.squareup.picasso.Picasso;


public class Dashboard extends Fragment  {  //class

    //created all the objects

    RecyclerView recyclerView;
    FirebaseRecyclerOptions<Features> options;
    FirebaseRecyclerAdapter<Features, MyViewHolder> adapter;
    FirebaseDatabase database;
    DatabaseReference DataRef;
    Button tryAgain;
    LinearLayout internetCheck;
    View v;
    //--------------------------




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_dashboard, container, false);

        //hooks
        internetCheck=(LinearLayout) v.findViewById(R.id.No_internet);
        recyclerView =(RecyclerView) v.findViewById(R.id.recylerView);
        tryAgain =(Button) v.findViewById(R.id.try_again);


        //---------------------------------------------

        //if condition to test the network Availability



        /*
        Else If network is available then condition will
        show type of user want to choose
         */


            recyclerView.setVisibility(View.VISIBLE);


            //getting the instance from firebase database reference

            database = FirebaseDatabase.getInstance();
            DataRef = database.getReference("Features");


            LinearLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setHasFixedSize(true);
            LoadData();




        //----------------------------------------------

        return v;
    }



    //Method to test Network connection

     /*

    Method wil check Network is available or not
    if available then it will return true else false

     */

    public boolean isNetworkAvailable() {     //method

        //ConnectivityManager object

        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {


            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());

                //Testing all the type of network availabilities

                if (capabilities != null) {
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {

                        return true;
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {

                        return true;
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {

                        return true;
                    }
                }
            }
        }

        return false;

    }//method

    //Method to load data


    private void LoadData() {
        options=new FirebaseRecyclerOptions.Builder<Features>().
                setQuery(DataRef,Features.class)
                .build();

        adapter=new FirebaseRecyclerAdapter<Features, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull Features model) {
                holder.title.setText(model.getTitle());
                Picasso.get().load(model.getImages()).into(holder.images);

                final Features clickItem=model;
                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //Get FeaturesId send  to next Activity
                        Intent detailList=new Intent(getActivity(),Details.class);
                        //because FeatureId is key ,so we get key of this item
                        detailList.putExtra("FeaturesId",adapter.getRef(position).getKey());
                        startActivity(detailList);
                    }
                });

            }

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.very_special,parent,false);

                return new MyViewHolder(v);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);



    }

    /*
    it will start the adapter and update the data as data change in database
     */

   /* @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }


    it is use ,so if the app is in the background it
    will not utilize the resources and adapter get stops

    @Override
    public void onStop() {   super.onStop();
        adapter.stopListening();
    }*/



}  //class








