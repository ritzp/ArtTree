package com.example.creators.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.creators.R;
import com.example.creators.jsp.JspHelper;
import com.example.creators.jsp.requests.GetUserRequest;
import com.example.creators.viewmodels.MyPageViewModel;

import org.json.JSONException;
import org.json.JSONObject;

public class MyPageFragment extends Fragment {

    private MyPageViewModel viewModel;
    private View loading, view;
    private TextView introText, contentsText, likesText;
    private ImageView iconImg, headerImg;

    private boolean[] isResponseCompleted;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(MyPageViewModel.class);
        isResponseCompleted = new boolean[]{false, false, false};
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.main_mypage, container, false);

        loading = (TextView)root.findViewById(R.id.mypage_txt_loading);
        view = (ScrollView)root.findViewById(R.id.mypage_view);

        introText = root.findViewById(R.id.mypage_txt_intro);
        contentsText = root.findViewById(R.id.mypage_txt_contents);
        likesText = root.findViewById(R.id.mypage_txt_likes);
        iconImg = root.findViewById(R.id.mypage_img_icon);
        headerImg = root.findViewById(R.id.mypage_img_header);

        viewModel.getUserId().setValue("testId1");
        sendRequest("testId1");

        viewModel.getUserIntroduction().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                introText.setText(s);
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

        return root;
    }

    private void sendRequest(String userId) {
        try {
            final Response.Listener<String> stringListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        if (!JspHelper.checkMessage(getActivity(), response))
                            return;
                        JSONObject jsonObject = new JSONObject(response);
                        viewModel.getUserIntroduction().setValue(jsonObject.getString("introduction"));
                        isResponseCompleted[0] = true;
                        loadView();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };
            final Response.Listener<Bitmap> iconListener = new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap response) {
                    viewModel.getUserIcon().setValue(response);
                    isResponseCompleted[1] = true;
                    loadView();
                }
            };
            final Response.Listener<Bitmap> headerListener = new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap response) {
                    viewModel.getUserHeader().setValue(response);
                    isResponseCompleted[2] = true;
                    loadView();
                }
            };
            final GetUserRequest userRequest = new GetUserRequest(userId, stringListener, iconListener, headerListener);
            userRequest.add();
        } catch (Exception e) {
            JspHelper.checkMessage(getActivity(), "SERVER ERROR");
            e.printStackTrace();
        }
    }

    private void loadView() {
        if (isResponseCompleted[0] && isResponseCompleted[1] && isResponseCompleted[2]) {
            loading.setVisibility(View.GONE);
            view.setVisibility(View.VISIBLE);
        }
    }
}