package com.example.lab6;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<String> messageToActivity = new MutableLiveData<>();
    private final MutableLiveData<String> messageToFragment1 = new MutableLiveData<>();
    private final MutableLiveData<String> messageToFragment2 = new MutableLiveData<>();

    public LiveData<String> getMessageToActivity() {
        return messageToActivity;
    }

    public void setMessageToActivity(String data) {
        messageToActivity.setValue(data);
    }

    public LiveData<String> getMessageToFragment1() {
        return messageToFragment1;
    }

    public void setMessageToFragment1(String data) {
        messageToFragment1.setValue(data);
    }

    public LiveData<String> getMessageToFragment2() {
        return messageToFragment2;
    }

    public void setMessageToFragment2(String data) {
        messageToFragment2.setValue(data);
    }
}
