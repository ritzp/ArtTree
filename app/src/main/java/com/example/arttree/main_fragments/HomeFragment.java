package com.example.arttree.main_fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arttree.ContentActivity;
import com.example.arttree.R;
import com.example.arttree.adapters.HomeContentListAdapter;
import com.example.arttree.adapters.OnItemClickListener;
import com.example.arttree.app.AppHelper;
import com.example.arttree.app.LoadingDialog;
import com.example.arttree.classes.Content;
import com.example.arttree.http.ApiInterface;
import com.example.arttree.http.RetrofitClient;
import com.example.arttree.http.response.ContentListResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class HomeFragment extends Fragment {

    private ApiInterface api;

    private ArrayList<Content> contentArray;
    private HomeContentListAdapter adapter;

    private RecyclerView recycler;

    private LoadingDialog loadingDialog;

    public static Context context;
    public static FragmentManager childFragmentManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentArray = new ArrayList<>();
        adapter = new HomeContentListAdapter(contentArray);
        context = getActivity();
        childFragmentManager = getChildFragmentManager();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.main_fragment_home, container, false);

        recycler = root.findViewById(R.id.home_contentList);

        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler.setAdapter(adapter);

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int pos) {
                Intent intent = new Intent(getActivity(), ContentActivity.class);
                Content content = contentArray.get(pos);
                intent.putExtra("contentId", content.getContentId());
                startActivity(intent);
            }
        });

        loadingDialog = new LoadingDialog(getActivity(), R.layout.alert_loading);
        loadingDialog.show();
        sendRequest();

        return root;
    }

    private void sendRequest() {
        api = RetrofitClient.getRetrofit().create(ApiInterface.class);
        Call<ContentListResponse> call = api.postContentList("home", "", AppHelper.getAccessingUserid());

        call.enqueue(new Callback<ContentListResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<ContentListResponse> call, retrofit2.Response<ContentListResponse> response) {
                for (int i=0; i<response.body().getContent().size(); i++) {
                    contentArray.add(new Content(
                                    response.body().getContent().get(i).getContentId(),
                                    response.body().getContent().get(i).getExtension(),
                                    response.body().getContent().get(i).getTitle(),
                                    response.body().getContent().get(i).getTag(),
                                    response.body().getContent().get(i).getViews(),
                                    response.body().getContent().get(i).getLikes(),
                                    response.body().getContent().get(i).getUserId(),
                                    response.body().getContent().get(i).getNickname()
                            )
                    );
                }
                adapter.notifyDataSetChanged();
                loadingDialog.off();
            }

            @Override
            public void onFailure(Call<ContentListResponse> call, Throwable t) {
                AppHelper.checkError(HomeFragment.this.getActivity(), AppHelper.RESPONSE_ERROR);
                t.printStackTrace();
                loadingDialog.off();
            }
        });
    }
}