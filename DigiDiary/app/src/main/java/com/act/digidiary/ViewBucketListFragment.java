package com.act.digidiary;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ViewBucketListFragment extends Fragment {

    RecyclerView r;
    BucketlistItemRecyclerviewAdapter adapter;
    Button add;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_bucket_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        r = getView().findViewById(R.id.bucketListRecyclerView);
        add = getView().findViewById(R.id.addItemToBL);

        adapter = new BucketlistItemRecyclerviewAdapter(getActivity(), getArrayList("Bucketlist"));

        r.setLayoutManager(new LinearLayoutManager(getActivity()));
        r.setAdapter(adapter);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

                alert.setTitle("Add an item to your bucketlist!");
                alert.setMessage("Just input your goal in the input below.");

// Set an EditText view to get user input
                final EditText input = new EditText(getActivity());
                alert.setView(input);

                int size = 0;
                try {
                    size = getArrayList("Bucketlist").size();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                final int finalSize = size;
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (input.getText().toString().replace(" ", "").isEmpty()){
                            Toast.makeText(getActivity(), "That input cannot be empty", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        addItemToArrayList(new BucketlistItem(input.getText().toString(), false, finalSize));
                        adapter = new BucketlistItemRecyclerviewAdapter(getActivity(), getArrayList("Bucketlist"));
                        r.setAdapter(adapter);
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });

                alert.show();
            }
        });
    }

    public void addItemToArrayList(BucketlistItem item){
        ArrayList<BucketlistItem> items;
        items = getArrayList("Bucketlist");
        if (items == null){
            items = new ArrayList<>();
        }
        items.add(item);
        saveArrayList(items, "Bucketlist");
    }

    public void saveArrayList(ArrayList<BucketlistItem> list, String key){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();
    }

    public ArrayList<BucketlistItem> getArrayList(String key){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Gson gson = new Gson();
        String json = prefs.getString(key, null);
        Type type = new TypeToken<ArrayList<BucketlistItem>>() {}.getType();
        return gson.fromJson(json, type);
    }
}