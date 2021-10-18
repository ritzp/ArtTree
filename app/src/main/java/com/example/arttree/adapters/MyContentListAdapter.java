package com.example.arttree.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arttree.R;
import com.example.arttree.classes.MyContent;
import com.example.arttree.http.ApiInterface;
import com.example.arttree.http.RetrofitClient;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyContentListAdapter extends RecyclerView.Adapter {
    private ArrayList<MyContent> contentArray;
    private OnItemClickListener clickListener = null;
    private OnItemClickListener deleteClickListener = null;

    public void setOnItemClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void setOnDeleteClickListener(OnItemClickListener clickListener) {
        this.deleteClickListener = clickListener;
    }

    public MyContentListAdapter(ArrayList<MyContent> array) {
        this.contentArray = new ArrayList<>();
        this.contentArray = array;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return this.contentArray.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_mycontentlist, parent, false);
        return new MyContentListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder)holder;
        viewHolder.title.setText(contentArray.get(position).getTitle());
        viewHolder.category.setImageResource(contentArray.get(position).getCategory());
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView category, delete;
        private TextView title;

        public ViewHolder(View view) {
            super(view);
            category = view.findViewById(R.id.myconlist_img_category);
            title = view.findViewById(R.id.myconlist_txt_title);
            delete = view.findViewById(R.id.myconlist_img_delete);

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getBindingAdapterPosition();
                    deleteClickListener.OnItemClick(v, position);
                }
            });

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
