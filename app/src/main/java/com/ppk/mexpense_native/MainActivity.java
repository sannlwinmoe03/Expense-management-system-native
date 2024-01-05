package com.ppk.mexpense_native;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    Button addBtn;
    TextView appName, tripDate, Risk, textCount;
    EditText tripName;
    EditText destinaTion;
    EditText descripTion;
    EditText tripTime;
    EditText employeeNumber;
    RadioGroup radioGroup;
    RadioButton radioButton;
    m_expenseDB m_ExpenseDB;
    FloatingActionButton listTrip;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String dateTime;
        Calendar calendar;
        SimpleDateFormat simpleDateFormat;

        addBtn = findViewById(R.id.add_btn);

        Risk = findViewById(R.id.risk);
        textCount = findViewById(R.id.text_count);
        appName = findViewById(R.id.app_name);

        tripName = findViewById(R.id.trip_name);
        destinaTion = findViewById(R.id.destination);
        descripTion = findViewById(R.id.description);
        tripDate = findViewById(R.id.trip_date);
        tripTime = findViewById(R.id.trip_time);
        employeeNumber = findViewById(R.id.employee_number);
        radioGroup = findViewById(R.id.radio_group);
        listTrip = findViewById(R.id.list_trip);


        m_ExpenseDB = new m_expenseDB(this);

        listTrip.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                goToTrip_list();
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                addTrip();
            }
        });

        tripDate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(view);
            }
        });

        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        dateTime = simpleDateFormat.format(calendar.getTime());
        tripTime.setText(dateTime);
    }

    private void addTrip()
    {
        int selectedId = radioGroup.getCheckedRadioButtonId();
        radioButton = (RadioButton) findViewById(selectedId);

        String trip_Name = tripName.getText().toString();
        String Destination = destinaTion.getText().toString();
        String Description = descripTion.getText().toString();
        String trip_Date = tripDate.getText().toString();
        String trip_Time = tripTime.getText().toString();
        String employee_Number = employeeNumber.getText().toString();

        if (trip_Name.isEmpty())
        {
            tripName.setError("Please fill out this field!");
        }
        if (Destination.isEmpty())
        {
            destinaTion.setError("Please fill out this field!");
        }
        if (tripDate.getText().toString().equals("Select The Trip Date"))
        {
            tripDate.setError("Please fill out this field!");
        }
        if (trip_Time.isEmpty())
        {
            tripTime.setError("Please fill out this field!");
        }
        if (employee_Number.isEmpty())
        {
            employeeNumber.setError("Please fill out this field!");
        }
        if(radioGroup.getCheckedRadioButtonId()==-1)
        {
            Risk.setError("Please Choose out a Button !");
        }
        else
        {
            if(!(trip_Name.isEmpty()) && !(Destination.isEmpty()) && !(tripDate.getText().toString().equals("Select The Trip Date"))
                && !(trip_Time.isEmpty()) && !(employee_Number.isEmpty()) && (radioGroup.getCheckedRadioButtonId()!=-1))
            {

                String tripMessage = "\nTrip Name: " + trip_Name + "\nDestination: " + Destination +
                        "\nDate of Trip: " + trip_Date +"\nDescription: " + Description +
                        "\nDeparture Time: " + trip_Time + "\nEmployee Count: " + employee_Number +
                        "\nRisk Assessment: " + radioButton.getText();
                new AlertDialog.Builder(this)
                        .setTitle("Trip Information")
                        .setMessage(tripMessage)
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {
                                String tName = tripName.getText().toString();
                                String tDestination = destinaTion.getText().toString();
                                String tDescription = descripTion.getText().toString();
                                String tDate = tripDate.getText().toString();
                                String tTime = tripTime.getText().toString();
                                String eNum = employeeNumber.getText().toString();
                                String risk = radioButton.getText().toString();

                                Trip trip = new Trip(tName,tDestination,tDescription,tDate,tTime,eNum,risk);

                                long iID = m_ExpenseDB.submitTrip(trip);

                                Toast.makeText(MainActivity.this,"Added 1 Trip Information"+iID, Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //go back to input form
                            }
                        })
                        .show();
            }
        }
    }

    private void showDatePickerDialog(View view)
    {
        DialogFragment myCalendarFragment = new DatePickerFragment();
        myCalendarFragment.show(getSupportFragmentManager(),"tripDate");
    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener
    {
        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

            Calendar c =Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker tripDate, int y, int m, int d)
        {
            m++;
            String dateText = d+"/"+m+"/"+y;
            ((MainActivity)getActivity()).tripDate.setText(dateText);
        }

    }
    public void goToTrip_list()
    {
        Intent intent = new Intent(this,trip_list.class);
        startActivity(intent);
    }



}