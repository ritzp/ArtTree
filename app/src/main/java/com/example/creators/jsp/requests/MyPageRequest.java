package com.example.creators.jsp.requests;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.example.creators.jsp.JspHelper;

import java.util.HashMap;
import java.util.Map;

public class MyPageRequest extends StringRequest {
    private String tag = "MyPageRequest";
    private String userId;

    public MyPageRequest(String userId, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, JspHelper.getURL("my_page"), listener, errorListener);
        super.setTag(tag);
        this.userId = userId;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        Map<String, String> params = new HashMap<>();
        params.put("userId", userId);
        return params;
    }
}
