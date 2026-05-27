package com.example.ex3.Model;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Task.class}, version = 1)
public abstract class TaskDatabase extends RoomDatabase {
    public abstract TaskDao taskDao();
    
    private static TaskDatabase INSTANCE;

    public static TaskDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(
                    context.getApplicationContext(),
                    TaskDatabase.class,
                    "movie_db"
            ).build();
        }
        return INSTANCE;
    }
}