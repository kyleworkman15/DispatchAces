package com.example.kevinbarbian14.dispatchaces;

import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by kevinbarbian on 4/17/18.
 */

public class RideClickListener implements View.OnClickListener {
    private DatabaseReference currentRides;
    private DatabaseReference activeRides;
    private EditText waitTime;
    RideInfo clickedRide;
    Boolean pending;

    public RideClickListener(RideInfo ride,boolean pending,EditText waitTime) {
        this.clickedRide = ride;
        this.waitTime = waitTime;
        this.pending = pending;
        activeRides = FirebaseDatabase.getInstance().getReference().child("ACTIVE RIDES");
        currentRides = FirebaseDatabase.getInstance().getReference().child("CURRENT RIDES");
    }

    @Override
    public void onClick(View arg0) {
        Log.d("TEST", "TEST");
        if (pending == true){
            currentRides.child(clickedRide.getEmail()).setValue(null);
            DatabaseReference activeRidess = FirebaseDatabase.getInstance().getReference().child("ACTIVE RIDES");
            activeRidess.child(clickedRide.email).setValue(clickedRide);
            activeRidess.child(clickedRide.email).child("waitTime").setValue(Integer.parseInt(waitTime.getText().toString()));
            //activeRides.child(clickedRide.getEmail()).setValue(clickedRide);
        }
        else {
            activeRides.child(clickedRide.getEmail()).setValue(null);
            DatabaseReference archivedRides = FirebaseDatabase.getInstance().getReference().child("ARCHIVED RIDES");
            archivedRides.child(clickedRide.getEmail() + "_" + clickedRide.getTime()).setValue(clickedRide);
        }

    }
}