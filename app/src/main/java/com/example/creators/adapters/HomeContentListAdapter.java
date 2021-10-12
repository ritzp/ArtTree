package com.example.creators.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.creators.R;
import com.example.creators.classes.Content;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeContentListAdapter extends RecyclerView.Adapter {

    private ArrayList<Content> contentArray;

    public HomeContentListAdapter(ArrayList<Content> itemData) {
        this.contentArray = new ArrayList<>();
        this.contentArray = itemData;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_homecontent, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return contentArray.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, description;
        ImageView content;
        CircleImageView icon;
        TextView nickname;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.home_txt_title);
            content = itemView.findViewById(R.id.home_img_content);
            icon = itemView.findViewById(R.id.home_img_icon);
            nickname = itemView.findViewById(R.id.home_txt_nickname);
        }
    }
}