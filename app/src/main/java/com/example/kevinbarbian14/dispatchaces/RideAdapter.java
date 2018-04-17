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
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kevinbarbian on 4/13/18.
 */

public class RideAdapter extends ArrayAdapter<RideInfo> {
    private Context mContext;
    private List<RideInfo> rideList = new ArrayList<>();
    private DatabaseReference database;
    LayoutInflater inflater;

    public RideAdapter(@NonNull Context context, ArrayList<RideInfo> list) {
        super(context, 0, list);
        inflater = LayoutInflater.from(context);
        mContext = context;
        rideList = list;
    }

    private class ViewHolder
    {
        TextView email;
        TextView from;
        TextView to;
        TextView riders;
        TextView time;
        EditText waitTime;
    }
    @Override

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.list_item, null);
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
        RideInfo currentRide = rideList.get(position);
        RideClickListener deleteRide = new RideClickListener(currentRide);
        WaitTimeWatcher waitTimeWatcher = new WaitTimeWatcher(currentRide);
        holder.email.setOnClickListener(deleteRide);
        holder.email.setText(currentRide.getEmail());
        holder.from.setOnClickListener(deleteRide);
        holder.from.setText(currentRide.getStart());
        holder.to.setOnClickListener(deleteRide);
        holder.to.setText(currentRide.getEnd());
        holder.riders.setOnClickListener(deleteRide);
        holder.riders.setText(String.valueOf(currentRide.getNumRiders()));
        holder.time.setOnClickListener(deleteRide);
        holder.time.setText(currentRide.getTime());
        holder.waitTime.setText(String.valueOf(currentRide.getWaitTime()));
        holder.waitTime.addTextChangedListener(waitTimeWatcher);
        return convertView;

    }
}
