package com.example.creators.forgot_password_fragments;

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

import com.example.creators.ForgotPasswordActivity;
import com.example.creators.R;
import com.example.creators.SignInActivity;

public class ForgotPasswordProcess1Fragment extends Fragment {

    ImageView close;
    Button next;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.forgot_password_process1, container, false);

        close = root.findViewById(R.id.forgot_process1_img_close);
        next = root.findViewById(R.id.forgot_process1_btn_next);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SignInActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ForgotPasswordActivity)getActivity()).replaceFragmentToProcess2();
            }
        });

        return root;
    }
}
