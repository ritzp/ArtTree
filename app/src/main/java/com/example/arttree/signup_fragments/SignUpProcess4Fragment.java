package com.example.arttree.signup_fragments;

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

import com.example.arttree.R;
import com.example.arttree.SignInActivity;
import com.example.arttree.SignUpActivity;
import com.example.arttree.app.AppHelper;
import com.example.arttree.app.PasswordEncryptor;
import com.example.arttree.app.UriParser;
import com.example.arttree.http.ApiInterface;
import com.example.arttree.http.RetrofitClient;

import org.jetbrains.annotations.NotNull;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class SignUpProcess4Fragment extends Fragment {

    private ApiInterface api;

    private Button submit, getIcon, getHeader;
    private ImageView back;
    private CircleImageView icon;
    private ImageView header;

    private Uri iconUri = null, headerUri = null;
    private String dateFormat = "yyyyMMdd";

    private AppCompatDialog uploadingDialog;

    private ActivityResultLauncher<Intent> getIconFile = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @RequiresApi(api = Build.VERSION_CODES.P)
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        iconUri = result.getData().getData();
                        icon.setImageURI(iconUri);
                    } else {
                        Toast.makeText(SignUpProcess4Fragment.this.getActivity(), getString(R.string.not_selected), Toast.LENGTH_SHORT).show();
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
                        header.setImageURI(headerUri);
                    } else {
                        Toast.makeText(SignUpProcess4Fragment.this.getActivity(), getString(R.string.not_selected), Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.signup_process4, container, false);

        submit = root.findViewById(R.id.signup_process4_btn_submit);
        back = root.findViewById(R.id.signup_process4_img_back);
        getIcon = root.findViewById(R.id.signup_process4_btn_getIcon);
        getHeader = root.findViewById(R.id.signup_process4_btn_getHeader);
        icon = root.findViewById(R.id.signup_process4_img_icon);
        header = root.findViewById(R.id.signup_process4_img_header);

        getIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                getIconFile.launch(intent);
            }
        });

        getHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                getHeaderFile.launch(intent);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadingDialog = new AppCompatDialog(SignUpProcess4Fragment.this.getActivity());
                uploadingDialog.setCancelable(false);
                uploadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                uploadingDialog.setContentView(R.layout.alert_loading);
                uploadingDialog.show();
                sendRequest();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((SignUpActivity)SignUpProcess4Fragment.this.getActivity()).replaceFragmentToProcess3();
            }
        });

        return root;
    }

    private void sendRequest() {
        api = RetrofitClient.getRetrofit().create(ApiInterface.class);
        Call<String> call = api.postSignUp(
                String.valueOf(((SignUpActivity)getActivity()).method),
                ((SignUpActivity)getActivity()).emailPhone,
                ((SignUpActivity)getActivity()).id,
                PasswordEncryptor.encrypt(((SignUpActivity)getActivity()).password),
                ((SignUpActivity)getActivity()).nickname,
                ((SignUpActivity)getActivity()).introduction
        );

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (!AppHelper.checkError(SignUpProcess4Fragment.this.getActivity(), response.body()))
                    return;

                if (response.body().equals("SUCCESS")) {
                    sendIconRequest();
                } else {
                    AppHelper.checkError(SignUpProcess4Fragment.this.getActivity(), AppHelper.CODE_ERROR);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                AppHelper.checkError(SignUpProcess4Fragment.this.getActivity(), AppHelper.RESPONSE_ERROR);
                offUploadingDialog();
                t.printStackTrace();
            }
        });
    }

    private void sendIconRequest() {
        api = RetrofitClient.getRetrofit().create(ApiInterface.class);
        if (iconUri != null) {
            MultipartBody.Part part = UriParser.uriToMultipart(iconUri, "icon", getActivity().getContentResolver());
            Call<String> call = api.postSignUpIcon(part, ((SignUpActivity) getActivity()).id);

            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.body().equals("SUCCESS")) {
                        sendHeaderRequest();
                    } else {
                        sendErrorRequest();
                        AppHelper.checkError(SignUpProcess4Fragment.this.getActivity(), AppHelper.CODE_ERROR);
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    AppHelper.checkError(SignUpProcess4Fragment.this.getActivity(), AppHelper.RESPONSE_ERROR);
                    sendErrorRequest();
                    t.printStackTrace();
                }
            });
        } else {
            sendHeaderRequest();
        }
    }

    private void sendHeaderRequest() {
        api = RetrofitClient.getRetrofit().create(ApiInterface.class);
        if (headerUri != null) {
            MultipartBody.Part part = UriParser.uriToMultipart(headerUri, "header", getActivity().getContentResolver());
            Call<String> call = api.postSignUpHeader(part, ((SignUpActivity)getActivity()).id);

            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.body().equals("SUCCESS")) {
                        Toast.makeText(SignUpProcess4Fragment.this.getActivity(), getString(R.string.sign_up_completed), Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(SignUpProcess4Fragment.this.getActivity(), SignInActivity.class);
                        startActivity(intent);
                        SignUpProcess4Fragment.this.getActivity().finish();
                    } else {
                        sendErrorRequest();
                        AppHelper.checkError(SignUpProcess4Fragment.this.getActivity(), AppHelper.CODE_ERROR);
                    }
                    offUploadingDialog();
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    AppHelper.checkError(SignUpProcess4Fragment.this.getActivity(), AppHelper.RESPONSE_ERROR);
                    sendErrorRequest();
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(SignUpProcess4Fragment.this.getActivity(), getString(R.string.sign_up_completed), Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(SignUpProcess4Fragment.this.getActivity(), SignInActivity.class);
            startActivity(intent);
            SignUpProcess4Fragment.this.getActivity().finish();
        }
    }

    private void sendErrorRequest() {
        api = RetrofitClient.getRetrofit().create(ApiInterface.class);
        Call<String> call = api.postSignUpError(((SignUpActivity)getActivity()).id);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                offUploadingDialog();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                AppHelper.checkError(SignUpProcess4Fragment.this.getActivity(), AppHelper.RESPONSE_ERROR);
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
