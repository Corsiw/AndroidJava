package com.example.lab3.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.lab3.Model.Movie;
import com.example.lab3.Repository.MovieRepository;

import java.util.List;

public class MovieViewModel extends AndroidViewModel {

    private final MovieRepository repository;
    private final LiveData<List<Movie>> allMovies;

    public MovieViewModel(@NonNull Application application) {
        super(application);
        repository = new MovieRepository(application);
        allMovies = repository.getAllMovies();
    }

    public LiveData<List<Movie>> getAllMovies() {
        return allMovies;
    }

    public LiveData<Movie> getMovieById(int id) {
        return repository.getMovieById(id); // DAO возвращает LiveData<Movie>
    }

    public void insert(Movie movie) {
        repository.insert(movie);
    }

    public void update(Movie movie) {
        repository.update(movie);
    }

    public void delete(Movie movie) {
        repository.delete(movie);
    }
}