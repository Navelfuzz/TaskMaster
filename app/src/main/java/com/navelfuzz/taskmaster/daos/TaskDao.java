package com.navelfuzz.taskmaster.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.navelfuzz.taskmaster.models.Task;

import java.util.List;

import java.util.List;

@Dao
public interface TaskDao {
    @Insert
    public void insertATask(Task task);

    @Query("SELECT * FROM Task")
    public List<Task> findAll();

    @Query("SELECT * FROM Task ORDER BY title ASC")
    public List<Task> findAllSortedByTitle();

    @Query("SELECT * FROM task WHERE id = :id")
    Task findByAnId(long id);
}
