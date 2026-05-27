package com.example.ex5.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ex5.R;
import com.example.ex5.model.NewsEntity;

import java.util.ArrayList;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private List<NewsEntity> newsList = new ArrayList<>();
    private OnNewsClickListener listener;

    public interface OnNewsClickListener {
        void onNewsClick(NewsEntity news);
    }

    public void setOnNewsClickListener(OnNewsClickListener listener) {
        this.listener = listener;
    }
    
    public void setNews(List<NewsEntity> news) {
        this.newsList = news;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_news, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        NewsEntity news = newsList.get(position);
        holder.tvTitle.setText(news.title);
        holder.tvDescription.setText(news.description);

        Glide.with(holder.itemView.getContext())
                .load(news.imageUrl)
                .placeholder(android.R.drawable.ic_menu_report_image)
                .error(android.R.drawable.ic_menu_close_clear_cancel)
                .into(holder.ivNews);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onNewsClick(news);
            }
        });

        holder.itemView.clearAnimation();
        holder.itemView.startAnimation(
                AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.fade_in)
        );
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public static class NewsViewHolder extends RecyclerView.ViewHolder {
        ImageView ivNews;
        TextView tvTitle, tvDescription;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            ivNews = itemView.findViewById(R.id.image);
            tvTitle = itemView.findViewById(R.id.title);
            tvDescription = itemView.findViewById(R.id.description);
        }
    }
}
