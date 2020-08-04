package com.example.summar.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.summar.Article;
import com.example.summar.R;
import com.example.summar.model.ItemClickListener;
import com.example.summar.model.News;

import java.util.ArrayList;

public class NewsSearchAdapter extends RecyclerView.Adapter<NewsSearchAdapter.NewsSearchViewHolder> implements Filterable{
    private Context mCtx;
    private ArrayList<News> newsList;
    private ArrayList<News> filteredList; //過濾過的資料
    private ItemClickListener itemClickListener;

    public NewsSearchAdapter(Context mCtx,ArrayList<News> newsList,ItemClickListener itemClickListener){
        this.mCtx=mCtx;
        this.newsList=newsList;
        this.filteredList=newsList;
        this.itemClickListener=itemClickListener;
    }


    @NonNull
    @Override
    public NewsSearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listcontent_layout,parent,false);
        final NewsSearchViewHolder newsSearchViewHolder = new NewsSearchViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onItemClick(filteredList.get(newsSearchViewHolder.getAdapterPosition()),newsSearchViewHolder.getAdapterPosition());
            }
        });
        return newsSearchViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NewsSearchViewHolder holder, int position) {
        String title=filteredList.get(position).getTitle();
        String pic_url=filteredList.get(position).getPic_url();

        holder.title.setText(title);

        if(pic_url==null || pic_url.isEmpty()){
            holder.imageView.setImageResource(R.drawable.noimage);
        }
        else {
            Glide.with(mCtx)
                    .load(pic_url)
                    .skipMemoryCache(false)
                    .placeholder(R.drawable.loading_timage) //等待時顯現的圖片
                    .error(R.drawable.fail)//錯誤時顯現的圖片
                    //.centerCrop()
                    .fitCenter()
                    .into(holder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String searchString = charSequence.toString();
                //判斷是否輸入字串
                if(searchString.isEmpty()){
                    filteredList=newsList;
                }else{
                    ArrayList<News> tempfilteredList = new ArrayList<>();

                    for(News news : newsList){
                        //search for title
                        if(news.getTitle().toLowerCase().contains(searchString)){
                            tempfilteredList.add(news);
                        }
                    }
                    filteredList=tempfilteredList;
                }
                FilterResults results = new FilterResults();
                results.values=filteredList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredList = (ArrayList<News>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    class NewsSearchViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView title;

        public NewsSearchViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView2);
            title = itemView.findViewById(R.id.textView1);
        }
    }
}

