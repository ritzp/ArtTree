package com.example.creators;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.creators.adapters.CommentAdapter;
import com.example.creators.app.AppHelper;
import com.example.creators.classes.Comment;
import com.example.creators.contents_fragments.ImageFragment;
import com.example.creators.contents_fragments.SoundFragment;
import com.example.creators.contents_fragments.TextFragment;
import com.example.creators.contents_fragments.VideoFragment;
import com.example.creators.jsp.JspHelper;
import com.example.creators.jsp.requests.ContentsRequest;
import com.example.creators.viewmodels.ContentsDetailViewModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ContentsActivity extends FragmentActivity {

    private ArrayList<Comment> commentArray;
    private CommentAdapter adapter;

    private ContentsDetailViewModel viewModel;
    private View loading, view;
    private ImageView close, icon;
    private TextView title, description, nickname, views, likes, comments;
    private RecyclerView commentList;

    private String contentsId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contents);

        Intent intent = getIntent();
        contentsId = intent.getStringExtra("contentsId");
        viewModel = new ViewModelProvider(this).get(ContentsDetailViewModel.class);
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

        viewModel.getComments().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                comments.setText(integer.toString());
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        sendRequest();
    }

    private void sendRequest() {
        final Response.Listener<String> listener = new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(String response) {
                if (!AppHelper.checkError(ContentsActivity.this, response.trim()))
                    return;

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject contentsJson = jsonObject.getJSONObject("contents");

                    String contentsType = contentsJson.getString("contentsId").substring(0,2);
                    String extension = contentsJson.getString("extension");

                    Bundle bundle = new Bundle();
                    bundle.putString("contentsId", contentsId);
                    bundle.putString("extension", extension);
                    if (contentsType.equals("IM")) {
                        ImageFragment imageFragment = new ImageFragment();
                        imageFragment.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction().replace(R.id.condetail_fragmentContainer, imageFragment).commit();
                    } else if (contentsType.equals("SO")) {
                        SoundFragment soundFragment = new SoundFragment();
                        soundFragment.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction().replace(R.id.condetail_fragmentContainer, soundFragment).commit();
                    } else if (contentsType.equals("VI")) {
                        VideoFragment videoFragment = new VideoFragment();
                        videoFragment.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction().replace(R.id.condetail_fragmentContainer, videoFragment).commit();
                    } else if (contentsType.equals("TE")) {
                        TextFragment textFragment = new TextFragment();
                        textFragment.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction().replace(R.id.condetail_fragmentContainer, textFragment).commit();
                    } else {
                        AppHelper.checkError(ContentsActivity.this, AppHelper.CODE_ERROR);
                        return;
                    }
                    viewModel.getTitle().setValue(contentsJson.getString("title"));
                    viewModel.getDescription().setValue(contentsJson.getString("description"));
                    viewModel.getNickname().setValue(contentsJson.getString("nickname"));

                    byte[] decodedIcon = java.util.Base64.getDecoder().decode(contentsJson.getString("icon"));
                    Bitmap icon = BitmapFactory.decodeByteArray(decodedIcon, 0, decodedIcon.length);
                    viewModel.getUserIcon().setValue(icon);

                    viewModel.getViews().setValue(contentsJson.getInt("views"));
                    viewModel.getLikes().setValue(contentsJson.getInt("likes"));

                    if (contentsJson.getInt("comments") > 0) {
                        JSONArray commentJsonArr = jsonObject.getJSONArray("comment");
                        for (int i = 0; i < commentJsonArr.length(); i++) {
                            JSONObject js = commentJsonArr.getJSONObject(i);
                            byte[] _decodedIcon = java.util.Base64.getDecoder().decode(js.getString("icon"));
                            Bitmap _icon = BitmapFactory.decodeByteArray(_decodedIcon, 0, _decodedIcon.length);
                            commentArray.add(new Comment(
                                    js.getString("nickname"),
                                    _icon,
                                    js.getString("comment")
                            ));
                        }
                    }
                    adapter.notifyDataSetChanged();
                    viewModel.getComments().setValue(adapter.getItemCount());

                    loadView();
                } catch (Exception e) {
                    AppHelper.checkError(ContentsActivity.this, AppHelper.CODE_ERROR);
                    e.printStackTrace();
                }
            }
        };
        final Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AppHelper.checkError(ContentsActivity.this, AppHelper.RESPONSE_ERROR);
            }
        };
        try {
            final ContentsRequest request = new ContentsRequest(contentsId, listener, errorListener);
            JspHelper.addRequestQueue(this, request);
        } catch (Exception e) {
            AppHelper.checkError(this, AppHelper.CODE_ERROR);
            e.printStackTrace();
        }
    }

    private void loadView() {
        loading.setVisibility(View.GONE);
        view.setVisibility(View.VISIBLE);
    }
}
