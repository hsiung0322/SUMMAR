package com.example.summar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.summar.model.Catalog;
import com.example.summar.model.News;
import com.example.summar.service.RetrofitClient;
import com.example.summar.service.SummarService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class classification extends AppCompatActivity implements View.OnClickListener {
    Button b1,b2,b3,b4,b5;
    TextView text;
    // 1. 宣告Service
    SummarService summarservice;
    //宣吿button list
    Button[] btn = null;

    //儲存分類的list
    ArrayList<Catalog> category_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.classification);
        Toolbar toolbar =findViewById(R.id.toolbar);
        toolbar.setTitle("分類");
        setSupportActionBar(toolbar);
        //工具列
        b1=findViewById(R.id.bt1);
        b1.setOnClickListener(this);
        b2=findViewById(R.id.bt2);
//        b2.setOnClickListener(this);
        b2.setBackgroundResource(R.drawable.claclick);
        b3=findViewById(R.id.bt3);
        b3.setOnClickListener(this);
        b4=findViewById(R.id.bt4);
        b4.setOnClickListener(this);
        b5=findViewById(R.id.bt5);
        b5.setOnClickListener(this);

        //設定buttonView
        setButtonView();

    }

    private void setButtonView() {
        //LinearLayout 讓button都是水平接下去
        final LinearLayout ll = findViewById(R.id.linearlayout);
        //讀取資料庫分類種類
        // 2. 透過RetrofitClient取得連線基底
        summarservice = RetrofitClient.getInstance().getAPI();
        // 3. 建立連線的Call，此處設置call為summarservice中的getNews()連線
        Call<List<Catalog>> call = summarservice.getCatalogList();
        // 4. 執行call
        call.enqueue(new Callback<List<Catalog>>() {
            @Override
            public void onResponse(Call<List<Catalog>> call, Response<List<Catalog>> response) {
                // 連線成功
                // 回傳的資料已轉成Category物件，可直接用get方法取得特定欄位
                category_list = new ArrayList<>(response.body());

                //根據讀到的分類數 設定button list的長度
                btn = new Button[category_list.size()];

                //先設定layout的width,height,weight
                LinearLayout.LayoutParams para = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
                para.setMargins(30, 20, 30, 0);

                //依據 分類list 生成分類button
                for (int i = 0; i < category_list.size(); i++) {

                    btn[i] = new Button(classification.this);

                    btn[i].setWidth(para.MATCH_PARENT);

                    //根據分類名稱 設定button text
                    btn[i].setText(category_list.get(i).getCatalog_name());
                    //將text color 設定為透明
                    btn[i].setTextColor(Color.TRANSPARENT);


                    //根據分類名稱 設定button圖片
                    set_pic(btn[i]);
                    System.out.println("生成新Button "+btn[i].getText());

                    btn[i].setOnClickListener(btn_Listener);

                    //把設定好的layout設定給btn
                    btn[i].setLayoutParams(para);

                    ll.addView(btn[i]);//在linearlayout下加一個btn，因為btn也是view
                }
            }
            @Override
            public void onFailure(Call<List<Catalog>> call, Throwable t) {
                //連線失敗
                text.setText(t.getMessage());
            }
            //設定分類相對應的背景圖片
            public void set_pic(Button btn) {
                if (btn.getText().equals("全球")) {
                    btn.setBackgroundResource(R.drawable.global_btn);
                } else if (btn.getText().equals("數位")) {
                    btn.setBackgroundResource(R.drawable.digital_btn);
                } else if (btn.getText().equals("產經")) {
                    btn.setBackgroundResource(R.drawable.business_btn);
                } else if (btn.getText().equals("運動")) {
                    btn.setBackgroundResource(R.drawable.sport_btn);
                } else if (btn.getText().equals("生活")) {
                    btn.setBackgroundResource(R.drawable.life_btn);
                }else if (btn.getText().equals("要聞")) {
                    btn.setBackgroundResource(R.drawable.politics_btn);
                }
            }
        });

    }
    //點值分類button
    Button.OnClickListener btn_Listener = new Button.OnClickListener() {

        @Override
        public void onClick(View view) {
            Button b = (Button)view;
            System.out.println("點擊 ： "+b.getText());

        Intent intent = new Intent();
        intent.setClass(getApplicationContext(),Article.class);
        intent.putExtra("c",b.getText());
        startActivity(intent);
        }
    };

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.bt1:
                // code for button when user clicks buttonOne.
                intent.setClass(this, home.class);
                startActivity(intent);
                break;
            case R.id.bt3:
                // do your code
                //Toast.makeText(this,"搜尋",Toast.LENGTH_SHORT).show();
                intent.setClass(this,Search.class);
                startActivity(intent);
//                startActivity(intent);
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

}
