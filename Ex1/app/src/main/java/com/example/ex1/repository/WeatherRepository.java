package com.example.ex1.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.ex1.model.ForecastResponse;
import com.example.ex1.model.GeocodingResponse;
import com.example.ex1.model.WeatherResponse;
import com.example.ex1.api.ApiClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherRepository {

    private static final String API_KEY = "394def8207444f7597a5f78f96fdfa71";

    public void getCurrentWeather(double lat, double lon, MutableLiveData<WeatherResponse> data) {

        ApiClient.getInstance()
                .getApi()
                .getCurrentWeather(lat, lon, API_KEY, "metric")
                .enqueue(new Callback<WeatherResponse>() {

                    @Override
                    public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                        data.postValue(response.body());
                    }

                    @Override
                    public void onFailure(Call<WeatherResponse> call, Throwable t) {
                    }
                });
    }

    public void getForecast(double lat, double lon, MutableLiveData<ForecastResponse> data) {

        ApiClient.getInstance()
                .getApi()
                .getForecast(lat, lon, API_KEY, "metric")
                .enqueue(new Callback<ForecastResponse>() {

                    @Override
                    public void onResponse(Call<ForecastResponse> call, Response<ForecastResponse> response) {
                        data.postValue(response.body());
                    }

                    @Override
                    public void onFailure(Call<ForecastResponse> call, Throwable t) {
                    }
                });
    }

    public MutableLiveData<List<GeocodingResponse>> getCoordinates(String city) {

        MutableLiveData<List<GeocodingResponse>> data = new MutableLiveData<>();

        ApiClient.getInstance()
                .getApi()
                .getCoordinates(city, 1, API_KEY)
                .enqueue(new Callback<List<GeocodingResponse>>() {

                    @Override
                    public void onResponse(@NonNull Call<List<GeocodingResponse>> call, @NonNull Response<List<GeocodingResponse>> response) {
                        data.setValue(response.body());
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<GeocodingResponse>> call, @NonNull Throwable t) {
                        data.setValue(null);
                    }
                });

        return data;
    }
}