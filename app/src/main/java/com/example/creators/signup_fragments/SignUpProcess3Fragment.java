package com.example.creators.signup_fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.creators.R;
import com.example.creators.SignUpActivity;

import org.jetbrains.annotations.NotNull;

public class SignUpProcess3Fragment extends Fragment {

    private EditText introduction;
    private Button next;
    private ImageView back;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.signup_process3, container, false);

        next = root.findViewById(R.id.signup_process3_btn_next);
        back = root.findViewById(R.id.signup_process3_img_back);
        introduction = root.findViewById(R.id.signup_process3_edt_intro);

        introduction.setText(((SignUpActivity)getActivity()).introduction);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((SignUpActivity)getActivity()).introduction = introduction.getText().toString();
                ((SignUpActivity)SignUpProcess3Fragment.this.getActivity()).replaceFragmentToProcess4();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((SignUpActivity)SignUpProcess3Fragment.this.getActivity()).replaceFragmentToProcess2();
            }
        });

        return root;
    }
}
