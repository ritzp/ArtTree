package com.example.creators.forgot_password_fragments;

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

public class ForgotPasswordProcess2Fragment extends Fragment {

    ImageView back;
    Button submit;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.forgot_password_process2, container, false);

        back = root.findViewById(R.id.forgot_process2_img_back);
        submit = root.findViewById(R.id.forgot_process2_btn_submit);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ForgotPasswordActivity)getActivity()).replaceFragmentToProcess1();
            }
        });

        return root;
    }
}
