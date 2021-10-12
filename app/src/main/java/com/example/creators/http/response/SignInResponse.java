package com.example.creators.http.response;

import com.google.gson.annotations.SerializedName;

public class SignInResponse {
    private String message;
    private String userId;
    private String password;

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    public String getMessage() {
        return message;
    }
}
