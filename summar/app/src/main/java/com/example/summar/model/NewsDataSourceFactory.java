package com.example.summar.model;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

public class NewsDataSourceFactory extends DataSource.Factory {

    private MutableLiveData<PageKeyedDataSource<Integer, News>> newsLiveDataSource = new MutableLiveData<>();

    @Override
    public DataSource create() {
        NewsDataSource newsDataSource = new NewsDataSource();
        newsLiveDataSource.postValue(newsDataSource);
        return newsDataSource;
    }

    public MutableLiveData<PageKeyedDataSource<Integer, News>> getNewsLiveDataSource() {
        return newsLiveDataSource;
    }
}