package com.cleanup.todoc.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.cleanup.todoc.model.Project;

import java.util.List;

@Dao
public interface ProjectDao {

    // Methods called in Dao tests

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertProjects(Project... projects);

    @Query("SELECT * FROM project WHERE id = :projectId")
    LiveData<Project> getProject(long projectId);

    @Query("SELECT * FROM project")
    LiveData<List<Project>> getProjects();

}
