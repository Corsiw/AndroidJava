package com.example.ex1.model;

import java.util.List;

public class ForecastResponse {

    public List<ForecastItem> list;

    public class ForecastItem {
        public long dt;
        public Main main;
        public List<Weather> weather;
        public String dt_txt;
    }

    public class Main {
        public double temp;
    }

    public class Weather {
        public String main;        // Clouds / Rain / Snow
        public String description; // scattered clouds
        public String icon;
    }
}