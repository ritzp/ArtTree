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
import android.widget.Toast;

import com.example.creators.R;
import com.example.creators.SignInActivity;
import com.example.creators.SignUpActivity;

import org.jetbrains.annotations.NotNull;

public class SignUpProcess1Fragment extends Fragment {
    private EditText certification1, certification2;
    private Button sendbutton1, sendbutton2, next, phone;
    private ImageView close;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.signup_process1, container, false);

        certification1 = root.findViewById(R.id.certification1);
        certification2 = root.findViewById(R.id.certification2);
        sendbutton1 = root.findViewById(R.id.sendbutton1);
        sendbutton2 = root.findViewById(R.id.sendbutton2);
        next = root.findViewById(R.id.next1);
        phone = root.findViewById(R.id.phone1);
        close = root.findViewById(R.id.cancel);

        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
