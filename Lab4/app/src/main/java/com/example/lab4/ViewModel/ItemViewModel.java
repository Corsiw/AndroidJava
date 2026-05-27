package com.example.lab4.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.lab4.Model.Item;
import com.example.lab4.Repository.ItemRepository;

import java.util.List;

public class ItemViewModel extends ViewModel {

    private final ItemRepository repository;
    private final MutableLiveData<List<Item>> itemsLiveData;
    private final MutableLiveData<Item> postItemLiveData;
    private MutableLiveData<Boolean> isLoading;

    public ItemViewModel() {
        repository = new ItemRepository();
        itemsLiveData = new MutableLiveData<>();
        postItemLiveData = new MutableLiveData<>();
        isLoading = new MutableLiveData<>(false);
    }

    public LiveData<List<Item>> getItemsLiveData() {
        return itemsLiveData;
    }

    public LiveData<Item> getPostItemLiveData() {
        return postItemLiveData;
    }
    
    public LiveData<Boolean> getIsLoadingLiveData() 
    {
        return isLoading;
    }
    
    public void setIsLoading(boolean value) {
        isLoading.setValue(value);
    }

    public void fetchItems() {
        if (isLoading.getValue() == null || isLoading.getValue()) return;
        
        isLoading.setValue(true);
        repository.getItems(itemsLiveData);
    }

    public void postItem(Item item) {
        repository.postItem(item, postItemLiveData);
    }
}