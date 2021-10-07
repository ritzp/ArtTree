package com.example.creators.jsp.requests;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.example.creators.jsp.JspHelper;

import java.util.HashMap;
import java.util.Map;

public class ContentsRequest extends StringRequest {
    private String tag = "ContentsRequest";
    private String contentsId;

    public ContentsRequest(String contentsId, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, JspHelper.getURL("contents_detail"), listener, errorListener);
        super.setTag(tag);
        this.contentsId = contentsId;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        Map<String, String> params = new HashMap<>();
        params.put("contentsId", contentsId);
        return params;
    }
}
