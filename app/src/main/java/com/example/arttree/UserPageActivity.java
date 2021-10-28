package com.example.arttree;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.arttree.app.AppHelper;
import com.example.arttree.app.LoadingDialog;
import com.example.arttree.http.ApiInterface;
import com.example.arttree.http.RetrofitClient;
import com.example.arttree.http.response.MyPageResponse;
import com.example.arttree.http.response.UserPageResponse;
import com.example.arttree.main_fragments.MyPageFragment;
import com.example.arttree.viewmodels.MyPageViewModel;
import com.example.arttree.viewmodels.UserPageViewModel;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserPageActivity extends AppCompatActivity {
    private ApiInterface api;

    private UserPageViewModel viewModel;

    private CircleImageView icon;
    private ImageView close, header;
    private TextView nickname, introduction, content, likes;
    private Button subscribe, unsubscribe;

    private LoadingDialog loadingDialog;

    private String userId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userpage);
        getSupportActionBar().hide();
        viewModel = new ViewModelProvider(this).get(UserPageViewModel.class);

        close = findViewById(R.id.userpage_img_close);
        icon = findViewById(R.id.userpage_img_icon);
        header = findViewById(R.id.userpage_img_header);
        nickname = findViewById(R.id.userpage_txt_nickname);
        introduction = findViewById(R.id.userpage_txt_intro);
        content = findViewById(R.id.userpage_txt_content);
        likes = findViewById(R.id.userpage_txt_likes);
        subscribe = findViewById(R.id.userpage_btn_subscribe);
        unsubscribe = findViewById(R.id.userpage_btn_unsubscribe);

        viewModel.getNickname().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                nickname.setText(s);
            }
        });

        viewModel.getUserIntroduction().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                introduction.setText(s);
            }
        });

        viewModel.getContent().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                content.setText(integer.toString());
            }
        });

        viewModel.getLikes().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                likes.setText(integer.toString());
            }
        });

        subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userId.equals(AppHelper.getAccessingUserid())) {
                    Toast.makeText(UserPageActivity.this, getString(R.string.subscribe_myself), Toast.LENGTH_SHORT).show();
                    return;
                }

                loadingDialog.show();
                sendSubscribeRequest();
            }
        });

        unsubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.show();
                sendUnsubscribeRequest();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");

        loadingDialog = new LoadingDialog(this, R.layout.alert_loading);
        loadingDialog.show();

        sendRequest();

        Glide.with(this).load(RetrofitClient.getIconUrl(userId)).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).placeholder(R.drawable.pic_icon_default).error(R.drawable.pic_icon_default).into(icon);
        Glide.with(this).load(RetrofitClient.getHeaderUrl(userId)).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).placeholder(R.color.grey).error(R.color.grey).into(header);
    }

    private void sendRequest() {
        api = RetrofitClient.getRetrofit().create(ApiInterface.class);
        Call<UserPageResponse> call = api.postUserPage(AppHelper.getAccessingUserid(), userId);

        call.enqueue(new Callback<UserPageResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<UserPageResponse> call, Response<UserPageResponse> response) {
                Log.e("tag", String.valueOf(response.body().isSubscribed()));
                viewModel.getNickname().setValue(response.body().getNickname());
                viewModel.getUserIntroduction().setValue(response.body().getIntroduction());
                viewModel.getContent().setValue(response.body().getContent());
                viewModel.getLikes().setValue(response.body().getLikes());
                if (response.body().isSubscribed()) {
                    subscribe.setVisibility(View.GONE);
                    unsubscribe.setVisibility(View.VISIBLE);
                } else {
                    subscribe.setVisibility(View.VISIBLE);
                    unsubscribe.setVisibility(View.GONE);
                }

                loadingDialog.off();
            }

            @Override
            public void onFailure(Call<UserPageResponse> call, Throwable t) {
                AppHelper.checkError(UserPageActivity.this, AppHelper.RESPONSE_ERROR);
                t.printStackTrace();

                loadingDialog.off();
            }
        });
    }

    private void sendSubscribeRequest() {
        api = RetrofitClient.getRetrofit().create(ApiInterface.class);
        Call<String> call = api.postSubscribe(AppHelper.getAccessingUserid(), userId);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (!AppHelper.checkError(UserPageActivity.this, response.body()))
                    return;

                if (response.body().equals("SUCCESS")) {
                    subscribe.setVisibility(View.GONE);
                    unsubscribe.setVisibility(View.VISIBLE);
                } else {
                    AppHelper.checkError(UserPageActivity.this, AppHelper.CODE_ERROR);
                }
                loadingDialog.off();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                AppHelper.checkError(UserPageActivity.this, AppHelper.RESPONSE_ERROR);
                loadingDialog.off();
                t.printStackTrace();
            }
        });
    }

    private void sendUnsubscribeRequest() {
        api = RetrofitClient.getRetrofit().create(ApiInterface.class);
        Call<String> call = api.postUnsubscribe(AppHelper.getAccessingUserid(), userId);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (!AppHelper.checkError(UserPageActivity.this, response.body()))
                    return;

                if (response.body().equals("SUCCESS")) {
                    subscribe.setVisibility(View.VISIBLE);
                    unsubscribe.setVisibility(View.GONE);
                } else {
                    AppHelper.checkError(UserPageActivity.this, AppHelper.CODE_ERROR);
                }
                loadingDialog.off();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                AppHelper.checkError(UserPageActivity.this, AppHelper.RESPONSE_ERROR);
                loadingDialog.off();
                t.printStackTrace();
            }
        });
    }
}
