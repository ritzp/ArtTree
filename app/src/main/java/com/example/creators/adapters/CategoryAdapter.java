package com.example.creators.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.creators.R;
import com.example.creators.classes.Category;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Category> categoryArray;
    private OnItemClickListener clickListener = null;

    public void setOnItemClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public CategoryAdapter(ArrayList<Category> array) {
        this.categoryArray = new ArrayList<>();
        this.categoryArray = array;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return categoryArray.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder)holder;
        viewHolder.img.setImageResource(categoryArray.get(position).getImg());
        viewHolder.txt.setText(categoryArray.get(position).getTxt());
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView img;
        private TextView txt;

        public ViewHolder(View view) {
            super(view);
            img = view.findViewById(R.id.category_img_categoryImg);
            txt = view.findViewById(R.id.category_txt_categoryName);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getBindingAdapterPosition();
                    clickListener.OnItemClick(v, position);
                }
            });
        }
    }
}
