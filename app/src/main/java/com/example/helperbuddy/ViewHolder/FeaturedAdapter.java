package com.example.helperbuddy.ViewHolder;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.helperbuddy.Details;
import com.example.helperbuddy.FullDetails;
import com.example.helperbuddy.HelperClasses.WorkerSignupHelper;
import com.example.helperbuddy.Interface.ItemClickListener;
import com.example.helperbuddy.R;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.ObservableSnapshotArray;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FeaturedAdapter extends RecyclerView.Adapter<  FeaturedAdapter.ViewHolder>{
    static final  String Tag="  featuredRecycler";;
    private Details ncontext;
    List<WorkerSignupHelper> featuredList;
    FirebaseRecyclerAdapter<WorkerSignupHelper, ViewHolder> adapter;
    float ratingValue;
    RecyclerView  featuredRecycler;


    public FeaturedAdapter(Details context, List<WorkerSignupHelper> featuredList) {
        this.ncontext=context;
        this.featuredList= featuredList;
    }//constructor that hold the data of recycler view

    @NonNull
    @Override
    public   FeaturedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v;
        v= LayoutInflater.from(parent.getContext()).inflate(R.layout.details,parent,false);
        ViewHolder vHolder=new ViewHolder(v);
      //  vHolder.setIsRecyclable(false);
        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Picasso.get().load(featuredList.get(position).getImages())
                .into(holder.images);
        holder.name.setText(featuredList.get(position).getName());
        holder.address.setText(featuredList.get(position).getAddress());

     float totalRatings = Float.valueOf(featuredList.get(position).getTotal_ratings()).floatValue();
        int encounterUsers = Integer.valueOf(featuredList.get(position).getTotal_users()).intValue();
       ratingValue = (float) Math.rint(totalRatings / encounterUsers);
       holder.workerRating.setRating(ratingValue);

        FeaturedAdapter featuredAdapter = new FeaturedAdapter(ncontext, featuredList);
      //  featuredRecycler.setAdapter(featuredAdapter);


     //  featuredList.remove(position);
   // featuredRecycler.removeViewAt(position);
      // featuredAdapter.notifyItemRemoved(position);

        //featuredAdapter.notifyItemRangeChanged(position,featuredList.size());
        //featuredAdapter.notifyDataSetChanged();
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                Intent fulldetail = new Intent(view.getContext(), FullDetails.class);
                fulldetail.putExtra("DetailId", featuredList.get(position).getId());//it will send detailId to new activity
                ncontext.startActivity(fulldetail);
            }


        });

    }
    public void setUsers(List<WorkerSignupHelper> list) {
        featuredList = list;
        notifyDataSetChanged();
    }

    public WorkerSignupHelper getUser(int position) {
        if (position > featuredList.size() - 1) {
            return new WorkerSignupHelper();
        } else {
            return featuredList.get(position);
        }
    }

    @Override
    public int getItemCount() {
        return featuredList.size();
    }

    public static class ViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView images;
        public TextView name, address;
        public RatingBar workerRating;

        private List<WorkerSignupHelper> users;
        private ItemClickListener itemClickListener;//variable of interface

        public ViewHolder(@NonNull View itemView) {   //method
            super(itemView);
            images = (ImageView) itemView.findViewById(R.id.special_image);
            name = (TextView) itemView.findViewById(R.id.special_title);
            address = (TextView) itemView.findViewById(R.id.special_address);
            workerRating = (RatingBar) itemView.findViewById(R.id.rating_bar_details);

            itemView.setOnClickListener(this);
        }

        //using listener for setting the item

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }


        //overriding the method onClick for getting the adapter position

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v, getAdapterPosition(), false);

        }

    }
    }

