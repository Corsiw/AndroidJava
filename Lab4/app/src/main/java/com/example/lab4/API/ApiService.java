package com.example.lab4.API;

import com.example.lab4.Model.Item;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {
    @GET("posts")
    Call<List<Item>> getPosts();

    @POST("posts")
    Call<Item> createPost(@Body Item item);
}
