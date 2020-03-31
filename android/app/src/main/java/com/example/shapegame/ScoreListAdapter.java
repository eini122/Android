/******************************************************************************
 * This is the class create a adapter read the list and store line by line to
 * the view
 *
 * @Kaitian LI
 * 3/30/2020
 * kxl180016
 ******************************************************************************/
package com.example.shapegame;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import java.util.ArrayList;

public class ScoreListAdapter extends ArrayAdapter<DTO> {
    private static final String TAG = "ScoreListAdapter";
    private Context mContext;
    int mResource;
    //constructor that read the input context and resource
    public ScoreListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<DTO> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
    }
    //set view by get the current position string and store line by line
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //get data form each position
        String order = position+1+"";
        String name = getItem(position).getName();
        String score = getItem(position).getScore();
        String date = getItem(position).getDate();

        DTO dto = new DTO(name, score, date);//create dto object

        LayoutInflater inflater = LayoutInflater.from(mContext);//set layout
        convertView = inflater.inflate(mResource, parent, false);
        //set textview for listview
        TextView tvName = convertView.findViewById(R.id.name);
        TextView tvScore = convertView.findViewById(R.id.score);
        TextView tvDate = convertView.findViewById(R.id.date);
        TextView tvOrder = convertView.findViewById(R.id.order);
        if(position == 0){
            tvOrder.setText("Order");
            tvName.setText("Name");
            tvScore.setText("Time");
            tvDate.setText("Date");
        }
        //set text
        tvName.setText(name);
        tvScore.setText(score);
        tvDate.setText(date);
        tvOrder.setText(order);

        return convertView;
    }
}
