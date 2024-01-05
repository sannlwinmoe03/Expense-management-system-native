package com.ppk.mexpense_native;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class addExpense extends AppCompatActivity {

    TextView  tripName;
    EditText eName, eAmount,eDate, eCom;
    Button btnExpense;

    Trip trip;

    private m_expenseDB DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        String Time;
        Calendar calendar;
        SimpleDateFormat simpleDateFormat;

        DB = new m_expenseDB(this);

        tripName = findViewById(R.id.trip_name);
        eName = findViewById(R.id.e_name);
        eAmount = findViewById(R.id.e_amount);
        eDate = findViewById(R.id.e_date);
        eCom = findViewById(R.id.e_com);
        btnExpense = findViewById(R.id.btn_expense);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle!=null)
            trip = (Trip) bundle.getSerializable("trip");

        tripName.setText(trip.gettName());

        btnExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addexpense();
            }
        });

        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy");
        Time = simpleDateFormat.format(calendar.getTime());
        eDate.setText(Time);
    }
    private void addexpense() {

        String e_name = eName.getText().toString();
        String e_amount = eAmount.getText().toString();
        String e_date = eDate.getText().toString();
        String e_com = eCom.getText().toString();
        int tID = trip.gettID();

        expense expense=new expense(e_name, e_amount, e_date, e_com, tID);

        if (e_name.isEmpty())
        {
            eName.setError("Please fill out this field!");
        }
        if (e_amount.isEmpty())
        {
            eAmount.setError("Please fill out this field!");
        }
        if (e_date.isEmpty())
        {
            eDate.setError("Please fill out this field!");
        }
        else
        {
            if(!(e_name.isEmpty()) && !(e_amount.isEmpty()) && !(e_date.isEmpty()))
            {
                DB.add_Expense(expense);

                Intent intent = new Intent(this, trip_list.class);
                startActivity(intent);
                finish();
            }
        }
    }
    private void showDatePickerDialog(View view)
    {
        DialogFragment myCalendarFragment = new MainActivity.DatePickerFragment();
        myCalendarFragment.show(getSupportFragmentManager(),"expenseDate");
    }
}