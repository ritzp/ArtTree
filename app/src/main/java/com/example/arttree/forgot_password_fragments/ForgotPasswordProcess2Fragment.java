package com.example.arttree.forgot_password_fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.example.arttree.app.LoadingDialog;
import com.example.arttree.app.RegExp;
import com.example.arttree.http.ApiInterface;
import com.example.arttree.main_fragments.settings.SettingsPasswordFragment;

import retrofit2.Call;
import retrofit2.Callback;

public class ForgotPasswordProcess2Fragment extends Fragment {

    private ApiInterface api;

    private EditText et_changepw, et_changepwcheck;
    private ImageView back;
    private Button submit;

    private LoadingDialog loadingDialog;
    private RegExp regExp;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.forgot_password_process2, container, false);

        regExp = new RegExp();

        back = root.findViewById(R.id.forgot_process2_img_back);
        submit = root.findViewById(R.id.forgot_process2_btn_submit);
        et_changepw = root.findViewById(R.id.forgot_process2_edt_newPass);
        et_changepwcheck = root.findViewById(R.id.forgot_process2_edt_newPassCon);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ForgotPasswordActivity)getActivity()).replaceFragmentToProcess1();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!regExp.isPasswordMatches(et_changepw.getText().toString())) {
                    Toast.makeText(getActivity(), getString(R.string.password_not_matches_regex), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!et_changepw.getText().toString().equals(et_changepwcheck.getText().toString())) {
                    Toast.makeText(ForgotPasswordProcess2Fragment.this.getActivity(), getString(R.string.passwords_do_not_match), Toast.LENGTH_SHORT).show();
                    return;
                }

                loadingDialog = new LoadingDialog(getActivity(), R.layout.alert_loading);
                loadingDialog.show();
                sendRequest();
            }
        });

        return root;
    }

    private void sendRequest() {
        api = com.example.arttree.http.RetrofitClient.getRetrofit().create(com.example.arttree.http.ApiInterface.class);
        Call<String> call = api.postForgotPassword(((ForgotPasswordActivity)getActivity()).email, et_changepw.getText().toString());

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {

                if (response.body().startsWith("SUCCESS")) {
                    try {
                        if (Integer.parseInt(response.body().substring(7)) > 0) {
                            Toast.makeText(ForgotPasswordProcess2Fragment.this.getActivity(),getString(R.string.password_changed_successfully), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ForgotPasswordProcess2Fragment.this.getActivity(), SignInActivity.class);
                            startActivity(intent);
                            ForgotPasswordProcess2Fragment.this.getActivity().finish();
                        } else {
                            Toast.makeText(ForgotPasswordProcess2Fragment.this.getActivity(),getString(R.string.password_change_failed), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        AppHelper.checkError(ForgotPasswordProcess2Fragment.this.getActivity(), AppHelper.SERVER_ERROR);
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(ForgotPasswordProcess2Fragment.this.getActivity(), getString(R.string.password_change_failed), Toast.LENGTH_SHORT).show();
                }
                loadingDialog.off();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                AppHelper.checkError(ForgotPasswordProcess2Fragment.this.getActivity(), AppHelper.RESPONSE_ERROR);
                t.printStackTrace();
                loadingDialog.off();
            }
        });
    }
}
