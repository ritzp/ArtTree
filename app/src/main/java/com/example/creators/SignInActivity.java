package com.example.creators;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.creators.app.AppHelper;
import com.example.creators.jsp.JspHelper;
import com.example.creators.jsp.requests.SignInReqeust;

import org.json.JSONException;
import org.json.JSONObject;

public class SignInActivity extends AppCompatActivity {

    EditText id, password;
    Button signIn, signUp;
    TextView forgotPassword;

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
        final Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    if (!AppHelper.checkError(SignInActivity.this, response))
                        return;

                    if (response.trim().startsWith("SUCCESS")) {
                        JSONObject jsonObject = new JSONObject(response.trim().substring(8));
                        AppHelper.setAccessingUserid(jsonObject.getString("userId"));
                        AppHelper.setAccessingUserpass(jsonObject.getString("password"));

                        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(SignInActivity.this, R.string.sign_in_failed, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    AppHelper.checkError(SignInActivity.this, AppHelper.CODE_ERROR);
                    e.printStackTrace();
                }
            }
        };
        final Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AppHelper.checkError(SignInActivity.this, AppHelper.RESPONSE_ERROR);
            }
        };
        try {
            SignInReqeust reqeust = new SignInReqeust(id.getText().toString(), password.getText().toString(), listener, errorListener);
            JspHelper.addRequestQueue(this, reqeust);
        } catch (Exception e) {
            AppHelper.checkError(this, AppHelper.CODE_ERROR);
            e.printStackTrace();
        }
    }
}
