package com.example.creators.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.creators.ContentDetailActivity;
import com.example.creators.R;
import com.example.creators.http.RetrofitClient;

public class ContentDetailImageAdapter extends RecyclerView.Adapter {
    private String category;
    private String contentId;
    private String[] extension;

    public ContentDetailImageAdapter(String category, String contentId, String[] extension) {
        this.category = category;
        this.contentId = contentId;
        this.extension = extension;
    }

    @Override
    public int getItemCount() {
        return extension.length;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_contentdetail_image, parent, false);
        return new ContentDetailImageAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder)holder;
        Glide.with(ContentDetailActivity.context).load(RetrofitClient.getContentUrl(category, contentId + "-" + position, extension[0])).into(viewHolder.img);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView img;

        public ViewHolder(View view) {
            super(view);
            img = view.findViewById(R.id.contentdetail_img_img);
        }
    }
}
