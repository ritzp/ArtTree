package com.example.creators.main_fragments;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.creators.MainActivity;
import com.example.creators.R;

public class SearchFragment extends Fragment {

    private EditText search;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.main_fragment_search, container, false);

        search = root.findViewById(R.id.search_edt_search);

        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (search.getText().length() <= 0) {
                    return false;
                }
                ((MainActivity)getActivity()).replaceFragmentBySearch(search.getText().toString());
                return true;
            }
        });

        return root;
    }
}