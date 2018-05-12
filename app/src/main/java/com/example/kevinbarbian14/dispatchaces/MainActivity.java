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

public class MainActivity extends Activity {
    private ListView list;
    private DatabaseReference currentRides;
    private DatabaseReference activeRides;
    private DatabaseReference flagStatus;
    private ListView activeList;
    private ToggleButton status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = findViewById(R.id.list);
        activeList = findViewById(R.id.active);
        status = (ToggleButton) findViewById(R.id.status);
        currentRides = FirebaseDatabase.getInstance().getReference().child("CURRENT RIDES");
        activeRides = FirebaseDatabase.getInstance().getReference().child("ACTIVE RIDES");
        flagStatus = FirebaseDatabase.getInstance().getReference().child("STATUS");
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        boolean statusFlag = preferences.getBoolean("ref",true);  //default is true
        Log.d("MSG",String.valueOf(statusFlag));
        if (statusFlag == true)
        {
            status.setChecked(true);
            status.setBackgroundColor(Color.GREEN);

        }
        else
        {
            Log.d("MSG","SUCCESS");
            status.setChecked(false);
            status.setBackgroundColor(Color.RED);

        }
        status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences preferences = getPreferences(MODE_PRIVATE);
                if (status.isChecked()){
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("ref", true); // value to store
                    editor.commit();
                    status.setBackgroundColor(Color.GREEN);
                }
                else
                {
                    Log.d("MSG",String.valueOf(status.isChecked()));
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("ref", false); // value to store
                    editor.commit();
                    status.setBackgroundColor(Color.RED);
                }
                }
        });
        status.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<RideInfo> temp = new ArrayList<RideInfo>();
                for (DataSnapshot shot : dataSnapshot.getChildren()) {
                    RideInfo rider = shot.getValue(RideInfo.class);
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
        currentRides.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    ArrayList<RideInfo> temp = new ArrayList<RideInfo>();
                    for (DataSnapshot shot : dataSnapshot.getChildren()) {
                        RideInfo rider = shot.getValue(RideInfo.class);
                        temp.add(rider);
                    }
                    RideAdapter arrayAdapter = new RideAdapter(getBaseContext(), temp,true);

                    //arrayAdapter.notifyDataSetChanged();
                    list.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void deleteUser(RideInfo rider){
        currentRides.child(rider.getEmail()).setValue(null);
        DatabaseReference archivedRides = FirebaseDatabase.getInstance().getReference().child("ARCHIVED RIDES");
        archivedRides.child(rider.getEmail()).setValue(rider);
    }
    private void clearArchives() {
        DatabaseReference archivedRides = FirebaseDatabase.getInstance().getReference().child("ARCHIVED RIDES");
        archivedRides.setValue(null);
    }

    @Override
    /**
     * Signs out the user if the back is pressed
     */
    public void onBackPressed() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(MainActivity.this, Google_SignIn.class));
    }



}