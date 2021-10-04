package com.example.creators.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.creators.R;
import com.example.creators.classes.ContentsList;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContentsListAdapter extends RecyclerView.Adapter {
    private ArrayList<ContentsList> contentsArray;
    private OnItemClickListener clickListener = null;

    public void setOnItemClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public ContentsListAdapter(ArrayList<ContentsList> array) {
        this.contentsArray = new ArrayList<>();
        this.contentsArray = array;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return this.contentsArray.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_contentslist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder)holder;
        viewHolder.title.setText(contentsArray.get(position).getTitle());
        viewHolder.icon.setImageBitmap(contentsArray.get(position).getIcon());
        viewHolder.nickname.setText(contentsArray.get(position).getNickname());
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView icon;
        private TextView title, nickname;

        public ViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.conlist_txt_title);
            icon = view.findViewById(R.id.conlist_img_icon);
            nickname = view.findViewById(R.id.conlist_txt_nickname);

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
