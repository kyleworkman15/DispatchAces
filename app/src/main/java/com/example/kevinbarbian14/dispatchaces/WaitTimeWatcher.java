package com.example.kevinbarbian14.dispatchaces;

import android.graphics.DashPathEffect;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by kevinbarbian on 4/17/18.
 */

public class WaitTimeWatcher implements TextWatcher {
    private DatabaseReference currentRides;
    private boolean edit;
    private RideInfo clickedRide;
    public WaitTimeWatcher(RideInfo ride) {
        clickedRide = ride;
        currentRides = FirebaseDatabase.getInstance().getReference().child("CURRENT RIDES");
        //currentRides = FirebaseDatabase.getInstance().getReference().child("ACTIVE RIDES");
    }

    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

    public void afterTextChanged(Editable editable) {
        String text = editable.toString();
        if (!text.equals("") && text.length()<3) {
            try{
                currentRides.child(clickedRide.getEmail()).child("waitTime").setValue(Integer.parseInt(text)); }
            catch (NumberFormatException exception){
                exception.printStackTrace();
            }
        }

    }
}