package com.example.creators.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.creators.R;

import java.util.List;

public class CategoryListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private String[] strings;

    public CategoryListAdapter(String[] strings) {
        this.strings = strings;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return strings.length;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_categorylist, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder vHolder = (ViewHolder)holder;
        vHolder.txt.setText(strings[position]);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txt;

        public ViewHolder(View v) {
            super(v);
            txt = v.findViewById(R.id.txt_categoryName);
        }
    }
}
