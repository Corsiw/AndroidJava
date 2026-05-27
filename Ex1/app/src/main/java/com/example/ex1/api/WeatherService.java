package com.example.ex1.api;

import com.example.ex1.model.ForecastResponse;
import com.example.ex1.model.GeocodingResponse;
import com.example.ex1.model.WeatherResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherService {
    @GET("data/2.5/weather")
    Call<WeatherResponse> getCurrentWeather(
            @Query("lat") double lat,
            @Query("lon") double lon,
            @Query("APPID") String apiKey,
            @Query("units") String units
    );

    @GET("geo/1.0/direct")
    Call<List<GeocodingResponse>> getCoordinates(
            @Query("q") String cityName,
            @Query("limit") int limit,
            @Query("APPID") String apiKey
    );

    @GET("data/2.5/forecast")
    Call<ForecastResponse> getForecast(
            @Query("lat") double lat,
            @Query("lon") double lon,
            @Query("APPID") String apiKey,
            @Query("units") String units
    );
}