package com.example.creators.content_fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.creators.ContentDetailActivity;
import com.example.creators.R;
import com.example.creators.http.RetrofitClient;

public class CartoonFragment extends Fragment {

    private ImageView image;
    private ImageView hasNext;

    private String contentId, extensions;
    private String[] extension;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.content_fragment_cartoon, container, false);
        Bundle bundle = getArguments();
        contentId = bundle.getString("contentId");
        extensions = bundle.getString("extension");
        extension = extensions.split("/");

        image = root.findViewById(R.id.contentCartoon_img);
        hasNext = root.findViewById(R.id.contentCartoon_img_hasnext);

        if (extension.length > 1) {
            hasNext.setVisibility(View.VISIBLE);
        }

        downloadImage();

        hasNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartoonFragment.this.getActivity(), ContentDetailActivity.class);
                intent.putExtra("category", "cartoon");
                intent.putExtra("contentId", contentId);
                intent.putExtra("extension", extension);
                startActivity(intent);
            }
        });

        return root;
    }

    private void downloadImage() {
        if (extension.length <= 1) {
            Glide.with(this).load(RetrofitClient.getContentUrl("cartoon", contentId, extension[0])).into(image);
        } else {
            Glide.with(this).load(RetrofitClient.getContentUrl("cartoon", contentId + "-0", extension[0])).into(image);
        }
    }
}