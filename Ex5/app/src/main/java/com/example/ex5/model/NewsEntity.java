package com.example.ex5.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "news")
public class NewsEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String title;
    public String description;
    public String imageUrl;
    public String url;
    public String publishedAt;

    public static NewsEntity fromArticle(Article article) {
        NewsEntity news = new NewsEntity();
        news.title = article.getTitle();
        news.description = article.getDescription();
        news.imageUrl = article.getUrlToImage();
        news.url = article.getUrl();
        news.publishedAt = article.getPublishedAt();
        return news;
    }
}