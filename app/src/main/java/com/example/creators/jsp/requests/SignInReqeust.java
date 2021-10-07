package com.example.creators.jsp.requests;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.example.creators.jsp.JspHelper;

import java.util.HashMap;
import java.util.Map;

public class SignInReqeust extends StringRequest {
    private String tag = "SignInRequest";
    private String id, password;

    public SignInReqeust(String id, String password, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, JspHelper.getURL("sign_in"), listener, errorListener);
        super.setTag(tag);
        this.id = id;
        this.password = password;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        Map<String, String> params = new HashMap<>();
        params.put("id", id);
        params.put("password", password);
        return params;
    }
}
