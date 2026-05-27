package com.example.ex5.infrastructure;

import android.app.usage.NetworkStatsManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.ex5.api.NewsApiService;
import com.example.ex5.api.NewsResponse;
import com.example.ex5.model.Article;
import com.example.ex5.model.NewsEntity;

import java.util.List;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsRepository {

    private NewsDao newsDao;
    private NewsApiService apiService;

    public NewsRepository(NewsDao newsDao, NewsApiService apiService) {
        this.newsDao = newsDao;
        this.apiService = apiService;
    }

    private static final String API_KEY = "11f5bc0df1c44990a668adaadb22aafe";

    public void loadFromApi(int page) {
        apiService.getTopNews("us", API_KEY, page).enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(@NonNull Call<NewsResponse> call, @NonNull Response<NewsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Executors.newSingleThreadExecutor().execute(() -> {
                        if (page == 1)
                            newsDao.deleteAll(); // Чистим только если это обновление (1 стр)
                        List<NewsEntity> entities = convert(response.body().getArticles());
                        newsDao.insertAll(entities);
                    });
                }
            }

            @Override
            public void onFailure(@NonNull Call<NewsResponse> call, @NonNull Throwable t) {
                Log.e("NewsApi", "Error", t);
            }
        });
    }
    
    public LiveData<List<NewsEntity>> getAllNews() {
        return newsDao.getAllNews();
    }

    private List<NewsEntity> convert(List<Article> articles) {
        if (articles == null) return new java.util.ArrayList<>();

        return articles.stream()
                .map(NewsEntity::fromArticle)
                .collect(java.util.stream.Collectors.toList());
    }
}