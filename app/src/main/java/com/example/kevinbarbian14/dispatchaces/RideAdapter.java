package com.example.kevinbarbian14.dispatchaces;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

/**
 * Reference used: https://stackoverflow.com/questions/8166497/custom-adapter-for-list-view
 * Custom Ride Adapter class used for holding our RideInfo objects--This allows us
 * to create a custom list item for the dispatcher/driver to view and interact with.
 * Date: 5/13/2018
 * @author  Tyler May, Kevin Barbian, Megan Janssen, Tan Nguyen
 */

public class RideAdapter extends ArrayAdapter<RideInfo> {
    private Context mContext;
    private List<RideInfo> rideList = new ArrayList<>(); //List to hold the RideInfo objects
    private boolean pendingFlag;
    LayoutInflater inflater;

    public RideAdapter(@NonNull Context context, ArrayList<RideInfo> list, boolean flag) {
        super(context, 0, list);
        inflater = LayoutInflater.from(context);
        this.pendingFlag = flag;
        mContext = context;
        rideList = list;
    }
    /**
     * Custom viewholder class containing all of the objects we want displayed in the list item
     * Date: 5/13/2018
     * @author  Tyler May, Kevin Barbian, Megan Janssen, Tan Nguyen
     */
    public class ViewHolder
    {
        TextView email; //The current users email address
        Button button; //send/clear button depending on scenario
        Button update; //update button
        TextView from; //the users start location
        TextView to;   //the users end location
        TextView riders; //number of riders
        TextView time; //current time of the request
        EditText waitTime; //wait time for the pick up
    }
    /**
     * Generates the view with the given information
     * @return the view being returned
     * @param position the position of the view
     * @param convertView the reused view
     * @param parent used to inflate the view on the display
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        //if no view, generate new one
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
        holder.waitTime.setText(String.valueOf(currentRide.getWaitTime()));
        RideClickListener deleteRide = new RideClickListener(currentRide,pendingFlag,holder.waitTime,false);
        RideClickListener updateRide = new RideClickListener(currentRide,pendingFlag,holder.waitTime,true);
        holder.button.setOnClickListener(deleteRide);
        holder.update.setOnClickListener(updateRide);
        //if the ride is pending set the text of the button to "Send" and hide the "Update" button
        if (deleteRide.pending==true) {
            holder.button.setText("Send");
            holder.update.setVisibility(View.GONE);
        }
        //If ride is active, the update button should be visible and the main button should read "Complete"
        else {
            holder.button.setText("Complete");
        }
        holder.email.setText(currentRide.getEmail());
        holder.from.setText(currentRide.getStart());
        holder.to.setText(currentRide.getEnd());
        holder.riders.setText(String.valueOf(currentRide.getNumRiders()));
        holder.time.setText(currentRide.getTime());
        return convertView;

    }
}
