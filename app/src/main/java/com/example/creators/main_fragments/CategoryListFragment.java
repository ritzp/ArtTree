package com.example.creators.main_fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.creators.MainActivity;
import com.example.creators.R;
import com.example.creators.adapters.CategoryListAdapter;
import com.example.creators.adapters.OnItemClickListener;
import com.example.creators.viewmodels.CategoryListViewModel;

public class CategoryListFragment extends Fragment {

    private CategoryListViewModel viewModel;
    private CategoryListAdapter imageAdapter, soundAdapter, videoAdapter, textAdapter;
    private RecyclerView imageList, soundList, videoList, textList;
    private ImageView imageListSwitch, soundListSwitch, videoListSwitch, textListSwitch;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(CategoryListViewModel.class);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.main_fragment_categorylist, container, false);

        imageAdapter = new CategoryListAdapter(viewModel.getImageCategories());
        soundAdapter = new CategoryListAdapter(viewModel.getSoundCategories());
        videoAdapter = new CategoryListAdapter(viewModel.getVideoCategories());
        textAdapter = new CategoryListAdapter(viewModel.getTextCategories());

        imageList = root.findViewById(R.id.list_img);
        soundList = root.findViewById(R.id.list_sound);
        videoList = root.findViewById(R.id.list_video);
        textList = root.findViewById(R.id.list_text);
        imageListSwitch = root.findViewById(R.id.img_imgListSwitch);
        soundListSwitch = root.findViewById(R.id.img_soundListSwitch);
        videoListSwitch = root.findViewById(R.id.img_videoListSwitch);
        textListSwitch = root.findViewById(R.id.img_textListSwitch);

        imageList.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        soundList.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        videoList.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        textList.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        imageList.setAdapter(imageAdapter);
        soundList.setAdapter(soundAdapter);
        videoList.setAdapter(videoAdapter);
        textList.setAdapter(textAdapter);

        imageListSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageList.getVisibility() == View.GONE) {
                    imageListSwitch.setImageResource(R.drawable.ic_up);
                    imageList.setVisibility(View.VISIBLE);
                } else {
                    imageListSwitch.setImageResource(R.drawable.ic_down);
                    imageList.setVisibility(View.GONE);
                }
            }
        });

        soundListSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (soundList.getVisibility() == View.GONE) {
                    soundListSwitch.setImageResource(R.drawable.ic_up);
                    soundList.setVisibility(View.VISIBLE);
                } else {
                    soundListSwitch.setImageResource(R.drawable.ic_down);
                    soundList.setVisibility(View.GONE);
                }
            }
        });

        videoListSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (videoList.getVisibility() == View.GONE) {
                    videoListSwitch.setImageResource(R.drawable.ic_up);
                    videoList.setVisibility(View.VISIBLE);
                } else {
                    videoListSwitch.setImageResource(R.drawable.ic_down);
                    videoList.setVisibility(View.GONE);
                }
            }
        });

        textListSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textList.getVisibility() == View.GONE) {
                    textListSwitch.setImageResource(R.drawable.ic_up);
                    textList.setVisibility(View.VISIBLE);
                } else {
                    textListSwitch.setImageResource(R.drawable.ic_down);
                    textList.setVisibility(View.GONE);
                }
            }
        });

        imageAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int pos) {
                ((MainActivity)getActivity()).replaceFragmentByList(((TextView)view.findViewById(R.id.txt_categoryName)).getText().toString());
            }
        });

        soundAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int pos) {
                ((MainActivity)getActivity()).replaceFragmentByList(((TextView)view.findViewById(R.id.txt_categoryName)).getText().toString());
            }
        });

        videoAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int pos) {
                ((MainActivity)getActivity()).replaceFragmentByList(((TextView)view.findViewById(R.id.txt_categoryName)).getText().toString());
            }
        });

        textAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int pos) {
                ((MainActivity)getActivity()).replaceFragmentByList(((TextView)view.findViewById(R.id.txt_categoryName)).getText().toString());
            }
        });

        return root;
    }
}