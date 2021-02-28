package com.act.digidiary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LifecycleObserver {

    private SQLiteDatabase database;
    public EventRecyclerviewAdapter adapter;
    AddEventFragment addEventFragment;
    ViewEventsFragment viewEventsFragment;
    ViewBucketListFragment viewBucketListFragment;
    private EventDBHelper eventDBHelper;
    Button viewDiary, viewBucketList;
    FloatingActionButton addEvent;
    FragmentContainerView fragmentContainerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViewEventsFragment();

        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);

        addEventFragment = new AddEventFragment();
        viewEventsFragment = new ViewEventsFragment();
        viewBucketListFragment = new ViewBucketListFragment();
        fragmentContainerView = findViewById(R.id.fragmentContainer);
        viewDiary = findViewById(R.id.viewDiary);
        addEvent = findViewById(R.id.addEventBtn);
        viewBucketList = findViewById(R.id.viewBucketList);

        getSupportFragmentManager().beginTransaction().add(fragmentContainerView.getId(), addEventFragment).add(fragmentContainerView.getId(), viewEventsFragment).add(fragmentContainerView.getId(), viewBucketListFragment).hide(viewBucketListFragment).hide(viewEventsFragment).commit();

        adapter = new EventRecyclerviewAdapter(this, getAllItems());

        viewDiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().hide(addEventFragment).hide(viewBucketListFragment).show(viewEventsFragment).commit();
            }
        });

        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().hide(viewEventsFragment).hide(viewBucketListFragment).show(addEventFragment).commit();
            }
        });

        viewBucketList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().hide(viewEventsFragment).show(viewBucketListFragment).hide(addEventFragment).commit();
            }
        });
    }

    private void initializeViewEventsFragment(){
        eventDBHelper = new EventDBHelper(this);
        database = eventDBHelper.getWritableDatabase();
        adapter = new EventRecyclerviewAdapter(this, getAllItems());
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    private void onAppBackgrounded() {
        Log.d("MyApp", "App in background");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private void onAppForegrounded() {
        addEventFragment.setDate();
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

}