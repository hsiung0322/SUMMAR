package com.example.summar;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.summar.adapter.NewsAdapter;
import com.example.summar.adapter.NewsSearchAdapter;
import com.example.summar.model.ItemClickListener;
import com.example.summar.model.News;
import com.example.summar.service.RetrofitClient;
import com.example.summar.viewmodel.NewsViewModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class home extends AppCompatActivity implements View.OnClickListener,Serializable{
    ConnectivityManager manager;
    Button b1,b2,b3,b4,b5; //工具列按鈕
    private RecyclerView recyclerView;
    private static ViewPager viewPager;
    private ImageView [] dots; // ViewPager上的点点
    private int dotscount=5;
    TextView tv;
    String useremail; //使用者信箱
    String username; //使用者名稱
    ArrayList<News> newsList_hot; //存放熱門
    ViewPagerAdapter viewPagerAdapter;

    private static Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    int currentItem = viewPager.getCurrentItem();
                    currentItem++;
                    viewPager.setCurrentItem(currentItem);
                    mHandler.sendEmptyMessageDelayed(1, 4000);
                    break;
                default:
                    break;
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("SUMMAR");
        setSupportActionBar(toolbar);
        //ViewStub
//        ((ViewStub)findViewById(R.id.stub)).setVisibility(View.VISIBLE);
        //定義工具列跟監聽
        b1=findViewById(R.id.bt1);
//        b1.setOnClickListener(this);
        b1.setBackgroundResource(R.drawable.homeclick);
        b2=findViewById(R.id.bt2);
        b2.setOnClickListener(this);
        b3=findViewById(R.id.bt3);
        b3.setOnClickListener(this);
        b4=findViewById(R.id.bt4);
        b4.setOnClickListener(this);
        b5=findViewById(R.id.bt5);
        b5.setOnClickListener(this);
        tv=findViewById(R.id.textView);
        //檢測網路是否連線
        checkNetworkState();

        //使用者帳號（信箱）
        useremail=SharedPrefManger.getInstance(this).getUserEmail();
        //使用者名稱
        username=SharedPrefManger.getInstance(this).getUserName();

        //定義title顯現的地方
        tv=findViewById(R.id.textwhite);

        //初始化recyclerview
        initRecyclerView();

        //初始化viewpager
        initviewpager();

        //定義小圓點...
        LinearLayout sliderDotspanel = (LinearLayout) findViewById(R.id.viewGroup);
        dots = new ImageView[dotscount];
        for(int i = 0;i<dotscount;i++){
            ImageView iv = new ImageView(home.this);
            if (i == 0) {
                iv.setImageResource(R.drawable.active_dot); //當前（被選到的）的底線
            } else {
                iv.setImageResource(R.drawable.nonactive_dot);
            }
            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.nonactive_dot));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(8,0,8,0);
            sliderDotspanel.addView(dots[i],params);
        }
        dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.active_dot)); //預設第一張
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) { // 當頁面被選取時執行
                for(int i = 0;i<dotscount;i++){
                    if(i==position%5) {
                        dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
                    }
                    else {
                        dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactive_dot));
                    }
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        mHandler.obtainMessage(1).sendToTarget();
    }

    private void initviewpager() {
        //定義播放照片
        viewPager =findViewById(R.id.viewpager);
        getNewsLimitFive();
    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        NewsViewModel newsViewModel = ViewModelProviders.of(this).get(NewsViewModel.class);
        final NewsAdapter newsAdapter = new NewsAdapter(this);

        newsViewModel.newsPagedList.observe(this, new Observer<PagedList<News>>() {
            @Override
            public void onChanged(PagedList<News> news) {
                newsAdapter.submitList(news);
            }
        });
        recyclerView.setAdapter(newsAdapter);
    }
    private void getNewsLimitFive() {
        RetrofitClient.getInstance()
                .getAPI()
                .getNewsByPagination(1,5)
                .enqueue(new Callback<List<News>>() {
                    @Override
                    public void onResponse(Call<List<News>> call, Response<List<News>> response) {
                        newsList_hot=new ArrayList<>(response.body());
                        viewPagerAdapter = new ViewPagerAdapter(getApplicationContext(),newsList_hot);
                        viewPager.setAdapter(viewPagerAdapter);
                    }

                    @Override
                    public void onFailure(Call<List<News>> call, Throwable t) {
                        Log.d("ERROR",t.getMessage());
                    }
                });
    }

    //跳轉頁面
    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.bt2:
                // do your code
                intent.setClass(this,classification.class);
                startActivity(intent);
                break;
            case R.id.bt3:
                // do your code
//                startActivity(intent);
                intent.setClass(this,Search.class);
                startActivity(intent);
                break;
            case R.id.bt4:
                // do your code
                intent.setClass(this,following.class);
                startActivity(intent);
                break;
            case R.id.bt5:
                // do your code
                intent.setClass(this,personal.class);
                startActivity(intent);
                break;

            default:
                break;
        }
    }

    //檢測網路是否連線
    private boolean checkNetworkState(){
        boolean flag = false;
        //得到網路連線資訊
                manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        //去進行判斷網路是否連線
        if (manager.getActiveNetworkInfo() != null) {
            flag = manager.getActiveNetworkInfo().isAvailable();
        }
        if (!flag) {
            setNetwork();
        }
        return flag;
    }
    private void setNetwork(){
    Toast.makeText(this, "wifi is closed!", Toast.LENGTH_SHORT).show();
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("網路提示資訊");
        builder.setMessage("網路不可用，如果繼續，請先設定網路！");
        builder.setPositiveButton("設定", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            Intent intent = null;
/**
 * 判斷手機系統的版本！如果API大於10 就是3.0
 * 因為3.0以上的版本的設定和3.0以下的設定不一樣，呼叫的方法不同
 */
            if (android.os.Build.VERSION.SDK_INT > 10) {
                intent = new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS);
            } else {
                intent = new Intent();
                ComponentName component = new ComponentName(
                        "com.android.settings",
                        "com.android.settings.WirelessSettings");
                intent.setComponent(component);
                intent.setAction("android.intent.action.VIEW");
            }
            startActivity(intent);
        }
    });
    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
        }
    });
builder.create();
builder.show();
}
}
