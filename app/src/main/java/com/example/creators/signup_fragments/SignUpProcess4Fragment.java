package com.example.creators.signup_fragments;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
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

import com.example.creators.MainActivity;
import com.example.creators.R;
import com.example.creators.SignInActivity;
import com.example.creators.SignUpActivity;
import com.example.creators.app.AppHelper;
import com.example.creators.http.ApiInterface;
import com.example.creators.http.RetrofitClient;
import com.example.creators.main_fragments.MyPageEditFragment;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okio.BufferedSink;
import okio.Okio;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class SignUpProcess4Fragment extends Fragment {

    private ApiInterface api;

    private TextView getIcon, getHeader;
    private Button submit;
    private ImageView back;
    private CircleImageView icon;
    private ImageView header;

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
                        icon.setImageURI(iconUri);
                        isIconChanged = true;
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
                        isHeaderChanged = true;
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
        getIcon = root.findViewById(R.id.signup_process4_txt_getIcon);
        getHeader = root.findViewById(R.id.signup_process4_txt_getHeader);
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
                uploadingDialog.setContentView(R.layout.upload_alert_uploading);
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

    private MultipartBody.Part uriToMultipart(final Uri uri, String name, final ContentResolver contentResolver) {
        final Cursor c = contentResolver.query(uri, null, null, null, null);
        if (c != null) {
            if(c.moveToNext()) {
                final String displayName = c.getString(c.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                RequestBody requestBody = new RequestBody() {
                    @Override
                    public MediaType contentType() {
                        return MediaType.parse(contentResolver.getType(uri));
                    }

                    @Override
                    public void writeTo(BufferedSink sink) {
                        try {
                            sink.writeAll(Okio.source(contentResolver.openInputStream(uri)));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                };
                c.close();
                return MultipartBody.Part.createFormData(name, displayName, requestBody);
            } else {
                c.close();
                return null;
            }
        } else {
            return null;
        }
    }

    private void sendRequest() {
        int[] callbacks = {-1, -1, -1};
        api = RetrofitClient.getRetrofit().create(ApiInterface.class);

        if (isIconChanged) {
            MultipartBody.Part part = uriToMultipart(iconUri, "icon", getActivity().getContentResolver());
            Call<String> call = api.postSignUpIcon(part, ((SignUpActivity)getActivity()).id);

            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.body().equals("SUCCESS"))
                        callbacks[1] = 1;
                    checkAllCallbacks(callbacks);
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    AppHelper.checkError(SignUpProcess4Fragment.this.getActivity(), AppHelper.RESPONSE_ERROR);
                    callbacks[1] = 0;
                    t.printStackTrace();
                }
            });
        }
        if (isHeaderChanged) {
            MultipartBody.Part part = uriToMultipart(headerUri, "header", getActivity().getContentResolver());
            Call<String> call = api.postSignUpHeader(part, ((SignUpActivity)getActivity()).id);

            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.body().equals("SUCCESS"))
                        callbacks[2] = 1;
                    checkAllCallbacks(callbacks);
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    AppHelper.checkError(SignUpProcess4Fragment.this.getActivity(), AppHelper.RESPONSE_ERROR);
                    callbacks[2] = 0;
                    t.printStackTrace();
                }
            });
        }

        api = RetrofitClient.getRetrofit().create(ApiInterface.class);
        Call<String> call = api.postSignUp(
                String.valueOf(((SignUpActivity)getActivity()).method),
                ((SignUpActivity)getActivity()).emailPhone,
                ((SignUpActivity)getActivity()).id,
                ((SignUpActivity)getActivity()).password,
                ((SignUpActivity)getActivity()).nickname,
                ((SignUpActivity)getActivity()).introduction
        );

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (!AppHelper.checkError(SignUpProcess4Fragment.this.getActivity(), response.body()))
                    return;

                if (response.body().equals("SUCCESS")) {
                    callbacks[0] = 1;
                    checkAllCallbacks(callbacks);
                } else {
                    callbacks[0] = 0;
                    AppHelper.checkError(SignUpProcess4Fragment.this.getActivity(), AppHelper.CODE_ERROR);
                }
                offUploadingDialog();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                AppHelper.checkError(SignUpProcess4Fragment.this.getActivity(), AppHelper.RESPONSE_ERROR);
                callbacks[0]= 0;
                offUploadingDialog();
                t.printStackTrace();
            }
        });
    }

    private void checkAllCallbacks(int[] callbacks) {
        for (int i=0; i<callbacks.length; i++) {
            if (callbacks[i] == -1) {
                return;
            } else if (callbacks[i] == 0) {
                AppHelper.checkError(SignUpProcess4Fragment.this.getActivity(), AppHelper.RESPONSE_ERROR);
                return;
            }
        }
        Toast.makeText(SignUpProcess4Fragment.this.getActivity(), getString(R.string.sign_up_completed), Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(SignUpProcess4Fragment.this.getActivity(), SignInActivity.class);
        startActivity(intent);
        SignUpProcess4Fragment.this.getActivity().finish();
    }

    private void offUploadingDialog() {
        if (uploadingDialog != null && uploadingDialog.isShowing()) {
            uploadingDialog.dismiss();
        }
    }
}
