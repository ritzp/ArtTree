package com.example.creators.contents_fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.creators.R;
import com.example.creators.jsp.JspHelper;

public class ImageFragment extends Fragment {

    private NetworkImageView image;

    private String contentsId, extension;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.contents_fragment_image, container, false);
        Bundle bundle = getArguments();
        contentsId = bundle.getString("contentsId");
        extension = bundle.getString("extension");

        image = root.findViewById(R.id.condetailImg_img);

        image.setImageUrl(JspHelper.getImageURL(contentsId, extension), JspHelper.getImageLoader());

        return root;
    }
}