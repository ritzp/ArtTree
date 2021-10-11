package com.example.creators.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.util.Log;

import com.squareup.picasso.Transformation;

public class ImageResize implements Transformation {
    public static Context context;

    @Override
    public Bitmap transform(Bitmap source) {
        if (context == null) {
            Log.e("Resize", "You did not set context");
            return source;
        }
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int width = source.getWidth(), height = source.getHeight();
        Bitmap result = source;
        double ratio;
        if (source.getWidth() >= source.getHeight()) {
            if (source.getWidth() > dm.widthPixels) {
                ratio = (double)dm.widthPixels / source.getWidth();
                width *= ratio;
                height *= ratio;
            }
            result = Bitmap.createScaledBitmap(source, width, height, false);
        } else {
            if (source.getHeight() > dm.widthPixels) {
                ratio = (double)dm.widthPixels / source.getHeight();
                width *= ratio;
                height *= ratio;
            }
            result = Bitmap.createScaledBitmap(source, width, height, false);
        }

        if (result != source) {
            source.recycle();
        }
        return result;
    }

    @Override
    public String key() {
        return "resizeTransformation#" + System.currentTimeMillis();
    }
}
