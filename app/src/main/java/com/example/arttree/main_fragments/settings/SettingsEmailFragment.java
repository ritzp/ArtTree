package com.example.arttree.main_fragments.settings;

import android.content.DialogInterface;
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
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.arttree.ContentActivity;
import com.example.arttree.MainActivity;
import com.example.arttree.R;
import com.example.arttree.SignInActivity;
import com.example.arttree.SignUpActivity;
import com.example.arttree.app.AppHelper;
import com.example.arttree.app.LoadingDialog;
import com.example.arttree.http.ApiInterface;
import com.example.arttree.http.RetrofitClient;
import com.example.arttree.signup_fragments.SignUpProcess1Fragment;
import com.example.arttree.smtp.MailSender;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsEmailFragment extends Fragment {
    private ApiInterface api;

    private ImageView back, check;
    private EditText emailEdt, codeEdt;
    private Button submit, sendCode, verifyCode;

    private String code;
    private boolean isVerified = false;

    private LoadingDialog loadingDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.settings_email, container, false);

        back = root.findViewById(R.id.settingsemail_img_back);
        check = root.findViewById(R.id.settingsemail_img_check);
        emailEdt = root.findViewById(R.id.settingsemail_edt_email);
        codeEdt = root.findViewById(R.id.settingsemail_edt_code);
        submit = root.findViewById(R.id.settingsemail_btn_submit);
        sendCode = root.findViewById(R.id.settingsemail_btn_sendCode);
        verifyCode = root.findViewById(R.id.settingsemail_btn_verifyCode);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).replaceFragmentToSettingsMain();
            }
        });

        sendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (emailEdt.getText().length() <= 0) {
                    Toast.makeText(getActivity(), R.string.enter_email, Toast.LENGTH_SHORT).show();
                    return;
                }
                sendEmailRequest();
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

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isVerified) {
                    loadingDialog = new LoadingDialog(getActivity(), R.layout.alert_loading);
                    loadingDialog.show();
                    sendRequest();
                } else {
                    Toast.makeText(getActivity(), R.string.check_verification, Toast.LENGTH_SHORT).show();
                }
            }
        });

        return root;
    }

    private void sendRequest() {
        api = RetrofitClient.getRetrofit().create(ApiInterface.class);
        Call<String> call = api.postChangeEmail(AppHelper.getAccessingUserid(), emailEdt.getText().toString());

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (!AppHelper.checkError(SettingsEmailFragment.this.getActivity(), response.body()))
                    return;

                if (response.body().equals("SUCCESS")) {
                    Toast.makeText(SettingsEmailFragment.this.getActivity(), getString(R.string.settings_completed), Toast.LENGTH_SHORT).show();
                    loadingDialog.off();
                    ((MainActivity)getActivity()).replaceFragmentToSettingsMain();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                AppHelper.checkError(SettingsEmailFragment.this.getActivity(), AppHelper.RESPONSE_ERROR);
                t.printStackTrace();
                loadingDialog.off();
            }
        });
    }

    private void sendEmailRequest() {
        api = RetrofitClient.getRetrofit().create(ApiInterface.class);
        Call<String> call = api.postCheckUserId(emailEdt.getText().toString());

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                if (response.body().equals("EXISTS")) {
                    Toast.makeText(SettingsEmailFragment.this.getActivity(), getString(R.string.id_already_exists), Toast.LENGTH_SHORT).show();
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
