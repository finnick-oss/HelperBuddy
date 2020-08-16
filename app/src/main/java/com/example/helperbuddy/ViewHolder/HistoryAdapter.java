package com.example.helperbuddy.ViewHolder;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.helperbuddy.Common.Common;
import com.example.helperbuddy.HelperClasses.Order;
import com.example.helperbuddy.History;
import com.example.helperbuddy.Interface.ItemClickListener;
import com.example.helperbuddy.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

class HistoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {
    TextView user_name,occupation;
    CircleImageView user_image;
    ImageButton phone;
   private ItemClickListener itemClickListener;
    public HistoryViewHolder(@NonNull View itemView) {
        super(itemView);
        user_name = (TextView) itemView.findViewById(R.id.name_text);
        occupation = (TextView)  itemView.findViewById(R.id.occupation);
        user_image = (CircleImageView)  itemView.findViewById(R.id.profile_image);
        phone = (ImageButton)  itemView.findViewById(R.id.call_button);

        itemView.setOnCreateContextMenuListener(this);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Select action");
        menu.add(0,0,getAdapterPosition(), Common.DELETE);

    }
}

public class HistoryAdapter extends RecyclerView.Adapter<HistoryViewHolder> {

    private List<Order> listData= new ArrayList<>();
    private Context context;


    public HistoryAdapter(List<Order> listData,Context context){
        this.listData=listData;
        this.context=context;

    }



    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View itemView=inflater.inflate(R.layout.list_layout,parent,false);
        return new HistoryViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, final int position) {
        holder.user_name.setText(listData.get(position).getName());
        holder.occupation.setText(listData.get(position).getOccupation());
        holder.phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+ listData.get(position).getPhoneNo()));
                context.startActivity(intent);
            }
        });
       Picasso.get().load(listData.get(position).getImages())
                .into(holder.user_image);

    }

    @Override
    public int getItemCount() {
       return listData.size();
    }
}
