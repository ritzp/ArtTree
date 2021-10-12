package com.example.creators;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.creators.app.AppHelper;
import com.example.creators.http.ApiInterface;
import com.example.creators.http.RetrofitClient;
import com.example.creators.http.response.SignInResponse;

import retrofit2.Call;
import retrofit2.Callback;

public class SignInActivity extends AppCompatActivity {

    private ApiInterface api;

    private EditText id, password;
    private Button signIn, signUp;
    private TextView forgotPassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin);
        getSupportActionBar().hide();

        id = findViewById(R.id.signin_edt_id);
        password = findViewById(R.id.signin_edt_pass);
        signIn = findViewById(R.id.signin_btn_signin);
        signUp = findViewById(R.id.signin_btn_signUp);
        forgotPassword = findViewById(R.id.signin_txt_forgotPass);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest();
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void sendRequest() {
        api = RetrofitClient.getRetrofit().create(ApiInterface.class);
        Call<SignInResponse> call = api.postSignIn(id.getText().toString(), password.getText().toString());

        call.enqueue(new Callback<SignInResponse>() {
            @Override
            public void onResponse(Call<SignInResponse> call, retrofit2.Response<SignInResponse> response) {
                if (response.body().getMessage().equals("FAILED")) {
                    Toast.makeText(SignInActivity.this, getString(R.string.sign_in_failed), Toast.LENGTH_SHORT).show();
                    return;
                }
                AppHelper.setAccessingUserid(response.body().getUserId());
                AppHelper.setAccessingUserpass(response.body().getPassword());

                Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(Call<SignInResponse> call, Throwable t) {
                AppHelper.checkError(SignInActivity.this, AppHelper.RESPONSE_ERROR);
                t.printStackTrace();
            }
        });
    }
}
