package com.example.creators.main_fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.creators.R;
import com.example.creators.app.AppHelper;
import com.example.creators.jsp.JspHelper;
import com.example.creators.jsp.requests.MyPageRequest;
import com.example.creators.viewmodels.MyPageViewModel;

import org.json.JSONException;
import org.json.JSONObject;

public class MyPageFragment extends Fragment {

    private MyPageViewModel viewModel;
    private View loading, view;
    private TextView nickname, introduction, contents, likes;
    private ImageView iconImg, headerImg;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(MyPageViewModel.class);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.main_fragment_mypage, container, false);

        loading = (TextView)root.findViewById(R.id.mypage_txt_loading);
        view = (ScrollView)root.findViewById(R.id.mypage_view);

        nickname = root.findViewById(R.id.mypage_txt_nickname);
        introduction = root.findViewById(R.id.mypage_txt_intro);
        contents = root.findViewById(R.id.mypage_txt_contents);
        likes = root.findViewById(R.id.mypage_txt_likes);
        iconImg = root.findViewById(R.id.mypage_img_icon);
        headerImg = root.findViewById(R.id.mypage_img_header);

        viewModel.getUserId().setValue("testId1");
        sendRequest("testId1");

        viewModel.getNickname().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                nickname.setText(s);
            }
        });

        viewModel.getUserIntroduction().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                introduction.setText(s);
            }
        });

        viewModel.getUserIcon().observe(getViewLifecycleOwner(), new Observer<Bitmap>() {
            @Override
            public void onChanged(Bitmap bitmap) {
                iconImg.setImageBitmap(bitmap);
            }
        });

        viewModel.getUserHeader().observe(getViewLifecycleOwner(), new Observer<Bitmap>() {
            @Override
            public void onChanged(Bitmap bitmap) {
                headerImg.setImageBitmap(bitmap);
            }
        });

        viewModel.getContents().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                contents.setText(integer.toString());
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        JspHelper.cancelRequests("MyPageRequest");
    }

    private void sendRequest(String userId) {
        final Response.Listener<String> listener = new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(String response) {
                if (!AppHelper.checkError(getActivity(), response.trim()))
                    return;

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    viewModel.getNickname().setValue(jsonObject.getString("nickname"));
                    viewModel.getUserIntroduction().setValue(jsonObject.getString("introduction"));

                    byte[] decodedIcon = java.util.Base64.getDecoder().decode(jsonObject.getString("icon"));
                    Bitmap icon = BitmapFactory.decodeByteArray(decodedIcon, 0, decodedIcon.length);
                    viewModel.getUserIcon().setValue(icon);

                    byte[] decodedHeader = java.util.Base64.getDecoder().decode(jsonObject.getString("header"));
                    Bitmap header = BitmapFactory.decodeByteArray(decodedHeader, 0, decodedHeader.length);
                    viewModel.getUserHeader().setValue(header);

                    viewModel.getContents().setValue(jsonObject.getInt("contents"));

                    loadView();
                } catch (Exception e) {
                    AppHelper.checkError(getActivity(), AppHelper.CODE_ERROR);
                    e.printStackTrace();
                }
            }
        };
        final Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AppHelper.checkError(getActivity(), AppHelper.RESPONSE_ERROR);
            }
        };
        try {
            final MyPageRequest request = new MyPageRequest(userId, listener, errorListener);
            JspHelper.addRequestQueue(getActivity(), request);
        } catch (Exception e) {
            AppHelper.checkError(getActivity(), AppHelper.CODE_ERROR);
            e.printStackTrace();
        }
    }

    private void loadView() {
        loading.setVisibility(View.GONE);
        view.setVisibility(View.VISIBLE);
    }
}