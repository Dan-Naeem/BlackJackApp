package com.example.dan.blackjackltd;

import android.app.Activity;
import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DAN on 3/29/18.
 */

public class StatsAct extends Activity {

    DatabaseHelper myDb;

    ListView statsList;

    ArrayList<String> items;

    ArrayAdapter<String> listAdapter;

    Button reset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stats_layout);
        myDb = new DatabaseHelper(this);

        //setup list view
        statsList = (ListView) findViewById(R.id.listView);

        //initialize list
        items = new ArrayList<String>();

        //create an array adapter for diplaying the list
        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, items);

        //connect adapter to a list view
        statsList.setAdapter(listAdapter);

        //set OnClick for reset button
        reset = (Button) findViewById(R.id.resetButton);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetData();
            }
        });

        //populate list
        getData();
    }

    public void getData() {
        //create a cursor for the db
        Cursor c = myDb.getAllData();

        if (c.getCount() == 0) {
            //show message
            showMessage("Error", "Nothing Found");
            return;
        }

        else {

            //go thru the db
            while ( c.moveToNext() ) {
                //store  data from one game
                String data = "";

                data += "\n";
                data += "ID : " + c.getString(0) + "\n";
                data += "TYPE : " + c.getString(1) + "\n";
                data += "RESULTS : " + c.getString(2) + "\n";
                data += "P1 : " + c.getString(3) + "\n";
                data += "P2 : " + c.getString(4) + "\n";
                data += "DEALER : " + c.getString(5) + "\n";

                //add to the list
                items.add(data);
            }
        }
    }

    public void resetData() {

        myDb.deleteData();

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
