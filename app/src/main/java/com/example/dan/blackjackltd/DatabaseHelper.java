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
    public static final String COL_1 = "game_type";
    public static final String COL_2 = "game_results";
    public static final String COL_3 = "player1_hand";
    public static final String COL_4 = "player2_hand";
    public static final String COL_5 = "dealer_hand";

    public DatabaseHelper(Context context, ) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
