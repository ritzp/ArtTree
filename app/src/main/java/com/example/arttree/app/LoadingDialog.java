package com.example.arttree.app;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import androidx.appcompat.app.AppCompatDialog;

import com.example.arttree.R;

public class LoadingDialog extends AppCompatDialog {

    public LoadingDialog(Context context, int layout) {
        super(context);
        super.setCancelable(false);
        super.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        super.setContentView(layout);
    }

    @Override
    public void show() {
        super.show();
    }

    public void off() {
        if (super.isShowing()) {
            super.dismiss();
        }
    }
}
