package com.example.lab3.Model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "movies")
public class Movie {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String title;

    public String genre;

    public int year;

    public float rating;

    public boolean watched;
}