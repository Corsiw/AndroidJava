package com.example.ex3.Model;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import java.util.List;

@Dao
public interface TaskDao {
    @Insert
    void insert(Task task);

    @Update
    void update(Task task);

    @Delete
    void delete(Task task);

    @Query("SELECT * FROM tasks WHERE id = :id")
    LiveData<Task> getTaskById(int id);

    @Query("SELECT * FROM tasks")
    LiveData<List<Task>> getAllTasks();

    // Сортировка по приоритету (от 1 до 3)
    @Query("SELECT * FROM tasks ORDER BY priority ASC")
    LiveData<List<Task>> getTasksSortedByPriority();

    // Фильтрация по статусу выполнения
    @Query("SELECT * FROM tasks WHERE isCompleted = :completed")
    LiveData<List<Task>> getTasksByStatus(boolean completed);
}
