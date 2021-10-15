package com.example.creators.content_fragments;

;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.creators.ContentDetailActivity;
import com.example.creators.R;
import com.example.creators.app.ImageResize;
import com.example.creators.http.RetrofitClient;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

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
        ImageResize.context = getActivity();
        if (extension.length <= 1) {
            Picasso.get().load(RetrofitClient.getContentUrl("drawing", contentId, extension[0])).transform(new ImageResize()).into(image);
        } else {
            Picasso.get().load(RetrofitClient.getContentUrl("drawing", contentId + "-0", extension[0])).networkPolicy(NetworkPolicy.NO_CACHE).transform(new ImageResize()).into(image);
        }
    }
}