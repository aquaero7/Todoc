package com.cleanup.todoc.database.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.cleanup.todoc.database.LiveDataTestUtil;
import com.cleanup.todoc.database.TodocDatabase;
import com.cleanup.todoc.model.Project;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

@RunWith(AndroidJUnit4.class)
public class ProjectDaoTest {

    private TodocDatabase mDatabase;
    private final Project[] allProjects = Project.getAllProjects();
    private final int projectsCount = allProjects.length;


    @Rule
    public InstantTaskExecutorRule mInstantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void initDb() throws Exception {
        this.mDatabase = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().getContext(),
                TodocDatabase.class).allowMainThreadQueries().build();
    }

    @After
    public void closeDb() throws Exception {
        mDatabase.close();
    }


    @Test
    public void insertAndGetProject() throws InterruptedException {

        List<Project> projects;

        // Check if table is empty before base prepopulation
        projects = LiveDataTestUtil.getValue(this.mDatabase.mProjectDao().getProjects());
        assertTrue(projects.isEmpty());

        // Base prepopulation
        this.mDatabase.mProjectDao().insertProjects(this.allProjects);

        // Get all fields of the table and check if they match with all projects list items
        projects = LiveDataTestUtil.getValue(this.mDatabase.mProjectDao().getProjects());
        assertEquals(projectsCount, projects.size());
        for (int pos = 0; pos < projectsCount; pos++) {
            assertEquals(this.allProjects[pos].getId(), projects.get(pos).getId());
            assertEquals(this.allProjects[pos].getName(), projects.get(pos).getName());
            assertEquals(this.allProjects[pos].getColor(), projects.get(pos).getColor());
        }

    }

}
