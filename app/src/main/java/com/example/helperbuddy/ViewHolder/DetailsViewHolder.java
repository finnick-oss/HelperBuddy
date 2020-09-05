

package com.example.helperbuddy.ViewHolder;

/*
Here is the View holder class
which holds a view for making view as
infinite scroll view.
 */

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.helperbuddy.HelperClasses.WorkerSignupHelper;
import com.example.helperbuddy.Interface.ItemClickListener;
import com.example.helperbuddy.R;

import java.util.List;

/*
Class Details ViewHolder which is
extending the RecyclerView.ViewHolder
and implementing the View.OnClickListener interface
 */

public class DetailsViewHolder  extends RecyclerView.ViewHolder implements View.OnClickListener {  //class

    //declared all attributes

    public ImageView images;
    public TextView name,address;
    public RatingBar workerRating;

    private List<WorkerSignupHelper> users;
    private ItemClickListener itemClickListener;//variable of interface

    public DetailsViewHolder(@NonNull View itemView) {   //method
        super(itemView);
        images=(ImageView) itemView.findViewById(R.id.special_image);
        name=(TextView) itemView.findViewById(R.id.special_title);
        address=(TextView) itemView.findViewById(R.id.special_address);
        workerRating=(RatingBar)itemView.findViewById(R.id.rating_bar_details);

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



