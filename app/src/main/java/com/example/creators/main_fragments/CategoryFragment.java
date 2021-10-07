package com.example.creators.main_fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.creators.MainActivity;
import com.example.creators.R;
import com.example.creators.adapters.CategoryAdapter;
import com.example.creators.adapters.OnItemClickListener;
import com.example.creators.viewmodels.CategoryViewModel;

public class CategoryFragment extends Fragment {

    private CategoryViewModel viewModel;
    private CategoryAdapter adapter;
    private RecyclerView categoryList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.main_fragment_category, container, false);

        adapter = new CategoryAdapter(viewModel.getCategoryArray());

        categoryList = root.findViewById(R.id.category_categoryList);

        categoryList.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        categoryList.setAdapter(adapter);

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int pos) {
                ((MainActivity)getActivity()).replaceFragmentByList(((TextView)view.findViewById(R.id.category_txt_categoryName)).getText().toString());
            }
        });

        return root;
    }
}