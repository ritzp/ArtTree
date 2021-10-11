package com.example.creators.content_fragments;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.creators.R;
import com.example.creators.app.AppHelper;
import com.example.creators.http.RetrofitClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class NovelFragment extends Fragment {

    private TextView text;

    private String contentId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.content_fragment_novel, container, false);
        Bundle bundle = getArguments();
        contentId = bundle.getString("contentId");

        text = root.findViewById(R.id.condetailNovel_txt);

        FileDownload fileDownload = new FileDownload();
        fileDownload.execute();

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
                    sb.append(line);
                }
                buffer.close();
                reader.close();

                return sb.toString();
            } catch (Exception e) {
                AppHelper.checkError(NovelFragment.this.getActivity(), AppHelper.CODE_ERROR);
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            text.setText(s);
        }
    }
}