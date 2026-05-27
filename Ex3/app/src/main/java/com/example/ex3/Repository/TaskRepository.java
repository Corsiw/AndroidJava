package com.example.ex3.Repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.ex3.Model.TaskDao;
import com.example.ex3.Model.TaskDatabase;
import com.example.ex3.Model.Task;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TaskRepository {

    private final TaskDao taskDao;
    private final ExecutorService executorService;

    public TaskRepository(Application application) {
        TaskDatabase db = TaskDatabase.getDatabase(application);
        taskDao = db.taskDao();
        // Используем общий пул потоков для всех операций, чтобы не создавать новый под каждый запрос
        executorService = Executors.newFixedThreadPool(4);
    }
    
    public LiveData<List<Task>> getAllTasks() {
        return taskDao.getAllTasks();
    }
    
    public LiveData<List<Task>> getTasksByStatus(boolean completed) {
        return taskDao.getTasksByStatus(completed);
    }
    
    public LiveData<List<Task>> getTasksSortedByPriority() {
        return taskDao.getTasksSortedByPriority();
    }
    
    public LiveData<Task> getTaskById(int id) {
        return taskDao.getTaskById(id);
    }
    
    public void insert(Task task) {
        executorService.execute(() -> taskDao.insert(task));
    }

    public void update(Task task) {
        executorService.execute(() -> taskDao.update(task));
    }

    public void delete(Task task) {
        executorService.execute(() -> taskDao.delete(task));
    }
}