package com.example.arttree.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit;
    private static String baseUrl = "http://***.***.***.***:8080/";

    public static Retrofit getRetrofit() {
        Gson gson = new GsonBuilder().setLenient().create();

        if (retrofit == null) {
            Retrofit.Builder builder = new Retrofit.Builder();
            builder.baseUrl(baseUrl);
            builder.addConverterFactory(GsonConverterFactory.create(gson));

            retrofit = builder.build();
        }
        return retrofit;
    }

    public static String getBaseUrl() {
        return baseUrl;
    }

    public static String getContentUrl(String category, String contentId, String extension) {
        return baseUrl + "ArtTree/data/content/" + category + "/" + contentId + "." + extension;
    }

    public static String getIconUrl(String userId) {
        return baseUrl + "ArtTree/data/user/icon/" + userId + ".png";
    }

    public static String getHeaderUrl(String userId) {
        return baseUrl + "ArtTree/data/user/header/" + userId + ".jpg";
    }
}
