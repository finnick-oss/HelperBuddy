package com.example.helperbuddy.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.example.helperbuddy.HelperClasses.Order;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteAssetHelper {

    private static final String DB_NAME="HelperBuddyHistory.db";
    private static final int DB_VER=1;
    public Database(Context context) {
        super(context, DB_NAME,null, DB_VER);
    }
    public List<Order> getCarts()
    {
        SQLiteDatabase db=getReadableDatabase();
        SQLiteQueryBuilder qb=new SQLiteQueryBuilder();

        String[] sqlSelect={"name","occupation","address","description","phoneNo","images"};
        String sqlTable="HistoryDetails";

        qb.setTables(sqlTable);
        Cursor c= qb.query(db,sqlSelect,null,null,null,null,null,null);
        final List<Order> result=new ArrayList<>();
        if(c.moveToFirst())
        {
            do{
                result.add(new Order(c.getString(c.getColumnIndex("name")),
                        c.getString(c.getColumnIndex("occupation")),
                        c.getString(c.getColumnIndex("address")),
                        c.getString(c.getColumnIndex("description")),
                        c.getString(c.getColumnIndex("phoneNo")),
                        c.getString(c.getColumnIndex("images"))
                ));



            }while (c.moveToNext());
        }
        return result;

    }

    public void addToCart(Order order)
    {
        SQLiteDatabase db=getReadableDatabase();
        String query=String.format("INSERT INTO HistoryDetails(name,occupation,address,description,phoneNo,images) VALUES('%s','%s','%s','%s','%s','%s');",
                                  order.getName(),
                                  order.getOccupation(),
                                  order.getAddress(),
                                  order.getDescription(),
                                  order.getPhoneNo(),
                                  order.getImages()
                                  );
                db.execSQL(query);
    }

    public void clearCart(Order order)
    {
        SQLiteDatabase db=getReadableDatabase();
        String query=String.format("DELETE FROM HistoryDetails");
        db.execSQL(query);
    }



}
