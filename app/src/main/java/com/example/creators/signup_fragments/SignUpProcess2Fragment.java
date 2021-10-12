package com.example.creators.signup_fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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
    private EditText id, password, passwordConfirm, nickname;

    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.signup_process2, container, false);

        next = root.findViewById(R.id.signup_process2_btn_next);
        back = root.findViewById(R.id.signup_process2_img_back);
        id = root.findViewById(R.id.signup_process2_edt_id);
        password = root.findViewById(R.id.signup_process2_edt_pass);
        passwordConfirm = root.findViewById(R.id.signup_process2_edt_conPass);
        nickname = root.findViewById(R.id.signup_process2_edt_nickname);

        id.setText(((SignUpActivity)getActivity()).id);
        password.setText(((SignUpActivity)getActivity()).password);
        nickname.setText(((SignUpActivity)getActivity()).nickname);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (id.getText().length() <= 0) {
                    Toast.makeText(SignUpProcess2Fragment.this.getActivity(), getString(R.string.id_required), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.getText().length() <= 0) {
                    Toast.makeText(SignUpProcess2Fragment.this.getActivity(), getString(R.string.password_required), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (nickname.getText().length() <= 0) {
                    Toast.makeText(SignUpProcess2Fragment.this.getActivity(), getString(R.string.nickname_required), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!password.getText().toString().equals(passwordConfirm.getText().toString())) {
                    Toast.makeText(SignUpProcess2Fragment.this.getActivity(), getString(R.string.passwords_do_not_match), Toast.LENGTH_SHORT).show();
                    return;
                }

                ((SignUpActivity)SignUpProcess2Fragment.this.getActivity()).id = id.getText().toString();
                ((SignUpActivity)SignUpProcess2Fragment.this.getActivity()).password = password.getText().toString();
                ((SignUpActivity)SignUpProcess2Fragment.this.getActivity()).nickname = nickname.getText().toString();
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
