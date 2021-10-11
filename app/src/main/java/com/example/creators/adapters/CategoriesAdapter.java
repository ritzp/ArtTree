package com.example.creators.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.creators.R;
import com.example.creators.classes.Category;

import java.util.ArrayList;

public class CategoriesAdapter extends BaseAdapter {
    private ArrayList<Category> categoryArray;
    private OnItemClickListener clickListener = null;

    public void setOnItemClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public CategoriesAdapter(ArrayList<Category> array) {
        this.categoryArray = new ArrayList<>();
        this.categoryArray = array;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return categoryArray.size();
    }

    @Override
    public Object getItem(int position) {
        return categoryArray.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;

        if (convertView == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_category, parent, false);
        } else {
            view = (View)convertView;
        }

        TextView txt = view.findViewById(R.id.category_txt_categoryName);
        ImageView img = view.findViewById(R.id.category_img_categoryImg);

        txt.setText(categoryArray.get(position).getTxt());
        img.setImageResource(categoryArray.get(position).getImg());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.OnItemClick(v, position);
            }
        });

        return view;
    }
}