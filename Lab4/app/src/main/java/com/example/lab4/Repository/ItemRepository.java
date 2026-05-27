package com.example.lab4.Repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.lab4.API.ApiClient;
import com.example.lab4.API.ApiService;
import com.example.lab4.Model.Item;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

public class ItemRepository {

    private final ApiService apiService;

    public ItemRepository() {
        apiService = ApiClient.getApiService();
    }

    public void getItems(MutableLiveData<List<Item>> liveData) {
        apiService.getPosts().enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(@NonNull Call<List<Item>> call, @NonNull Response<List<Item>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    liveData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Item>> call, @NonNull Throwable t) {
                liveData.postValue(null);
            }
        });
    }

    public void postItem(Item item, MutableLiveData<Item> liveData) {
        apiService.createPost(item).enqueue(new Callback<Item>() {
            @Override
            public void onResponse(@NonNull Call<Item> call, @NonNull Response<Item> response) {
                if (response.isSuccessful() && response.body() != null) {
                    liveData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<Item> call, Throwable t) {
                liveData.postValue(null);
            }
        });
    }
}