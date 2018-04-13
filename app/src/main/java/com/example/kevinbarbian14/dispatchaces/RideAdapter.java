package com.example.kevinbarbian14.dispatchaces;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kevinbarbian on 4/13/18.
 */

public class RideAdapter extends ArrayAdapter<RideInfo> {
    private Context mContext;
    private List<RideInfo> rideList = new ArrayList<>();

    public RideAdapter(@NonNull Context context, ArrayList<RideInfo> list) {
        super(context, 0, list);
        mContext = context;
        rideList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false);
        RideInfo currentRide = rideList.get(position);

        TextView email = (TextView) listItem.findViewById(R.id.email_l);
        email.setText(currentRide.getEmail());

        TextView from = (TextView) listItem.findViewById(R.id.start_l);
        from.setText(currentRide.getStart());

        TextView to = (TextView) listItem.findViewById(R.id.end_l);
        to.setText(currentRide.getEnd());

        TextView numRiders = listItem.findViewById(R.id.riders_l);
        numRiders.setText(String.valueOf(currentRide.getNumRiders()));

        TextView time = (TextView) listItem.findViewById(R.id.time_l);
        time.setText(currentRide.getTime());

        return listItem;

    }
}
