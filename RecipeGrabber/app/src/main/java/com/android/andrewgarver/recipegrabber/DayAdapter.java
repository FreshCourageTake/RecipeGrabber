package com.android.andrewgarver.recipegrabber;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

/**
 * Created by Reed Atwood on 11/14/2015.
 */
public class DayAdapter extends RecyclerView.Adapter<DayAdapter.MyViewHolder> {

    // to interact with each day
    private LayoutInflater inflater;

    // ensure that there is no null pointers
    List<CalDay> day = Collections.emptyList();

    public DayAdapter(Context context, List<CalDay> day) {
        //
        inflater =LayoutInflater.from(context);
        this.day = day;
    }


    /*
     * Holds the position of each of the objects in the view
     */
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.day_layout, parent, false);

        // attach the items to the view
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    /*
     * Add the items to the view
     */
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        CalDay current = day.get(position);
        holder.textView.setText(current.day_of_month);
        //holder.imageView.setImageResource(current.monthId);
    }

    @Override
    public int getItemCount() {
        return day.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        ImageView imageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.dayTV);
            imageView = (ImageView) itemView.findViewById(R.id.iconIV);

        }
    }
}
