package com.example.arttree.adapters;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.arttree.R;
import com.example.arttree.classes.Content;
import com.example.arttree.http.RetrofitClient;
import com.example.arttree.main_fragments.HomeFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeContentListAdapter extends RecyclerView.Adapter {
    private ArrayList<Content> contentArray;
    private OnItemClickListener clickListener = null;

    public void setOnItemClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public HomeContentListAdapter(ArrayList<Content> array) {
        this.contentArray = new ArrayList<>();
        this.contentArray = array;
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
        ViewHolder viewHolder = (ViewHolder)holder;
        String category = contentArray.get(position).getContentId().substring(0,2);
        if (category.equals("PH")) {
            viewHolder.imgPrev.setVisibility(View.VISIBLE);
            downloadImage("photo", contentArray.get(position).getContentId(), contentArray.get(position).getExtension(), viewHolder);
            viewHolder.category.setText(HomeFragment.context.getString(R.string.photo));
        } else if (category.equals("DR")) {
            viewHolder.imgPrev.setVisibility(View.VISIBLE);
            downloadImage("drawing", contentArray.get(position).getContentId(), contentArray.get(position).getExtension(), viewHolder);
            viewHolder.category.setText(HomeFragment.context.getString(R.string.drawing));
        } else if (category.equals("MU")) {
            viewHolder.imgPrev.setVisibility(View.GONE);
            viewHolder.category.setText(HomeFragment.context.getString(R.string.music));
        } else if (category.equals("VI")) {
            viewHolder.imgPrev.setVisibility(View.GONE);
            viewHolder.category.setText(HomeFragment.context.getString(R.string.video));
        } else if (category.equals("CA")) {
            viewHolder.imgPrev.setVisibility(View.VISIBLE);
            downloadImage("cartoon", contentArray.get(position).getContentId(), contentArray.get(position).getExtension(), viewHolder);
            viewHolder.category.setText(HomeFragment.context.getString(R.string.cartoon));
        } else if (category.equals("NO")) {
            viewHolder.imgPrev.setVisibility(View.GONE);
            viewHolder.category.setText(HomeFragment.context.getString(R.string.novel));
        }
        viewHolder.title.setText(contentArray.get(position).getTitle());
        String tagOriginal = contentArray.get(position).getTag();
        if (tagOriginal.length() > 0) {
            viewHolder.tag.setVisibility(View.VISIBLE);
            String tag = "";
            String[] tagArray = tagOriginal.split("/");
            for (int i=0; i<tagArray.length; i++) {
                tag += ("#" + tagArray[i] + " ");
            }
            viewHolder.tag.setText(tag);
        } else {
            viewHolder.tag.setText(null);
            viewHolder.tag.setVisibility(View.GONE);
        }
        Log.e("tag",tagOriginal);
        viewHolder.views.setText(String.valueOf(contentArray.get(position).getViews()));
        viewHolder.likes.setText(String.valueOf(contentArray.get(position).getLikes()));
        viewHolder.nickname.setText(contentArray.get(position).getNickname());
        Glide.with(HomeFragment.context).load(RetrofitClient.getIconUrl(contentArray.get(position).getUserId())).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).placeholder(R.drawable.pic_icon_default).error(R.drawable.pic_icon_default).into(viewHolder.icon);
    }

    private void downloadImage(String category, String contentId, String extensions, ViewHolder viewHolder) {
        String[] extension = extensions.split("/");
        if (extension.length <= 1) {
            Glide.with(HomeFragment.context).load(RetrofitClient.getContentUrl(category, contentId, extension[0])).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(viewHolder.imgPrev);
        } else {
            Glide.with(HomeFragment.context).load(RetrofitClient.getContentUrl(category, contentId + "-0", extension[0])).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(viewHolder.imgPrev);
        }
    }

    @Override
    public int getItemCount() {
        return contentArray.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView category, title, tag, views, likes, nickname;
        CircleImageView icon;
        ImageView imgPrev;

        public ViewHolder(View view) {
            super(view);
            category = view.findViewById(R.id.home_txt_category);
            title = view.findViewById(R.id.home_txt_title);
            tag = view.findViewById(R.id.home_txt_tag);
            views = view.findViewById(R.id.home_txt_views);
            likes = view.findViewById(R.id.home_txt_likes);
            icon = view.findViewById(R.id.home_img_icon);
            nickname = view.findViewById(R.id.home_txt_nickname);
            imgPrev = view.findViewById(R.id.home_img_preview);

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