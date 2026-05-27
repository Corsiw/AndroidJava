package com.example.lab10.Models;

public class UnsplashPhoto {

    private Urls urls;

    public Urls getUrls() {
        return urls;
    }

    public static class Urls {

        private String small;

        public String getSmall() {
            return small;
        }
    }
}
