package com.example.summar.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;

import com.example.summar.model.News;
import com.example.summar.model.NewsDataSource;
import com.example.summar.model.NewsDataSourceFactory;

public class NewsViewModel extends ViewModel {

    public LiveData<PagedList<News>> newsPagedList;
    public LiveData<PageKeyedDataSource<Integer,News>> liveDataSource;

    public NewsViewModel(){
        NewsDataSourceFactory newsDataSourceFactory = new NewsDataSourceFactory();
        liveDataSource=newsDataSourceFactory.getNewsLiveDataSource();

        PagedList.Config config =
                (new PagedList.Config.Builder())
                        .setEnablePlaceholders(false)
                        .setPageSize(NewsDataSource.PAGE_SIZE)
                        .build();
        newsPagedList = (new LivePagedListBuilder(newsDataSourceFactory,config)).build();
    }

}