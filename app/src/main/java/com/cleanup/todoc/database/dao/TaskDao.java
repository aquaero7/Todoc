package com.cleanup.todoc.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.cleanup.todoc.model.Task;

import java.util.List;

@Dao
public interface TaskDao {

    // Get project's tasks
    @Query("SELECT * from task WHERE projectId = :projectId")
    LiveData<List<Task>> getProjectTasks(long projectId);

    // Get all projects tasks
    @Query("SELECT * from task")
    LiveData<List<Task>> getTasks();

    // Insert a task
    @Insert
    void insertTask(Task task);
    // long insertTask(Task task);

    // Delete a task by id
    @Query("DELETE from task WHERE id = :taskId")
    void deleteTaskById(long taskId);
    // int deleteTask(long taskId);

    // Delete a task (other method)
    @Delete
    void deleteTask(Task task);

}
