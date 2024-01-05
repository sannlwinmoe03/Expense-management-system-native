package com.ppk.mexpense_native;

import android.view.View;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class viewHolder extends RecyclerView.ViewHolder
{
    TextView TripName,dest,date,rik,desc,clock,ec;
    ImageView deleteATrip,add_Expense,show_Expense,updateTrip;

    public viewHolder(View view) {
        super(view);
        TripName=view.findViewById(R.id.tripName);
        dest=view.findViewById(R.id.dest);
        date=view.findViewById(R.id.date);
        rik=view.findViewById(R.id.rik);
        desc=view.findViewById(R.id.desc);
        clock=view.findViewById(R.id.clock);
        ec=view.findViewById(R.id.ec);

        updateTrip=view.findViewById(R.id.editTrip);
        deleteATrip=view.findViewById(R.id.deleteTrip);
        add_Expense=view.findViewById(R.id.addExpense);
        show_Expense=view.findViewById(R.id.showExpense);
    }
}
