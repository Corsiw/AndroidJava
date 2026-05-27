package com.example.lab10;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab10.Adapters.ImageAdapter;
import com.example.lab10.Api.RetrofitClient;
import com.example.lab10.Models.UnsplashPhoto;

import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    private ImageAdapter adapter;
    private List<UnsplashPhoto> photos;

    private boolean isLoading = false;
    private int page = 1;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);

        photos = new ArrayList<>();

        adapter = new ImageAdapter(this, photos);
        recyclerView.setAdapter(adapter);

        loadImages();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {

                super.onScrolled(recyclerView, dx, dy);

                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItem = layoutManager.findFirstVisibleItemPosition();

                if (!isLoading && (visibleItemCount + firstVisibleItem) >= totalItemCount) {
                    loadImages();
                }
            }
        });
    }

    private void loadImages() {

        isLoading = true;
        progressBar.setVisibility(View.VISIBLE);

        Call<List<UnsplashPhoto>> call =
                RetrofitClient.getApi().getPhotos(BuildConfig.UNSPLASH_ACCESS_KEY, page, 20);

        call.enqueue(new Callback<List<UnsplashPhoto>>() {

            @Override
            public void onResponse(@NonNull Call<List<UnsplashPhoto>> call,
                                   @NonNull Response<List<UnsplashPhoto>> response) {

                progressBar.setVisibility(View.GONE);
                isLoading = false;

                if (response.isSuccessful() && response.body() != null) {

                    photos.addAll(response.body());
                    adapter.notifyDataSetChanged();
                    page++;
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<UnsplashPhoto>> call, @NonNull Throwable t) {

                progressBar.setVisibility(View.GONE);
                isLoading = false;

                Log.e("LoadImageFailure", t.getMessage());
            }
        });
    }
}
