package com.example.creators.contents_fragments;

import android.app.ActionBar;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.creators.R;
import com.example.creators.app.AppHelper;
import com.example.creators.jsp.JspHelper;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class TextFragment extends Fragment {

    private TextView text;

    private String contentsId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.contents_fragment_text, container, false);
        Bundle bundle = getArguments();
        contentsId = bundle.getString("contentsId");

        text = root.findViewById(R.id.condetailTxt_txt);

        FileDownload fileDownload = new FileDownload();
        fileDownload.execute();

        return root;
    }

    class FileDownload extends AsyncTask<Void, String, String> {
        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(JspHelper.getTextURL(contentsId));
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
                AppHelper.checkError(getActivity(), AppHelper.CODE_ERROR);
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