package com.example.creators;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.creators.adapters.CommentAdapter;
import com.example.creators.classes.Comment;
import com.example.creators.contents_fragments.ImageFragment;
import com.example.creators.jsp.JspHelper;
import com.example.creators.jsp.requests.ContentsDetailRequest;
import com.example.creators.viewmodels.ContentsDetailViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ContentsDetailActivity extends FragmentActivity {

    private ImageFragment imageFragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private ArrayList<Comment> commentArray;
    private CommentAdapter adapter;

    private ContentsDetailViewModel viewModel;
    private ImageView close, icon;
    private TextView title, description, nickname, comments;
    private RecyclerView commentList;

    private String contentsId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contents_detail);

        Intent intent = getIntent();
        contentsId = intent.getStringExtra("contentsId");
        viewModel = new ViewModelProvider(this).get(ContentsDetailViewModel.class);
        imageFragment = new ImageFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.condetail_fragmentContainer, imageFragment).commit();

        commentArray = new ArrayList<>();
        adapter = new CommentAdapter(commentArray);

        close = findViewById(R.id.condetail_img_close);
        title = findViewById(R.id.condetail_txt_title);
        description = findViewById(R.id.condetail_txt_desc);
        nickname = findViewById(R.id.condetail_txt_nickname);
        icon = findViewById(R.id.condetail_img_icon);
        comments = findViewById(R.id.condetail_txt_comments);
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
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject contentsJson = jsonObject.getJSONObject("contents");
                    viewModel.getTitle().setValue(contentsJson.getString("title"));
                    viewModel.getDescription().setValue(contentsJson.getString("description"));
                    viewModel.getNickname().setValue(contentsJson.getString("nickname"));

                    byte[] decodedIcon = java.util.Base64.getDecoder().decode(contentsJson.getString("icon"));
                    Bitmap icon = BitmapFactory.decodeByteArray(decodedIcon, 0, decodedIcon.length);
                    viewModel.getUserIcon().setValue(icon);

                    JSONArray commentJsonArr = jsonObject.getJSONArray("comment");
                    for (int index=0; index<commentJsonArr.length(); index++) {
                        JSONObject js = commentJsonArr.getJSONObject(index);
                        byte[] _decodedIcon = java.util.Base64.getDecoder().decode(js.getString("icon"));
                        Bitmap _icon = BitmapFactory.decodeByteArray(_decodedIcon, 0, _decodedIcon.length);
                        commentArray.add(new Comment(
                                js.getString("nickname"),
                                _icon,
                                js.getString("comment")
                        ));
                    }
                    adapter.notifyDataSetChanged();
                    viewModel.getComments().setValue(adapter.getItemCount());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        final Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                JspHelper.checkMessage(ContentsDetailActivity.this, JspHelper.SERVER_ERROR);
            }
        };
        final ContentsDetailRequest request = new ContentsDetailRequest(contentsId, listener, errorListener);
        JspHelper.addRequestQueue(this, request);
    }
}
