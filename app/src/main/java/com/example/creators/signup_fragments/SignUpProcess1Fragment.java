package com.example.creators.signup_fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.creators.R;
import com.example.creators.SignInActivity;
import com.example.creators.SignUpActivity;

import org.jetbrains.annotations.NotNull;

public class SignUpProcess1Fragment extends Fragment {
    private EditText emailPhone, code;
    private Button next;
    private TextView sendCode, verifyCode, email, phone, changeEmail, changePhone;
    private ImageView close, check;

    private int method = 0;
    private boolean isVerified = false;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.signup_process1, container, false);

        emailPhone = root.findViewById(R.id.signup_process1_edt_emailPhone);
        code = root.findViewById(R.id.signup_process1_edt_code);
        sendCode = root.findViewById(R.id.signup_process1_txt_sendCode);
        verifyCode = root.findViewById(R.id.signup_process1_txt_verifyCode);
        next = root.findViewById(R.id.signup_process1_btn_next);
        email = root.findViewById(R.id.signup_process1_txt_email);
        phone = root.findViewById(R.id.signup_process1_txt_phone);
        changeEmail = root.findViewById(R.id.signup_process1_txt_selectEmail);
        changePhone = root.findViewById(R.id.signup_process1_txt_selectPhone);
        close = root.findViewById(R.id.signup_process1_img_close);
        check = root.findViewById(R.id.signup_process1_img_check);

        emailPhone.setText(((SignUpActivity)getActivity()).emailPhone);

        changeEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                method = 0;
                email.setVisibility(View.GONE);
                changePhone.setVisibility(View.GONE);
                phone.setVisibility(View.VISIBLE);
                changeEmail.setVisibility(View.VISIBLE);
            }
        });

        changePhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                method = 1;
                email.setVisibility(View.VISIBLE);
                changePhone.setVisibility(View.VISIBLE);
                phone.setVisibility(View.GONE);
                changeEmail.setVisibility(View.GONE);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((SignUpActivity)SignUpProcess1Fragment.this.getActivity()).method = method;
                ((SignUpActivity)SignUpProcess1Fragment.this.getActivity()).emailPhone = emailPhone.getText().toString();
                ((SignUpActivity)SignUpProcess1Fragment.this.getActivity()).replaceFragmentToProcess2();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpProcess1Fragment.this.getActivity(), SignInActivity.class);
                startActivity(intent);
                SignUpProcess1Fragment.this.getActivity().finish();
            }
        });

        return root;
    }
}
