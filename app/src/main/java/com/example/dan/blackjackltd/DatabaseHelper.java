package com.example.dan.blackjackltd;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by DAN on 5/11/18.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "statistics.db";
    public static final String TABLE_NAME = "stats_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "TYPE";
    public static final String COL_3 = "RESULTS";
    public static final String COL_4 = "P1";
    public static final String COL_5 = "P2";
    public static final String COL_6 = "DEALER";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (ID INTEGER  PRIMARY KEY AUTOINCREMENT, " +
                "TYPE TEXT, RESULTS TEXT, P1 INTEGER, P2 INTEGER, DEALER INTEGER) ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String type, String results, Integer p1, Integer p2, Integer dealer) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, type);
        contentValues.put(COL_3, results);
        contentValues.put(COL_4, p1);
        contentValues.put(COL_5, p2);
        contentValues.put(COL_6, dealer);
        long results = db.insert(TABLE_NAME, null, contentValues);

        if (results == -1)
            return false;
        else
            return true;
    }
}
