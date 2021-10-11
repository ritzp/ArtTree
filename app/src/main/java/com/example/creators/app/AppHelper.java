package com.example.creators.app;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.creators.R;

import java.util.Locale;

public class AppHelper extends Application {
    private static String ACCESSING_USERID = null;
    private static String ACCESSING_USERPASS = null;

    private static Locale LOCALE = Locale.getDefault();

    public static String getAccessingUserid() {
        return ACCESSING_USERID;
    }

    public static void setAccessingUserid(String id) {
        AppHelper.ACCESSING_USERID = id;
    }

    public static void setAccessingUserpass(String pass) {
        AppHelper.ACCESSING_USERPASS = pass;
    }

    public static String SERVER_ERROR = "SERVER ERROR";
    public static String CODE_ERROR = "CODE ERROR";
    public static String RESPONSE_ERROR = "RESPONSE ERROR";

    public static boolean checkError(Context context, String message) {
        if (message.equals(SERVER_ERROR)) {
            Toast.makeText(context, context.getString(R.string.server_error), Toast.LENGTH_SHORT).show();
            return false;
        } else if (message.equals(CODE_ERROR)) {
            Toast.makeText(context, R.string.code_error, Toast.LENGTH_SHORT).show();
            return false;
        } else if (message.equals(RESPONSE_ERROR)) {
            Toast.makeText(context, R.string.response_error, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public static Locale getLocale() {
        return LOCALE;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void setLocale(String locale) {
        LOCALE = Locale.forLanguageTag(locale);
    }

    public static int MAX_ID_SIZE = 20;
    public static int MAX_PASSWORD_SIZE = 20;
    public static int MAX_INTRODUCTION_SIZE = 200;
    public static int MAX_TITLE_SIZE = 50;
    public static int MAX_DESCRIPTION_SIZE = 200;
    public static int MAX_COMMENT_SIZE = 200;
}
