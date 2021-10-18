package com.example.arttree.main_fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arttree.ContentActivity;
import com.example.arttree.MainActivity;
import com.example.arttree.R;
import com.example.arttree.SignInActivity;
import com.example.arttree.UploadActivity;
import com.example.arttree.adapters.MyContentListAdapter;
import com.example.arttree.adapters.OnItemClickListener;
import com.example.arttree.app.AppHelper;
import com.example.arttree.app.LoadingDialog;
import com.example.arttree.classes.MyContent;
import com.example.arttree.http.ApiInterface;
import com.example.arttree.http.RetrofitClient;
import com.example.arttree.http.response.MyContentListResponse;
import com.example.arttree.main_fragments.settings.SettingsMainFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyContentListFragment extends Fragment {

    private ApiInterface api;

    private ArrayList<MyContent> myContentArray;
    private MyContentListAdapter adapter;

    private ImageView close;
    private RecyclerView list;
    private FloatingActionButton fab;

    private LoadingDialog loadingDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myContentArray = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.mycontentlist, container, false);

        adapter = new MyContentListAdapter(myContentArray);

        close = root.findViewById(R.id.myconlist_img_close);
        list = root.findViewById(R.id.myconlist_conList);
        fab = root.findViewById(R.id.myconlist_fab_upload);

        list.setLayoutManager(new LinearLayoutManager(getActivity()));
        list.setAdapter(adapter);

        loadingDialog = new LoadingDialog(MyContentListFragment.this.getActivity(), R.layout.alert_loading);

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int pos) {
                Intent intent = new Intent(getActivity(), ContentActivity.class);
                MyContent content = myContentArray.get(pos);
                intent.putExtra("contentId", content.getContentId());
                startActivity(intent);
            }
        });

        adapter.setOnDeleteClickListener(new OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int pos) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(MyContentListFragment.this.getActivity());
                alertBuilder.setMessage(getString(R.string.delete_alert_message));
                alertBuilder.setPositiveButton(getString(R.string.delete), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        loadingDialog.show();
                        sendDeleteRequest(myContentArray.get(pos).getContentId());
                    }
                }).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertBuilder.show();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).replaceFragmentToMyPage();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UploadActivity.class);
                startActivity(intent);
            }
        });

        loadingDialog.show();
        sendRequest();

        return root;
    }

    private void sendRequest() {
        api = RetrofitClient.getRetrofit().create(ApiInterface.class);
        Call<MyContentListResponse> call = api.postMyContentList(AppHelper.getAccessingUserid());

        call.enqueue(new Callback<MyContentListResponse>() {
            @Override
            public void onResponse(Call<MyContentListResponse> call, retrofit2.Response<MyContentListResponse> response) {
                for (int i=0; i<response.body().getMyContent().size(); i++) {
                    String type = response.body().getMyContent().get(i).getContentId().substring(0,2);
                    int category;
                    if (type.equals("PH")) {
                        category = R.drawable.ic_photo;
                    } else if (type.equals("DR")) {
                        category = R.drawable.ic_drawing;
                    } else  if (type.equals("MU")) {
                        category = R.drawable.ic_music;
                    } else if (type.equals("VI")) {
                        category = R.drawable.ic_video;
                    } else if (type.equals("CA")) {
                        category = R.drawable.ic_cartoon;
                    } else if (type.equals("NO")) {
                        category = R.drawable.ic_novel;
                    } else {
                        category = R.drawable.ic_photo;
                    }
                    myContentArray.add(new MyContent(
                            response.body().getMyContent().get(i).getContentId(),
                            response.body().getMyContent().get(i).getTitle(),
                            category
                    ));
                }
                adapter.notifyDataSetChanged();
                loadingDialog.off();
            }

            @Override
            public void onFailure(Call<MyContentListResponse> call, Throwable t) {
                AppHelper.checkError(MyContentListFragment.this.getActivity(), AppHelper.RESPONSE_ERROR);
                t.printStackTrace();
                loadingDialog.off();
            }
        });
    }

    private void sendDeleteRequest(String contentId) {
        api = RetrofitClient.getRetrofit().create(ApiInterface.class);
        Call<String> call = api.postDeleteContent(contentId);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body().equals("SUCCESS")) {
                    Toast.makeText(MyContentListFragment.this.getActivity(), getString(R.string.delete_completed), Toast.LENGTH_SHORT).show();
                } else {
                    AppHelper.checkError(MyContentListFragment.this.getActivity(), AppHelper.CODE_ERROR);
                }
                ((MainActivity)MyContentListFragment.this.getActivity()).replaceFragmentToMyContentList();
                loadingDialog.off();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                AppHelper.checkError(MyContentListFragment.this.getActivity(), AppHelper.RESPONSE_ERROR);
                loadingDialog.off();
                t.printStackTrace();
            }
        });
    }
}
