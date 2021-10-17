package com.example.creators.main_fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.creators.MainActivity;
import com.example.creators.R;
import com.example.creators.adapters.CategoriesAdapter;
import com.example.creators.adapters.OnItemClickListener;
import com.example.creators.classes.Category;

import java.util.ArrayList;

public class CategoriesFragment extends Fragment {

    public static Context context;

    private ArrayList<Category> categoryArray;
    private CategoriesAdapter adapter;
    private GridView categoryList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        final int[] imgs = {R.drawable.pic_photo, R.drawable.pic_drawing, R.drawable.pic_music, R.drawable.pic_video, R.drawable.pic_cartoon, R.drawable.pic_novel};
        final String[] txts = {CategoriesFragment.context.getString(R.string.photo), CategoriesFragment.context.getString(R.string.drawing),
                CategoriesFragment.context.getString(R.string.music),CategoriesFragment.context.getString(R.string.video),
                CategoriesFragment.context.getString(R.string.cartoon), CategoriesFragment.context.getString(R.string.novel)};
        categoryArray = new ArrayList<>();
        for (int i=0; i<imgs.length; i++) {
            categoryArray.add(new Category(imgs[i], txts[i]));
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.main_fragment_categories, container, false);

        adapter = new CategoriesAdapter(categoryArray);

        categoryList = root.findViewById(R.id.categoies_categoryList);

        categoryList.setAdapter(adapter);

        final String[] categories = {getActivity().getString(R.string.photo), getActivity().getString(R.string.drawing),
                getActivity().getString(R.string.music), getActivity().getString(R.string.video),
                getActivity().getString(R.string.cartoon), getActivity().getString(R.string.novel)};
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int pos) {
                ((MainActivity)getActivity()).replaceFragmentByList(categories[pos]);
            }
        });

        return root;
    }
}