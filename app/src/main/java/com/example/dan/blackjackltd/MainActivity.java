package com.example.dan.blackjackltd;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDb = new DatabaseHelper(this);

    }

    public void soloButtonClicked(View view){
        // Second view when click the button of the main view
        startActivity(new Intent(this, SoloCampaign.class));
    }

    public void coopButtonClicked (View view) {
        //
        startActivity(new Intent(this, CoopMission.class));
    }

    public void statsButtonClicked (View view) {
        startActivity(new Intent (this, StatsAct.class));
    }
}
