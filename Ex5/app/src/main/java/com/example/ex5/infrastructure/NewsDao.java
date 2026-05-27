package com.example.ex5.infrastructure;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.ex5.model.Article;
import com.example.ex5.model.NewsEntity;

import java.util.List;

@Dao
public interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<NewsEntity> news);

    @Query("SELECT * FROM news")
    LiveData<List<NewsEntity>> getAllNews();

    @Query("DELETE FROM news")
    void deleteAll();
}