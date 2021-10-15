package com.example.creators.signup_fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.AsyncTask;
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
import com.example.creators.app.AppHelper;
import com.example.creators.http.ApiInterface;
import com.example.creators.http.RetrofitClient;
import com.example.creators.smtp.MailSender;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;

public class SignUpProcess1Fragment extends Fragment {

    private ApiInterface api;

    private EditText emailEdt, codeEdt;
    private Button next;
    private TextView sendCode, verifyCode, email;
    private ImageView close, check;

    private int method = 0;
    private String code;
    private boolean isVerified = false;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.signup_process1, container, false);

        emailEdt = root.findViewById(R.id.signup_process1_edt_email);
        codeEdt = root.findViewById(R.id.signup_process1_edt_code);
        sendCode = root.findViewById(R.id.signup_process1_txt_sendCode);
        verifyCode = root.findViewById(R.id.signup_process1_txt_verifyCode);
        next = root.findViewById(R.id.signup_process1_btn_next);
        email = root.findViewById(R.id.signup_process1_txt_email);
        close = root.findViewById(R.id.signup_process1_img_close);
        check = root.findViewById(R.id.signup_process1_img_check);

        emailEdt.setText(((SignUpActivity)getActivity()).emailPhone);
        sendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (emailEdt.getText().length() <= 0) {
                    Toast.makeText(getActivity(), R.string.enter_emailPhone, Toast.LENGTH_SHORT).show();
                    return;
                }
                sendRequest();
            }
        });

        verifyCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (codeEdt.getText().toString().equals(code)) {
                    check.setVisibility(View.VISIBLE);
                    isVerified = true;
                } else {
                    Toast.makeText(getActivity(), R.string.do_not_match_code, Toast.LENGTH_SHORT).show();
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isVerified) {
                    ((SignUpActivity)SignUpProcess1Fragment.this.getActivity()).method = method;
                    ((SignUpActivity)SignUpProcess1Fragment.this.getActivity()).emailPhone = emailEdt.getText().toString();
                    ((SignUpActivity)SignUpProcess1Fragment.this.getActivity()).replaceFragmentToProcess2();
                } else {
                    Toast.makeText(getActivity(), R.string.check_verification, Toast.LENGTH_SHORT).show();
                }
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

    private void sendRequest() {
        api = RetrofitClient.getRetrofit().create(ApiInterface.class);
        Call<String> call = api.postCheckUserId(emailEdt.getText().toString());

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                if (response.body().equals("EXISTS")) {
                    Toast.makeText(SignUpProcess1Fragment.this.getActivity(), getString(R.string.id_already_exists), Toast.LENGTH_SHORT).show();
                    return;
                }

                new AsyncTask<Void, Void, String>() {
                    @Override
                    protected String doInBackground(Void... voids) {
                        String message = null;
                        try {
                            MailSender mailSender = new MailSender("kzd1322@tw.ac.kr", "!kzd12322");
                            code = mailSender.createEmailCode();
                            mailSender.sendMail(getString(R.string.app_name) + " - Verification Code", "Code: " + code, emailEdt.getText().toString());
                            message = getString(R.string.sent_code);
                        } catch (Exception e) {
                            message = getString(R.string.could_not_send_code);
                            e.printStackTrace();
                        }
                        return message;
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
                        super.onPostExecute(s);
                    }
                }.execute();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                AppHelper.checkError(getActivity(), AppHelper.RESPONSE_ERROR);
                t.printStackTrace();
            }
        });
    }
}
