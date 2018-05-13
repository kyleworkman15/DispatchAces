package com.example.kevinbarbian14.dispatchaces;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kevinbarbian on 4/13/18.
 */

public class RideAdapter extends ArrayAdapter<RideInfo> {
    private Context mContext;
    private List<RideInfo> rideList = new ArrayList<>();
    private DatabaseReference database;
    private boolean pendingFlag;
    LayoutInflater inflater;

    public RideAdapter(@NonNull Context context, ArrayList<RideInfo> list, boolean flag) {
        super(context, 0, list);
        inflater = LayoutInflater.from(context);
        database = FirebaseDatabase.getInstance().getReference().child("ACTIVE RIDES");
        this.pendingFlag = flag;
        mContext = context;
        rideList = list;
    }

    public class ViewHolder
    {
        TextView email;
        Button button;
        Button update;
        TextView from;
        TextView to;
        TextView riders;
        TextView time;
        EditText waitTime;
    }

    @Override

    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.list_item, null);
            holder.button = (Button) convertView.findViewById(R.id.button2);
            holder.update = (Button) convertView.findViewById(R.id.update);
            holder.email = (TextView) convertView.findViewById(R.id.email_l);
            holder.from = (TextView) convertView.findViewById(R.id.start_l);
            holder.to = (TextView) convertView.findViewById(R.id.end_l);
            holder.riders = (TextView) convertView.findViewById(R.id.riders_l);
            holder.time = (TextView) convertView.findViewById(R.id.time_l);
            holder.waitTime = (EditText) convertView.findViewById(R.id.editText);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final RideInfo currentRide = rideList.get(position);
        WaitTimeWatcher waitTimeWatcher = new WaitTimeWatcher(currentRide);
        holder.waitTime.setText(String.valueOf(currentRide.getWaitTime()));
        RideClickListener deleteRide = new RideClickListener(currentRide,pendingFlag,holder.waitTime,false);
        RideClickListener updateRide = new RideClickListener(currentRide,pendingFlag,holder.waitTime,true);
        holder.button.setOnClickListener(deleteRide);
        holder.update.setOnClickListener(updateRide);
        if (deleteRide.pending==true) {
            holder.button.setText("SEND");
            holder.update.setVisibility(View.GONE);
        }
        else {
            holder.button.setText("CLEAR");
        }
        holder.email.setText(currentRide.getEmail());
        holder.from.setText(currentRide.getStart());
        holder.to.setText(currentRide.getEnd());
        holder.riders.setText(String.valueOf(currentRide.getNumRiders()));
        holder.time.setText(currentRide.getTime());
        return convertView;

    }
}
