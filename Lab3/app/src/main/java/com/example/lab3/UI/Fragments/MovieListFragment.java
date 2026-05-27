package com.example.lab3.UI.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lab3.Adapter.MovieAdapter;
import com.example.lab3.R;
import com.example.lab3.ViewModel.MovieViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MovieListFragment extends Fragment {
    private MovieViewModel movieViewModel;
    private MovieAdapter adapter;

    public MovieListFragment() {
    }

    public static MovieListFragment newInstance() {
        MovieListFragment fragment = new MovieListFragment();
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
        return inflater.inflate(R.layout.fragment_movie_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        FloatingActionButton addButton = view.findViewById(R.id.addMovieButton);

        adapter = new MovieAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        movieViewModel = new ViewModelProvider(requireActivity()).get(MovieViewModel.class);

        movieViewModel.getAllMovies().observe(getViewLifecycleOwner(), movies -> adapter.setMovies(movies));

        addButton.setOnClickListener(v -> getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, AddEditMovieFragment.newInstance())
                .addToBackStack(null)
                .commit());

        adapter.setOnItemClickListener(movie -> {
            Bundle bundle = new Bundle();
            bundle.putInt("movieId", movie.id);

            MovieDetailsFragment fragment = MovieDetailsFragment.newInstance();
            fragment.setArguments(bundle);
            getParentFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).addToBackStack(null).commit();
        });
    }
}