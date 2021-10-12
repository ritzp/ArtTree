package com.example.creators;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.creators.adapters.CommentAdapter;
import com.example.creators.app.AppHelper;
import com.example.creators.classes.Comment;
import com.example.creators.content_fragments.CartoonFragment;
import com.example.creators.content_fragments.DrawingFragment;
import com.example.creators.content_fragments.PhotoFragment;
import com.example.creators.content_fragments.MusicFragment;
import com.example.creators.content_fragments.NovelFragment;
import com.example.creators.content_fragments.VideoFragment;
import com.example.creators.http.ApiInterface;
import com.example.creators.http.RetrofitClient;
import com.example.creators.http.response.ContentResponse;
import com.example.creators.http.response.classes.Like;
import com.example.creators.viewmodels.ContentViewModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContentActivity extends AppCompatActivity {

    private ApiInterface api;

    private ArrayList<Comment> commentArray;
    private CommentAdapter adapter;

    private ContentViewModel viewModel;
    private View loading, view, fragment;
    private ImageView close, icon, writeComment, like;
    private EditText comment;
    private TextView title, description, nickname, views, likes, comments;
    private RecyclerView commentList;

    private String contentId;
    private boolean isLiked = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content);
        getSupportActionBar().hide();

        Intent intent = getIntent();
        contentId = intent.getStringExtra("contentId");
        viewModel = new ViewModelProvider(this).get(ContentViewModel.class);
        commentArray = new ArrayList<>();
        adapter = new CommentAdapter(commentArray);

        loading = findViewById(R.id.condetail_loading);
        view = findViewById(R.id.condetail_view);
        close = findViewById(R.id.condetail_close);
        title = findViewById(R.id.condetail_title);
        description = findViewById(R.id.condetail_desc);
        nickname = findViewById(R.id.condetail_nickname);
        icon = findViewById(R.id.condetail_icon);
        views = findViewById(R.id.condetail_views);
        likes = findViewById(R.id.condetail_likes);
        comments = findViewById(R.id.condetail_comments);
        comment = findViewById(R.id.condetail_edt_comment);
        writeComment = findViewById(R.id.condetail_img_writeComment);
        like = findViewById(R.id.condetail_img_like);
        commentList = findViewById(R.id.condetail_commentsList);

        commentList.setLayoutManager(new LinearLayoutManager(this));
        commentList.setAdapter(adapter);

        viewModel.getTitle().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                title.setText(s);
            }
        });

        viewModel.getDescription().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                description.setText(s);
            }
        });

        viewModel.getNickname().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                nickname.setText(s);
            }
        });

        viewModel.getUserIcon().observe(this, new Observer<Bitmap>() {
            @Override
            public void onChanged(Bitmap bitmap) {
                icon.setImageBitmap(bitmap);
            }
        });

        viewModel.getViews().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                views.setText(integer.toString());
            }
        });

        viewModel.getLikes().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                likes.setText(integer.toString());
            }
        });

        viewModel.getIsLiked().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                isLiked = aBoolean;
                if (isLiked) {
                    like.setImageResource(R.drawable.ic_like_ac);
                } else {
                    like.setImageResource(R.drawable.ic_like);
                }
            }
        });

        viewModel.getComments().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                comments.setText(integer.toString());
            }
        });

        writeComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (comment.getText().length() <= 0) {
                    return;
                } else if (comment.getText().length() > 200) {
                    Toast.makeText(ContentActivity.this, getString(R.string.comment_overed), Toast.LENGTH_SHORT).show();
                }
                sendCommentRequest();
            }
        });

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLiked) {
                    isLiked = false;
                } else {
                    isLiked = true;
                }
                sendLikeRequest();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        sendRequest("true");
    }

    private void sendRequest(String isFirst) {
        api = RetrofitClient.getRetrofit().create(ApiInterface.class);
        Call<ContentResponse> call = api.postContent(AppHelper.getAccessingUserid(), contentId, isFirst);

        call.enqueue(new Callback<ContentResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<ContentResponse> call, retrofit2.Response<ContentResponse> response) {
                Bundle bundle = new Bundle();
                bundle.putString("contentId", contentId);
                bundle.putString("extension", response.body().getContent().get(0).getExtension());
                String type = contentId.substring(0,2);
                if (type.equals("PH")) {
                    PhotoFragment photoFragment = new PhotoFragment();
                    photoFragment.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().replace(R.id.condetail_fragmentContainer, photoFragment).commit();
                } else if (type.equals("DR")) {
                    DrawingFragment drawingFragment = new DrawingFragment();
                    drawingFragment.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().replace(R.id.condetail_fragmentContainer, drawingFragment).commit();
                } else if (type.equals("CA")) {
                    CartoonFragment cartoonFragment = new CartoonFragment();
                    cartoonFragment.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().replace(R.id.condetail_fragmentContainer, cartoonFragment).commit();
                } else if (type.equals("MU")) {
                    MusicFragment musicFragment = new MusicFragment();
                    musicFragment.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().replace(R.id.condetail_fragmentContainer, musicFragment).commit();
                } else if (type.equals("VI")) {
                    VideoFragment videoFragment = new VideoFragment();
                    videoFragment.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().replace(R.id.condetail_fragmentContainer, videoFragment).commit();
                } else if (type.equals("NO")) {
                    NovelFragment novelFragment = new NovelFragment();
                    novelFragment.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().replace(R.id.condetail_fragmentContainer, novelFragment).commit();
                } else {
                    AppHelper.checkError(ContentActivity.this, AppHelper.CODE_ERROR);
                    return;
                }
                viewModel.getTitle().setValue(response.body().getContent().get(0).getTitle());
                viewModel.getDescription().setValue(response.body().getContent().get(0).getDescription());
                viewModel.getViews().setValue(response.body().getContent().get(0).getViews());
                viewModel.getLikes().setValue(response.body().getContent().get(0).getLikes());
                viewModel.getUserId().setValue(response.body().getContent().get(0).getUserId());
                viewModel.getNickname().setValue(response.body().getContent().get(0).getNickname());
                byte[] iconBytes = java.util.Base64.getDecoder().decode(response.body().getContent().get(0).getIcon());
                Bitmap icon = BitmapFactory.decodeByteArray(iconBytes, 0, iconBytes.length);
                viewModel.getIsLiked().setValue(response.body().getContent().get(0).getIsLiked());
                viewModel.getUserIcon().setValue(icon);
                viewModel.getComments().setValue(response.body().getContent().get(0).getComments());

                if (response.body().getContent().get(0).getComments() > 0) {
                    for (int i = 0; i < response.body().getComment().size(); i++) {
                        byte[] cIconBytes = java.util.Base64.getDecoder().decode(response.body().getComment().get(i).getIcon());
                        Bitmap cIcon = BitmapFactory.decodeByteArray(cIconBytes, 0, cIconBytes.length);
                        commentArray.add(new Comment(
                                response.body().getComment().get(i).getNickname(),
                                cIcon,
                                response.body().getComment().get(i).getComment()
                        ));
                    }
                }
                adapter.notifyDataSetChanged();

                loadView();
            }

            @Override
            public void onFailure(Call<ContentResponse> call, Throwable t) {
                AppHelper.checkError(ContentActivity.this, AppHelper.RESPONSE_ERROR);
                t.printStackTrace();
            }
        });
    }

    private void sendCommentRequest() {
        api = RetrofitClient.getRetrofit().create(ApiInterface.class);
        Call<String> call = api.postComment(contentId, AppHelper.getAccessingUserid(), comment.getText().toString());

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (!AppHelper.checkError(ContentActivity.this, response.body()))
                    return;

                if (response.body().equals("SUCCESS"))
                    refresh();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                AppHelper.checkError(ContentActivity.this, AppHelper.RESPONSE_ERROR);
                t.printStackTrace();
            }
        });
    }

    private void sendLikeRequest() {
        api = RetrofitClient.getRetrofit().create(ApiInterface.class);
        Call<Like> call = api.postLike(AppHelper.getAccessingUserid(), contentId);

        call.enqueue(new Callback<Like>() {
            @Override
            public void onResponse(Call<Like> call, Response<Like> response) {
                viewModel.getIsLiked().setValue(response.body().getLike());
                viewModel.getLikes().setValue(response.body().getLikes());
                isLiked = response.body().getLike();
            }

            @Override
            public void onFailure(Call<Like> call, Throwable t) {
                AppHelper.checkError(ContentActivity.this, AppHelper.RESPONSE_ERROR);
                t.printStackTrace();
            }
        });
    }

    private void loadView() {
        loading.setVisibility(View.GONE);
        view.setVisibility(View.VISIBLE);
    }

    private void refresh() {
        loading.setVisibility(View.VISIBLE);
        view.setVisibility(View.GONE);
        commentArray.clear();
        sendRequest("false");
    }
}
