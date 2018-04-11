package com.example.kevinbarbian14.dispatchaces;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.provider.ContactsContract;
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
import java.util.HashMap;

public class MainActivity extends Activity {
    private ListView list;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabase2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = findViewById(R.id.list);
        mDatabase = FirebaseDatabase.getInstance().getReference("USERS");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> temp = new ArrayList<String>();
                for (DataSnapshot shot: dataSnapshot.getChildren()){
                    //temp.add((String)shot.getValue());

                    Log.d("MSG",(String)shot.getKey());
                    HashMap<String,String> h = (HashMap<String,String>) shot.getValue();
                    String info = h.get("Email") + "\n" + h.get("From") + "\n" + h.get("To");

                    temp.add(info);
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,temp);
                arrayAdapter.notifyDataSetChanged();
                list.setAdapter(arrayAdapter);
                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String info = (String)list.getItemAtPosition(position);
                        String email = ((String) list.getItemAtPosition(position)).substring(0,info.indexOf("\n"));
                        DatabaseReference tempRef = FirebaseDatabase.getInstance().getReference().child("USERS").child(email);
                        tempRef.setValue(null);
                        Log.d("MSG",email);
                    }
                });
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