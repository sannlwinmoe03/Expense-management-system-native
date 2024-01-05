package com.ppk.mexpense_native;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class trip_list extends AppCompatActivity
{
    RecyclerView tripList;
    m_expenseDB m_ExpenseDB;
    Adapter tAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_list);

        tripList = findViewById(R.id.trip_list_show);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        tripList.setLayoutManager(linearLayoutManager);
        tripList.setHasFixedSize(true);

        m_ExpenseDB = new m_expenseDB(this);

        ArrayList<Trip> allTrip = m_ExpenseDB.showTrip();
        if (allTrip.size() > 0) {
            tripList.setVisibility(View.VISIBLE);
            tAdapter = new Adapter(this, allTrip);
            tripList.setAdapter(tAdapter);
        }
        else {
            tripList.setVisibility(View.GONE);
            Toast.makeText(this, "There is no Trip in the database. You Have to Add Trip !", Toast.LENGTH_LONG).show();
        }

    }

    public void toUpload(){
        Intent intent = new Intent(this, json_upload.class);
        startActivity(intent);
    }

    public void toMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.backMain:
                toMainActivity();
                return true;
            case R.id.delete_all:
                new AlertDialog.Builder(this)
                        .setTitle("Are You Sure Want To Delete All Data ?")
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {
                                m_ExpenseDB.deleteAllTrip();
                                Toast.makeText(trip_list.this,"All Data Have Been Deleted", Toast.LENGTH_SHORT).show();
                                Intent go= new Intent(trip_list.this, trip_list.class);
                                startActivity(go);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {

                            }
                        })
                        .show();
                return true;
            case R.id.upload:
                toUpload();
                return true;

            default:return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);

        MenuItem menuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String stSearch = newText;
                tAdapter.getFilter().filter(stSearch);
                return false;
            }
        });
        return true;
    }
}