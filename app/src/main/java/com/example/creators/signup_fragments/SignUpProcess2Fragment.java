package com.example.creators.signup_fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.creators.R;
import com.example.creators.SignInActivity;
import com.example.creators.SignUpActivity;

import org.jetbrains.annotations.NotNull;

public class SignUpProcess2Fragment extends Fragment {

    private Button next;
    private ImageView back;

    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.signup_process2, container, false);

        next = root.findViewById(R.id.signup_process2_next);
        back = root.findViewById(R.id.signup_process2_back);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((SignUpActivity)SignUpProcess2Fragment.this.getActivity()).replaceFragmentToProcess3();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((SignUpActivity)SignUpProcess2Fragment.this.getActivity()).replaceFragmentToProcess1();
            }
        });

        return root;
    }
}
