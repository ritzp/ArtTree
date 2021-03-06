package com.example.arttree.main_fragments;

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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.arttree.MainActivity;
import com.example.arttree.R;
import com.example.arttree.app.AppHelper;
import com.example.arttree.app.LoadingDialog;
import com.example.arttree.http.ApiInterface;
import com.example.arttree.http.RetrofitClient;
import com.example.arttree.http.response.MyPageResponse;
import com.example.arttree.viewmodels.MyPageViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyPageFragment extends Fragment {

    private ApiInterface api;

    private MyPageViewModel viewModel;
    private TextView nickname, introduction, content, likes;
    private ImageView icon, header, edit, settings;
    private LinearLayout myContent, likedContent, subscriptions;

    private LoadingDialog loadingDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(MyPageViewModel.class);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.main_fragment_mypage, container, false);

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
        likedContent = root.findViewById(R.id.mypage_likedContent);
        subscriptions = root.findViewById(R.id.mypage_subscriptions);

        loadingDialog = new LoadingDialog(getActivity(), R.layout.alert_loading);
        loadingDialog.show();

        sendRequest();

        Glide.with(MyPageFragment.this).load(RetrofitClient.getIconUrl(AppHelper.getAccessingUserid())).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).placeholder(R.drawable.pic_icon_default).error(R.drawable.pic_icon_default).into(icon);
        Glide.with(MyPageFragment.this).load(RetrofitClient.getHeaderUrl(AppHelper.getAccessingUserid())).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).placeholder(R.color.grey).error(R.color.grey).into(header);

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

        likedContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)MyPageFragment.this.getActivity()).replaceFragmentByLiked(AppHelper.getAccessingUserid());
            }
        });

        subscriptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)MyPageFragment.this.getActivity()).replaceFragmentBySubscriptions(AppHelper.getAccessingUserid());
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
                viewModel.getContent().setValue(response.body().getContent());
                viewModel.getLikes().setValue(response.body().getLikes());

                loadingDialog.off();
            }

            @Override
            public void onFailure(Call<MyPageResponse> call, Throwable t) {
                AppHelper.checkError(MyPageFragment.this.getActivity(), AppHelper.RESPONSE_ERROR);
                t.printStackTrace();

                loadingDialog.off();
            }
        });
    }
}