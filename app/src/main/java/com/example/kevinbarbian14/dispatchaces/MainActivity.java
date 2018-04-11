package com.example.kevinbarbian14.dispatchaces;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.renderscript.Sampler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends Activity {
    private ListView list;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = findViewById(R.id.list);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> temp = new ArrayList<String>();
                for (DataSnapshot shot: dataSnapshot.getChildren()){
                    long x = shot.getChildrenCount();
                    //temp.add((String)shot.getValue());

                    Log.d("MSG",(String)shot.getKey() + (String)shot.getValue());
                    temp.add((String)shot.getValue());
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,temp);
                list.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public class CustomClickListener implements AdapterView.OnItemClickListener
    {
        public CustomClickListener() {
        }
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {

            //read your lovely variable
        }

    };

}