package com.example.arttree.content_fragments.upload;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.arttree.R;

public class UploadDrawingFragment extends Fragment {

    ImageView img;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.upload_fragment_drawing, container, false);

        Uri uri = Uri.parse(getArguments().getString("uri"));

        img = root.findViewById(R.id.upload_drawing);

        img.setImageURI(uri);

        return root;
    }
}