package com.example.dan.blackjackltd;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Created by DAN on 3/29/18.
 */

public class CoopMission extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coop_mission);
    }

    public void localButtonClicked(View view){
      startActivity(new Intent(this, LocalMatch.class ));
    }
}
