package com.example.lab10.Api;

import com.example.lab10.Models.UnsplashPhoto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface UnsplashApi {

    @Headers("Accept-Version: v1")
    @GET("photos")
    Call<List<UnsplashPhoto>> getPhotos(

            @Query("client_id") String accessKey,
            @Query("page") int page,
            @Query("per_page") int perPage
    );
}
