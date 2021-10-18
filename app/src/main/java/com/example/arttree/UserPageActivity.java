package com.example.arttree;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
                AppHelper.checkError(UserPageActivity.this, AppHelper.RESPONSE_ERROR);
                t.printStackTrace();

                loadingDialog.off();
            }
        });
    }
}
