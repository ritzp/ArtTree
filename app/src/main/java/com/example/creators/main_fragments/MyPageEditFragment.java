package com.example.creators.main_fragments;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatDialog;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.creators.MainActivity;
import com.example.creators.R;
import com.example.creators.app.AppHelper;
import com.example.creators.app.UriParser;
import com.example.creators.http.ApiInterface;
import com.example.creators.http.RetrofitClient;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class MyPageEditFragment extends Fragment {

    private ApiInterface api;

    private View iconBtn, headerBtn;
    private Button submit;
    private ImageView close;
    private ImageView icon, header;
    private EditText nickname, introduction;

    private Uri iconUri, headerUri;
    private boolean isIconChanged = false, isHeaderChanged = false;

    private AppCompatDialog uploadingDialog;

    private ActivityResultLauncher<Intent> getIconFile = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @RequiresApi(api = Build.VERSION_CODES.P)
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        iconUri = result.getData().getData();
                        Glide.with(MyPageEditFragment.this).pauseRequests();
                        icon.setImageURI(iconUri);
                        isIconChanged = true;
                    } else {
                        Toast.makeText(MyPageEditFragment.this.getActivity(), getString(R.string.not_selected), Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    private ActivityResultLauncher<Intent> getHeaderFile = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @RequiresApi(api = Build.VERSION_CODES.P)
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        headerUri = result.getData().getData();
                        Glide.with(MyPageEditFragment.this).pauseRequests();
                        header.setImageURI(headerUri);
                        isHeaderChanged = true;
                    } else {
                        Toast.makeText(MyPageEditFragment.this.getActivity(), getString(R.string.not_selected), Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.main_fragment_mypage_edit, container, false);

        nickname = root.findViewById(R.id.mypageedt_edt_nickname);
        introduction = root.findViewById(R.id.mypageedt_edt_intro);
        icon = root.findViewById(R.id.mypageedt_img_icon);
        header = root.findViewById(R.id.mypageedt_img_header);
        iconBtn = root.findViewById(R.id.mypageedt_icon);
        headerBtn = root.findViewById(R.id.mypageedt_header);
        close = root.findViewById(R.id.mypageedt_img_close);
        submit = root.findViewById(R.id.mypageedt_btn_submit);

        Bundle bundle = getArguments();

        nickname.setText(bundle.getString("nickname"));
        introduction.setText(bundle.getString("introduction"));
        Glide.with(this).load(RetrofitClient.getIconUrl(AppHelper.getAccessingUserid())).placeholder(R.drawable.pic_icon_default).error(R.drawable.pic_icon_default).into(icon);
        Glide.with(this).load(RetrofitClient.getHeaderUrl(AppHelper.getAccessingUserid())).placeholder(R.color.grey).error(R.color.grey).into(header);

        iconBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                getIconFile.launch(intent);
            }
        });

        headerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                getHeaderFile.launch(intent);
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)MyPageEditFragment.this.getActivity()).replaceFragmentToMyPage();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadingDialog = new AppCompatDialog(MyPageEditFragment.this.getActivity());
                uploadingDialog.setCancelable(false);
                uploadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                uploadingDialog.setContentView(R.layout.alert_loading);
                uploadingDialog.show();
                sendRequest();
            }
        });

        return root;
    }

    private void sendRequest() {
        ArrayList<MultipartBody.Part> parts = new ArrayList<>();
        if (isIconChanged)
            parts.add(UriParser.uriToMultipart(iconUri, "icon", getActivity().getContentResolver()));
        if (isHeaderChanged)
            parts.add(UriParser.uriToMultipart(headerUri, "header", getActivity().getContentResolver()));

        api = RetrofitClient.getRetrofit().create(ApiInterface.class);
        Call<String> call = null;
        if (parts.size() == 0) {
            call = api.postMyPageEdit(AppHelper.getAccessingUserid(), nickname.getText().toString(), introduction.getText().toString());
        } else {
            call = api.postMyPageEditWithParts(parts, AppHelper.getAccessingUserid(), nickname.getText().toString(), introduction.getText().toString());
        }

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (!AppHelper.checkError(MyPageEditFragment.this.getActivity(), response.body()))
                    return;

                if (response.body().equals("SUCCESS")) {
                    Toast.makeText(MyPageEditFragment.this.getActivity(), getString(R.string.mypage_edit_completed), Toast.LENGTH_SHORT).show();
                    ((MainActivity)MyPageEditFragment.this.getActivity()).replaceFragmentToMyPage();
                } else {
                    AppHelper.checkError(MyPageEditFragment.this.getActivity(), AppHelper.CODE_ERROR);
                }
                offUploadingDialog();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                AppHelper.checkError(MyPageEditFragment.this.getActivity(), AppHelper.RESPONSE_ERROR);
                offUploadingDialog();
                t.printStackTrace();
            }
        });
    }

    private void offUploadingDialog() {
        if (uploadingDialog != null && uploadingDialog.isShowing()) {
            uploadingDialog.dismiss();
        }
    }
}
