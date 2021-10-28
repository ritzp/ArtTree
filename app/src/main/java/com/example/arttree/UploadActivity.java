package com.example.arttree;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
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
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.arttree.app.AppHelper;
import com.example.arttree.app.LoadingDialog;
import com.example.arttree.app.UriParser;
import com.example.arttree.content_fragments.BlankFragment;
import com.example.arttree.content_fragments.upload.UploadCartoonFragment;
import com.example.arttree.content_fragments.upload.UploadDrawingFragment;
import com.example.arttree.content_fragments.upload.UploadMusicFragment;
import com.example.arttree.content_fragments.upload.UploadPhotoFragment;
import com.example.arttree.content_fragments.upload.UploadVideoFragment;
import com.example.arttree.http.ApiInterface;
import com.example.arttree.http.RetrofitClient;

import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadActivity extends AppCompatActivity {

    private ApiInterface api;
    public static Context context;

    private ImageView close, addTag;
    private Spinner categoriesSpinner;
    private EditText title, description, tagEdt;
    private Button fileUplaod, resetTags, upload;
    public TextView textContent, tagTxt;

    private String category, tag = "";
    private Uri uri = null;
    private ArrayList<Uri> uris;
    private boolean isText = false;
    private LoadingDialog loadingDialog;

    private ActivityResultLauncher<Intent> getPhotoFile = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        ClipData clipData = result.getData().getClipData();
                        if (clipData == null) {
                            uri = result.getData().getData();
                        } else {
                            uris = new ArrayList<>();
                            for (int i=0; i<clipData.getItemCount(); i++) {
                                uris.add(clipData.getItemAt(i).getUri());
                            }
                            uri = clipData.getItemAt(0).getUri();
                        }
                        Bundle bundle = new Bundle();
                        bundle.putString("uri", uri.toString());

                        UploadPhotoFragment photoFragment = new UploadPhotoFragment();
                        photoFragment.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction().replace(R.id.upload_fragmentContainer, photoFragment).commit();
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
                        ClipData clipData = result.getData().getClipData();
                        if (clipData == null) {
                            uri = result.getData().getData();
                        } else {
                            uris = new ArrayList<>();
                            for (int i=0; i<clipData.getItemCount(); i++) {
                                uris.add(clipData.getItemAt(i).getUri());
                            }
                            uri = clipData.getItemAt(0).getUri();
                        }
                        Bundle bundle = new Bundle();
                        bundle.putString("uri", uri.toString());

                        UploadDrawingFragment drawingFragment = new UploadDrawingFragment();
                        drawingFragment.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction().replace(R.id.upload_fragmentContainer, drawingFragment).commit();
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
                        ClipData clipData = result.getData().getClipData();
                        if (clipData == null) {
                            uri = result.getData().getData();
                        } else {
                            uris = new ArrayList<>();
                            for (int i=0; i<clipData.getItemCount(); i++) {
                                uris.add(clipData.getItemAt(i).getUri());
                            }
                            uri = clipData.getItemAt(0).getUri();
                        }
                        Bundle bundle = new Bundle();
                        bundle.putString("uri", uri.toString());

                        UploadCartoonFragment cartoonFragment = new UploadCartoonFragment();
                        cartoonFragment.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction().replace(R.id.upload_fragmentContainer, cartoonFragment).commit();
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
        addTag = findViewById(R.id.upload_img_addTag);
        categoriesSpinner = findViewById(R.id.upload_spn_categories);
        title = findViewById(R.id.upload_edt_title);
        description = findViewById(R.id.upload_edt_desc);
        tagEdt = findViewById(R.id.upload_edt_tag);
        fileUplaod = findViewById(R.id.upload_btn_fileUpload);
        resetTags = findViewById(R.id.upload_btn_resetTags);
        upload = findViewById(R.id.upload_btn_upload);
        textContent = findViewById(R.id.upload_txt_textContent);
        tagTxt = findViewById(R.id.upload_txt_tag);

        final String[] spinnerCategories = {getString(R.string.photo), getString(R.string.drawing),
                getString(R.string.music), getString(R.string.video), getString(R.string.cartoon),
                getString(R.string.novel)};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.listitem_stringspinner, R.id.stringSpinner_txt, spinnerCategories);
        categoriesSpinner.setAdapter(adapter);

        final String[] categories = {"photo", "drawing", "music", "video", "cartoon", "novel"};
        categoriesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                uri = null;
                BlankFragment blankFragment = new BlankFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.upload_fragmentContainer, blankFragment).commit();
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
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                if (title.getText().length() <= 0) {
                    Toast.makeText(UploadActivity.this, R.string.title_not_entered, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (title.getText().length() > AppHelper.MAX_TITLE_SIZE) {
                    Toast.makeText(UploadActivity.this, R.string.title_over_chars, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (description.getText().length() > AppHelper.MAX_DESCRIPTION_SIZE) {
                    Toast.makeText(UploadActivity.this, R.string.description_over_chars, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!isText && (uri == null)) {
                    Toast.makeText(UploadActivity.this, R.string.file_not_uploaded, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (isText && textContent.length() <= 0) {
                    Toast.makeText(UploadActivity.this, R.string.text_not_entered, Toast.LENGTH_SHORT).show();
                    return;
                }

                loadingDialog = new LoadingDialog(UploadActivity.this, R.layout.alert_uploading);
                loadingDialog.show();
                sendRequest();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        addTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tagEdt.getText().length() <= 0 ) {
                    Toast.makeText(UploadActivity.this, getString(R.string.tag_not_entered), Toast.LENGTH_SHORT).show();
                    return;
                } else if (tagEdt.getText().length() > 20) {
                    Toast.makeText(UploadActivity.this, getString(R.string.tag_over_chars), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (tagEdt.getText().toString().contains("/")) {
                    Toast.makeText(UploadActivity.this, getString(R.string.tag_entered_slash), Toast.LENGTH_SHORT).show();
                    return;
                }

                tagTxt.setText(tagTxt.getText() + "#" + tagEdt.getText().toString() + " ");
                tag += tagEdt.getText().toString() + "/";
                tagEdt.setText(null);
            }
        });

        resetTags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tagTxt.setText(null);
                tag = null;
            }
        });
    }

    private void getFile(int position) {
        Intent intent;
        if (position == 0) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.putExtra(intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.setType("image/*");
            getPhotoFile.launch(intent);
        } else if (position == 1) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.putExtra(intent.EXTRA_ALLOW_MULTIPLE, true);
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
            intent.putExtra(intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.setType("image/*");
            getCartoonFile.launch(intent);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void sendRequest() {
        if (category.equals("novel")) {
            String content = textContent.getText().toString();

            RequestBody body = RequestBody.create(MediaType.parse("text/plain"), content);
            MultipartBody.Part part = MultipartBody.Part.createFormData("file", "sendText.txt", body);

            api = RetrofitClient.getRetrofit().create(ApiInterface.class);
            Call<String> call = api.postUpload(part, category, "txt", title.getText().toString(), description.getText().toString(), tag, AppHelper.getAccessingUserid());

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
                    loadingDialog.off();
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    AppHelper.checkError(UploadActivity.this, AppHelper.RESPONSE_ERROR);
                    loadingDialog.off();
                    t.printStackTrace();
                }
            });
        } else if (category.equals("cartoon") || category.equals("photo") || category.equals("drawing")) {
            if (uris == null || uris.size()==1) {
                MultipartBody.Part part = UriParser.uriToMultipart(uri, "file", getContentResolver());
                api = RetrofitClient.getRetrofit().create(ApiInterface.class);
                Call<String> call = api.postUpload(part, category, MimeTypeMap.getSingleton().getExtensionFromMimeType(getContentResolver().getType(uri)),
                        title.getText().toString(), description.getText().toString(), tag, AppHelper.getAccessingUserid());

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
                        loadingDialog.off();
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        AppHelper.checkError(UploadActivity.this, AppHelper.RESPONSE_ERROR);
                        loadingDialog.off();
                        t.printStackTrace();
                    }
                });
            } else {
                if (uris.size() > 20) {
                    Toast.makeText(this, getString(R.string.multiple_upload_over_items), Toast.LENGTH_SHORT).show();
                    return;
                }

                ArrayList<MultipartBody.Part> parts = new ArrayList<>();
                String extensions = "";
                for (int i=0; i<uris.size(); i++) {
                    MultipartBody.Part part = UriParser.uriToMultipart(uris.get(i), "file"+i, getContentResolver());
                    extensions += (MimeTypeMap.getSingleton().getExtensionFromMimeType(getContentResolver().getType(uri)) + "/");
                    parts.add(part);
                }
                api = RetrofitClient.getRetrofit().create(ApiInterface.class);
                Call<String> call = api.postMultipleUpload(parts, category, extensions, title.getText().toString(), description.getText().toString(), tag, AppHelper.getAccessingUserid());

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
                        loadingDialog.off();
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        AppHelper.checkError(UploadActivity.this, AppHelper.RESPONSE_ERROR);
                        loadingDialog.off();
                        t.printStackTrace();
                    }
                });
            }
        } else {
            MultipartBody.Part part = UriParser.uriToMultipart(uri, "file", getContentResolver());
            api = RetrofitClient.getRetrofit().create(ApiInterface.class);
            Call<String> call = api.postUpload(part, category, MimeTypeMap.getSingleton().getExtensionFromMimeType(getContentResolver().getType(uri)),
                    title.getText().toString(), description.getText().toString(), tag, AppHelper.getAccessingUserid());

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
                    loadingDialog.off();
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    AppHelper.checkError(UploadActivity.this, AppHelper.RESPONSE_ERROR);
                    loadingDialog.off();
                    t.printStackTrace();
                }
            });
        }
    }
}
