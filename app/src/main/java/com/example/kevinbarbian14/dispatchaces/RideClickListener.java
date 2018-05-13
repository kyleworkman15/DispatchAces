package com.example.kevinbarbian14.dispatchaces;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * Created by kevinbarbian on 4/17/18.
 */

public class RideClickListener implements View.OnClickListener {
    private DatabaseReference currentRides;
    private DatabaseReference activeRides;
    private EditText waitTime;
    private boolean updated;
    RideInfo clickedRide;
    Boolean pending;

    public RideClickListener(RideInfo ride,boolean pending,EditText waitTime,boolean updated) {
        this.updated = updated;
        this.clickedRide = ride;
        this.waitTime = waitTime;
        this.pending = pending;
        activeRides = FirebaseDatabase.getInstance().getReference().child("ACTIVE RIDES");
        currentRides = FirebaseDatabase.getInstance().getReference().child("CURRENT RIDES");
    }

    @Override
    public void onClick(View arg0) {
        clickedRide.setEmail(clickedRide.getEmail().replace(".",","));
        if (TextUtils.isDigitsOnly(waitTime.getText()) && !waitTime.getText().toString().equals("")) {
            if (updated == true && pending == false) {
                DatabaseReference activeRidess = FirebaseDatabase.getInstance().getReference().child("ACTIVE RIDES");
                activeRidess.child(clickedRide.getEmail()).child("waitTime").setValue(Integer.parseInt(waitTime.getText().toString()));
            } else if (pending == true) {
                currentRides.child(clickedRide.getEmail()).setValue(null);
                DatabaseReference activeRidess = FirebaseDatabase.getInstance().getReference().child("ACTIVE RIDES");
                activeRidess.child(clickedRide.getEmail()).setValue(clickedRide);
                activeRidess.child(clickedRide.getEmail()).child("waitTime").setValue(Integer.parseInt(waitTime.getText().toString()));
            } else {
                activeRides.child(clickedRide.getEmail().replace(".", ",")).setValue(null);
                DatabaseReference archivedRides = FirebaseDatabase.getInstance().getReference().child("ARCHIVED RIDES");
                //Get the time for when the ride was completed.
                Timestamp ts = new Timestamp(System.currentTimeMillis());
                String time = new SimpleDateFormat("MMM d hh:mm aaa").format(ts);
                clickedRide.setEndTime(time);
                archivedRides.child(clickedRide.getEmail() + "_" + clickedRide.getTime()).setValue(clickedRide);
            }
        }

    }
}