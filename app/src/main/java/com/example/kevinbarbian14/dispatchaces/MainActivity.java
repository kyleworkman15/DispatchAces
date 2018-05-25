package com.example.kevinbarbian14.dispatchaces;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.ToggleButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
/**
 * Main screen used to maintain ride data for the dispatcher/driver.
 * From here the dispatcher is able to accept and assign wait times to rides; they can
 * also update the wait time on the rides after they have been accepted. This allows the dispatcher
 * to update the wait time in the scenario that they entered the time incorrectly or there were cancelled requests
 * that might shorten the time.
 * Date: 5/13/2018
 * @author  Tyler May, Kevin Barbian, Megan Janssen, Tan Nguyen
 */
public class MainActivity extends Activity {
    private ListView list; //The list being displayed on the screen and storing RideInfo data for pending rides
    private DatabaseReference pendingRides; //A reference to the pending rides in the database
    private DatabaseReference activeRides; //A reference to the active rides in the database
    private DatabaseReference flagStatus; //A reference to the ON/OFF flag in the database
    private ListView activeList; //List being displayed on the screen for active rides
    private ToggleButton status; //Current status of the ON/OFF button at the top of the screen

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = findViewById(R.id.list);
        activeList = findViewById(R.id.active);
        status = (ToggleButton) findViewById(R.id.status);
        pendingRides = FirebaseDatabase.getInstance().getReference().child("PENDING RIDES");
        activeRides = FirebaseDatabase.getInstance().getReference().child("ACTIVE RIDES");
        flagStatus = FirebaseDatabase.getInstance().getReference().child("STATUS");
        //we store the value of checked button in shared preferences so that
        //the app will remember if ACES was turned off or on last time it was used
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        boolean statusFlag = preferences.getBoolean("ref",true);  //default is true
        Log.d("MSG",String.valueOf(statusFlag));
        //communicate the status of the flag with the user
        if (statusFlag == true)
        {
            status.setChecked(true);
            status.setBackgroundColor(Color.GREEN);

        }
        else
        {
            status.setChecked(false);
            status.setBackgroundColor(Color.RED);

        }
        status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences preferences = getPreferences(MODE_PRIVATE);
                //if ACES is on then store the reference as true, set background to green
                if (status.isChecked()){
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("ref", true); // value to store
                    editor.commit();
                    status.setBackgroundColor(Color.GREEN);
                }
                else
                {
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("ref", false); // value to store
                    editor.commit();
                    status.setBackgroundColor(Color.RED);
                }
                }
        });
        status.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            /*
            * Checks to see if the dispatcher turned aces on or off. Send the request to firebase
            * and change the status of the flag.
            */
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    flagStatus.child("FLAG").setValue("ON");
                    // The toggle is enabled
                } else {
                    flagStatus.child("FLAG").setValue("OFF");
                    // The toggle is disabled
                }
            }
        });

       // archivedRides = FirebaseDatabase.getInstance().getReference();
        activeRides.addValueEventListener(new ValueEventListener() {
            @Override
            /*
            * Generate the list of active rides to be displayed for the dispatcher/driver.
            * This updates in realtime and reads in data from the firebase database reference.
            * Template for this display is based on our list_item.xml file.
            */
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<RideInfo> temp = new ArrayList<RideInfo>();
                for (DataSnapshot shot : dataSnapshot.getChildren()) {
                    RideInfo rider = shot.getValue(RideInfo.class);
                    rider.setEmail(rider.getEmail().replace(",","."));
                    temp.add(rider);
                }
                RideAdapter arrayAdapter = new RideAdapter(getBaseContext(), temp, false);
                //arrayAdapter.notifyDataSetChanged();
                activeList.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        pendingRides.addValueEventListener(new ValueEventListener() {
            /*
            * Generate the list of pending rides to be displayed for the dispatcher/driver.
            * This updates in realtime and reads in data from the firebase database reference.
            * Template for this display is based on our list_item.xml file.
            */
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    ArrayList<RideInfo> temp = new ArrayList<RideInfo>();
                    for (DataSnapshot shot : dataSnapshot.getChildren()) {
                        RideInfo rider = shot.getValue(RideInfo.class);
                        rider.setEmail(rider.getEmail().replace(",","."));
                        temp.add(rider);
                    }
                    RideAdapter arrayAdapter = new RideAdapter(getBaseContext(), temp,true);
                    list.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    @Override
    /**
     * Signs out the user if the back is pressed and starts the google sign in activity.
     */
    public void onBackPressed() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(MainActivity.this, Google_SignIn.class));
    }



}