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

public class AddEditMovieFragment extends Fragment {

    private MovieViewModel movieViewModel;

    private EditText titleInput;
    private EditText genreInput;
    private EditText yearInput;
    private RatingBar ratingBar;
    private CheckBox watchedCheck;

    public AddEditMovieFragment() {
        // Required empty public constructor
    }
    
    public static AddEditMovieFragment newInstance() {
        AddEditMovieFragment fragment = new AddEditMovieFragment();
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
        return inflater.inflate(R.layout.fragment_add_edt_movie, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        titleInput = view.findViewById(R.id.inputTitle);
        genreInput = view.findViewById(R.id.inputGenre);
        yearInput = view.findViewById(R.id.inputYear);
        ratingBar = view.findViewById(R.id.inputRating);
        watchedCheck = view.findViewById(R.id.inputWatched);

        Button saveButton = view.findViewById(R.id.saveButton);

        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);

        saveButton.setOnClickListener(v -> {

            Movie movie = new Movie();

            movie.title = titleInput.getText().toString();
            movie.genre = genreInput.getText().toString();
            movie.year = Integer.parseInt(yearInput.getText().toString());
            movie.rating = ratingBar.getRating();
            movie.watched = watchedCheck.isChecked();

            movieViewModel.insert(movie);

            getParentFragmentManager().popBackStack();
        });
    }
}