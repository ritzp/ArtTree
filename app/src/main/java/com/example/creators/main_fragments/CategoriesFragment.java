package com.example.creators.main_fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.creators.MainActivity;
import com.example.creators.R;
import com.example.creators.adapters.CategoriesAdapter;
import com.example.creators.adapters.OnItemClickListener;
import com.example.creators.viewmodels.CategoriesViewModel;

public class CategoriesFragment extends Fragment {

    public static Context context;

    private CategoriesViewModel viewModel;
    private CategoriesAdapter adapter;
    private GridView categoryList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        viewModel = new ViewModelProvider(this).get(CategoriesViewModel.class);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.main_fragment_categories, container, false);

        adapter = new CategoriesAdapter(viewModel.getCategoryArray());

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