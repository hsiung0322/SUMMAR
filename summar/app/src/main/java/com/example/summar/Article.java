package com.example.summar;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.summar.model.News;
import com.example.summar.service.RetrofitClient;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Article extends AppCompatActivity  {
    TextView article_tv,title_tv,date_tv,classificationbtn,tags_tv;
    ImageView imageView;
    Button next,back,citizen;
    ImageButton collection;
    String sa="",title="",category="",date="",url="",pic="",keyword="",id="";
    ProgressDialog mDialog;
    private TabLayout mTabs;
    private ViewPager mViewPager;
    JSONArray jsonArray;
    int i=0; //第一篇
    String c;
    int flag = 0;
    ArrayList<News> newsList;
    ArrayList<String> collections=new ArrayList<>(); //存取使用者收藏
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article);
        next=findViewById(R.id.b);
        back=findViewById(R.id.n);
        collection=findViewById(R.id.collectbtn);
        article_tv =findViewById(R.id.sa);
        title_tv =findViewById(R.id.title);
        date_tv = findViewById(R.id.date);
        classificationbtn = findViewById(R.id.classificationbtn);
        imageView=findViewById(R.id.iv);
        tags_tv=findViewById(R.id.tags);
        //讀取資料等待
        mDialog = new ProgressDialog(this);
        mDialog.setMessage("Loading ...");
        mDialog.setCancelable(false);


        //分類傳過來
        c = getIntent().getStringExtra("c");
        if(c!=null) {
            getNewsByClassification(c);//取資料(分類)
        }
        else{//首頁、搜尋過來的資料
            title=getIntent().getStringExtra("title");
            sa=getIntent().getStringExtra("sa");
            category=getIntent().getStringExtra("category");
            date=getIntent().getStringExtra("date");
            url=getIntent().getStringExtra("url");
            pic=getIntent().getStringExtra("pic");
            keyword=getIntent().getStringExtra("keyword");
            //顯示在畫面上
            onlayout(title,sa,category,date,keyword);
        }

        //判斷該文章是否被收藏
        for(int i=0;i<collections.size();i++){
            if(id.equals(collections.get(i))){ //已經存在
                flag=1;
                collection.setImageResource(R.drawable.pencil);
            }
        }


        mTabs =findViewById(R.id.tablayout);
        mTabs.addTab(mTabs.newTab().setText("最新"));
        mTabs.addTab(mTabs.newTab().setText("熱門"));

        mViewPager = findViewById(R.id.viewPager);
        mViewPager.setAdapter(new SamplePagerAdapter());

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabs));
        mTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private void getNewsByClassification(String c) {
        RetrofitClient.getInstance()
                .getAPI()
                .getNewsByCategoryName(c)
                .enqueue(new Callback<List<News>>() {
                    @Override
                    public void onResponse(Call<List<News>> call, Response<List<News>> response) {
                        newsList=new ArrayList<>(response.body());
                        loadInto();
                    }
                    @Override
                    public void onFailure(Call<List<News>> call, Throwable t) {
                        Log.d("ERROR",t.getMessage());
                    }
                });
    }

    private void onlayout(String t,String s,String c,String d,String w) {
        mDialog.show();
        classificationbtn.setText(c);
        title_tv.setText(t);
        date_tv.setText(d);
        article_tv.setText(s);
        tags_tv.setText("#"+w);

        //上下篇的按鈕消失
        next.setVisibility(View.GONE);
        back.setVisibility(View.GONE);

        //圖片顯現
        if(pic==null || pic.isEmpty()){
            imageView.setImageResource(R.drawable.logo); //沒有圖片顯示的
        }
        else{
            Glide
                    .with(this)
                    .load(pic)
                    .skipMemoryCache(false)
                    .placeholder(R.drawable.loading_timage) //等待時顯現的圖片
                    .error(R.drawable.fail)//錯誤時顯現的圖片
                    //.centerCrop()
                    .fitCenter()
                    .into(imageView);
        }
        mDialog.dismiss(); //結束等待loading...
    }

    private void loadInto(){
        if(newsList.size()<=0){
            mDialog.dismiss(); //結束等待loading...
        }
        if(newsList.size()>0) {
            i=0; //最新的
            News news = newsList.get(i);
            title = news.getTitle();
            category = news.getClassification();
            date = news.getDate();
            url = news.getUrl();
            pic = news.getPic_url();
            sa = news.getSummary();
            keyword=news.getKeyword();

            classificationbtn.setText(category);
            title_tv.setText(title);
            date_tv.setText(date);
            article_tv.setText(sa);
            tags_tv.setText("#"+keyword);

            //圖片顯現
            if(pic==null || pic.isEmpty()){
                imageView.setImageResource(R.drawable.logo);
            }
            else{
                Glide
                        .with(this)
                        .load(pic)
                        .skipMemoryCache(false)
                        .placeholder(R.drawable.loading_timage) //等待時顯現的圖片
                        .error(R.drawable.fail)//錯誤時顯現的圖片
                        //.centerCrop()
                        .fitCenter()
                        .into(imageView);
            }

            mDialog.dismiss(); //結束等待loading...
        }
    }
    public void back(View view){
        if(i>0) {
            i--;
            News news = newsList.get(i);
            title = news.getTitle();
            category = news.getClassification();
            date = news.getDate();
            url = news.getUrl();
            pic = news.getPic_url();
            sa = news.getSummary();
            keyword=news.getKeyword();

            classificationbtn.setText(category);
            title_tv.setText(title);
            date_tv.setText(date);
            article_tv.setText(sa);
            tags_tv.setText("#"+keyword);

            //圖片顯現
            if(pic==null || pic.isEmpty()){
                imageView.setImageResource(R.drawable.logo);
            }
            else{
                Glide
                        .with(this)
                        .load(pic)
                        .skipMemoryCache(false)
                        .placeholder(R.drawable.loading_timage) //等待時顯現的圖片
                        .error(R.drawable.fail)//錯誤時顯現的圖片
                        //.centerCrop()
                        .fitCenter()
                        .into(imageView);
            }
        }
        else{i=0;}
    }
    public void next(View view){
        if(i<newsList.size()-1) {
            i++;

            News news = newsList.get(i);
            title = news.getTitle();
            category = news.getClassification();
            date = news.getDate();
            url = news.getUrl();
            pic = news.getPic_url();
            sa = news.getSummary();
            keyword=news.getKeyword();

            classificationbtn.setText(category);
            title_tv.setText(title);
            date_tv.setText(date);
            article_tv.setText(sa);
            tags_tv.setText("#"+keyword);

            //圖片顯現
            if(pic==null || pic.isEmpty()){
                imageView.setImageResource(R.drawable.logo);
            }
            else{
                Glide
                        .with(this)
                        .load(pic)
                        .skipMemoryCache(false)
                        .placeholder(R.drawable.loading_timage) //等待時顯現的圖片
                        .error(R.drawable.fail)//錯誤時顯現的圖片
                        //.centerCrop()
                        .fitCenter()
                        .into(imageView);
            }
        }
        else{
            //載入更多
            i=newsList.size()-1;
        }
    }
    public void openurl(View view) {
        Intent intent = new Intent();
        intent.setClass(Article.this , Web_url.class);
        Bundle bundle = new Bundle();
        bundle.putString("url",url);
        intent.putExtras(bundle);
        startActivity(intent);
    }
    class SamplePagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return o == view;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "Item " + (position + 1);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = getLayoutInflater().inflate(R.layout.pager_item,
                    container, false);
            container.addView(view);
            TextView title = (TextView) view.findViewById(R.id.item_title);
            title.setText("");
            return view;
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
