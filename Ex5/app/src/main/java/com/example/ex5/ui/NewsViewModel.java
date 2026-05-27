package com.example.ex5.ui;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.ex5.api.RetrofitClient;
import com.example.ex5.infrastructure.AppDatabase;
import com.example.ex5.infrastructure.NewsRepository;
import com.example.ex5.model.NewsEntity;
import java.util.List;

public class NewsViewModel extends AndroidViewModel {
    private int currentPage = 1;
    private final NewsRepository repository;
//    private final LiveData<List<NewsEntity>> allNews;

    public NewsViewModel(@NonNull Application application) {
        super(application);
        AppDatabase db = AppDatabase.getInstance(application);
        repository = new NewsRepository(db.newsDao(), RetrofitClient.getNewsApiService());
        repository.loadFromApi(1);
    }

    public LiveData<List<NewsEntity>> getAllNews() {
        return repository.getAllNews();
    }

    public void loadNextPage() {
        currentPage++;
        repository.loadFromApi(currentPage);
    }

    public void refreshNews() {
        currentPage = 1;
        repository.loadFromApi(currentPage);
    }
}