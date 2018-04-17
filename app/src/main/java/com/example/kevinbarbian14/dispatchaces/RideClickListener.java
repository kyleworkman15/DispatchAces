package com.example.kevinbarbian14.dispatchaces;

import android.util.Log;
import android.view.View;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by kevinbarbian on 4/17/18.
 */

public class RideClickListener implements View.OnClickListener {
    private DatabaseReference currentRides;
    RideInfo clickedRide;

    public RideClickListener(RideInfo ride) {
        this.clickedRide = ride;
        currentRides = FirebaseDatabase.getInstance().getReference().child("CURRENT RIDES");
    }

    @Override
    public void onClick(View arg0) {
        Log.d("TEST", "TEST");

        currentRides.child(clickedRide.getEmail()).setValue(null);
        DatabaseReference archivedRides = FirebaseDatabase.getInstance().getReference().child("ARCHIVED RIDES");
        archivedRides.child(clickedRide.getEmail()).setValue(clickedRide);

    }
}