package com.example.creators.http;

import com.example.creators.http.response.ContentListResponse;
import com.example.creators.http.response.ContentResponse;
import com.example.creators.http.response.MyContentListResponse;
import com.example.creators.http.response.MyPageResponse;
import com.example.creators.http.response.SignInResponse;
import com.example.creators.http.response.classes.Like;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiInterface {
    @FormUrlEncoded
    @POST("Creators/app_requests/sign_in.jsp")
    Call<SignInResponse> postSignIn(
            @Field("id") String id,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("Creators/app_requests/my_page.jsp")
    Call<MyPageResponse> postMyPage(
            @Field("userId") String userId
    );

    @FormUrlEncoded
    @POST("Creators/app_requests/my_content_list.jsp")
    Call<MyContentListResponse> postMyContentList(
            @Field("userId") String userId
    );

    @FormUrlEncoded
    @POST("Creators/app_requests/content_list.jsp")
    Call<ContentListResponse> postContentList(
            @Field("searchMethod") String searchMethod,
            @Field("keyword") String keyword,
            @Field("userId") String userId
    );

    @FormUrlEncoded
    @POST("Creators/app_requests/content.jsp")
    Call<ContentResponse> postContent(
            @Field("userId") String userId,
            @Field("contentId") String contentId,
            @Field("isFirst") String isFirst
    );

    @Multipart
    @POST("Creators/app_requests/upload")
    Call<String> postUpload(
            @Part MultipartBody.Part file,
            @Query("category") String category,
            @Query("extension") String extension,
            @Query("title") String title,
            @Query("description") String description,
            @Query("userId") String userId
    );

    @Multipart
    @POST("Creators/app_requests/upload_multiple")
    Call<String> postMultipleUpload(
            @Part ArrayList<MultipartBody.Part> files,
            @Query("category") String category,
            @Query("extensions") String extensions,
            @Query("title") String title,
            @Query("description") String description,
            @Query("userId") String userId
    );

    @FormUrlEncoded
    @POST("Creators/app_requests/comment.jsp")
    Call<String> postComment(
            @Field("contentId") String contentId,
            @Field("userId") String userId,
            @Field("comment") String comment
    );

    @FormUrlEncoded
    @POST("Creators/app_requests/like.jsp")
    Call<Like> postLike(
            @Field("userId") String userId,
            @Field("contentId") String contentId
    );

    @Multipart
    @POST("Creators/app_requests/my_page_edit")
    Call<String> postMyPageEditWithParts(
            @Part List<MultipartBody.Part> parts,
            @Query("userId") String userId,
            @Query("nickname") String nickname,
            @Query("introduction") String introduction
    );

    @POST("Creators/app_requests/my_page_edit.jsp")
    Call<String> postMyPageEdit(
            @Query("userId") String userId,
            @Query("nickname") String nickname,
            @Query("introduction") String introduction
    );

    @FormUrlEncoded
    @POST("Creators/app_requests/sign_up.jsp")
    Call<String> postSignUp(
            @Field("method") String method,
            @Field("emailPhone") String emailPhone,
            @Field("userId") String userId,
            @Field("password") String password,
            @Field("nickname") String nickname,
            @Field("introduction") String introduction
    );

    @Multipart
    @POST("Creators/app_requests/sign_up_icon")
    Call<String> postSignUpIcon(
            @Part MultipartBody.Part icon,
            @Query("userId") String userId
    );

    @Multipart
    @POST("Creators/app_requests/sign_up_header")
    Call<String> postSignUpHeader(
            @Part MultipartBody.Part header,
            @Query("userId") String userId
    );

    @FormUrlEncoded
    @POST("Creators/app_requests/check_userId.jsp")
    Call<String> postCheckUserId(
            @Field("id") String id
    );

    @FormUrlEncoded
    @POST("Creators/app_requests/sign_up_error.jsp")
    Call<String> postSignUpError(
            @Field("userId") String userId
    );

    @FormUrlEncoded
    @POST("Creators/app_requests/delete_content.jsp")
    Call<String> postDeleteContent(
            @Field("contentId") String contentId
    );

    @FormUrlEncoded
    @POST("Creators/app_requests/delete_comment.jsp")
    Call<String> postDeleteComment(
            @Field("commentId") String commentId
    );

    @FormUrlEncoded
    @POST("Creators/app_requests/delete_account.jsp")
    Call<String> postDeleteAccount(
            @Field("userId") String userId,
            @Field("password") String password,
            @Field("confirmPassword") String confirmPassword
    );
}
