package com.example.helperbuddy;  //package

/*
Here in this class
tll the search performance has done
like by using search bar you can search the any occupation
worker by name.
 */


//imported all the important classes and all the interfaces or packages

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.helperbuddy.HelperClasses.Features;
import com.example.helperbuddy.HelperClasses.WorkerSignupHelper;
import com.example.helperbuddy.Interface.ItemClickListener;
import com.example.helperbuddy.ViewHolder.DetailsViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Details extends AppCompatActivity {  //class

    //Reference for Displaying average Rating-----------------------------
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    float ratingValue;
    float totalRatings;
    int encounterUsers;

//-----------------------------------------------------------


    RecyclerView recyclerView;  //recyclerView Object

    FirebaseRecyclerOptions<WorkerSignupHelper> options;
    FirebaseRecyclerAdapter<WorkerSignupHelper, DetailsViewHolder> adapter;
    FirebaseDatabase database;

    DatabaseReference detailList;  //DatabaseReference object

    FirebaseRecyclerAdapter<WorkerSignupHelper, DetailsViewHolder> searchadapter;

    List<String> suggestList=new ArrayList<>();  //collection of list object

    MaterialSearchBar materialSearchBar;  //material search bar object

    String FeaturesId ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);



        //Reference for firebase


        rootNode = FirebaseDatabase.getInstance();



        //getting the instance from firebase

        database=FirebaseDatabase.getInstance();
        detailList =database.getReference("worker");

        recyclerView=findViewById(R.id.recylerView);
        LinearLayoutManager layoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        //Getting Intent Here

        if(getIntent()!=null)
            FeaturesId=getIntent().getStringExtra("FeaturesId");
        if(!FeaturesId.isEmpty() && FeaturesId!=null){
            LoadData(FeaturesId);
        }

        //search
        materialSearchBar=(MaterialSearchBar)findViewById(R.id.searchBar);
        materialSearchBar.setHint("Search For Service");
        loadSuggest();//this method is for suggestion in search box from firebase
        materialSearchBar.setLastSuggestions(suggestList);
        materialSearchBar.setCardViewElevation(5);
        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //when user type their text ,it will change suggest list
                List<String> suggest=new ArrayList<String>();
                for(String search:suggestList)
                {
                    if(search.toLowerCase().contains(materialSearchBar.getText().toLowerCase()))suggest.add(search);
                }
                materialSearchBar.setLastSuggestions(suggest);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                //when search bar is close
                //Restore original suggest adapter
                if(!enabled)
                    recyclerView.setAdapter(searchadapter);
            }
            @Override
            public void onSearchConfirmed(CharSequence text) {
                //when search finish then it show result
                startSearch(text);
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });
    }

    private void startSearch(CharSequence text) {


        options=new FirebaseRecyclerOptions.Builder<WorkerSignupHelper>().
                setQuery(detailList.orderByChild("name").equalTo(text.toString()),WorkerSignupHelper.class)//compare name
                .build();

        searchadapter=new FirebaseRecyclerAdapter<WorkerSignupHelper, DetailsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull DetailsViewHolder detailsViewHolder, int i, @NonNull WorkerSignupHelper model) {
                detailsViewHolder.name.setText(model.getName());
                detailsViewHolder.address.setText(model.getAddress());
                Picasso.get().load(model.getImages()).into(detailsViewHolder.images);

                float totalRatings = Float.valueOf(model.getTotal_ratings()).floatValue();
                int  encounterUsers =Integer.valueOf(model.getTotal_users()).intValue();
                ratingValue =(float) Math.rint(totalRatings/encounterUsers);
                detailsViewHolder.workerRating.setRating(ratingValue);

                final WorkerSignupHelper clickItem=model;
                detailsViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent fulldetail =new Intent(Details.this,FullDetails.class);
                        fulldetail.putExtra("DetailId",searchadapter.getRef(position).getKey());//it will send detailId to new activity
                        startActivity(fulldetail);
                    }
                });


            }


            @NonNull
            @Override
            public DetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.details,parent,false);

                return new DetailsViewHolder(v);
            }
        };
        searchadapter.startListening();
        recyclerView.setAdapter(searchadapter);//set adapter for recycler view is search layout
    }

    private void loadSuggest() {  //this method is for suggestion in search box
        detailList.orderByChild("occupation").equalTo(FeaturesId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot postSnapshot:snapshot.getChildren()){
                            WorkerSignupHelper item=postSnapshot.getValue(WorkerSignupHelper.class);
                            suggestList.add(item.getName());//add name of food to suggest list
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void LoadData(String featuresId) {
        options=new FirebaseRecyclerOptions.Builder<WorkerSignupHelper>().
                setQuery(detailList.orderByChild("occupation").equalTo(featuresId),WorkerSignupHelper.class)
                .build();

        adapter=new FirebaseRecyclerAdapter<WorkerSignupHelper, DetailsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull DetailsViewHolder detailsViewHolder, int i, @NonNull WorkerSignupHelper model) {
                detailsViewHolder.name.setText(model.getName());
                detailsViewHolder.address.setText(model.getAddress());
                Picasso.get().load(model.getImages()).into(detailsViewHolder.images);

                 float totalRatings = Float.valueOf(model.getTotal_ratings()).floatValue();
                 int  encounterUsers =Integer.valueOf(model.getTotal_users()).intValue();
                ratingValue =(float) Math.rint(totalRatings/encounterUsers);
                detailsViewHolder.workerRating.setRating(ratingValue);



                final WorkerSignupHelper clickItem=model;
                detailsViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent fulldetail =new Intent(Details.this,FullDetails.class);
                        fulldetail.putExtra("DetailId",adapter.getRef(position).getKey());//it will send detailId to new activity
                        startActivity(fulldetail);
                    }
                });

            }


            @NonNull
            @Override
            public DetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.details,parent,false);

                return new DetailsViewHolder(v);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

    /*
    it will start the adapter and update the data as data change in database
     */

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    /*
    it is use ,so if the app is in the background it
    will not utilize the resources and adapter get stops

     */

    @Override
    public void onStop() { super.onStop();
        adapter.stopListening();
    }
}
