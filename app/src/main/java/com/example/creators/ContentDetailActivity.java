package com.example.creators;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.creators.adapters.ContentDetailImageAdapter;

public class ContentDetailActivity extends Activity {

    private String contentId;

    private ContentDetailImageAdapter adapter;

    private RecyclerView imgRecycler;
    private TextView textView;
    private View textContainer;
    private ImageView close;

    public static Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_detail);
        context = this;

        imgRecycler = findViewById(R.id.contentdetail_imgList);
        textView = findViewById(R.id.contentdetail_txt_txt);
        textContainer = findViewById(R.id.contentdetail_txtContainer);
        close = findViewById(R.id.contentdetail_img_close);

        Intent intent = getIntent();
        contentId = intent.getStringExtra("contentId");

        if (contentId.startsWith("NO")) {
            textView.setText(intent.getStringExtra("text"));
            textContainer.setVisibility(View.VISIBLE);
        } else {
            adapter = new ContentDetailImageAdapter(intent.getStringExtra("category"), contentId, intent.getStringArrayExtra("extension"));
            imgRecycler.setLayoutManager(new LinearLayoutManager(this));
            imgRecycler.setAdapter(adapter);
            imgRecycler.setVisibility(View.VISIBLE);
        }

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
