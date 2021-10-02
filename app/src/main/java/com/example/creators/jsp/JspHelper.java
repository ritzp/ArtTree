package com.example.creators.jsp;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.creators.R;
import com.example.creators.app.AppHelper;

public class JspHelper {
    private static String SERVER_IP_ADDRESS = "10.0.2.2:8080";
    //public static String CHARSET = "UTF-8";
    public static RequestQueue REQUEST_QUEUE;

    public static String getStringURL(String docName) {
        return "http://" + SERVER_IP_ADDRESS + "/Creators/requests/" + docName + ".jsp";
    }

    public static String getImageURL(String imgDir, String imgName, String imgType) {
        return "http://" + SERVER_IP_ADDRESS + "/Creators/images/" + imgDir + "/" + imgName + "." + imgType;
    }

    /*public static String removeTag(String doc) {
        return doc.replaceAll("(<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>)", "");
    }*/

    public static boolean checkMessage(Context context, String message) {
        if (message.equals("SERVER ERROR")) {
            Toast.makeText(context, context.getString(R.string.server_error), Toast.LENGTH_SHORT).show();
            return false;
        } else if (message.equals("RESPONSE ERROR")) {
            Toast.makeText(context, R.string.response_error, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
