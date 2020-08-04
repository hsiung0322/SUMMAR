package com.example.summar.service;

import com.example.summar.model.Catalog;
import com.example.summar.model.News;

import java.util.List;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SummarService {
    @GET("news") //設置一個GET連線，路徑為news
    Call<List<News>> getNews(); // 取得的回傳資料用News物件接收，連線名稱取為getNews

    @GET("news/cal/{categoryName}")
    Call<List<News>> getNewsByCategoryName(@Path("categoryName") String categoryName); // 取得的回傳資料用News物件接收，連線名稱取為getNewsByCategoryName

    @GET("news/title")
    Call<List<News>> getNewsByTitle(@Query("title") String title);

    @GET("news/title")
    Call<List<News>> getNewsTitleWithPagination(@Query("title") String title,@Query("page") int page, @Query("perpage") int perpage);

    @GET("news")
    Call<List<News>> getNewsByPagination(@Query("page") int page, @Query("perpage") int perpage);

    @GET("catalogList")
    Call<List<Catalog>> getCatalogList();

    @FormUrlEncoded
    @POST("users/register")
    Call<ResponseBody> register(
            @Field("email") String email,
            @Field("password") String password,
            @Field("name") String name,
            @Field("gender") String gender,
            @Field("birth") String birth
    );

    @FormUrlEncoded
    @POST("users/login")
    Call<ResponseBody> login(
            @Field("email") String email,
            @Field("password") String password
    );
}

