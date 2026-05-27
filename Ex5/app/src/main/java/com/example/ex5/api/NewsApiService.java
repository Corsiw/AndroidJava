package com.example.ex5.api;


import retrofit2.http.GET;
import retrofit2.Call;
import retrofit2.http.Query;

public interface NewsApiService {

    @GET("v2/top-headlines")
    Call<NewsResponse> getTopNews(
            @Query("country") String country,
            @Query("apiKey") String apiKey,
            @Query("page") int page
    );
}