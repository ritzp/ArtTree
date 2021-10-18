package com.example.arttree.main_fragments.settings;

import android.content.DialogInterface;
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
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.arttree.MainActivity;
import com.example.arttree.R;
import com.example.arttree.SignInActivity;
import com.example.arttree.UploadActivity;
import com.example.arttree.app.AppHelper;
import com.example.arttree.app.LoadingDialog;
import com.example.arttree.http.ApiInterface;
import com.example.arttree.http.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsDeleteAccountFragment extends Fragment {

    private ApiInterface api;

    private ImageView back;
    private EditText password, confirmPassword;
    private Button submit;

    private LoadingDialog loadingDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.settings_deleteaccount, container, false);

        back = root.findViewById(R.id.settingsdel_img_back);
        password = root.findViewById(R.id.settingsdel_edt_pass);
        confirmPassword = root.findViewById(R.id.settingsdel_edt_conPass);
        submit = root.findViewById(R.id.settingsdel_btn_submit);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).replaceFragmentToSettingsMain();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(SettingsDeleteAccountFragment.this.getActivity());
                alertBuilder.setMessage(getString(R.string.delete_account_alert_message));
                alertBuilder.setPositiveButton(getString(R.string.delete), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (password.getText().toString().equals(confirmPassword.getText().toString())) {
                            loadingDialog = new LoadingDialog(SettingsDeleteAccountFragment.this.getActivity(), R.layout.alert_loading);
                            loadingDialog.show();
                            sendRequest();
                        } else {
                            Toast.makeText(SettingsDeleteAccountFragment.this.getActivity(), getString(R.string.passwords_do_not_match), Toast.LENGTH_SHORT).show();
                        }
                    }
                }).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertBuilder.show();
            }
        });

        return root;
    }

    private void sendRequest() {
        api = RetrofitClient.getRetrofit().create(ApiInterface.class);
        Call<String> call = api.postDeleteAccount(AppHelper.getAccessingUserid(), password.getText().toString(), confirmPassword.getText().toString());

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (!AppHelper.checkError(SettingsDeleteAccountFragment.this.getActivity(), response.body()))
                    return;

                if (response.body().equals("SUCCESS")) {
                    Toast.makeText(SettingsDeleteAccountFragment.this.getActivity(), getString(R.string.delete_account_completed, AppHelper.getAccessingUserid()), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SettingsDeleteAccountFragment.this.getActivity(), SignInActivity.class);
                    startActivity(intent);
                    SettingsDeleteAccountFragment.this.getActivity().finish();
                } else if (response.body().equals("FAILED")) {
                    Toast.makeText(SettingsDeleteAccountFragment.this.getActivity(), getString(R.string.password_invalid), Toast.LENGTH_SHORT).show();
                } else {
                    AppHelper.checkError(SettingsDeleteAccountFragment.this.getActivity(), AppHelper.CODE_ERROR);
                }
                loadingDialog.off();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                AppHelper.checkError(SettingsDeleteAccountFragment.this.getActivity(), AppHelper.RESPONSE_ERROR);
                loadingDialog.off();
                t.printStackTrace();
            }
        });
    }
}
