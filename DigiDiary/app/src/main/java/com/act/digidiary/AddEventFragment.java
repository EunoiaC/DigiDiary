package com.act.digidiary;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;


public class AddEventFragment extends Fragment {

    private TextInputLayout editTextEvent;
    private TextView textViewDate;
    private Button addEvent;
    private SQLiteDatabase database;
    private EventDBHelper eventDBHelper;
    String addItemDateText;
    private static final String SHARED_PREFS = "sharedPrefs";
    String date;

    //TODO: Add time the event was added, eg 10:17, 9:41

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_event, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editTextEvent = getView().findViewById(R.id.eventInput);
        textViewDate = getView().findViewById(R.id.date);
        addEvent = getView().findViewById(R.id.addEvent);

        eventDBHelper = new EventDBHelper(getActivity());
        database = eventDBHelper.getWritableDatabase();

        setDate();

        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem(editTextEvent.getEditText().getText().toString());
            }
        });
    }

    public void setDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        date = dateFormat.format(calendar.getTime());
        addItemDateText = date + " at ";

        //TODO: Add a shared preferences for date, and detect if date string matches the shared preferences one, if not change the shared preferences to match the current date

        textViewDate.setText(date);
    }

    private void addItem(String item) {
        if (item.replace(" ", "").isEmpty()){
            Toast.makeText(getActivity(), "Please make sure the text field is not blank.", Toast.LENGTH_SHORT).show();
            return;
        }
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        addItemDateText = "";

        ContentValues contentValues = new ContentValues();
        //Adding the event to the db
        contentValues.put(Event.EventEntry.COLUMN_EVENT, item);
        //Adding the time to the db
        contentValues.put(Event.EventEntry.COLUMN_DATE, addItemDateText + sdf.format(cal.getTime()));
        //Notifying the adapter that the database has new values
        ((MainActivity) getActivity()).adapter.swapCursor(getAllItems());
        //Inserting all the items into a row in the db
        database.insert(Event.EventEntry.TABLE_NAME, null, contentValues);

        if (loadData("Date", getActivity()).equals(date)){
            Log.d("Detect Date", "Date checked");
        } else{
            saveData("Date", date, getContext());
            ContentValues contentValues2 = new ContentValues();
            //Adding the event to the db
            contentValues2.put(Event.EventEntry.COLUMN_EVENT, "");
            //Adding the time to the db
            contentValues2.put(Event.EventEntry.COLUMN_DATE, date);
            //Notifying the adapter that the database has new values
            ((MainActivity) getActivity()).adapter.swapCursor(getAllItems());
            //Inserting all the items into a row in the db
            database.insert(Event.EventEntry.TABLE_NAME, null, contentValues2);
        }
    }

    private Cursor getAllItems(){
        return database.query(
                Event.EventEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                Event.EventEntry.COLUMN_TIMESTAMP + " DESC"
        );
    }

    public static void saveData(String key, String item, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, item);
        editor.apply();
    }

    public static String loadData(String key, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String text = sharedPreferences.getString(key, "");
        return text;
    }
}