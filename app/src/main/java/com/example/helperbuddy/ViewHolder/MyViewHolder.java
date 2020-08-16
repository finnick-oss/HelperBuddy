package com.example.helperbuddy.ViewHolder;  //package

/*
Class holds the view which will display
the view with Image and title

 */

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.helperbuddy.Interface.ItemClickListener;
import com.example.helperbuddy.R;


/*
Class Details MyViewHolder which is
extending the RecyclerView.ViewHolder
and implementing the View.OnClickListener interface
 */

 public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

     //declared all attributes

    public ImageView images;
    public   TextView title;
    private ItemClickListener itemClickListener;//variable of interface

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        images=(ImageView) itemView.findViewById(R.id.special_image);
        title=(TextView) itemView.findViewById(R.id.special_title);

        itemView.setOnClickListener(this);
    }

     //using listener for setting the item

     public void setItemClickListener(ItemClickListener itemClickListener) {
         this.itemClickListener = itemClickListener;
     }

      //overriding the method onClick for getting the adapter position

     @Override
     public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),false);
     }

 }  //class
