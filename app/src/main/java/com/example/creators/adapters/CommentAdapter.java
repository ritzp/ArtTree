package com.example.creators.adapters;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.creators.ContentActivity;
import com.example.creators.R;
import com.example.creators.app.AppHelper;
import com.example.creators.classes.Comment;
import com.example.creators.http.RetrofitClient;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends RecyclerView.Adapter {
    private ArrayList<Comment> commentArray;
    private OnItemClickListener deleteClickListener;

    public void setOnDeleteClickListener(OnItemClickListener clickListener) {
        this.deleteClickListener = clickListener;
    }

    public CommentAdapter(ArrayList<Comment> array) {
        this.commentArray = new ArrayList<>();
        this.commentArray = array;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return commentArray.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_comment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder)holder;
        viewHolder.nickname.setText(commentArray.get(position).getNickname());
        viewHolder.comment.setText(commentArray.get(position).getComment());
        Glide.with(ContentActivity.context).load(RetrofitClient.getIconUrl(commentArray.get(position).getUserId())).placeholder(R.drawable.pic_icon_default).error(R.drawable.pic_icon_default).into(viewHolder.icon);
        if (commentArray.get(position).getUserId().equals(AppHelper.getAccessingUserid())) {
            viewHolder.delete.setVisibility(View.VISIBLE);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nickname, comment;
        private CircleImageView icon;
        private ImageView delete;

        public ViewHolder(View view) {
            super(view);
            nickname = view.findViewById(R.id.comment_txt_nickname);
            comment = view.findViewById(R.id.comment_txt_comment);
            icon = view.findViewById(R.id.comment_img_icon);
            delete = view.findViewById(R.id.comment_img_delete);

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getBindingAdapterPosition();
                    deleteClickListener.OnItemClick(v, position);
                }
            });
        }
    }
}
