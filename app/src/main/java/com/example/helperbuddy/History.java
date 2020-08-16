package com.example.helperbuddy;


/*
this is for to store the worker
history
 */


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.helperbuddy.Common.Common;
import com.example.helperbuddy.Database.Database;
import com.example.helperbuddy.HelperClasses.Order;
import com.example.helperbuddy.ViewHolder.HistoryAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class History extends Fragment {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference request;

    List<Order> cart=new ArrayList<>();
    HistoryAdapter adapter;
    View v;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v= inflater.inflate(R.layout.fragment_history,container,false);

        //getting the instance from firebase database reference
        database=FirebaseDatabase.getInstance();
        request=database.getReference("Requests");

        recyclerView =(RecyclerView) v.findViewById(R.id.recylerViewHistory);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        loadHistoryWorker();
        return v;

    }

    private void loadHistoryWorker() {
        cart=new Database(getActivity()).getCarts();
        adapter=new HistoryAdapter(cart,getActivity());
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getTitle().equals(Common.DELETE))
            deleteCart(item.getOrder());
            return true;
    }

    private void deleteCart(int position) {
        cart.remove(position);//we are removing item at List<Order> by its position
        new Database(getActivity()).clearCart(new Order());//after that we are removing all the old data from SOLite

        for(Order item:cart)
            new Database(getActivity()).addToCart(item);//in last,i am updating the history
        loadHistoryWorker();
    }

}
