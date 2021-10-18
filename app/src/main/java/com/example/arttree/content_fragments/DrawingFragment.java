package com.example.arttree.content_fragments;

;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.arttree.ContentDetailActivity;
import com.example.arttree.R;
import com.example.arttree.http.RetrofitClient;

public class DrawingFragment extends Fragment {

    private ImageView image;
    private ImageView hasNext;

    private String contentId, extensions;
    private String[] extension;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.content_fragment_drawing, container, false);
        Bundle bundle = getArguments();
        contentId = bundle.getString("contentId");
        extensions = bundle.getString("extension");
        extension = extensions.split("/");

        image = root.findViewById(R.id.contentDrawing_img);
        hasNext = root.findViewById(R.id.contentDrawing_img_hasnext);

        if (extension.length > 1) {
            hasNext.setVisibility(View.VISIBLE);
        }

        downloadImage();

        hasNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DrawingFragment.this.getActivity(), ContentDetailActivity.class);
                intent.putExtra("category", "drawing");
                intent.putExtra("contentId", contentId);
                intent.putExtra("extension", extension);
                startActivity(intent);
            }
        });

        return root;
    }

    private void downloadImage() {
        if (extension.length <= 1) {
            Glide.with(this).load(RetrofitClient.getContentUrl("drawing", contentId, extension[0])).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(image);
        } else {
            Glide.with(this).load(RetrofitClient.getContentUrl("drawing", contentId + "-0", extension[0])).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(image);
        }
    }
}