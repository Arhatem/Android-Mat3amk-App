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
    private static final int dbVersion = 1;

    public Database(Context context) {
        super(context, dbName, null, dbVersion);
    }


    public List<Order> getCarts(String userEmail) {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        String[] sqlSelect = {"userEmail","ProductName", "Quantity", "Price"};
        String sqlTable = "OrderDetail";
        qb.setTables(sqlTable);
        Cursor c = qb.query(db, sqlSelect, "userEmail=?", new String[]{userEmail}, null, null, null);
        final List<Order> result = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                result.add(new Order(
                        c.getString(c.getColumnIndex("userEmail")),
                        c.getString(c.getColumnIndex("ProductName")),
                        c.getString(c.getColumnIndex("Quantity")),
                        c.getString(c.getColumnIndex("Price"))
                ));
            } while (c.moveToNext());
        }

        return result;
    }

    public void addToCart(Order order) {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("INSERT  INTO OrderDetail(userEmail,ProductName,Quantity,Price) VALUES(\"%s\",\"%s\" , '%s' , '%s')"
             , order.getUserEmail()  , order.getProductName(), order.getQuantity(), order.getPrice());
        db.execSQL(query);
    }

    public void cleanCart(String userEmail) {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM OrderDetail WHERE userEmail= \"%s\"",userEmail);
        db.execSQL(query);
    }

    public void removeFromCart(String productId, String productQun) {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM OrderDetail WHERE ProductName = \"%s\" and Quantity = '%s'", productId, productQun);
        db.execSQL(query);
    }

    public boolean exists(String productName,String userEmail) {
        SQLiteDatabase database = getReadableDatabase();
        String query = String.format("SELECT * FROM OrderDetail WHERE ProductName = \"%s\" AND userEmail = \"%s\";", productName,userEmail);
        Cursor cursor = database.rawQuery(query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;

    }

    public int getProductCount(String productName,String userEmail) {

        SQLiteDatabase database = getReadableDatabase();
        String query = String.format("SELECT Quantity FROM OrderDetail WHERE ProductName = \"%s\" AND userEmail = \"%s\";", productName,userEmail);
        Cursor cursor = database.rawQuery(query, null);
        cursor.moveToFirst();
        return cursor.getInt(cursor.getColumnIndex("Quantity"));
    }

    public void updateProductCount(String productName, int count,String userEmail) {
        SQLiteDatabase database = getReadableDatabase();
        String query = String.format("UPDATE OrderDetail SET Quantity =" + count + " WHERE ProductName = \"%s\" AND userEmail = \"%s\";", productName,userEmail);
        database.execSQL(query);
    }

    //Favorites
    public void addToFavorites(String foodName,String userEmail) {
        SQLiteDatabase database = getReadableDatabase();
        String query = String.format("INSERT INTO Favorites(foodName,userEmail) VALUES(\"%s\",\"%s\");", foodName,userEmail);
        database.execSQL(query);
    }

    public void removeFromFavorites(String foodName,String userEmail) {
        SQLiteDatabase database = getReadableDatabase();
        String query = String.format("DELETE FROM Favorites WHERE foodName = \"%s\" AND userEmail= \"%s\";", foodName, userEmail);
        database.execSQL(query);
    }

    public boolean isFavorite(String foodName,String userEmail) {
        SQLiteDatabase database = getReadableDatabase();
        String query = String.format("SELECT * FROM Favorites WHERE foodName = \"%s\" AND userEmail = \"%s\";", foodName,userEmail);
        Cursor cursor = database.rawQuery(query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public int getCountCart(String userEmail) {

        int count = 0;
        SQLiteDatabase database = getReadableDatabase();
        String query = String.format("SELECT COUNT(*) FROM OrderDetail WHERE userEmail = \"%s\"",userEmail);
        Cursor cursor = database.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                count = cursor.getInt(0);

            } while (cursor.moveToNext());
        }
        return count;
    }

}
