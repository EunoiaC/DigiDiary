package com.act.digidiary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements LifecycleObserver {

    private SQLiteDatabase database;
    public EventRecyclerviewAdapter adapter;
    AddEventFragment addEventFragment;
    ViewEventsFragment viewEventsFragment;
    private EventDBHelper eventDBHelper;
    FragmentContainerView fragmentContainerView;
    BottomNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViewEventsFragment();

        //TODO: Migrate Recyclerview adapter to eventsvieew fragment

        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);

        navigationView = findViewById(R.id.bottomAppBar);
        addEventFragment = new AddEventFragment();
        viewEventsFragment = new ViewEventsFragment();
        fragmentContainerView = findViewById(R.id.fragmentContainer);

        getSupportFragmentManager().beginTransaction().add(fragmentContainerView.getId(), addEventFragment).add(fragmentContainerView.getId(), viewEventsFragment).hide(viewEventsFragment).commit();

        adapter = new EventRecyclerviewAdapter(this, getAllItems());

        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.yourDiary:
                        getSupportFragmentManager().beginTransaction().show(viewEventsFragment).commit();
                        getSupportFragmentManager().beginTransaction().hide(addEventFragment).commit();
                        return true;
                    case R.id.addEventItem:
                        getSupportFragmentManager().beginTransaction().show(addEventFragment).commit();
                        getSupportFragmentManager().beginTransaction().hide(viewEventsFragment).commit();
                        return true;
                }
                return false;
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