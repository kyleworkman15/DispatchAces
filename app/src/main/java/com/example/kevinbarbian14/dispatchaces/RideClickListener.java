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
 * Custom ride click listener that determines whether the ride should be sent to active, archived, or just updated.
 * This class is used to handle the different scenarios that can occur while the dispatcher/driver are handling rides.
 * Date: 5/13/2018
 * @author  Tyler May, Kevin Barbian, Megan Janssen, Tan Nguyen
 */

public class RideClickListener implements View.OnClickListener {
    private DatabaseReference pendingRides; //reference to the pending rides
    private DatabaseReference activeRides; //reference to the active rides
    private EditText waitTime; //editText that is being modified
    private boolean updated; //flag for whether or not we want to update the wait time
    RideInfo clickedRide; //the ride object being selected
    Boolean pending; //whether or not the ride is pending or active

    public RideClickListener(RideInfo ride,boolean pending,EditText waitTime,boolean updated) {
        this.updated = updated;
        this.clickedRide = ride;
        this.waitTime = waitTime;
        this.pending = pending;
        activeRides = FirebaseDatabase.getInstance().getReference().child("ACTIVE RIDES");
        pendingRides = FirebaseDatabase.getInstance().getReference().child("CURRENT RIDES");
    }

    @Override
    public void onClick(View arg0) {
        //replace the "." with "," as we use email as a key value in firebase and those cannot contain "."'s
        clickedRide.setEmail(clickedRide.getEmail().replace(".",","));
        //Check to see if the dispatcher/driver entered a valid integer
        if (TextUtils.isDigitsOnly(waitTime.getText()) && !waitTime.getText().toString().equals("")) {
            //updating an active ride
            if (updated == true && pending == false) {
                DatabaseReference activeRidess = FirebaseDatabase.getInstance().getReference().child("ACTIVE RIDES");
                activeRidess.child(clickedRide.getEmail()).child("waitTime").setValue(Integer.parseInt(waitTime.getText().toString()));

            }
            //sending a pending ride to active
            else if (pending == true) {
                pendingRides.child(clickedRide.getEmail()).setValue(null);
                DatabaseReference activeRidesRef = FirebaseDatabase.getInstance().getReference().child("ACTIVE RIDES");
                activeRidesRef.child(clickedRide.getEmail()).setValue(clickedRide);
                activeRidesRef.child(clickedRide.getEmail()).child("waitTime").setValue(Integer.parseInt(waitTime.getText().toString()));
            }
            //sending an active ride to archived
            else {
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