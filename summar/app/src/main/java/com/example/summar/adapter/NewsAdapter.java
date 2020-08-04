package com.example.summar.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.summar.Article;
import com.example.summar.R;
import com.example.summar.model.News;

public class NewsAdapter extends PagedListAdapter<News,NewsAdapter.NewsViewHolder> {
    private Context mCtx;

    public NewsAdapter(Context mCtx) {
        super(DIFF_CALLBACK);
        this.mCtx=mCtx;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.list_item,parent,false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        News news = getItem(position);

        if(news!=null){

            holder.title.setText(news.getTitle());
            holder.summary.setText(news.getSummary());

            if(news.getPic_url()==null || news.getPic_url().isEmpty()){
                holder.imageView.setImageResource(R.drawable.noimage);
            }
            else {
                Glide.with(mCtx)
                        .load(news.getPic_url())
                        .skipMemoryCache(false)
                        .placeholder(R.drawable.loading_timage) //等待時顯現的圖片
                        .error(R.drawable.fail)//錯誤時顯現的圖片
                        //.centerCrop()
                        .fitCenter()
                        .into(holder.imageView);
            }
        }else{
            Toast.makeText(mCtx, "Item is null", Toast.LENGTH_LONG).show();
        }
    }

    private static DiffUtil.ItemCallback<News> DIFF_CALLBACK=
            new DiffUtil.ItemCallback<News>() {
                @Override
                public boolean areItemsTheSame(@NonNull News oldItem, @NonNull News newItem) {
                    return oldItem.getArticle_id() == newItem.getArticle_id();
                }

                @SuppressLint("DiffUtilEquals")
                @Override
                public boolean areContentsTheSame(@NonNull News oldItem, @NonNull News newItem) {
                    return oldItem.equals(newItem);
                }
            };

    class NewsViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView title;
        TextView summary;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.iv);
            title = itemView.findViewById(R.id.title);
            summary = itemView.findViewById(R.id.des);

            //點擊項目時item
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    News news = getItem(position);
                    Log.d("Click",getItem(position).getTitle());
                    //傳送到Article.class
                    Intent intent = new Intent(mCtx,Article.class);
                    intent.putExtra("id", news.getArticle_id());
                    intent.putExtra("title", news.getTitle());
                    intent.putExtra("sa", news.getSummary());
                    intent.putExtra("category", news.getClassification());
                    intent.putExtra("date", news.getDate());
                    intent.putExtra("url", news.getUrl());
                    intent.putExtra("pic", news.getPic_url());
                    intent.putExtra("keyword",news.getKeyword());
                    mCtx.startActivity(intent);
                }
            });

        }
    }
}

