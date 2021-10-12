package com.example.creators.main_fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.creators.MainActivity;
import com.example.creators.R;
import com.example.creators.app.AppHelper;
import com.example.creators.app.ImageResize;
import com.example.creators.http.ApiInterface;
import com.example.creators.http.RetrofitClient;
import com.example.creators.http.response.MyPageResponse;
import com.example.creators.viewmodels.MyPageViewModel;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyPageFragment extends Fragment {

    private ApiInterface api;

    private MyPageViewModel viewModel;
    private View loading, view;
    private TextView nickname, introduction, content, likes;
    private ImageView icon, header, edit, settings;
    private LinearLayout myContent;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(MyPageViewModel.class);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.main_fragment_mypage, container, false);

        loading = (TextView)root.findViewById(R.id.mypage_txt_loading);
        view = (ScrollView)root.findViewById(R.id.mypage_view);

        edit = root.findViewById(R.id.mypage_img_edit);
        settings = root.findViewById(R.id.mypage_img_settings);
        settings = root.findViewById(R.id.mypage_img_settings);
        nickname = root.findViewById(R.id.mypage_txt_nickname);
        introduction = root.findViewById(R.id.mypage_txt_intro);
        content = root.findViewById(R.id.mypage_txt_content);
        likes = root.findViewById(R.id.mypage_txt_likes);
        icon = root.findViewById(R.id.mypage_img_icon);
        header = root.findViewById(R.id.mypage_img_header);
        myContent = root.findViewById(R.id.mypage_myContent);

        viewModel.getUserId().setValue("testId1");
        sendRequest();

        Picasso.get().load(RetrofitClient.getIconUrl(AppHelper.getAccessingUserid())).memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE).tag("myPage").into(icon);
        Picasso.get().load(RetrofitClient.getHeaderUrl(AppHelper.getAccessingUserid())).memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE).tag("myPage").into(header);

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

        viewModel.getContent().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                content.setText(integer.toString());
            }
        });

        viewModel.getLikes().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                likes.setText(integer.toString());
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("nickname", nickname.getText().toString());
                bundle.putString("introduction", introduction.getText().toString());
                ((MainActivity)MyPageFragment.this.getActivity()).replaceFragmentToMyPageEdit(bundle);
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)MyPageFragment.this.getActivity()).replaceFragmentToSettingsMain();
            }
        });

        myContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).replaceFragmentToMyContentList();
            }
        });

        return root;
    }

    private void sendRequest() {
        api = RetrofitClient.getRetrofit().create(ApiInterface.class);
        Call<MyPageResponse> call = api.postMyPage(AppHelper.getAccessingUserid());

        call.enqueue(new Callback<MyPageResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<MyPageResponse> call, Response<MyPageResponse> response) {
                viewModel.getNickname().setValue(response.body().getNickname());
                viewModel.getUserIntroduction().setValue(response.body().getIntroduction());
                /*byte[] iconBytes = java.util.Base64.getDecoder().decode(response.body().getIcon());
                Bitmap icon = BitmapFactory.decodeByteArray(iconBytes, 0, iconBytes.length);
                viewModel.getUserIcon().setValue(icon);
                byte[] headerBytes = java.util.Base64.getDecoder().decode(response.body().getHeader());
                Bitmap header = BitmapFactory.decodeByteArray(headerBytes, 0, headerBytes.length);
                viewModel.getUserHeader().setValue(header);*/
                viewModel.getContent().setValue(response.body().getContent());
                viewModel.getLikes().setValue(response.body().getLikes());

                loadView();
            }

            @Override
            public void onFailure(Call<MyPageResponse> call, Throwable t) {
                AppHelper.checkError(MyPageFragment.this.getActivity(), AppHelper.RESPONSE_ERROR);
                t.printStackTrace();
            }
        });
    }

    private void loadView() {
        loading.setVisibility(View.GONE);
        view.setVisibility(View.VISIBLE);
    }
}