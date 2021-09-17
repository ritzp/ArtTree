package com.example.creators.db;

import android.content.Context;
import android.widget.Toast;

public class dbHelper {
    private static String SERVER_IP_ADDRESS = "10.0.2.2:8080";

    public static String getServerURL(String docName) {
        return "http://" + SERVER_IP_ADDRESS + "/Creators/" + docName;
    }

    public static String removeTag(String doc) {
        return doc.replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", "");
    }
}
