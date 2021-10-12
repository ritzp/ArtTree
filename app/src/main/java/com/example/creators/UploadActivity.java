package com.example.creators;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialog;

import com.example.creators.app.AppHelper;
import com.example.creators.content_fragments.upload.UploadCartoonFragment;
import com.example.creators.content_fragments.upload.UploadDrawingFragment;
import com.example.creators.content_fragments.upload.UploadMusicFragment;
import com.example.creators.content_fragments.upload.UploadPhotoFragment;
import com.example.creators.content_fragments.upload.UploadVideoFragment;
import com.example.creators.http.ApiInterface;
import com.example.creators.http.RetrofitClient;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okio.BufferedSink;
import okio.Okio;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadActivity extends AppCompatActivity {

    private ApiInterface api;
    public static Context context;

    private ImageView close;
    private Spinner categoriesSpinner;
    private EditText title, description;
    private Button fileUplaod, upload;
    public TextView textContent;

    private String category;
    private Uri uri = null;
    private boolean isFileUploaded = false, isText = false;
    private AppCompatDialog uploadingDialog;

    private ActivityResultLauncher<Intent> getPhotoFile = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        uri = result.getData().getData();
                        Bundle bundle = new Bundle();
                        bundle.putString("uri", uri.toString());

                        UploadPhotoFragment photoFragment = new UploadPhotoFragment();
                        photoFragment.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction().replace(R.id.upload_fragmentContainer, photoFragment).commit();
                        isFileUploaded = true;
                    } else {
                        Toast.makeText(UploadActivity.this, getString(R.string.not_selected), Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    private ActivityResultLauncher<Intent> getDrawingFile = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        uri = result.getData().getData();
                        Bundle bundle = new Bundle();
                        bundle.putString("uri", uri.toString());

                        UploadDrawingFragment drawingFragment = new UploadDrawingFragment();
                        drawingFragment.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction().replace(R.id.upload_fragmentContainer, drawingFragment).commit();
                        isFileUploaded = true;
                    } else {
                        Toast.makeText(UploadActivity.this, getString(R.string.not_selected), Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    private ActivityResultLauncher<Intent> getMusicFile = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        uri = result.getData().getData();
                        Bundle bundle = new Bundle();
                        bundle.putString("uri", uri.toString());

                        UploadMusicFragment musicFragment = new UploadMusicFragment();
                        musicFragment.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction().replace(R.id.upload_fragmentContainer, musicFragment).commit();
                        isFileUploaded = true;
                    } else {
                        Toast.makeText(UploadActivity.this, getString(R.string.not_selected), Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    private ActivityResultLauncher<Intent> getVideoFile = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        uri = result.getData().getData();
                        Bundle bundle = new Bundle();
                        bundle.putString("uri", uri.toString());

                        UploadVideoFragment videoFragment = new UploadVideoFragment();
                        videoFragment.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction().replace(R.id.upload_fragmentContainer, videoFragment).commit();
                        isFileUploaded = true;
                    } else {
                        Toast.makeText(UploadActivity.this, getString(R.string.not_selected), Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    private ActivityResultLauncher<Intent> getCartoonFile = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        uri = result.getData().getData();
                        Bundle bundle = new Bundle();
                        bundle.putString("uri", uri.toString());

                        UploadCartoonFragment cartoonFragment = new UploadCartoonFragment();
                        cartoonFragment.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction().replace(R.id.upload_fragmentContainer, cartoonFragment).commit();
                        isFileUploaded = true;
                    } else {
                        Toast.makeText(UploadActivity.this, getString(R.string.not_selected), Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload);
        getSupportActionBar().hide();
        context = this;

        close = findViewById(R.id.upload_img_close);
        categoriesSpinner = findViewById(R.id.upload_spn_categories);
        title = findViewById(R.id.upload_edt_title);
        description = findViewById(R.id.upload_edt_desc);
        fileUplaod = findViewById(R.id.upload_btn_fileUpload);
        upload = findViewById(R.id.upload_btn_upload);
        textContent = findViewById(R.id.upload_txt_textContent);

        final String[] spinnerCategories = {getString(R.string.photo), getString(R.string.drawing),
                getString(R.string.music), getString(R.string.video), getString(R.string.cartoon),
                getString(R.string.novel)};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.listitem_stringspinner, R.id.stringSpinner_txt, spinnerCategories);
        categoriesSpinner.setAdapter(adapter);

        final String[] categories = {"photo", "drawing", "music", "video", "cartoon", "novel"};
        categoriesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                UploadActivity.this.category = categories[position];

                if (position == 5) {
                    isText = true;
                    textContent.setVisibility(View.VISIBLE);
                    fileUplaod.setVisibility(View.GONE);
                } else {
                    isText = false;
                    textContent.setVisibility(View.GONE);
                    fileUplaod.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        fileUplaod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFile(categoriesSpinner.getSelectedItemPosition());
            }
        });

        textContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UploadActivity.this, UploadTextEditActivity.class);
                intent.putExtra("text", textContent.getText().toString());
                startActivity(intent);
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (title.getText().length() <= 0) {
                    Toast.makeText(UploadActivity.this, R.string.title_not_inputted, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (title.getText().length() > AppHelper.MAX_TITLE_SIZE) {
                    Toast.makeText(UploadActivity.this, R.string.title_overed, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (description.getText().length() > AppHelper.MAX_DESCRIPTION_SIZE) {
                    Toast.makeText(UploadActivity.this, R.string.description_overed, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!isText && !isFileUploaded) {
                    Toast.makeText(UploadActivity.this, R.string.file_not_uploaded, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (isText && textContent.length() <= 0) {
                    Toast.makeText(UploadActivity.this, R.string.text_not_inputted, Toast.LENGTH_SHORT).show();
                    return;
                }

                uploadingDialog = new AppCompatDialog(UploadActivity.this);
                uploadingDialog.setCancelable(false);
                uploadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                uploadingDialog.setContentView(R.layout.upload_alert_uploading);
                uploadingDialog.show();
                sendRequest();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getFile(int position) {
        Intent intent;
        if (position == 0) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            getPhotoFile.launch(intent);
        } else if (position == 1) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            getDrawingFile.launch(intent);
        } else if (position == 2) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("audio/*");
            getMusicFile.launch(intent);
        } else if (position == 3) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("video/*");
            getVideoFile.launch(intent);
        } else if (position == 4) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            getCartoonFile.launch(intent);
        }
    }

    private String getExtension(Uri uri) {
        String path = uri.getPath();
        int index = path.lastIndexOf(".");
        return path.substring(index+1, path.length());
    }

    private static MultipartBody.Part uriToMultipart(final Uri uri, String name, final ContentResolver contentResolver) {
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
        if (!category.equals("novel")) {
            MultipartBody.Part part = uriToMultipart(uri, "file", getContentResolver());
            api = RetrofitClient.getRetrofit().create(ApiInterface.class);
            Call<String> call = api.postUpload(part, category, getExtension(uri), title.getText().toString(), description.getText().toString(), AppHelper.getAccessingUserid());

            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (!AppHelper.checkError(UploadActivity.this, response.body()))
                        return;

                    if (response.body().equals("SUCCESS")) {
                        Toast.makeText(UploadActivity.this, getString(R.string.upload_completed), Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        AppHelper.checkError(UploadActivity.this, AppHelper.CODE_ERROR);
                    }
                    offUploadingDialog();
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    AppHelper.checkError(UploadActivity.this, AppHelper.RESPONSE_ERROR);
                    offUploadingDialog();
                    t.printStackTrace();
                }
            });
        } else {
            String content = textContent.getText().toString();

            RequestBody body = RequestBody.create(MediaType.parse("text/plain"), content);
            MultipartBody.Part part = MultipartBody.Part.createFormData("file", "sendText.txt", body);

            api = RetrofitClient.getRetrofit().create(ApiInterface.class);
            Call<String> call = api.postUpload(part, category, "txt", title.getText().toString(), description.getText().toString(), AppHelper.getAccessingUserid());

            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (!AppHelper.checkError(UploadActivity.this, response.body()))
                        return;

                    if (response.body().equals("SUCCESS")) {
                        Toast.makeText(UploadActivity.this, getString(R.string.upload_completed), Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        AppHelper.checkError(UploadActivity.this, AppHelper.CODE_ERROR);
                    }
                    offUploadingDialog();
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    AppHelper.checkError(UploadActivity.this, AppHelper.RESPONSE_ERROR);
                    offUploadingDialog();
                    t.printStackTrace();
                }
            });
        }
    }

    private void offUploadingDialog() {
        if (uploadingDialog != null && uploadingDialog.isShowing()) {
            uploadingDialog.dismiss();
        }
    }
}
