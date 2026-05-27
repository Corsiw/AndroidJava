package com.example.lab3.UI.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.lab3.Model.Movie;
import com.example.lab3.R;
import com.example.lab3.ViewModel.MovieViewModel;

public class MovieDetailsFragment extends Fragment {

    private MovieViewModel movieViewModel;
    private Movie currentMovie;

    private EditText titleEdit;
    private EditText genreEdit;
    private EditText yearEdit;
    private RatingBar ratingBar;
    private CheckBox watchedCheck;

    public MovieDetailsFragment() {
        // Required empty public constructor
    }

    public static MovieDetailsFragment newInstance() {
        MovieDetailsFragment fragment = new MovieDetailsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        titleEdit = view.findViewById(R.id.detailsTitle);
        genreEdit = view.findViewById(R.id.detailsGenre);
        yearEdit = view.findViewById(R.id.detailsYear);
        ratingBar = view.findViewById(R.id.detailsRating);
        watchedCheck = view.findViewById(R.id.detailsWatched);

        Button updateButton = view.findViewById(R.id.updateButton);
        Button deleteButton = view.findViewById(R.id.deleteButton);

        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);

        // TODO movieId if null = 0
        int movieId = getArguments() != null ? getArguments().getInt("movieId") : 0;

        movieViewModel.getMovieById(movieId).observe(getViewLifecycleOwner(), movie -> {

            if (movie != null && currentMovie == null) {

                currentMovie = movie;

                titleEdit.setText(movie.title);
                genreEdit.setText(movie.genre);
                yearEdit.setText(String.valueOf(movie.year));
                ratingBar.setRating(movie.rating);
                watchedCheck.setChecked(movie.watched);
            }
        });

        updateButton.setOnClickListener(v -> {
            if (currentMovie != null) {
                String titleStr = titleEdit.getText().toString().trim();
                String genreStr = genreEdit.getText().toString().trim();
                String yearStr = yearEdit.getText().toString().trim();

                // Проверка на пустые поля
                if (titleStr.isEmpty()) {
                    titleEdit.setError("Title cannot be empty");
                    titleEdit.requestFocus();
                    return;
                }

                if (genreStr.isEmpty()) {
                    genreEdit.setError("Genre cannot be empty");
                    genreEdit.requestFocus();
                    return;
                }

                int year;
                try {
                    year = Integer.parseInt(yearStr);
                } catch (NumberFormatException e) {
                    yearEdit.setError("Year must be a number");
                    yearEdit.requestFocus();
                    return;
                }
                
                int minYear = 1900;
                int maxYear = 2100; // Можно добавлять фильмы, которые еще не вышли
                if (year < minYear || year > maxYear) {
                    yearEdit.setError("Year must be between " + minYear + " and " + maxYear);
                    yearEdit.requestFocus();
                    return;
                }
                
                currentMovie.title = titleStr;
                currentMovie.genre = genreStr;
                currentMovie.year = year;
                currentMovie.rating = ratingBar.getRating();
                currentMovie.watched = watchedCheck.isChecked();

                movieViewModel.update(currentMovie);
            }
        });

        deleteButton.setOnClickListener(v -> {
            if (currentMovie != null) {
                movieViewModel.delete(currentMovie);
                getParentFragmentManager().popBackStack();
            }
        });
    }
}