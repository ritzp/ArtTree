package com.example.arttree.main_fragments;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.arttree.MainActivity;
import com.example.arttree.R;

public class SearchFragment extends Fragment {

    private EditText search;
    private ImageView submit;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.main_fragment_search, container, false);

        search = root.findViewById(R.id.search_edt_search);
        submit = root.findViewById(R.id.search_img_submit);

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

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).replaceFragmentBySearch(search.getText().toString());
            }
        });

        return root;
    }
}