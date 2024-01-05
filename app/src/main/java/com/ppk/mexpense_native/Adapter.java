package com.ppk.mexpense_native;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;


public class Adapter extends RecyclerView.Adapter<viewHolder> implements Filterable
{
    private Context context;
    private ArrayList<Trip> showTrip;
    private ArrayList<Trip> Search;
    private m_expenseDB m_ExpenseDB;


    public Adapter(Context context, ArrayList<Trip> showTrip) {
        this.context = context;
        this.showTrip = showTrip;
        this.Search = showTrip;
        m_ExpenseDB = new m_expenseDB(context);
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trip_list_layout, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        final Trip trip = showTrip.get(position);
        holder.TripName.setText(trip.gettName());
        holder.dest.setText(trip.getDestination());
        holder.date.setText(trip.gettDate());
        holder.rik.setText(trip.getRisk());
        holder.desc.setText(trip.getDescription());
        holder.clock.setText(trip.gettTime());
        holder.ec.setText(trip.geteNum());

        holder.updateTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                editTaskDialog(trip);
            }
        });

        holder.deleteATrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Are you sure want to delete this data?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                m_ExpenseDB.deleteATrip(trip.gettID());
                                ((Activity) context).finish();
                                context.startActivity(((Activity) context).getIntent());
                            }
                        })
                        .setNegativeButton("No",null)
                        .show();
            }
        });
        holder.show_Expense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showExpense(trip);
            }
        });
        holder.add_Expense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_Expense(trip);
            }
        });

    }
    private void showExpense(Trip trip) {

        int eID = trip.gettID();
        String name = trip.gettName();

        ArrayList<expense> expenseList = m_ExpenseDB.showExpense(eID);
        StringBuilder sb = new StringBuilder();

        if(expenseList.size()==0)sb.append("No expense history");

        for(int i=0;i<expenseList.size();i++)
        {
            expense expense = expenseList.get(i);
            sb.append(expense.geteName()+"\n");
            sb.append(expense.geteAmount()+ " $" +"\n");
            sb.append(expense.geteDate()+"\n");
            sb.append(expense.geteCom()+"\n");
            sb.append("\n\n");
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Expense List of "+name)
                .setMessage(sb.toString())
                .setPositiveButton("Delete All", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        m_ExpenseDB.deleteExpense(trip.gettID());
                        ((Activity) context).finish();
                        context.startActivity(((Activity) context).getIntent());
                    }
                })
                .setNegativeButton("Go Back",null)
                .show();
    }

    private void add_Expense(Trip trip) {
        ((Activity) context).finish();
        Intent intent = new Intent((Activity)context, addExpense.class);
        intent.putExtra("trip",trip);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return showTrip.size();
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if(constraint == null || constraint.length() == 0){
                    filterResults.values = Search;
                    filterResults.count = Search.size();
                }
            else{
                    String stSearch = constraint.toString().toLowerCase();
                    ArrayList<Trip> tripArrayList = new ArrayList<>();
                    for(Trip trip: Search){
                        if((trip.gettName().toLowerCase().contains(stSearch) ||  trip.getDestination().toLowerCase().contains(stSearch) ||  trip.gettDate().toLowerCase().contains(stSearch)))
                        {
                            tripArrayList.add(trip);
                        }
                    }

                    filterResults.values = tripArrayList;
                    filterResults.count = tripArrayList.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                showTrip = (ArrayList<Trip>) results.values;
                notifyDataSetChanged();
            }
        };
        return filter;
    }

    private void editTaskDialog(final Trip trip) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View subView = inflater.inflate(R.layout.update, null);
        final EditText tripName = subView.findViewById(R.id.trip_namee);
        final EditText destinaTion = subView.findViewById(R.id.destinatione);
        final EditText tripDate = subView.findViewById(R.id.trip_datee);
        final EditText descripTion = subView.findViewById(R.id.descriptione);
        final EditText tripTime = subView.findViewById(R.id.trip_timee);
        final EditText employeeNumber = subView.findViewById(R.id.employee_numbere);

        String Date;
        Calendar calendar;
        SimpleDateFormat simpleDateFormat;

        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy");
        Date = simpleDateFormat.format(calendar.getTime());
        tripDate.setText(Date);

        if (trip != null) {
            tripName.setText(trip.gettName());
            destinaTion.setText(trip.getDestination());
            tripDate.setText(trip.gettDate());
            descripTion.setText(trip.getDescription());
            tripTime.setText(trip.gettTime());
            employeeNumber.setText(trip.geteNum());

        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("EDIT TRIP");
        builder.setView(subView);
        builder.create();
        builder.setPositiveButton("Edit Trip", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String name = tripName.getText().toString();
                final String dest = destinaTion.getText().toString();
                final String date = tripDate.getText().toString();
                final String desc = descripTion.getText().toString();
                final String clock = tripTime.getText().toString();
                final String ec = employeeNumber.getText().toString();

                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(dest) || TextUtils.isEmpty(date) || TextUtils.isEmpty(desc) || TextUtils.isEmpty(clock) || TextUtils.isEmpty(ec)) {
                    Toast.makeText(context, "Text Fields Must Not Be Empty", Toast.LENGTH_LONG).show();
                } else {
                    m_ExpenseDB.updateTrip(new
                            Trip(Objects.requireNonNull(trip).gettID(), name, dest, date, desc, clock, ec));
                    ((Activity) context).finish();
                    context.startActivity(((Activity)
                            context).getIntent());
                }
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(context, "Nothing Change",Toast.LENGTH_LONG).show();
            }
        });
        builder.show();
    }


}
