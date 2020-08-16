package com.example.helperbuddy.Interface;

import android.view.View;

public interface ItemClickListener {//when we click on recycler view then it will tell the postion or id of that card view
    void onClick(View view,int position,boolean isLongClick);
}
