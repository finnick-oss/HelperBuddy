package com.example.helperbuddy;  //package
/*
Here in this class
tll the search performance has done
like by using search bar you can search the any occupation
worker by name.
 */
//imported all the important classes and all the interfaces or packages


import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.helperbuddy.Common.Common;
import com.example.helperbuddy.Details;
import com.example.helperbuddy.FullDetails;
import com.example.helperbuddy.HelperClasses.WorkerSignupHelper;
import com.example.helperbuddy.HelperClasses.sharePreference_OneTimeLogin;
import com.example.helperbuddy.Interface.ItemClickListener;
import com.example.helperbuddy.R;

import com.example.helperbuddy.ViewHolder.FeaturedAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.ObservableSnapshotArray;
import com.google.firebase.database.ChildEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;


import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.example.helperbuddy.HelperClasses.WorkerSignupHelper;
import com.example.helperbuddy.Interface.ItemClickListener;
import com.example.helperbuddy.ViewHolder.DetailsViewHolder;
import com.firebase.geofire.GeoFire;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicMarkableReference;

public class Details extends AppCompatActivity implements AdapterView.OnItemSelectedListener {  //class

    private Details ncontext = Details.this;
    private ArrayList<WorkerSignupHelper> featuredList;
    RecyclerView featuredRecycler;
    public FeaturedAdapter featuredAdapter;

    //Reference for Displaying average Rating-----------------------------
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    private DatabaseReference databas;
    float ratingValue;
    private GeoFire geofire;
    public Query query;
    String text;

    FusedLocationProviderClient fusedLocationProviderClient;
    //-----------------------------------------------------------
    private static final String LOG_TAG = "Details";
    private boolean fetchedUserIds;
    private Set<String> userIdsWithListeners = new HashSet<String>();
    private Map<String, Location> userIdsToLocations = new HashMap<>();
    private int initialListSize;
    private int iterationCount;
    private ValueEventListener userValueListener;
    private List<WorkerSignupHelper> users = new ArrayList<>();
    private Set<GeoQuery> geoQueries = new HashSet<>();
    public String userIds;

    RecyclerView recyclerView;  //recyclerView Object

    FirebaseRecyclerOptions<WorkerSignupHelper> options;
    FirebaseRecyclerAdapter<WorkerSignupHelper, DetailsViewHolder> adapter;

    FirebaseDatabase database;
    DatabaseReference detailList;  //DatabaseReference object
    DatabaseReference FullList;
    FirebaseRecyclerAdapter<WorkerSignupHelper, DetailsViewHolder> searchadapter;

    List<String> suggestList = new ArrayList<>();  //collection of list object
    MaterialSearchBar materialSearchBar;  //material search bar object

    public String FeaturesId = "";

    GeoLocation CURRENT_LOCATION1;
    GeoLocation CURRENT_LOCATION;
    int distance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        location();
        sharePreference_OneTimeLogin shared= new sharePreference_OneTimeLogin(Details.this);

     CURRENT_LOCATION = new GeoLocation(Double.parseDouble(shared.getLatitude()), Double.parseDouble(shared.getLongitude()));


        Spinner   simpleSpinner = findViewById(R.id.aSpinner);
      //  aSpinner.setOnItemSelectedListener(this);

       // Spinner simpleSpinner = findViewById(R.id.simpleSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.Spinner_items,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        simpleSpinner.setAdapter(adapter);
        simpleSpinner.setOnItemSelectedListener(this);



        featuredRecycler = findViewById(R.id.recylerView);
        LinearLayoutManager layoutManager1 = new GridLayoutManager(this, 2);
        featuredRecycler.setLayoutManager(layoutManager1);
        featuredRecycler.setHasFixedSize(true);
        featuredList = new ArrayList<WorkerSignupHelper>();
        geoQueries= new HashSet<>();
        // featuredRecycler();
//--------------------------------------------------------------------------------------------------------------
        //Reference for firebase
        rootNode = FirebaseDatabase.getInstance();
        //getting the instance from firebase
        database = FirebaseDatabase.getInstance();
        detailList = database.getReference("worker");

        recyclerView = findViewById(R.id.recylerView);
        LinearLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        //Getting Intent Here

        if (getIntent() != null)
            FeaturesId = getIntent().getStringExtra("FeaturesId");
        if (!FeaturesId.isEmpty() && FeaturesId != null) {
            // LoadData(FeaturesId);
            // setupFirebase(FeaturesId);
        }

        //search
        materialSearchBar = (MaterialSearchBar) findViewById(R.id.searchBar);
        materialSearchBar.setHint("Search For Service");

        materialSearchBar.setLastSuggestions(suggestList);
        materialSearchBar.setCardViewElevation(5);
        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //when user type their text ,it will change suggest list
                List<String> suggest = new ArrayList<String>();
                for (String search : suggestList) {
                    if (search.toLowerCase().contains(materialSearchBar.getText().toLowerCase()))
                        suggest.add(search);
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
                if (!enabled)
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


    @Override
    protected void onDestroy() {
        removeListeners();
        super.onDestroy();
    }
    private void location(){
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(Details.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getLocation();
        } else {
            ActivityCompat.requestPermissions(Details.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location != null) {
                    Geocoder geocoder = new Geocoder(Details.this, Locale.getDefault());
                    try {
                        List<Address> addresses = geocoder.getFromLocation(
                                location.getLatitude(), location.getLongitude(), 1
                        );
                        CURRENT_LOCATION1 = new GeoLocation(+addresses.get(0).getLatitude(), +addresses.get(0).getLongitude());
                        Toast.makeText(Details.this, "Latitude =" +CURRENT_LOCATION1, Toast.LENGTH_SHORT).show();
                       // Toast.makeText(this, "Longitude =" +Double.parseDouble(shared.getLongitude()), Toast.LENGTH_SHORT).show();
                        Toast.makeText(Details.this, "Location"+addresses.get(0).getLocality(), Toast.LENGTH_SHORT).show();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        HashMap<String,Integer> map=new HashMap<>();
        map.put("Within 1 Km",1);
        map.put("Within 5 Km",5);
        map.put("Within 20 Km",20);
        map.put("Within 50 Km",50);
        map.put("More Than 100km",1000);

        text=parent.getItemAtPosition(position).toString();

        distance=map.get(text);

        Toast.makeText(Details.this, "Adapter"+distance, Toast.LENGTH_SHORT).show();
        loadSuggest();//this method is for suggestion in search box from firebase
        setupFirebase(FeaturesId);
        // LoadData(FeaturesId);
        setupList();
        fetchUsers();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    private void startSearch(CharSequence text) {


        options = new FirebaseRecyclerOptions.Builder<WorkerSignupHelper>().
                setQuery(detailList.orderByChild("name").equalTo(text.toString()), WorkerSignupHelper.class)//compare name
                .build();

        searchadapter = new FirebaseRecyclerAdapter<WorkerSignupHelper, DetailsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull DetailsViewHolder detailsViewHolder, int i, @NonNull WorkerSignupHelper model) {
                detailsViewHolder.name.setText(model.getName());
                detailsViewHolder.address.setText(model.getAddress());
                Picasso.get().load(model.getImages()).into(detailsViewHolder.images);

                float totalRatings = Float.valueOf(model.getTotal_ratings()).floatValue();
                int encounterUsers = Integer.valueOf(model.getTotal_users()).intValue();
                ratingValue = (float) Math.rint(totalRatings / encounterUsers);
                detailsViewHolder.workerRating.setRating(ratingValue);

                final WorkerSignupHelper clickItem = model;
                detailsViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent fulldetail = new Intent(Details.this, FullDetails.class);
                        fulldetail.putExtra("DetailId", searchadapter.getRef(position).getKey());//it will send detailId to new activity
                        startActivity(fulldetail);
                    }
                });


            }


            @NonNull
            @Override
            public DetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.details, parent, false);

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
                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                            WorkerSignupHelper item = postSnapshot.getValue(WorkerSignupHelper.class);
                            suggestList.add(item.getName());//add name of food to suggest list
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

  /*private void LoadData(String featuresId) {
        options = new FirebaseRecyclerOptions.Builder<WorkerSignupHelper>().
                setQuery(detailList.orderByChild("occupation").equalTo(featuresId), WorkerSignupHelper.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<WorkerSignupHelper, DetailsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull DetailsViewHolder detailsViewHolder, int i, @NonNull WorkerSignupHelper model) {
                detailsViewHolder.name.setText(model.getName());
                detailsViewHolder.address.setText(model.getAddress());
                Picasso.get().load(model.getImages()).into(detailsViewHolder.images);

                float totalRatings = Float.valueOf(model.getTotal_ratings()).floatValue();
                int encounterUsers = Integer.valueOf(model.getTotal_users()).intValue();
                ratingValue = (float) Math.rint(totalRatings / encounterUsers);
                detailsViewHolder.workerRating.setRating(ratingValue);


                final WorkerSignupHelper clickItem = model;
                detailsViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent fulldetail = new Intent(Details.this, FullDetails.class);
                        fulldetail.putExtra("DetailId", adapter.getRef(position).getKey());//it will send detailId to new activity
                        startActivity(fulldetail);
                    }
                });

            }


            @NonNull
            @Override
            public DetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.details, parent, false);

                return new DetailsViewHolder(v);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

    /*
    it will start the adapter and update the data as data change in database
     */

    //--------------------------------------------------------------------------------------------------------------------------------------

  GeoLocation  CURRENT_LOCATION3 = new GeoLocation(28.6991436,77.0987288);


    private void fetchUsers() {


        List<String> userIds = new ArrayList<>();
        GeoQuery geoQuery = geofire.queryAtLocation(CURRENT_LOCATION, distance);//show things within 50km to my current location
       // Toast.makeText(Details.this, "Location3=="+CURRENT_LOCATION3, Toast.LENGTH_SHORT).show();
        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
               /* Location to = new Location("to");
                to.setLatitude(location.latitude);
                to.setLongitude(location.longitude);*/
                if (!fetchedUserIds) {
                      userIds.add(key);

                   //userIdsToLocations.put(key, to);
                } else {
                    //userIdsToLocations.put(key, to);
                    addUserListener(key);

                }
            }

            @Override
            public void onKeyExited(String key) {
                Log.d(LOG_TAG, "onKeyExited: ");
                if (userIdsWithListeners.contains(key)) {
                    int position = getUserPosition(key);
                    featuredList.remove(position);
                    featuredAdapter.notifyItemRemoved(position);
                }
            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {

            }

            @Override
            public void onGeoQueryReady() {
                Log.d(LOG_TAG, "onGeoQueryReady: ");
                initialListSize=userIds.size();
                iterationCount=0;
                for (String userId :userIds){
                    addUserListener(userId);
                   // Toast.makeText(Details.this, "GeoQuery"+initialListSize,Toast.LENGTH_SHORT).show();
                }

            }
            private void addUserListener(String userId) {
                databas.child("worker").child(userId)
              .addListenerForSingleValueEvent(userValueListener);

               // Toast.makeText(Details.this, "ListenerId"+userId,Toast.LENGTH_SHORT).show();
               // FullList=databas.child("worker").child(userId);
               // query = FirebaseDatabase.getInstance().getReference("worker").child(userId);




                userIdsWithListeners.add(userId);
                //LoadData(FeaturesId);
            }
            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });
        geoQueries.add(geoQuery);
    }

    private void setupList() {
        clearAlll();

      userValueListener= new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                  //  WorkerSignupHelper featured = new WorkerSignupHelper();
                     WorkerSignupHelper featured = dataSnapshot.getValue(WorkerSignupHelper.class);
                    featured.setId(dataSnapshot.getKey().toString());
                    featured.setName(dataSnapshot.child("name").getValue().toString());
                    featured.setImages(dataSnapshot.child("images").getValue().toString());
                    featured.setAddress(dataSnapshot.child("address").getValue().toString());
                    featured.setTotal_users(dataSnapshot.child("total_users").getValue().toString());
                    featured.setTotal_ratings(dataSnapshot.child("total_ratings").getValue().toString());



                int position = getUserPosition(featured.getId());

                FeaturedAdapter featuredAdapter = new FeaturedAdapter(ncontext, featuredList);
                featuredRecycler.setAdapter(featuredAdapter);
                featuredAdapter.notifyDataSetChanged();
                featuredList.add(featured);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
    }
    private void clearAlll(){
        if(featuredList != null){
            featuredList.clear();
            if(featuredAdapter !=null){
                featuredAdapter.notifyDataSetChanged();
            }
        }
        featuredList=new ArrayList<WorkerSignupHelper>();
    }
    private void setupFirebase(String featuresId) {
        databas = FirebaseDatabase.getInstance().getReference();
        geofire = new GeoFire(databas.child("geofire").child(featuresId));
       featuredRecycler();
        //LoadData(FeaturesId);
    }

    private void featuredRecycler() {

      //  Query query=reference.getReference("featured");
        Toast.makeText(Details.this, "before", Toast.LENGTH_SHORT).show();
        userValueListener=new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    WorkerSignupHelper featured = dataSnapshot.getValue(WorkerSignupHelper.class);

                Toast.makeText(Details.this, "featuredRecycler", Toast.LENGTH_SHORT).show();
                featured.setId(dataSnapshot.getKey().toString());
                if (featuredList.contains(featured)) {
                    Toast.makeText(Details.this, "userupdated", Toast.LENGTH_SHORT).show();
                    userUpdated(featured);
                } else {
                    Toast.makeText(Details.this, "newUser", Toast.LENGTH_SHORT).show();
                    newUser(featured);


                }
                }

            private void newUser(WorkerSignupHelper u) {
                Log.d(LOG_TAG, "onDataChange: new user");
                iterationCount++;
                featuredList.add(0, u);
                if (!fetchedUserIds && iterationCount == initialListSize) {
                    fetchedUserIds = true;
                    featuredAdapter.setUsers(featuredList);
                } else  {

                    featuredAdapter.notifyItemInserted(0);
                }
            }

            private void userUpdated(WorkerSignupHelper u) {
                Log.d(LOG_TAG, "onDataChange: update");
                int position = getUserPosition(u.getId());
                featuredList.set(position, u);
                featuredAdapter.notifyItemChanged(position);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
    }
    private void removeListeners() {
        for (GeoQuery geoQuery : geoQueries) {
            geoQuery.removeAllListeners();
        }

        for (String userId : userIdsWithListeners) {
            databas.child("worker").child(userId)
                    .removeEventListener(userValueListener);
        }
    }


    private void LoadData(String featuresId) {
       // Toast.makeText(Details.this, "FeaturedId"+FullList,Toast.LENGTH_SHORT).show();
        options = new FirebaseRecyclerOptions.Builder<WorkerSignupHelper>().
                setQuery(query, WorkerSignupHelper.class)
                .build();
        Toast.makeText(Details.this, "FeaturedId"+detailList.orderByChild("occupation").equalTo(featuresId),Toast.LENGTH_SHORT).show();
//        Toast.makeText(Details.this, "FullList"+FullList.orderByChild("occupation").equalTo(featuresId),Toast.LENGTH_SHORT).show();
        adapter = new FirebaseRecyclerAdapter<WorkerSignupHelper, DetailsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull DetailsViewHolder detailsViewHolder, int i, @NonNull WorkerSignupHelper model) {

                detailsViewHolder.name.setText(model.getName());
                detailsViewHolder.address.setText(model.getAddress());
                Picasso.get().load(model.getImages()).into(detailsViewHolder.images);

               float totalRatings = Float.valueOf(model.getTotal_ratings()).floatValue();
                int encounterUsers = Integer.valueOf(model.getTotal_users()).intValue();
                ratingValue = (float) Math.rint(totalRatings / encounterUsers);
                detailsViewHolder.workerRating.setRating(ratingValue);

           /*     if (users.contains(model)) {
                    userUpdated(model);
                } else {
                    newUser(model);
                }*/

                final WorkerSignupHelper clickItem = model;
                detailsViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent fulldetail = new Intent(Details.this, FullDetails.class);
                        fulldetail.putExtra("DetailId", adapter.getRef(position).getKey());//it will send detailId to new activity
                        startActivity(fulldetail);
                    }
                });

            }
            private void newUser(WorkerSignupHelper u) {
                Log.d(LOG_TAG, "onDataChange: new user");
                iterationCount++;
                users.add(0, u);
                if (!fetchedUserIds && iterationCount == initialListSize) {
                    fetchedUserIds = true;


                    adapter.notifyDataSetChanged();
                    // adapter.setUsers(users);
                } else if (fetchedUserIds) {

                    adapter.notifyItemInserted(getIndexOfNewUser(u));
                }
            }

            private void userUpdated(WorkerSignupHelper u) {
                Log.d(LOG_TAG, "onDataChange: update");
                int position = getUserPosition(u.getId());
                users.set(position, u);
                adapter.notifyItemChanged(position);
            }
            public void setUsers(List<WorkerSignupHelper> list) {
                users = list;
                notifyDataSetChanged();
            }

            public WorkerSignupHelper getUser(int position) {
                if (position > users.size() - 1) {
                    return new WorkerSignupHelper();
                } else {
                    return users.get(position);
                }

            }


            @NonNull
            @Override
            public DetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.details, parent, false);

                return new DetailsViewHolder(v);
            }

        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);



    }



    private int getIndexOfNewUser(WorkerSignupHelper u) {
        for (int i = 0; i < featuredList.size(); i++) {
            if (featuredList.get(i).getId().equals(u.getId())) {
                Log.d(LOG_TAG, "getIndexOfNewUser: " + i);
                return i;
            }
        }
        throw new RuntimeException();
    }

    private int getUserPosition(String id) {
        for (int i = 0; i < featuredList.size(); i++) {
            if (featuredList.get(i).getId().equals(id)) {
                return i;
            }
        }
        return -1;
    }



}


