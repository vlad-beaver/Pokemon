package com.example.pokemon.ui;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Spinner;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


import com.example.pokemon.DataBase.DbHelper;
import com.example.pokemon.R;

public class DataBase extends AppCompatActivity implements View.OnClickListener {

    private DbHelper dbHelper;
    private SQLiteDatabase db;
    private EditText mDateWith, mDateOn;
    private String dateWith, dateOn;
    private Spinner mFaculty;
    private GridView mResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_base);

        dbHelper = new DbHelper(this);
        db = dbHelper.getWritableDatabase();

        mDateWith = findViewById(R.id.date_with);
        mDateOn = findViewById(R.id.date_on);
        mFaculty = findViewById(R.id.faculty);
        mResult =  findViewById(R.id.result);

        findViewById(R.id.button_select).setOnClickListener(this);

        initSpiner();
        getCosts();
    }

    @Override
    protected void onStart() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                getCosts());
        mResult.setAdapter(adapter);
        super.onStart();
    }

    protected ArrayList<String> getCosts() {
        DbHelper dbHelper = new DbHelper(this);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ArrayList<String> data = new ArrayList<>();

        Cursor cursor = database.rawQuery("select * from PokemonView", null);
        if (cursor.moveToFirst()) {
//            int indexNote = cursor.getColumnIndex("LastVersion");
            int indexDate = cursor.getColumnIndex("Name");
//            int indexTime = cursor.getColumnIndex("NextVersion");
            do {
                data.add(cursor.getString(indexDate));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return data;
    }


    protected void initSpiner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getFacultys());
        mFaculty.setAdapter(adapter);
    }

    protected ArrayList<String> getFacultys() {
        ArrayList<String> data = new ArrayList<>();
        String query = "select TypePokemon.TypePokemon as type from TypePokemon";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            int facultyIndex = cursor.getColumnIndex("type");
            do {
                data.add(cursor.getString(facultyIndex));
            } while (cursor.moveToNext());
        }
        return data;
    }

    protected boolean formatDateIsCorrect(String date) {
        String DATE_FORMAT = "yyyy-MM-dd";
        SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT);
        df.setLenient(false);
        return df.parse(date, new ParsePosition(0)) != null;
    }

    protected boolean getPeriod(){
        if(formatDateIsCorrect(mDateWith.getText().toString())) {
            dateWith = mDateWith.getText().toString();
            if(formatDateIsCorrect(mDateOn.getText().toString())) {
                dateOn = mDateOn.getText().toString();
                return true;
            } else {
                mDateOn.setError("Error date format");
                mDateOn.requestFocus();
                return false;
            }
        } else {
            mDateWith.setError("Error date format");
            mDateWith.requestFocus();
            return false;
        }
    }

    protected void selectBestStudentsForPeriodOnFaculty() {
        if (getPeriod()) {
            mResult.setNumColumns(4);
            mResult.setAdapter(null);

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
            adapter.add("Первая версия");
            adapter.add("Имя");
            adapter.add("Посл. версия");
            adapter.add("Дата");


            String query = "select LastVersion, Name, NextVersion, Date, Type from PokemonView "
                    + "where Type = '" + mFaculty.getSelectedItem().toString() + "' "
                    + "and Date BETWEEN '" + dateWith + "' and '" + dateOn + "' ";
            Cursor cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                int facultyIndex = cursor.getColumnIndex("LastVersion");
                int groupNameIndex = cursor.getColumnIndex("Name");
                int markIndex = cursor.getColumnIndex("NextVersion");
                int strudentNameIndex = cursor.getColumnIndex("Date");
                do {
                    adapter.add(cursor.getString(facultyIndex));
                    adapter.add(cursor.getString(groupNameIndex));

                    adapter.add(cursor.getString(markIndex));
                    adapter.add(cursor.getString(strudentNameIndex));
                } while (cursor.moveToNext());
            }
            mResult.setAdapter(adapter);
            cursor.close();
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_select:
                selectBestStudentsForPeriodOnFaculty();
                break;
        }
    }
}