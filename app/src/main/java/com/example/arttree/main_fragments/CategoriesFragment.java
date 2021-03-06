package com.example.arttree.main_fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.arttree.MainActivity;
import com.example.arttree.R;
import com.example.arttree.adapters.CategoriesAdapter;
import com.example.arttree.adapters.OnItemClickListener;
import com.example.arttree.classes.Category;

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

        final Category[] categories = {
                new Category(R.drawable.ic_photo, "Photo"),
                new Category(R.drawable.ic_drawing, "Drawing"),
                new Category(R.drawable.ic_music, "Music"),
                new Category(R.drawable.ic_video, "Video"),
                new Category(R.drawable.ic_cartoon, "Cartoon"),
                new Category(R.drawable.ic_novel, "Novel")
        };
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int pos) {
                ((MainActivity)getActivity()).replaceFragmentByList(categories[pos]);
            }
        });

        return root;
    }
}