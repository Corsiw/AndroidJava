package com.example.ex3.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.ex3.Model.Task;
import com.example.ex3.Repository.TaskRepository;

import java.util.List;

public class TaskViewModel extends AndroidViewModel {

    private final TaskRepository repository;

    private final MutableLiveData<String> filterMode = new MutableLiveData<>("ALL");
    private final LiveData<List<Task>> filteredTasks;

    public TaskViewModel(@NonNull Application application) {
        super(application);
        repository = new TaskRepository(application);

        filteredTasks = Transformations.switchMap(filterMode, mode -> {
            switch (mode) {
                case "PRIORITY":
                    return repository.getTasksSortedByPriority();
                case "COMPLETED":
                    return repository.getTasksByStatus(true);
                case "INCOMPLETE":
                    return repository.getTasksByStatus(false);
                default:
                    return repository.getAllTasks();
            }
        });
    }

    public LiveData<List<Task>> getTasks() {
        return filteredTasks;
    }

    public void setFilterMode(String mode) {
        filterMode.setValue(mode);
    }

    public void insert(Task task) {
        repository.insert(task);
    }

    public void update(Task task) {
        repository.update(task);
    }

    public void delete(Task task) {
        repository.delete(task);
    }

    public LiveData<Task> getTaskById(int id) {
        return repository.getTaskById(id);
    }
}