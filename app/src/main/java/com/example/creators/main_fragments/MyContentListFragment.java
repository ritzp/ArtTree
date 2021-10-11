package com.example.creators.main_fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.creators.ContentActivity;
import com.example.creators.MainActivity;
import com.example.creators.R;
import com.example.creators.UploadActivity;
import com.example.creators.adapters.MyContentListAdapter;
import com.example.creators.adapters.OnItemClickListener;
import com.example.creators.app.AppHelper;
import com.example.creators.classes.MyContent;
import com.example.creators.http.ApiInterface;
import com.example.creators.http.RetrofitClient;
import com.example.creators.http.response.MyContentListResponse;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class MyContentListFragment extends Fragment {

    private ApiInterface api;

    private ArrayList<MyContent> myContentArray;
    private MyContentListAdapter adapter;

    private ImageView close;
    private RecyclerView list;
    private FloatingActionButton fab;

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

        sendRequest();

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int pos) {
                Intent intent = new Intent(getActivity(), ContentActivity.class);
                MyContent content = myContentArray.get(pos);
                intent.putExtra("contentId", content.getContentId());
                startActivity(intent);
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
            }

            @Override
            public void onFailure(Call<MyContentListResponse> call, Throwable t) {
                AppHelper.checkError(MyContentListFragment.this.getActivity(), AppHelper.RESPONSE_ERROR);
                t.printStackTrace();
            }
        });
    }
}
