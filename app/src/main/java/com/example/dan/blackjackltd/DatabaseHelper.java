package com.example.dan.blackjackltd;

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

    public DatabaseHelper(Context context, ) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
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
}
