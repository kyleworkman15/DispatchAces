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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TextView;

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
    private Button add_btn;
    private EditText email_text;
    private EditText from_text;
    private EditText to_text;
    private EditText num_riders;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = findViewById(R.id.list);
        add_btn = findViewById(R.id.add_btn);
        email_text = findViewById(R.id.email_text);
        from_text = findViewById(R.id.start_text);
        to_text = findViewById(R.id.end_text);
        num_riders = findViewById(R.id.num_riders);
        mDatabase = FirebaseDatabase.getInstance().getReference("USERS");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> temp = new ArrayList<String>();
                for (DataSnapshot shot: dataSnapshot.getChildren()){
                    RideInfo rider = shot.getValue(RideInfo.class);
                    String info = "Email: " + rider.getEmail() + "\n" +
                            "Start: " + rider.getStart() + "\n" +
                            "End: " + rider.getEnd() + "\n" +
                            "# riders: " + rider.getNumRiders() + "\n" +
                            "Time: " + rider.getTime();
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
//                        DatabaseReference archiveRef = FirebaseDatabase.getInstance().getReference().child("RIDES")
                        tempRef.setValue(null);
                        Log.d("MSG",email);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = email_text.getText().toString().replace(".",",");
                String from = from_text.getText().toString();
                String to = to_text.getText().toString();
                String riders = num_riders.getText().toString();
                HashMap<String,String> addInfo = new HashMap<String,String>();
                addInfo.put("Email",email);
                addInfo.put("From",from);
                addInfo.put("To",to);
                addInfo.put("Number of riders",riders);
                mDatabase.child(email).setValue(addInfo);

            }
        });
//        for (int i = 0; i < 10; i++){
//            HashMap<String,String> addInfo = new HashMap<String,String>();
//            addInfo.put("Email","Email" + i);
//            addInfo.put("From","FROM" +i);
//            addInfo.put("To","TO" + i);
//            addInfo.put("Number of riders","RIDERS" + i);
//            mDatabase.child("Email" + i).setValue(addInfo);
//    }

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