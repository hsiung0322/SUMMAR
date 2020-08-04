package com.example.summar;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.summar.adapter.NewsSearchAdapter;
import com.example.summar.model.ItemClickListener;
import com.example.summar.model.News;
import com.example.summar.service.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Search extends AppCompatActivity implements View.OnClickListener{
    Button b1,b2,b3,b4,b5; //工具列按鈕
    SearchView searchView;
    NewsSearchAdapter newsSearchAdapter;
    RecyclerView recyclerView;
    ArrayList<News> newsLiat;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("搜尋");
        setSupportActionBar(toolbar);

        //定義工具列跟監聽
        b1=findViewById(R.id.bt1);
        b1.setOnClickListener(this);
        b2=findViewById(R.id.bt2);
        b2.setOnClickListener(this);
        b3=findViewById(R.id.bt3);
        b3.setBackgroundResource(R.drawable.searched);
//        b3.setOnClickListener(this);
        b4=findViewById(R.id.bt4);
        b4.setOnClickListener(this);
        b5=findViewById(R.id.bt5);
        b5.setOnClickListener(this);

        //定義
        searchView=findViewById(R.id.searchview);//搜尋位址
        searchView.setIconifiedByDefault(true);
        searchView.setFocusable(false);
        setSearch_function();//對searchview進行監聽

        //定義SearchView樣式
        SearchViewTheme(searchView);

        initRecyclerView();
        getNewsByTitle();

    }

    private void getNewsByTitle() {
        progressDialog = createProgressDialog(Search.this);
        RetrofitClient.getInstance()
                .getAPI()
                .getNews()
                .enqueue(new Callback<List<News>>() {
                    @Override
                    public void onResponse(Call<List<News>> call, Response<List<News>> response) {
                        progressDialog.dismiss();
                        newsLiat=new ArrayList<>(response.body());
                        newsSearchAdapter = new NewsSearchAdapter(getApplicationContext(), newsLiat, new ItemClickListener() {
                            @Override
                            public void onItemClick(News news, int position) {
                                //點擊項目時item
                                Intent intent = new Intent(getApplicationContext(),Article.class);
                                intent.putExtra("id", news.getArticle_id());
                                intent.putExtra("title", news.getTitle());
                                intent.putExtra("sa", news.getSummary());
                                intent.putExtra("category", news.getClassification());
                                intent.putExtra("date", news.getDate());
                                intent.putExtra("url", news.getUrl());
                                intent.putExtra("pic", news.getPic_url());
                                intent.putExtra("keyword",news.getKeyword());
                                startActivity(intent);
                            }
                        });
                        recyclerView.setAdapter(newsSearchAdapter);
                    }

                    @Override
                    public void onFailure(Call<List<News>> call, Throwable t) {
                        progressDialog.dismiss();
                        Log.d("ERROR",t.getMessage());
                    }
                });
    }

    public ProgressDialog createProgressDialog(Context mContext) {
        ProgressDialog dialog = new ProgressDialog(mContext);
        try {
            dialog.show();
        } catch (WindowManager.BadTokenException e) {

        }
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_layout);
        return dialog;
    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }


    private void setSearch_function() {//對searchview進行監聽
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            //當完成輸入內容點選搜尋按鈕時會回傳
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            //當文字框每次內容發生改變時方法會回傳
            public boolean onQueryTextChange(String s) {
                newsSearchAdapter.getFilter().filter(s);
                return false;
            }
        });
    }

    public void SearchViewTheme(SearchView searchView){
        searchView.setBackgroundColor(Color.parseColor("#EDE9E5"));//背景顏色
        searchView.setQueryHint("Search");//提示顯示Hint
        searchView.setIconifiedByDefault(false);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.bt1:
                intent.setClass(this, home.class);
                startActivity(intent);
                break;
            case R.id.bt2:
                intent.setClass(this,classification.class);
                startActivity(intent);
                break;
            case R.id.bt4:
                intent.setClass(this,following.class);
                startActivity(intent);
                break;
            case R.id.bt5:
                intent.setClass(this,personal.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

}
