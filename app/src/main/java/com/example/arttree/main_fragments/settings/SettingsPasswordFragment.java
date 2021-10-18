package com.example.arttree.main_fragments.settings;

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

import com.example.arttree.MainActivity;
import com.example.arttree.R;
import com.example.arttree.app.AppHelper;
import com.example.arttree.app.LoadingDialog;
import com.example.arttree.app.RegExp;
import com.example.arttree.http.ApiInterface;
import com.example.arttree.http.RetrofitClient;
import com.example.arttree.http.response.SignInResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsPasswordFragment extends Fragment {
    private ApiInterface api;

    private ImageView back;
    private EditText password, newPassword, newPasswordConfirm;
    private Button submit;

    private LoadingDialog loadingDialog;
    private RegExp regExp;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.settings_password, container, false);

        back = root.findViewById(R.id.settingspass_img_back);
        password = root.findViewById(R.id.settingspass_edt_pass);
        newPassword = root.findViewById(R.id.settingspass_edt_newPass);
        newPasswordConfirm = root.findViewById(R.id.settingspass_edt_newPassCon);
        submit = root.findViewById(R.id.settingspass_btn_submit);

        regExp = new RegExp();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).replaceFragmentToSettingsMain();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!regExp.isPasswordMatches(password.getText().toString())) {
                    Toast.makeText(getActivity(), getString(R.string.password_not_matches_regex), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!newPassword.getText().toString().equals(newPasswordConfirm.getText().toString())) {
                    Toast.makeText(SettingsPasswordFragment.this.getActivity(), getString(R.string.passwords_do_not_match), Toast.LENGTH_SHORT).show();
                    return;
                }

                loadingDialog = new LoadingDialog(getActivity(), R.layout.alert_loading);
                loadingDialog.show();
                sendCheckRequest();
            }
        });

        return root;
    }

    private void sendCheckRequest() {
        api = RetrofitClient.getRetrofit().create(ApiInterface.class);
        Call<SignInResponse> call = api.postSignIn(AppHelper.getAccessingUserid(), password.getText().toString());

        call.enqueue(new Callback<SignInResponse>() {
            @Override
            public void onResponse(Call<SignInResponse> call, retrofit2.Response<SignInResponse> response) {
                if (response.body().getMessage().equals("FAILED")) {
                    Toast.makeText(SettingsPasswordFragment.this.getActivity(), getString(R.string.password_invalid), Toast.LENGTH_SHORT).show();
                    loadingDialog.off();
                } else {
                    sendRequest();
                }
            }

            @Override
            public void onFailure(Call<SignInResponse> call, Throwable t) {
                AppHelper.checkError(SettingsPasswordFragment.this.getActivity(), AppHelper.RESPONSE_ERROR);
                t.printStackTrace();
                loadingDialog.off();
            }
        });
    }

    private void sendRequest() {
        api = RetrofitClient.getRetrofit().create(ApiInterface.class);
        Call<String> call = api.postChangePassword(AppHelper.getAccessingUserid(), newPassword.getText().toString());

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (!AppHelper.checkError(SettingsPasswordFragment.this.getActivity(), response.body())) {
                    loadingDialog.off();
                    return;
                }

                if (response.body().equals("SUCCESS")) {
                    Toast.makeText(SettingsPasswordFragment.this.getActivity(), getString(R.string.settings_completed), Toast.LENGTH_SHORT).show();
                    loadingDialog.off();
                    ((MainActivity)getActivity()).replaceFragmentToSettingsMain();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                AppHelper.checkError(SettingsPasswordFragment.this.getActivity(), AppHelper.RESPONSE_ERROR);
                t.printStackTrace();
                loadingDialog.off();
            }
        });
    }
}
