package com.example.creators.app;

import android.app.Application;
import android.content.Context;

public class AppHelper extends Application {
    private static String ACCESSING_USERID = null;
    private static String ACCESSING_USERPASS = null;

    public static String getAccessingUserid() {
        return ACCESSING_USERID;
    }

    public static String getAccessingUserpass() {
        return ACCESSING_USERPASS;
    }

    public static void setAccessingUserid(String id) {
        AppHelper.ACCESSING_USERID = id;
    }

    public static void setAccessingUserpass(String pass) {
        AppHelper.ACCESSING_USERPASS = pass;
    }
}
