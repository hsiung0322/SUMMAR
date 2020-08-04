package com.example.summar.service;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static RetrofitClient instance = new RetrofitClient();
    private SummarService summarservice;

    private RetrofitClient(){
        // 建立OkHttpClient
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(30, TimeUnit.SECONDS)   // 設置連線Timeout 30秒
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
                .build();

        // 設置baseUrl即要連的網站，addConverterFactory用Gson作為資料處理Converter
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.6.200.141:3000")
//                .baseUrl("http://163.13.202.209:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient) // 將okHttpClient加入連線基底
                .build();
        summarservice = retrofit.create(SummarService.class);
    }
    public static synchronized RetrofitClient getInstance(){
        return instance;
    }
    public SummarService getAPI(){
        return summarservice;
    }
}
