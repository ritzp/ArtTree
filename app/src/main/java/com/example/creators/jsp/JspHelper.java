package com.example.creators.jsp;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

public class JspHelper {
    private static String SERVER_IP_ADDRESS = "10.0.2.2:8080";
    //public static String CHARSET = "UTF-8";
    private static RequestQueue REQUEST_QUEUE;
    private static ImageLoader IMAGE_LOADER;


    public static String getURL(String docName) {
        return "http://" + SERVER_IP_ADDRESS + "/Creators/requests/" + docName + ".jsp";
    }

    public static String getImageURL(String fileName, String extension) {
        return "http://" + SERVER_IP_ADDRESS + "/Creators/data/contents/image/" + fileName + "." + extension;
    }

    public static String getSoundURL(String fileName, String extension) {
        return "http://" + SERVER_IP_ADDRESS + "/Creators/data/contents/sound/" + fileName + "." + extension;
    }

    public static String getVideoURL(String fileName, String extension) {
        return "http://" + SERVER_IP_ADDRESS + "/Creators/data/contents/video/" + fileName + "." + extension;
    }

    public static String getTextURL(String fileName) {
        return "http://" + SERVER_IP_ADDRESS + "/Creators/data/contents/text/" + fileName + ".txt";
    }

    public static void addRequestQueue(Context context, Request request) {
        if (REQUEST_QUEUE == null) {
            REQUEST_QUEUE = Volley.newRequestQueue(context.getApplicationContext());
        }
        REQUEST_QUEUE.add(request);
    }

    public static void cancelRequests(String tag) {
        REQUEST_QUEUE.cancelAll(tag);
    }

    public static ImageLoader getImageLoader() {
        if (IMAGE_LOADER == null) {
            IMAGE_LOADER = new ImageLoader(REQUEST_QUEUE, new BitmapLruCache(1024 * 1024 * 20));
        }
        return IMAGE_LOADER;
    }
}
