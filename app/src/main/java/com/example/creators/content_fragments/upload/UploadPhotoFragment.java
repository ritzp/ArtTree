package com.example.creators.content_fragments.upload;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.creators.R;

public class UploadPhotoFragment extends Fragment {

    ImageView img;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.upload_fragment_photo, container, false);

        Uri uri = Uri.parse(getArguments().getString("uri"));

        img = root.findViewById(R.id.upload_photo);

        img.setImageURI(uri);

        return root;
    }
}