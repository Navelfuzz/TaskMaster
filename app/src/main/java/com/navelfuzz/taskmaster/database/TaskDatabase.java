package com.navelfuzz.taskmaster.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.navelfuzz.taskmaster.daos.TaskDao;
import com.navelfuzz.taskmaster.models.Task;

@TypeConverters({TaskConverters.class})
@Database(entities = {Task.class}, version = 1)
public abstract class TaskDatabase extends RoomDatabase {
    public abstract TaskDao taskDao();
}
