package com.example.arttree;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.example.arttree.app.AppHelper;

public class UploadTextEditActivity extends Activity {

    private EditText edt;
    private ImageView close;
    private Button submit;

    private String text;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_textedit);

        Intent inIntent = getIntent();
        text = inIntent.getStringExtra("text");

        edt = findViewById(R.id.upload_edt_textContent);
        close = findViewById(R.id.upload_img_textClose);
        submit = findViewById(R.id.upload_btn_submitText);

        edt.setText(text);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((UploadActivity)UploadActivity.context).textContent.setText(edt.getText().toString());
                finish();
            }
        });
    }
}
