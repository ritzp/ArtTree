package com.example.creators.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.creators.R;
import com.example.creators.classes.Content;
import com.example.creators.http.RetrofitClient;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContentListAdapter extends RecyclerView.Adapter {
    private ArrayList<Content> contentArray;
    private OnItemClickListener clickListener = null;

    public void setOnItemClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public ContentListAdapter(ArrayList<Content> array) {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_contentlist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder)holder;
        viewHolder.title.setText(contentArray.get(position).getTitle());
        viewHolder.nickname.setText(contentArray.get(position).getNickname());
        Picasso.get().load(RetrofitClient.getIconUrl(contentArray.get(position).getUserId())).into(viewHolder.icon);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView icon;
        private TextView title, nickname;

        public ViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.conlist_txt_title);
            nickname = view.findViewById(R.id.conlist_txt_nickname);
            icon = view.findViewById(R.id.conlist_img_icon);

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
