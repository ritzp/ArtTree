package com.example.creators.main_fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.creators.ContentActivity;
import com.example.creators.R;
import com.example.creators.adapters.HomeContentListAdapter;
import com.example.creators.adapters.OnItemClickListener;
import com.example.creators.app.AppHelper;
import com.example.creators.classes.Content;
import com.example.creators.http.ApiInterface;
import com.example.creators.http.RetrofitClient;
import com.example.creators.http.response.ContentListResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class HomeFragment extends Fragment {

    private ApiInterface api;

    private ArrayList<Content> contentArray;
    private HomeContentListAdapter adapter;

    private RecyclerView recycler;

    public static Context context;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentArray = new ArrayList<>();
        adapter = new HomeContentListAdapter(contentArray);
        context = getActivity();
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
                                    response.body().getContent().get(i).getTitle(),
                                    response.body().getContent().get(i).getViews(),
                                    response.body().getContent().get(i).getLikes(),
                                    response.body().getContent().get(i).getUserId(),
                                    response.body().getContent().get(i).getNickname()
                            )
                    );
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ContentListResponse> call, Throwable t) {
                AppHelper.checkError(HomeFragment.this.getActivity(), AppHelper.RESPONSE_ERROR);
                t.printStackTrace();
            }
        });
    }
}