package com.example.mat3amk.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.example.mat3amk.Order;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteAssetHelper {
    private static final String dbName = "amrDB.db";
    private static final int dbVersion =  1;
    public Database(Context context) {
        super(context, dbName, null, dbVersion);
    }


    public List<Order> getCarts()
    {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        String[] sqlSelect =  {"ProductName","Quantity","Price"};
        String sqlTable = "OrderDetail";
        qb.setTables(sqlTable);
        Cursor c = qb.query(db,sqlSelect,null,null,null,null,null);
        final List<Order>  result = new ArrayList<>();
        if(c.moveToFirst())
        {
            do {
result.add(new Order(
        c.getString(c.getColumnIndex("ProductName")),
        c.getString(c.getColumnIndex("Quantity")),
        c.getString(c.getColumnIndex("Price"))
));
            }while (c.moveToNext());
        }

        return result;
    }

    public void addToCart(Order order)
    {
        SQLiteDatabase db = getReadableDatabase();
        String query  =     String.format("INSERT INTO OrderDetail(ProductName,Quantity,Price) VALUES('%s' , '%s' , '%s')"
        ,order.getProductName(),order.getQuantity(),order.getPrice());
        db.execSQL(query);
    }

    public void cleanCart()
    {
        SQLiteDatabase db = getReadableDatabase();
       String query = String.format("DELETE FROM OrderDetail");
        db.execSQL(query);
    }

    public  void removeFromCart(String productId , String productQun)
    {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM OrderDetail WHERE ProductName = '%s' and Quantity = '%s'",productId,productQun );
        db.execSQL(query);
    }
}
