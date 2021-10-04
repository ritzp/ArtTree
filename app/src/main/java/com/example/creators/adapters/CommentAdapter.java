package com.example.creators.adapters;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.creators.R;
import com.example.creators.classes.Comment;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends RecyclerView.Adapter {
    private ArrayList<Comment> commentArray;

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
        viewHolder.icon.setImageBitmap(commentArray.get(position).getIcon());
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nickname, comment;
        private CircleImageView icon;

        public ViewHolder(View view) {
            super(view);
            nickname = view.findViewById(R.id.comment_txt_nickname);
            comment = view.findViewById(R.id.comment_txt_comment);
            icon = view.findViewById(R.id.comment_img_icon);
        }
    }
}
