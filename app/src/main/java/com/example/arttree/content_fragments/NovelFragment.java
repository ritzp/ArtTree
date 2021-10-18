package com.example.arttree.content_fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.arttree.ContentDetailActivity;
import com.example.arttree.R;
import com.example.arttree.app.AppHelper;
import com.example.arttree.http.RetrofitClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class NovelFragment extends Fragment {

    private TextView text;
    private ImageView hasNext;

    private String contentId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.content_fragment_novel, container, false);
        Bundle bundle = getArguments();
        contentId = bundle.getString("contentId");

        text = root.findViewById(R.id.contentNovel_txt);
        hasNext = root.findViewById(R.id.contentNovel_img_hasnext);

        FileDownload fileDownload = new FileDownload();
        fileDownload.execute();

        hasNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NovelFragment.this.getActivity(), ContentDetailActivity.class);
                intent.putExtra("contentId", contentId);
                intent.putExtra("text", text.getText().toString());
                startActivity(intent);
            }
        });

        return root;
    }

    class FileDownload extends AsyncTask<Void, String, String> {
        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(RetrofitClient.getContentUrl("novel", contentId,  "txt"));
                URLConnection conn = url.openConnection();
                conn.connect();

                InputStreamReader reader = new InputStreamReader(conn.getInputStream(), "UTF-8");
                BufferedReader buffer = new BufferedReader(reader);
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line=buffer.readLine()) != null) {
                    sb.append(line + "\n");
                }
                buffer.close();
                reader.close();

                return sb.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return AppHelper.CODE_ERROR;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            text.setText(s);
        }
    }
}