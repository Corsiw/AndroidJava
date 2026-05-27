package com.example.lab7;

import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CounterViewModel extends ViewModel {
    private final MutableLiveData<Integer> counter = new MutableLiveData<>(0);
    private final MutableLiveData<Boolean> isRunning = new MutableLiveData<>(false);
    private final Handler handler = new Handler(Looper.getMainLooper());
    private Runnable runnable;

    public LiveData<Integer> getCounter() {
        return counter;
    }

    public LiveData<Boolean> getIsRunning() {
        return isRunning;
    }

    public void toggleCounter() {
        if (isRunning.getValue() != null && isRunning.getValue()) {
            stopCounter();
        } else {
            startCounter();
        }
    }
    
    public void startCounter() {
        if (isRunning.getValue() != null && isRunning.getValue()) return;
        isRunning.setValue(true);

        runnable = new Runnable() {
            @Override
            public void run() {
                Integer currentValue = counter.getValue();
                if (currentValue != null) {
                    counter.setValue(currentValue + 1);
                }

                if (isRunning.getValue() != null && isRunning.getValue()) {
                    handler.postDelayed(this, 1000);
                }
            }
        };
        handler.post(runnable);
    }

    private void stopCounter() {
        if (runnable != null) {
            handler.removeCallbacks(runnable);
            isRunning.setValue(false);
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        stopCounter();
    }
}