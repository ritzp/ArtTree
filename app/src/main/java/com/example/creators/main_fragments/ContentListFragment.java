package com.example.creators.main_fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.creators.ContentActivity;
import com.example.creators.MainActivity;
import com.example.creators.R;
import com.example.creators.adapters.ContentListAdapter;
import com.example.creators.adapters.OnItemClickListener;
import com.example.creators.app.AppHelper;
import com.example.creators.app.LoadingDialog;
import com.example.creators.classes.Content;
import com.example.creators.http.ApiInterface;
import com.example.creators.http.RetrofitClient;
import com.example.creators.http.response.ContentListResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class ContentListFragment extends Fragment {

    private ApiInterface api;
    private Bundle bundle;

    private ArrayList<Content> contentArray;
    private ContentListAdapter adapter;

    private LinearLayout searchHeader, categoryHeader;
    private TextView searchHeaderText, categoryHeaderText;
    private RecyclerView list;

    private LoadingDialog loadingDialog;

    private String searchMethod, keyword = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = getArguments();
        searchMethod = bundle.getString("searchMethod");
        keyword = bundle.getString("keyword");
        contentArray = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.contentlist, container, false);

        adapter = new ContentListAdapter(contentArray);

        searchHeader = root.findViewById(R.id.conlist_searchHeader);
        categoryHeader = root.findViewById(R.id.conlist_categoryHeader);
        searchHeaderText = root.findViewById(R.id.conlist_txt_searchHeader);
        categoryHeaderText = root.findViewById(R.id.conlist_txt_categoryHeader);
        list = root.findViewById(R.id.conlist_contentlist);

        Bundle bundle = getArguments();
        if (bundle.getString("searchMethod").equals("category")) {
            searchHeader.setVisibility(View.GONE);
            categoryHeader.setVisibility(View.VISIBLE);
            categoryHeaderText.setText(bundle.getString("keyword"));
        } else {
            categoryHeader.setVisibility(View.GONE);
            searchHeader.setVisibility(View.VISIBLE);
            searchHeaderText.setText(bundle.getString("keyword"));
        }
        list.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        list.setAdapter(adapter);

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int pos) {
                Intent intent = new Intent(getActivity(), ContentActivity.class);
                Content content = contentArray.get(pos);
                intent.putExtra("contentId", content.getContentId());
                startActivity(intent);
            }
        });

        searchHeaderText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)ContentListFragment.this.getActivity()).replaceFragmentToSearch();
            }
        });

        loadingDialog = new LoadingDialog(getActivity(), R.layout.alert_loading);
        loadingDialog.show();
        sendRequest();

        return root;
    }

    private void sendRequest() {
        api = RetrofitClient.getRetrofit().create(ApiInterface.class);
        if (searchMethod.equals("category")) {
            if (keyword.equals(getActivity().getString(R.string.photo))) {
                keyword = "photo";
            } else if (keyword.equals(getActivity().getString(R.string.drawing))) {
                keyword = "drawing";
            } else if (keyword.equals(getActivity().getString(R.string.music))) {
                keyword = "music";
            } else if (keyword.equals(getActivity().getString(R.string.video))) {
                keyword = "video";
            } else if (keyword.equals(getActivity().getString(R.string.cartoon))) {
                keyword = "cartoon";
            } else if (keyword.equals(getActivity().getString(R.string.novel))) {
                keyword = "novel";
            }
        }
        Call<ContentListResponse> call = api.postContentList(searchMethod, keyword, AppHelper.getAccessingUserid());

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
                loadingDialog.off();
            }

            @Override
            public void onFailure(Call<ContentListResponse> call, Throwable t) {
                AppHelper.checkError(ContentListFragment.this.getActivity(), AppHelper.RESPONSE_ERROR);
                t.printStackTrace();
                loadingDialog.off();
            }
        });
    }
}
