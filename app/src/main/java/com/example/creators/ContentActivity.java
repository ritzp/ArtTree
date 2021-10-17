package com.example.creators;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.creators.adapters.CommentAdapter;
import com.example.creators.adapters.OnItemClickListener;
import com.example.creators.app.AppHelper;
import com.example.creators.app.LoadingDialog;
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
    public static Context context;

    private ArrayList<Comment> commentArray;
    private CommentAdapter adapter;

    private ContentViewModel viewModel;
    private ImageView close, icon, writeComment, like;
    private EditText comment;
    private TextView title, description, nickname, views, likes, comments;
    private RecyclerView commentList;
    private View user;

    private String contentId;
    private boolean isLiked = false;

    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content);
        getSupportActionBar().hide();
        context = this;

        Intent intent = getIntent();
        contentId = intent.getStringExtra("contentId");
        viewModel = new ViewModelProvider(this).get(ContentViewModel.class);
        commentArray = new ArrayList<>();
        adapter = new CommentAdapter(commentArray);

        close = findViewById(R.id.content_close);
        title = findViewById(R.id.content_title);
        description = findViewById(R.id.content_desc);
        nickname = findViewById(R.id.content_nickname);
        icon = findViewById(R.id.content_icon);
        views = findViewById(R.id.content_views);
        likes = findViewById(R.id.content_likes);
        comments = findViewById(R.id.content_comments);
        comment = findViewById(R.id.content_edt_comment);
        writeComment = findViewById(R.id.content_img_writeComment);
        like = findViewById(R.id.content_img_like);
        commentList = findViewById(R.id.content_commentsList);
        user = findViewById(R.id.content_user);

        commentList.setLayoutManager(new LinearLayoutManager(this));
        commentList.setAdapter(adapter);

        loadingDialog = new LoadingDialog(this, R.layout.alert_loading);

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

        adapter.setOnDeleteClickListener(new OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int pos) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(ContentActivity.this);
                alertBuilder.setMessage(getString(R.string.delete_alert_message));
                alertBuilder.setPositiveButton(getString(R.string.delete), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        loadingDialog.show();
                        sendDeleteCommentRequest(commentArray.get(pos).getCommentId());
                    }
                }).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertBuilder.show();
            }
        });

        writeComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (comment.getText().length() <= 0) {
                    return;
                } else if (comment.getText().length() > 200) {
                    Toast.makeText(ContentActivity.this, getString(R.string.comment_over_chars), Toast.LENGTH_SHORT).show();
                    return;
                }
                loadingDialog.show();
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

        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewModel.getUserId().getValue() != null) {
                    Intent intent = new Intent(ContentActivity.this, UserPageActivity.class);
                    intent.putExtra("userId", viewModel.getUserId().getValue());
                    startActivity(intent);
                }
            }
        });

        loadingDialog.show();
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
                    getSupportFragmentManager().beginTransaction().replace(R.id.content_fragmentContainer, photoFragment).commit();
                } else if (type.equals("DR")) {
                    DrawingFragment drawingFragment = new DrawingFragment();
                    drawingFragment.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().replace(R.id.content_fragmentContainer, drawingFragment).commit();
                } else if (type.equals("CA")) {
                    CartoonFragment cartoonFragment = new CartoonFragment();
                    cartoonFragment.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().replace(R.id.content_fragmentContainer, cartoonFragment).commit();
                } else if (type.equals("MU")) {
                    MusicFragment musicFragment = new MusicFragment();
                    musicFragment.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().replace(R.id.content_fragmentContainer, musicFragment).commit();
                } else if (type.equals("VI")) {
                    VideoFragment videoFragment = new VideoFragment();
                    videoFragment.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().replace(R.id.content_fragmentContainer, videoFragment).commit();
                } else if (type.equals("NO")) {
                    NovelFragment novelFragment = new NovelFragment();
                    novelFragment.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().replace(R.id.content_fragmentContainer, novelFragment).commit();
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
                viewModel.getIsLiked().setValue(response.body().getContent().get(0).getIsLiked());
                viewModel.getComments().setValue(response.body().getContent().get(0).getComments());
                Glide.with(ContentActivity.this).load(RetrofitClient.getIconUrl(viewModel.getUserId().getValue())).placeholder(R.drawable.pic_icon_default).error(R.drawable.pic_icon_default).into(icon);

                if (response.body().getContent().get(0).getComments() > 0) {
                    for (int i = 0; i < response.body().getComment().size(); i++) {
                        commentArray.add(new Comment(
                                response.body().getComment().get(i).getCommentId(),
                                response.body().getComment().get(i).getUserId(),
                                response.body().getComment().get(i).getNickname(),
                                response.body().getComment().get(i).getComment()
                        ));
                    }
                }
                adapter.notifyDataSetChanged();
                loadingDialog.off();
            }

            @Override
            public void onFailure(Call<ContentResponse> call, Throwable t) {
                AppHelper.checkError(ContentActivity.this, AppHelper.RESPONSE_ERROR);
                t.printStackTrace();
                loadingDialog.off();
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
                    comment.setText("");
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
                loadingDialog.off();
            }

            @Override
            public void onFailure(Call<Like> call, Throwable t) {
                AppHelper.checkError(ContentActivity.this, AppHelper.RESPONSE_ERROR);
                t.printStackTrace();
                loadingDialog.off();
            }
        });
    }

    private void sendDeleteCommentRequest(String commentId) {
        api = RetrofitClient.getRetrofit().create(ApiInterface.class);
        Call<String> call = api.postDeleteComment(String.valueOf(commentId));

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body().equals("SUCCESS")) {
                    Toast.makeText(ContentActivity.this, getString(R.string.delete_completed), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ContentActivity.this, ContentActivity.class);
                    intent.putExtra("contentId", contentId);
                    startActivity(intent);
                    finish();
                } else {
                    AppHelper.checkError(ContentActivity.this, AppHelper.CODE_ERROR);
                }
                loadingDialog.off();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                AppHelper.checkError(ContentActivity.this, AppHelper.RESPONSE_ERROR);
                loadingDialog.off();
                t.printStackTrace();
            }
        });
    }

    private void refresh() {
        loadingDialog.show();
        commentArray.clear();
        sendRequest("false");
    }
}
