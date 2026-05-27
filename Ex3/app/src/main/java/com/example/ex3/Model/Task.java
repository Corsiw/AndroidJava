package com.example.ex3.Model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tasks")
public class Task {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String title;
    public String description;
    public int priority; // 1 - Высокий, 2 - Средний, 3 - Низкий
    public boolean isCompleted;
    
    public Task(String title, String description, int priority, boolean isCompleted) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.isCompleted = isCompleted;
    }
}