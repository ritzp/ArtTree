package com.example.creators.jsp.requests;

import android.graphics.Bitmap;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.creators.jsp.JspHelper;

import java.util.HashMap;
import java.util.Map;

public class GetUserRequest {
    private UserStringRequest stringRequest;
    private UserIconRequest iconRequest;
    private UserHeaderRequest headerRequest;

    public GetUserRequest(String userId, Response.Listener<String> stringListener, Response.Listener<Bitmap> iconListener, Response.Listener<Bitmap> headerListener) {
        stringRequest = new UserStringRequest(userId, stringListener);
        iconRequest = new UserIconRequest(userId, iconListener);
        headerRequest = new UserHeaderRequest(userId, headerListener);
        stringRequest.setShouldCache(false);
        iconRequest.setShouldCache(false);
        headerRequest.setShouldCache(false);
    }

    public void add() {
        try {
            JspHelper.REQUEST_QUEUE.add(stringRequest);
            JspHelper.REQUEST_QUEUE.add(iconRequest);
            JspHelper.REQUEST_QUEUE.add(headerRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class UserStringRequest extends StringRequest {
        private String userId;

        public UserStringRequest(String userId, Response.Listener<String> listener) {
            super(Request.Method.POST, JspHelper.getStringURL("user"), listener, null);
            this.userId = userId;
        }

        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            HashMap<String, String> params = new HashMap<>();
            params.put("userId", userId);
            return params;
        }
    }

    class UserIconRequest extends ImageRequest {
        public UserIconRequest(String userId, Response.Listener<Bitmap> listener) {
            super(JspHelper.getImageURL("user/icons", userId, "png"), listener, 0, 0, Bitmap.Config.ARGB_4444, null);
        }
    }

    class UserHeaderRequest extends ImageRequest {
        public UserHeaderRequest(String userId, Response.Listener<Bitmap> listener) {
            super(JspHelper.getImageURL("user/headers", userId, "jpg"), listener, 0, 0, Bitmap.Config.ARGB_4444, null);
        }
    }
}
