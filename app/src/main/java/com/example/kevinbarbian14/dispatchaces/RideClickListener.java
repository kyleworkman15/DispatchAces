package com.example.kevinbarbian14.dispatchaces;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

/**
 * Custom ride click listener that determines whether the ride should be sent to active, archived, or just updated.
 * This class is used to handle the different scenarios that can occur while the dispatcher/driver are handling rides.
 * Date: 5/13/2018
 * @author  Tyler May, Kevin Barbian, Megan Janssen, Tan Nguyen
 */

public class RideClickListener implements View.OnClickListener {
    private DatabaseReference pendingRides; //reference to the pending rides
    private DatabaseReference activeRides; //reference to the active rides
    private DatabaseReference archivedRides; //reference to the archived rides
    private EditText waitTimeField; //editText that is being modified
    private boolean updated; //flag for whether or not we want to update the wait time
    RideInfo clickedRide; //the ride object being selected
    Boolean pending; //whether or not the ride is pending or active

    public RideClickListener(RideInfo ride,boolean pending,EditText waitTimeField,boolean updated) {
        this.updated = updated;
        this.clickedRide = ride;
        this.waitTimeField = waitTimeField;
        this.pending = pending;
        activeRides = FirebaseDatabase.getInstance().getReference().child("ACTIVE RIDES");
        pendingRides = FirebaseDatabase.getInstance().getReference().child("CURRENT RIDES");
        archivedRides = FirebaseDatabase.getInstance().getReference().child("ARCHIVED RIDES");
    }

    @Override
    public void onClick(View arg0) {
        //replace the "." with "," as we use email as a key value in firebase and those cannot contain "."'s
        clickedRide.setEmail(clickedRide.getEmail().replace(".",","));
        //Check to see if the dispatcher/driver entered a valid integer
        if (TextUtils.isDigitsOnly(waitTimeField.getText()) && !waitTimeField.getText().toString().equals("")) {
            //updating an active ride
            int waitTime = Integer.parseInt(waitTimeField.getText().toString());
            Timestamp ts = new Timestamp(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(waitTime));
            String eta = new SimpleDateFormat("hh:mm aaa").format(ts);
            if (updated == true && pending == false) {
                activeRides.child(clickedRide.getEmail()).child("waitTime").setValue(waitTime);
                clickedRide.setWaitTime(waitTime);
                activeRides.child(clickedRide.getEmail()).child("eta").setValue(eta);
                clickedRide.setETA(eta);
            }
            //sending a pending ride to active
            else if (pending == true) {
                pendingRides.child(clickedRide.getEmail()).setValue(null);
                activeRides.child(clickedRide.getEmail()).setValue(clickedRide);
                activeRides.child(clickedRide.getEmail()).child("waitTime").setValue(waitTime);
                clickedRide.setWaitTime(waitTime);
                activeRides.child(clickedRide.getEmail()).child("eta").setValue(eta);
                clickedRide.setETA(eta);
            }
            //sending an active ride to archived
            else {
                activeRides.child(clickedRide.getEmail()).setValue(null);
                //Get the time for when the ride was completed.
                String time = new SimpleDateFormat("MMM d hh:mm aaa").format(new Timestamp(System.currentTimeMillis()));
                clickedRide.setEndTime(time);
                archivedRides.child(clickedRide.getEmail() + "_" + clickedRide.getTime()).setValue(clickedRide);
            }
        }
    }
}