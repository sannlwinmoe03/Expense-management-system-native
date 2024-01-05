package com.ppk.mexpense_native;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class m_expenseDB extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "trip_db";
    private static final String TABLE_NAME = "trip_table";

    private static final String TRIP_ID = "tID";
    private static final String TRIP_NAME = "tName";
    private static final String DESTINATION = "destination";
    private static final String DESCRIPTION = "description";
    private static final String TRIP_DATE = "tDate";
    private static final String TRIP_TIME = "tTime";
    private static final String EMPLOYEE_NUM = "eNum";
    private static final String RISK = "risk";

    private static final String EXPENSE_TABLE = "expense";
    private static final String EXPENSE_NAME = "eName";
    private static final String EXPENSE_AMOUNT = "eAmount";
    private static final String EXPENSE_DATE = "eDate";
    private static final String EXPENSE_COM = "eCom";
    private static final String EXPENSE_ID = "eID";

    public m_expenseDB(@Nullable Context context)
    {
        super(context, DATABASE_NAME, null , DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase tDB)
    {
        String createTable="create table "+TABLE_NAME+" ("+TRIP_ID+" integer primary key autoincrement," +
                " "+TRIP_NAME+" text, "+DESTINATION+" text, "+DESCRIPTION+" text," +
                " "+TRIP_DATE+" text, "+TRIP_TIME+" text, "+EMPLOYEE_NUM+" text, "+RISK+" text)";
        tDB.execSQL(createTable);

        String createExpense="create table "+EXPENSE_TABLE+" ("+TRIP_ID+" integer primary key autoincrement," +
                " "+EXPENSE_NAME+" text, "+EXPENSE_AMOUNT+" text, "+EXPENSE_DATE+" text," +
                " "+EXPENSE_COM+" text, "+EXPENSE_ID+" integer," + " FOREIGN KEY ("+EXPENSE_ID+")"
                + " REFERENCES "+TABLE_NAME+"("+TRIP_ID+")"
                + " ON DELETE CASCADE"
                + ")";
        tDB.execSQL(createExpense);
    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int i, int i1)
    {
        DB.execSQL("drop table if exists "+TABLE_NAME);
        DB.execSQL("drop table if exists "+EXPENSE_TABLE);
        onCreate(DB);
    }
    @Override
    public void onOpen(SQLiteDatabase DB) {
        super.onOpen(DB);
        DB.execSQL("PRAGMA foreign_keys = ON;");
    }

    public long submitTrip(Trip trip)
    {
        SQLiteDatabase tDB = getWritableDatabase();
        ContentValues tripValues = new ContentValues();

        tripValues.put(TRIP_NAME,trip.gettName());
        tripValues.put(DESTINATION, trip.getDestination());
        tripValues.put(DESCRIPTION, trip.getDescription());
        tripValues.put(TRIP_DATE, trip.gettDate());
        tripValues.put(TRIP_TIME, trip.gettTime());
        tripValues.put(EMPLOYEE_NUM, trip.geteNum());
        tripValues.put(RISK, trip.getRisk());

        return tDB.insert(TABLE_NAME, null, tripValues);
    }
    ArrayList<Trip> showTrip() {
        String sql = "select * from " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Trip> tripArrayList = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                int tID = Integer.parseInt(cursor.getString(0));
                String tName = cursor.getString(1);
                String tDestination = cursor.getString(2);
                String tDescription = cursor.getString(3);
                String tDate = cursor.getString(4);
                String tTime = cursor.getString(5);
                String eNum = cursor.getString(6);
                String risk = cursor.getString(7);
                tripArrayList.add(new Trip(tID, tName, tDestination,tDescription,tDate,tTime,eNum,risk));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return tripArrayList;
    }

    public void deleteATrip(int tid) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, TRIP_ID + " = ?", new String[]{String.valueOf(tid)});
    }

    public void deleteAllTrip()
    {
        SQLiteDatabase tDB = getWritableDatabase();
        tDB.delete(TABLE_NAME,null,null);

    }

    public long updateTrip(Trip trip) {
        ContentValues values = new ContentValues();
        values.put(TRIP_NAME, trip.gettName());
        values.put(DESTINATION, trip.getDestination());
        values.put(DESCRIPTION, trip.getDescription());
        values.put(TRIP_DATE, trip.gettDate());
        values.put(TRIP_TIME, trip.gettTime());
        values.put(EMPLOYEE_NUM, trip.geteNum());
        SQLiteDatabase db = this.getWritableDatabase();
        return db.update(TABLE_NAME, values, TRIP_ID + " = ?", new String[]{String.valueOf(trip.gettID())});
    }

    ArrayList<expense> showExpense(int eID)
    {
        String sql = "select * from " + EXPENSE_TABLE +" where eID = "+eID;
        SQLiteDatabase DB = this.getReadableDatabase();
        ArrayList<expense> storeExpense = new ArrayList<>();
        Cursor cursor = DB.rawQuery(sql,null);
        if (cursor.moveToFirst()) {
            do {
                int e_ID = Integer.parseInt(cursor.getString(0));
                String e_Name = cursor.getString(1);
                String e_Amount = cursor.getString(2);
                String e_Date = cursor.getString(3);
                String e_Com = cursor.getString(4);
                storeExpense.add(new expense(e_ID, e_Name, e_Amount,e_Date,e_Com,eID));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return storeExpense;
    }
    void add_Expense (expense expense) {
        ContentValues values = new ContentValues();
        values.put(EXPENSE_NAME, expense.geteName());
        values.put(EXPENSE_AMOUNT, expense.geteAmount());
        values.put(EXPENSE_DATE, expense.geteDate());
        values.put(EXPENSE_COM, expense.geteCom());
        values.put(EXPENSE_ID, expense.geteID());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(EXPENSE_TABLE , null, values);
    }

    void deleteExpense(int e_ID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(EXPENSE_TABLE, EXPENSE_ID + " = ?", new String[]{String.valueOf(e_ID)});
    }

    ArrayList<Details> Upload() {
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase database = this.getReadableDatabase();
        ArrayList<Details> detailList = new ArrayList<>();
        Cursor cursor = database.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                int ID = Integer.parseInt(cursor.getString(0));
                String name = cursor.getString(1);

                String sql = "SELECT * FROM " + EXPENSE_TABLE + " WHERE eID = " + ID;
                Cursor data = database.rawQuery(sql, null);

                if (data.getCount() == 0) {
                    detailList.add(new Details(name));
                } else {
                    if (data.moveToFirst()) {
                        do {
                            String expenseType = data.getString(1);
                            String expenseAmount = data.getString(2);
                            String expenseTime = data.getString(3);
                            String comment = data.getString(4);
                            detailList.add(new Details(name, expenseType, expenseAmount + " $", expenseTime, comment));
                        }
                        while (data.moveToNext());
                    }
                    data.close();
                }
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return detailList;
    }
}
