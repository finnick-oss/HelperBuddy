package com.example.helperbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.helperbuddy.HelperClasses.WorkerSignupHelper;
import com.example.helperbuddy.Interface.ItemClickListener;
import com.example.helperbuddy.ViewHolder.DetailsViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class Search extends AppCompatActivity {

    private EditText mSearchField;
    private ImageButton mSearchBtn;
    FirebaseRecyclerAdapter<WorkerSignupHelper, UsersViewHolder>  firebaseRecyclerAdapter;

    private RecyclerView mResultList;
    FirebaseRecyclerOptions<WorkerSignupHelper> options;
    private DatabaseReference mUserDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);




            mUserDatabase = FirebaseDatabase.getInstance().getReference("worker");


            mSearchField = (EditText) findViewById(R.id.search_field1);

            mSearchBtn = (ImageButton) findViewById(R.id.search_btn1);

            mResultList = (RecyclerView) findViewById(R.id.result_list);
            mResultList.setHasFixedSize(true);
            mResultList.setLayoutManager(new LinearLayoutManager(this));

            mSearchBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String searchText = mSearchField.getText().toString();//taking value from search bar pass to FirebaseUserSearch method

                    firebaseUserSearch(searchText);

                }
            });

        }

        private void firebaseUserSearch(String searchText) {

            Toast.makeText(Search.this, "Searching.....", Toast.LENGTH_LONG).show();


            options=new FirebaseRecyclerOptions.Builder<WorkerSignupHelper>().
                    setQuery( mUserDatabase.orderByChild("name").startAt(searchText).endAt(searchText + "\uf8ff"),WorkerSignupHelper.class)
                    .build();//if user exist  with name then it will show the user
                firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<WorkerSignupHelper,UsersViewHolder>(options) {
                @NonNull
                @Override
                public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_layout,parent,false);

                    return new UsersViewHolder(v);
                }

                @Override
                protected void onBindViewHolder(@NonNull UsersViewHolder holder, int position, @NonNull WorkerSignupHelper model) {
                    holder.setDetails(getApplicationContext(), model.getName(), model.getOccupation(), model.getImages());

                    final WorkerSignupHelper clickItem=model;
                    holder.setItemClickListener(new ItemClickListener() {
                        @Override
                        public void onClick(View view, int position, boolean isLongClick) {
                            Intent fulldetail =new Intent(Search.this,FullDetails.class);
                            fulldetail.putExtra("SearchId",firebaseRecyclerAdapter.getRef(position).getKey());//it will send detailId to new activity
                            startActivity(fulldetail);
                        }
                    });
                }


            };
            firebaseRecyclerAdapter.startListening();
            mResultList.setAdapter(firebaseRecyclerAdapter);

        }



        // View Holder Class

        public static class UsersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            View mView;
            private ItemClickListener itemClickListener;//variable of interface

            public UsersViewHolder(View itemView) {
                super(itemView);
                mView = itemView;
            }

            public void setDetails(Context ctx, String userName, String userStatus, String userImage){

                TextView user_name = (TextView) mView.findViewById(R.id.name_text);
                TextView occupation = (TextView) mView.findViewById(R.id.occupation);
                CircleImageView user_image = (CircleImageView) mView.findViewById(R.id.profile_image);
                itemView.setOnClickListener(this);


                user_name.setText(userName);
                occupation.setText(userStatus);

                //Glide Library to get url of images and convert into images
                Glide.with(ctx).load(userImage).into(user_image);

            }

            public void setItemClickListener(ItemClickListener itemClickListener) {
                this.itemClickListener = itemClickListener;
            }
            @Override
            public void onClick(View v) {
                itemClickListener.onClick(v,getAdapterPosition(),false);
            }
        }

    }

