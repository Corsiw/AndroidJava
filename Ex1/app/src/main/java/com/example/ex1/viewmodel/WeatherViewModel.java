package com.example.ex1.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.ex1.model.ForecastResponse;
import com.example.ex1.model.GeocodingResponse;
import com.example.ex1.model.WeatherResponse;
import com.example.ex1.repository.WeatherRepository;

public class WeatherViewModel extends ViewModel {

    private final WeatherRepository repository = new WeatherRepository();

    // Создаем финальные объекты LiveData один раз
    private final MutableLiveData<WeatherResponse> currentWeather = new MutableLiveData<>();
    private final MutableLiveData<ForecastResponse> forecastWeather = new MutableLiveData<>();

    public void loadWeather(double lat, double lon) {
        // Репозиторий должен принимать MutableLiveData и записывать в них результат через .setValue() или .postValue()
        repository.getCurrentWeather(lat, lon, currentWeather);
        repository.getForecast(lat, lon, forecastWeather);
    }

    public void loadWeatherByCity(String city) {
        repository.getCoordinates(city).observeForever(geoList -> {
            if (geoList != null && !geoList.isEmpty()) {
                GeocodingResponse geo = geoList.get(0);
                loadWeather(geo.lat, geo.lon);
            }
        });
    }

    public LiveData<WeatherResponse> getCurrentWeather() {
        return currentWeather;
    }

    public LiveData<ForecastResponse> getForecastWeather() {
        return forecastWeather;
    }
}