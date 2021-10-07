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

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.creators.ContentsActivity;
import com.example.creators.R;
import com.example.creators.adapters.ContentsListAdapter;
import com.example.creators.adapters.OnItemClickListener;
import com.example.creators.app.AppHelper;
import com.example.creators.classes.ContentsList;
import com.example.creators.jsp.JspHelper;
import com.example.creators.jsp.requests.ContentsListRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ContentsListFragment extends Fragment {

    private ArrayList<ContentsList> contentsArray;
    private ContentsListAdapter adapter;

    private LinearLayout searchHeader, categoryHeader;
    private TextView searchHeaderText, categoryHeaderText;
    private RecyclerView list;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentsArray = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.contentslist, container, false);

        adapter = new ContentsListAdapter(contentsArray);

        searchHeader = root.findViewById(R.id.conlist_searchHeader);
        categoryHeader = root.findViewById(R.id.conlist_categoryHeader);
        searchHeaderText = root.findViewById(R.id.conlist_txt_searchHeader);
        categoryHeaderText = root.findViewById(R.id.conlist_txt_categoryHeader);
        list = root.findViewById(R.id.conlist_contentslist);

        Bundle bundle = getArguments();
        if (bundle.getString("route").equals("category")) {
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
                Intent intent = new Intent(getActivity(), ContentsActivity.class);
                ContentsList contents = contentsArray.get(pos);
                intent.putExtra("contentsId", contents.getContentsId());
                startActivity(intent);
            }
        });

        sendRequest();

        return root;
    }

    private void sendRequest() {

        final Response.Listener<String> listener = new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(String response) {
                if (!AppHelper.checkError(getActivity(), response.trim()))
                    return;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("contents");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject js = jsonArray.getJSONObject(i);
                        byte[] decodedIcon = java.util.Base64.getDecoder().decode(js.getString("icon"));
                        Bitmap icon = BitmapFactory.decodeByteArray(decodedIcon, 0, decodedIcon.length);
                        contentsArray.add(new ContentsList(
                                js.getString("contentsId"),
                                js.getString("title"),
                                js.getInt("views"),
                                js.getString("userId"),
                                js.getString("nickname"),
                                icon
                        ));
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    AppHelper.checkError(getActivity(), AppHelper.CODE_ERROR);
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    AppHelper.checkError(getActivity(), AppHelper.CODE_ERROR);
                    e.printStackTrace();
                }
            }
        };
        final Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AppHelper.checkError(getActivity(), AppHelper.RESPONSE_ERROR);
            }
        };
        try {
            final ContentsListRequest request = new ContentsListRequest(listener, errorListener);
            JspHelper.addRequestQueue(getActivity(), request);
        } catch (Exception e) {
            AppHelper.checkError(getActivity(), AppHelper.CODE_ERROR);
            e.printStackTrace();
        }
    }
}
