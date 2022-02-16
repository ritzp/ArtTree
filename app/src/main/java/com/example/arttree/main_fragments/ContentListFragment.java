package com.example.arttree.main_fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arttree.ContentActivity;
import com.example.arttree.MainActivity;
import com.example.arttree.R;
import com.example.arttree.adapters.ContentListAdapter;
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

public class ContentListFragment extends Fragment {

    private ApiInterface api;
    private Bundle bundle;

    private ArrayList<Content> contentArray;
    private ContentListAdapter adapter;

    private LinearLayout searchHeader, categoryHeader, header;
    private TextView searchHeaderText, categoryHeaderText, headerText;
    private RecyclerView list;
    private ImageView categoryIcon;

    private LoadingDialog loadingDialog;

    private String searchMethod, keyword = null;

    public static Context context;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this.getActivity();
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
        header = root.findViewById(R.id.conlist_header);
        searchHeaderText = root.findViewById(R.id.conlist_txt_searchHeader);
        categoryHeaderText = root.findViewById(R.id.conlist_txt_categoryHeader);
        headerText = root.findViewById(R.id.conlist_txt_header);
        list = root.findViewById(R.id.conlist_contentlist);
        categoryIcon = root.findViewById(R.id.conlist_img_categoryIcon);

        Bundle bundle = getArguments();
        if (bundle.getString("searchMethod").equals("category")) {
            searchHeader.setVisibility(View.GONE);
            header.setVisibility(View.GONE);
            categoryHeader.setVisibility(View.VISIBLE);
            if (keyword.equals("Photo")) {
                categoryHeaderText.setText(getContext().getString(R.string.photo));
            } else if (keyword.equals("Drawing")) {
                categoryHeaderText.setText(getContext().getString(R.string.drawing));
            } else if (keyword.equals("Music")) {
                categoryHeaderText.setText(getContext().getString(R.string.music));
            } else if (keyword.equals("Video")) {
                categoryHeaderText.setText(getContext().getString(R.string.video));
            } else if (keyword.equals("Cartoon")) {
                categoryHeaderText.setText(getContext().getString(R.string.cartoon));
            } else if (keyword.equals("Novel")) {
                categoryHeaderText.setText(getContext().getString(R.string.novel));
            }
            categoryIcon.setImageResource(bundle.getInt("icon"));
        } else if (bundle.getString("searchMethod").equals("search")) {
            categoryHeader.setVisibility(View.GONE);
            header.setVisibility(View.GONE);
            searchHeader.setVisibility(View.VISIBLE);
            searchHeaderText.setText(keyword);
        } else if (bundle.getString("searchMethod").equals("liked")) {
            searchHeader.setVisibility(View.GONE);
            categoryHeader.setVisibility(View.GONE);
            header.setVisibility(View.VISIBLE);
            headerText.setText(getString(R.string.liked_content));
        } else if (bundle.getString("searchMethod").equals("subscriptions")) {
            searchHeader.setVisibility(View.GONE);
            categoryHeader.setVisibility(View.GONE);
            header.setVisibility(View.VISIBLE);
            headerText.setText(getString(R.string.subscriptions));
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
