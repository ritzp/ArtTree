package com.example.creators.content_fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.creators.R;
import com.example.creators.app.ImageResize;
import com.example.creators.http.RetrofitClient;
import com.squareup.picasso.Picasso;

public class PhotoFragment extends Fragment {

    private ImageView image;

    private String contentId, extension;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.content_fragment_photo, container, false);
        Bundle bundle = getArguments();
        contentId = bundle.getString("contentId");
        extension = bundle.getString("extension");

        image = root.findViewById(R.id.condetailPhoto_img);

        downloadImage();

        return root;
    }

    private void downloadImage() {
        ImageResize.context = getContext();
        Picasso.get().load(RetrofitClient.getContentUrl("photo", contentId, extension)).transform(new ImageResize()).into(image);
    }
}