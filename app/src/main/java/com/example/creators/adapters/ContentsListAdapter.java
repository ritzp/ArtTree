package com.example.creators.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.creators.R;
import com.example.creators.contents.ContentsList;

import java.util.ArrayList;

public class ContentsListAdapter extends RecyclerView.Adapter {
    private ArrayList<ContentsList> contents;
    private OnItemClickListener clickListener = null;

    public void setOnItemClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public ContentsListAdapter(ArrayList<ContentsList> contents) {
        this.contents = new ArrayList<>();
        this.contents = contents;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return this.contents.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_contentslist, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder vHolder = (ViewHolder)holder;
        vHolder.title.setText(contents.get(position).getTitle());
        vHolder.icon.setImageBitmap(contents.get(position).getIcon());
        vHolder.nickname.setText(contents.get(position).getNickname());
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView icon;
        private TextView title, nickname;

        public ViewHolder(View v) {
            super(v);
            title = v.findViewById(R.id.conlistImg_txt_title);
            icon = v.findViewById(R.id.conlistImg_img_icon);
            nickname = v.findViewById(R.id.conlistImg_txt_nickname);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getBindingAdapterPosition();
                    clickListener.OnItemClick(v, position);
                }
            });
        }
    }
}
