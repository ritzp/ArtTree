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

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.creators.contents_fragments.ImageFragment;
import com.example.creators.jsp.JspHelper;
import com.example.creators.jsp.requests.ContentsDetailRequest;
import com.example.creators.viewmodels.ContentsDetailViewModel;

import org.json.JSONException;
import org.json.JSONObject;

public class ContentsDetailActivity extends FragmentActivity {

    private ImageFragment imageFragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private ContentsDetailViewModel viewModel;
    private ImageView close, icon;
    private TextView title, description, nickname;

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

        close = findViewById(R.id.condetail_img_close);
        title = findViewById(R.id.condetail_txt_title);
        description = findViewById(R.id.condetail_txt_desc);
        nickname = findViewById(R.id.condetail_txt_nickname);
        icon = findViewById(R.id.condetail_img_icon);

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
                    viewModel.getTitle().setValue(jsonObject.getString("title"));
                    viewModel.getDescription().setValue(jsonObject.getString("description"));
                    viewModel.getNickname().setValue(jsonObject.getString("nickname"));

                    byte[] decodedIcon = java.util.Base64.getDecoder().decode(jsonObject.getString("icon"));
                    Bitmap icon = BitmapFactory.decodeByteArray(decodedIcon, 0, decodedIcon.length);
                    viewModel.getUserIcon().setValue(icon);
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
