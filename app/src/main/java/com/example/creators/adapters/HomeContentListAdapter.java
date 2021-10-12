package com.example.creators.adapters;

import android.content.res.Resources;
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
import com.example.creators.http.RetrofitClient;
import com.example.creators.main_fragments.HomeFragment;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
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
            viewHolder.category.setText(HomeFragment.context.getString(R.string.photo));
        } else if (category.equals("DR")) {
            viewHolder.category.setText(HomeFragment.context.getString(R.string.drawing));
        } else if (category.equals("MU")) {
            viewHolder.category.setText(HomeFragment.context.getString(R.string.music));
        } else if (category.equals("VI")) {
            viewHolder.category.setText(HomeFragment.context.getString(R.string.video));
        } else if (category.equals("CA")) {
            viewHolder.category.setText(HomeFragment.context.getString(R.string.cartoon));
        } else if (category.equals("NO")) {
            viewHolder.category.setText(HomeFragment.context.getString(R.string.novel));
        }
        viewHolder.title.setText(contentArray.get(position).getTitle());
        viewHolder.views.setText(String.valueOf(contentArray.get(position).getViews()));
        viewHolder.likes.setText(String.valueOf(contentArray.get(position).getLikes()));
        viewHolder.nickname.setText(contentArray.get(position).getNickname());
        Picasso.get().load(RetrofitClient.getIconUrl(contentArray.get(position).getUserId())).into(viewHolder.icon);
    }

    @Override
    public int getItemCount() {
        return contentArray.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView category, title, views, likes, nickname;
        CircleImageView icon;

        public ViewHolder(View view) {
            super(view);
            category = view.findViewById(R.id.home_txt_category);
            title = view.findViewById(R.id.home_txt_title);
            views = view.findViewById(R.id.home_txt_views);
            likes = view.findViewById(R.id.home_txt_likes);
            icon = view.findViewById(R.id.home_img_icon);
            nickname = view.findViewById(R.id.home_txt_nickname);

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