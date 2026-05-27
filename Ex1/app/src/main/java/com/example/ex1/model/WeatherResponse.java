package com.example.ex1.model;

import java.util.List;

public class WeatherResponse {

    public Main main;
    public List<Weather> weather;
    public Wind wind;
    public String name;

    public class Main {
        public double temp;
        public double feels_like;
        public double temp_min;
        public double temp_max;
        public int humidity;
    }

    public class Weather {
        public String main;
        public String description;
        public String icon;
    }

    public class Wind {
        public double speed;
    }
}