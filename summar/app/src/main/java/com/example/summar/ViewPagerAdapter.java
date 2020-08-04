package com.example.summar;

import android.content.Context;
import android.content.Intent;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.summar.model.News;

import java.util.List;

public class ViewPagerAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    List<News> list; //存放新聞的地方
    private Integer[] images = {R.drawable.fail,R.drawable.fail,R.drawable.fail,R.drawable.fail,R.drawable.fail};

    public ViewPagerAdapter(Context context,List<News> list){
        this.context = context;
        this.list=list;
    }

    @Override
    public int getCount(){
        //因為要實現輪播，所以將數值設置的大一些
        return Integer.MAX_VALUE;
    }
    @Override
    public boolean isViewFromObject(View view, Object object){
        return view==object;
    }
    @Override
    public Object instantiateItem(final ViewGroup container, int position){
        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.sideshow_layout,null);

            final int realPosition = position % images.length;
            if(list.size()<=0){}
            else {
                News news = list.get(realPosition);
                //設定背景圖片
                TextView tv = view.findViewById(R.id.textwhite); //標題
                ImageView imageView =view.findViewById(R.id.iv);

                String title = news.getTitle();
                String pic = news.getPic_url();

                tv.setText(title);
                imageView.setImageResource(images[realPosition]);
                //圖片顯現

                if (pic == null || pic.isEmpty()) {
                    imageView.setImageResource(R.drawable.noimage);
                } else {
                    Glide
                            .with(context)
                            .load(pic)
                            .skipMemoryCache(false)
                            .placeholder(R.drawable.loading_timage) //等待時顯現的圖片
                            .error(R.drawable.fail)//錯誤時顯現的圖片
                            //.centerCrop()
                            .fitCenter()
                            .into(imageView);
                }

                // item點擊
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        News news = list.get(realPosition);
                        Intent intent = new Intent(context,Article.class);
                        intent.putExtra("id",news.getArticle_id());
                        intent.putExtra("title", news.getTitle());
                        intent.putExtra("sa", news.getSummary());
                        intent.putExtra("category", news.getClassification());
                        intent.putExtra("date", news.getDate());
                        intent.putExtra("url", news.getUrl());
                        intent.putExtra("pic", news.getPic_url());
                        intent.putExtra("keyword",news.getKeyword());
                        context.startActivity(intent);
                    }
                });
            }
        ViewPager vp = (ViewPager)container;
        vp.addView(view);
        return view;
    }
    @Override
    public void destroyItem(ViewGroup container,int position,Object object){
        ViewPager vp = (ViewPager)container;
        View view = (View) object;
        vp.removeView(view);
    }
}
