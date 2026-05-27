package com.example.lab3.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab3.Model.Movie;
import com.example.lab3.R;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private final List<Movie> movies = new ArrayList<>();
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Movie movie);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setMovies(List<Movie> newMovies) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return movies.size();
            }

            @Override
            public int getNewListSize() {
                return newMovies.size();
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                return movies.get(oldItemPosition).id == newMovies.get(newItemPosition).id;
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                Movie oldMovie = movies.get(oldItemPosition);
                Movie newMovie = newMovies.get(newItemPosition);
                return oldMovie.title.equals(newMovie.title)
                        && oldMovie.genre.equals(newMovie.genre)
                        && oldMovie.year == newMovie.year
                        && oldMovie.rating == newMovie.rating
                        && oldMovie.watched == newMovie.watched;
            }
        });

        movies.clear();
        movies.addAll(newMovies);
        diffResult.dispatchUpdatesTo(this);
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_movie, parent, false);

        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {

        Movie movie = movies.get(position);

        holder.titleText.setText(movie.title);
        holder.genreText.setText(movie.genre);
        holder.yearText.setText(String.valueOf(movie.year));

        // Только для отображения, нельзя менять
        holder.ratingBar.setRating(movie.rating);
        holder.ratingBar.setIsIndicator(true);

        holder.watchedCheck.setChecked(movie.watched);
        holder.watchedCheck.setEnabled(false);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(movie);
            }
        });
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {

        TextView titleText;
        TextView genreText;
        TextView yearText;
        RatingBar ratingBar;
        CheckBox watchedCheck;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);

            titleText = itemView.findViewById(R.id.movieTitle);
            genreText = itemView.findViewById(R.id.movieGenre);
            yearText = itemView.findViewById(R.id.movieYear);
            ratingBar = itemView.findViewById(R.id.movieRating);
            watchedCheck = itemView.findViewById(R.id.movieWatched);
        }
    }
}