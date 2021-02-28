package com.act.digidiary;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class BucketlistItemRecyclerviewAdapter extends RecyclerView.Adapter<BucketlistItemRecyclerviewAdapter.BucketlistItemViewHolder>{

    public ArrayList<BucketlistItem> bucketlistItems;
    Context mContext;

    public BucketlistItemRecyclerviewAdapter(Context c, ArrayList<BucketlistItem> bucketlistItems){
        this.bucketlistItems = bucketlistItems;
        mContext = c;
    }

    @NonNull
    @Override
    public BucketlistItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.bucketlist_item_layout, parent, false);

        return new BucketlistItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BucketlistItemViewHolder holder, final int position) {
        holder.title.setText(bucketlistItems.get(position).title);
        holder.checkBox.setChecked(bucketlistItems.get(position).isCompleted);

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                bucketlistItems.get(position).setIsCompleted(isChecked);
                saveArrayList(bucketlistItems, "Bucketlist");
            }
        });
    }

    @Override
    public int getItemCount() {
        try {
            return bucketlistItems.size();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public class BucketlistItemViewHolder extends RecyclerView.ViewHolder{
        public TextView title;
        public CheckBox checkBox;

        public BucketlistItemViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.bucketListTitle);
            checkBox = itemView.findViewById(R.id.checkBox);
        }
    }

    public void saveArrayList(ArrayList<BucketlistItem> list, String key){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();
    }
}
