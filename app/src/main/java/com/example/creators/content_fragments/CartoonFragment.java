package com.example.creators.content_fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.creators.ContentDetailActivity;
import com.example.creators.R;
import com.example.creators.app.ImageResize;
import com.example.creators.http.RetrofitClient;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

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
        ImageResize.context = getActivity();
        if (extension.length <= 1) {
            Picasso.get().load(RetrofitClient.getContentUrl("cartoon", contentId, extension[0])).networkPolicy(NetworkPolicy.NO_CACHE).transform(new ImageResize()).into(image);
        } else {
            Picasso.get().load(RetrofitClient.getContentUrl("cartoon", contentId + "-0", extension[0])).networkPolicy(NetworkPolicy.NO_CACHE).transform(new ImageResize()).into(image);
        }
    }
}