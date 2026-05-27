package com.example.lab3.Repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.lab3.Model.Movie;
import com.example.lab3.Model.MovieDao;
import com.example.lab3.Model.MovieDatabase;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;


public class MovieRepository {

    private final MovieDao movieDao;
    private final LiveData<List<Movie>> allMovies;

    public MovieRepository(Application application) {

        MovieDatabase db = MovieDatabase.getDatabase(application);
        movieDao = db.movieDao();
        allMovies = movieDao.getAllMovies();
    }

    public LiveData<List<Movie>> getAllMovies() {
        return allMovies;
    }

    public LiveData<Movie> getMovieById(int id) {
        try {
            return Executors.newSingleThreadExecutor().submit(() -> movieDao.getMovie(id)).get();
        } catch (ExecutionException | InterruptedException e) {
            Log.e("MovieRepository", e.getMessage(), e);
        }
        return null;
    }

    public void insert(Movie movie) {
        Executors.newSingleThreadExecutor().execute(() -> {
            movieDao.insert(movie);
        });
    }

    public void update(Movie movie) {
        Executors.newSingleThreadExecutor().execute(() -> {
            movieDao.update(movie);
        });
    }

    public void delete(Movie movie) {
        Executors.newSingleThreadExecutor().execute(() -> {
            movieDao.delete(movie);
        });
    }
}
