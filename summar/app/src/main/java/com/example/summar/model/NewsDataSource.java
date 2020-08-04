package com.example.summar.model;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import com.example.summar.service.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsDataSource extends PageKeyedDataSource<Integer,News> {
    public static final int PAGE_SIZE = 20;
    private static final int FIRST_PAGE =1;

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, News> callback) {

        RetrofitClient.getInstance()
                .getAPI()
                .getNewsByPagination(FIRST_PAGE,PAGE_SIZE)
                .enqueue(new Callback<List<News>>() {
                    @Override
                    public void onResponse(Call<List<News>> call, Response<List<News>> response) {
                        if(response.body() != null){
                            callback.onResult(response.body(),null,FIRST_PAGE+1);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<News>> call, Throwable t) {
                        Log.d("ERROR-DataSource",t.getMessage());
                    }
                });

    }

    @Override
    public void loadBefore(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, News> callback) {

        RetrofitClient.getInstance()
                .getAPI()
                .getNewsByPagination(params.key,PAGE_SIZE)
                .enqueue(new Callback<List<News>>() {
                    @Override
                    public void onResponse(Call<List<News>> call, Response<List<News>> response) {
                        Integer key = (params.key >1 )? params.key -1 :null;
                        if(response.body() != null){
                            callback.onResult(response.body(),key);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<News>> call, Throwable t) {

                    }
                });

    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, News> callback) {
        RetrofitClient.getInstance()
                .getAPI()
                .getNewsByPagination(params.key,PAGE_SIZE)
                .enqueue(new Callback<List<News>>() {
                    @Override
                    public void onResponse(Call<List<News>> call, Response<List<News>> response) {
                        if(response.body() != null){
                            Integer key = true? params.key + 1 : null;
                            callback.onResult(response.body(),key);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<News>> call, Throwable t) {

                    }
                });
    }
}
