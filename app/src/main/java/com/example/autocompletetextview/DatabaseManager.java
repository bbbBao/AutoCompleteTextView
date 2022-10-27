package com.example.autocompletetextview;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DatabaseManager extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "EmailDB";
    private static final int DATABASE_VERSION = 1;
    private static String NAME = "email";
    private static String TABLE_Email = "Email";
    private static String ID = "id";


    public DatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // creating table
    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = "";

        sql += "CREATE TABLE " + TABLE_Email;
        sql += " ( ";
        sql += ID + " INTEGER PRIMARY KEY AUTOINCREMENT, ";
        sql += NAME + " TEXT ";
        sql += " ) ";

        db.execSQL(sql);

    }

    // When upgrading the database, it will drop the current table and recreate.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String sql = "DROP TABLE IF EXISTS " + TABLE_Email;
        db.execSQL(sql);

        onCreate(db);
    }

    // create new record
    // @param myObj contains details to be added as single row.
    public boolean create(Email myObj) {

        boolean createSuccessful = false;


        if(!checkIfExists(myObj.objectName)){

            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(NAME, myObj.objectName);
            createSuccessful = db.insert(TABLE_Email, null, values) > 0;

            db.close();

            if(createSuccessful){
                Log.w("MainActivity", myObj.objectName + " created.");
            }
        }
        return createSuccessful;
    }

    // check if a record exists so it won't insert the next time you run this code
    public boolean checkIfExists(String objectName){

        boolean recordExists = false;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + ID + " FROM " + TABLE_Email + " WHERE " + NAME + " = '" + objectName + "'", null);
        if(cursor!=null) {

            if(cursor.getCount()>0) {
                recordExists = true;
            }
        }

        cursor.close();
        db.close();

        return recordExists;
    }

    // Read records related to the search term
    public ArrayList<Email> read(String searchTerm) {

        ArrayList<Email> recordsList = new ArrayList<Email>();

        // select query
        String sql = "";
        sql += "SELECT * FROM " + TABLE_Email;
        sql += " WHERE " + NAME + " LIKE '%" + searchTerm + "%'";
        sql += " ORDER BY " + ID + " DESC";
        sql += " LIMIT 0,5";

        SQLiteDatabase db = this.getWritableDatabase();

        // execute the query
        Cursor cursor = db.rawQuery(sql, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                // int productId = Integer.parseInt(cursor.getString(cursor.getColumnIndex(fieldProductId)));
                @SuppressLint("Range") String objectName = cursor.getString(cursor.getColumnIndex(NAME));
                Email myObject = new Email(objectName);
                recordsList.add(myObject);


            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        // return the list of records
        return recordsList;
    }
}
