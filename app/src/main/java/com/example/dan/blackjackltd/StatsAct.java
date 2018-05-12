package com.example.dan.blackjackltd;

import android.app.Activity;
import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;

/**
 * Created by DAN on 3/29/18.
 */

public class StatsAct extends Activity {

    DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stats_layout);
        myDb = new DatabaseHelper(this);

        viewAll();
    }

    public void viewAll() {
       Cursor res = myDb.getAllData();

       //if no data available
       if(res.getCount() == 0){
           //show message
           showMessage("Error", "Nothing Found");
           return;
       }
       else{
           StringBuffer buffer = new StringBuffer();

           while( res.moveToNext() ) {
               buffer.append("ID : " + res.getString(0) + "\n");
               buffer.append("TYPE : " + res.getString(1) + "\n");
               buffer.append("RESULTS : " + res.getString(2) + "\n");
               buffer.append("P1 : " + res.getString(3) + "\n");
               buffer.append("P2 : " + res.getString(4) + "\n");
               buffer.append("DEALER : " + res.getString(5) + "\n\n");
           }

           //show all data
           showMessage( "Data", buffer.toString() );
       }
    }

    public void showMessage (String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
}
