package com.example.arttree.forgot_password_fragments;

import android.content.Intent;
import android.os.AsyncTask;
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

import com.example.arttree.ForgotPasswordActivity;
import com.example.arttree.R;
import com.example.arttree.SignInActivity;
import com.example.arttree.app.AppHelper;
import com.example.arttree.http.ApiInterface;
import com.example.arttree.main_fragments.settings.SettingsPasswordFragment;
import com.example.arttree.smtp.MailSender;

import retrofit2.Call;
import retrofit2.Callback;

public class ForgotPasswordProcess1Fragment extends Fragment {

    private ApiInterface api;

    private EditText et_email, et_code;
    private ImageView close, check;
    private Button next, sendCode, verifyCode;

    private String code;
    private boolean isVerified = false;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.forgot_password_process1, container, false);

        et_email = root.findViewById(R.id.forgot_process1_edt_email);
        et_code = root.findViewById(R.id.forgot_process1_edt_code);
        close = root.findViewById(R.id.forgot_process1_img_close);
        check = root.findViewById(R.id.forgot_process1_img_check);
        next = root.findViewById(R.id.forgot_process1_btn_next);
        sendCode = root.findViewById(R.id.forgot_process1_btn_sendCode);
        verifyCode = root.findViewById(R.id.forgot_process1_btn_verifyCode);

        et_email.setText(((ForgotPasswordActivity)getActivity()).email);

        sendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest();
            }
        });


        verifyCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_code.getText().toString().equals(code)) {
                    check.setVisibility(View.VISIBLE);
                    isVerified = true;
                } else {
                    Toast.makeText(getActivity(), R.string.do_not_match_code, Toast.LENGTH_SHORT).show();
                }
            }
        });

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
                if (isVerified) {
                    ((ForgotPasswordActivity)getActivity()).email = et_email.getText().toString();
                    ((ForgotPasswordActivity)getActivity()).replaceFragmentToProcess2();
                } else {
                    Toast.makeText(getActivity(), R.string.check_verification, Toast.LENGTH_SHORT).show();
                }
            }
        });

        return root;
    }

    private void sendRequest() {
        api = com.example.arttree.http.RetrofitClient.getRetrofit().create(com.example.arttree.http.ApiInterface.class);
        Call<String> call = api.postCheckUserId(et_email.getText().toString());

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                if (!response.body().equals("EXISTS")) {
                    Toast.makeText(ForgotPasswordProcess1Fragment.this.getActivity(), getString(R.string.email_invalid), Toast.LENGTH_SHORT).show();
                    return;
                }

                new AsyncTask<Void, Void, String>() {
                    @Override
                    protected String doInBackground(Void... voids) {
                        String message = null;
                        try {
                            MailSender mailSender = new MailSender("kzd1322@tw.ac.kr", "!kzd12322");
                            code = mailSender.createEmailCode();
                            mailSender.sendMail(getString(R.string.app_name) + " - Verification Code", "Code: " + code, et_email.getText().toString());
                            message = getString(R.string.sent_code);
                        } catch (Exception e) {
                            message = getString(R.string.could_not_send_code);
                            e.printStackTrace();
                        }
                        return message;
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        Toast.makeText(ForgotPasswordProcess1Fragment.this.getActivity(), s, Toast.LENGTH_SHORT).show();
                        super.onPostExecute(s);
                    }
                }.execute();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                AppHelper.checkError(ForgotPasswordProcess1Fragment.this.getActivity(), AppHelper.RESPONSE_ERROR);
                t.printStackTrace();
            }
        });
    }
}
