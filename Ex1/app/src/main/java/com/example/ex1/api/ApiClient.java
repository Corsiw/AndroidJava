package com.example.ex1.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static ApiClient instance;
    private final Retrofit retrofit;
    
    private static final String BASE_URL = "https://api.openweathermap.org/";

    private ApiClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static synchronized ApiClient getInstance() {
        if (instance == null)
            instance = new ApiClient();

        return instance;
    }

    public WeatherService getApi() {
        return retrofit.create(WeatherService.class);
    }
}