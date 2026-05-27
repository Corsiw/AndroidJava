package com.example.ex1.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ex1.R;
import com.example.ex1.model.ForecastResponse;
import com.example.ex1.viewmodel.WeatherViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private WeatherViewModel viewModel;

    private EditText cityInput;
    private Button searchBtn;
    private TextView todayTemp, todayDesc, cityName;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchBtn = findViewById(R.id.searchBtn);
        cityInput = findViewById(R.id.cityInput);
        todayTemp = findViewById(R.id.todayTemp);
        todayDesc = findViewById(R.id.todayDesc);
        cityName = findViewById(R.id.cityName);
        recyclerView = findViewById(R.id.recyclerWeek);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        viewModel = new ViewModelProvider(this).get(WeatherViewModel.class);


        searchBtn.setOnClickListener(v -> {
            String city = cityInput.getText().toString().trim();
            if (!city.isEmpty()) {
                viewModel.loadWeatherByCity(city);
            }
        });
        
        viewModel.loadWeather(55.75, 37.61);
        
        viewModel.getCurrentWeather().observe(this, data -> {

            if (data == null) return;

            todayTemp.setText(data.main.temp + "°C");
            todayDesc.setText(data.weather.get(0).main);
            cityName.setText(data.name);
        });
        
        viewModel.getForecastWeather().observe(this, data -> {

            if (data == null) return;

            List<ForecastResponse.ForecastItem> filtered =
                    filterDaily(data.list);
            
            recyclerView.setAdapter(new ForecastAdapter(filtered));
        });
    }

    private List<ForecastResponse.ForecastItem> filterDaily(List<ForecastResponse.ForecastItem> list) {

        List<ForecastResponse.ForecastItem> result = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            
            if (list.get(i).dt_txt.contains("12:00:00") || i == list.size() - 1) {
                result.add(list.get(i));
            }
        }

        return result;
    }
}