package com.example.creators.jsp.requests;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.example.creators.jsp.JspHelper;

import java.util.Map;

public class ContentsListRequest extends StringRequest {
    private String tag = "ContentsListRequest";

    public ContentsListRequest(Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, JspHelper.getURL("contents_list"), listener, errorListener);
        super.setTag(tag);
    }
}
