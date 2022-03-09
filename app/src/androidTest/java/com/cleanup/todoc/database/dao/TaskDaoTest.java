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
import com.cleanup.todoc.model.Task;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class TaskDaoTest {

    private TodocDatabase mDatabase;

    private final Project[] allProjects = Project.getAllProjects();
    private final Task task1 = new Task(allProjects[0].getId(), "Task1", new Date().getTime());
    private final Task task2 = new Task(allProjects[1].getId(), "Task2", new Date().getTime());
    private final Task task3 = new Task(allProjects[2].getId(), "Task3", new Date().getTime());
    private final Task task4 = new Task(allProjects[0].getId(), "Task4", new Date().getTime());
    private final Task task5 = new Task(allProjects[1].getId(), "Task5", new Date().getTime());
    private final Task task6 = new Task(allProjects[2].getId(), "Task5", new Date().getTime());


    @Rule
    public InstantTaskExecutorRule mInstantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void initDb() throws Exception {
        this.mDatabase = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().getContext(),
                TodocDatabase.class).allowMainThreadQueries().build();

        this.mDatabase.mProjectDao().insertProjects(this.allProjects);

    }

    @After
    public void closeDb() throws Exception {
        mDatabase.close();
    }

    @Test
    public void getTasksWhenNoItemInserted() throws InterruptedException {
        List<Task> tasks = LiveDataTestUtil.getValue(this.mDatabase.mTaskDao().getTasks());
        assertTrue(tasks.isEmpty());
    }

    @Test
    public void insertAndGetTask() throws InterruptedException {
        List<Project> projects = LiveDataTestUtil.getValue(this.mDatabase.mProjectDao().getProjects());
        List<Task> tasks;

        this.mDatabase.mTaskDao().insertTask(this.task1);
        this.mDatabase.mTaskDao().insertTask(this.task2);
        this.mDatabase.mTaskDao().insertTask(this.task3);
        this.mDatabase.mTaskDao().insertTask(this.task4);
        this.mDatabase.mTaskDao().insertTask(this.task5);
        this.mDatabase.mTaskDao().insertTask(this.task6);

        tasks = LiveDataTestUtil.getValue(this.mDatabase.mTaskDao().getTasks());

        assertEquals(6, tasks.size());

        assertEquals(projects.get(0).getId(), tasks.get(0).getProjectId());
        assertEquals(projects.get(1).getId(), tasks.get(1).getProjectId());
        assertEquals(projects.get(2).getId(), tasks.get(2).getProjectId());
        assertEquals(projects.get(0).getId(), tasks.get(3).getProjectId());
        assertEquals(projects.get(1).getId(), tasks.get(4).getProjectId());
        assertEquals(projects.get(2).getId(), tasks.get(5).getProjectId());

        assertEquals(task1.getName(), tasks.get(0).getName());
        assertEquals(task2.getName(), tasks.get(1).getName());
        assertEquals(task3.getName(), tasks.get(2).getName());
        assertEquals(task4.getName(), tasks.get(3).getName());
        assertEquals(task5.getName(), tasks.get(4).getName());
        assertEquals(task6.getName(), tasks.get(5).getName());

        assertEquals(task1.getCreationTimestamp(), tasks.get(0).getCreationTimestamp());
        assertEquals(task2.getCreationTimestamp(), tasks.get(1).getCreationTimestamp());
        assertEquals(task3.getCreationTimestamp(), tasks.get(2).getCreationTimestamp());
        assertEquals(task4.getCreationTimestamp(), tasks.get(3).getCreationTimestamp());
        assertEquals(task5.getCreationTimestamp(), tasks.get(4).getCreationTimestamp());
        assertEquals(task6.getCreationTimestamp(), tasks.get(5).getCreationTimestamp());
    }

    @Test
    public void insertAndGetProjectTask() throws InterruptedException {
        List<Project> projects = LiveDataTestUtil.getValue(this.mDatabase.mProjectDao().getProjects());
        long projectId = allProjects[1].getId();
        List<Task> tasks;

        this.mDatabase.mTaskDao().insertTask(this.task1);
        this.mDatabase.mTaskDao().insertTask(this.task2);
        this.mDatabase.mTaskDao().insertTask(this.task3);
        this.mDatabase.mTaskDao().insertTask(this.task4);
        this.mDatabase.mTaskDao().insertTask(this.task5);
        this.mDatabase.mTaskDao().insertTask(this.task6);

        tasks = LiveDataTestUtil.getValue(this.mDatabase.mTaskDao().getProjectTasks(projectId));

        assertEquals(2, tasks.size());
        assertEquals(projects.get(1).getId(), tasks.get(0).getProjectId());
        assertEquals(projects.get(1).getId(), tasks.get(1).getProjectId());
        assertEquals(task2.getName(), tasks.get(0).getName());
        assertEquals(task5.getName(), tasks.get(1).getName());
        assertEquals(task2.getCreationTimestamp(), tasks.get(0).getCreationTimestamp());
        assertEquals(task5.getCreationTimestamp(), tasks.get(1).getCreationTimestamp());
    }

    @Test
    public void deleteTask() throws InterruptedException {
        List<Task> tasks;

        this.mDatabase.mTaskDao().insertTask(this.task1);
        this.mDatabase.mTaskDao().insertTask(this.task2);
        this.mDatabase.mTaskDao().insertTask(this.task3);
        this.mDatabase.mTaskDao().insertTask(this.task4);
        this.mDatabase.mTaskDao().insertTask(this.task5);
        this.mDatabase.mTaskDao().insertTask(this.task6);

        tasks = LiveDataTestUtil.getValue(this.mDatabase.mTaskDao().getTasks());
        assertEquals(6, tasks.size());
        assertEquals(task2.getProjectId(), tasks.get(1).getProjectId());
        assertEquals(task2.getName(), tasks.get(1).getName());
        assertEquals(task2.getCreationTimestamp(), tasks.get(1).getCreationTimestamp());

        this.mDatabase.mTaskDao().deleteTask(tasks.get(1));

        tasks = LiveDataTestUtil.getValue(this.mDatabase.mTaskDao().getTasks());
        assertEquals(5, tasks.size());
        assertEquals(task3.getProjectId(), tasks.get(1).getProjectId());
        assertEquals(task3.getName(), tasks.get(1).getName());
        assertEquals(task3.getCreationTimestamp(), tasks.get(1).getCreationTimestamp());
    }

    @Test
    public void deleteTaskById() throws InterruptedException {
        List<Task> tasks;

        this.mDatabase.mTaskDao().insertTask(this.task1);
        this.mDatabase.mTaskDao().insertTask(this.task2);
        this.mDatabase.mTaskDao().insertTask(this.task3);
        this.mDatabase.mTaskDao().insertTask(this.task4);
        this.mDatabase.mTaskDao().insertTask(this.task5);
        this.mDatabase.mTaskDao().insertTask(this.task6);

        tasks = LiveDataTestUtil.getValue(this.mDatabase.mTaskDao().getTasks());
        assertEquals(6, tasks.size());
        assertEquals(task2.getProjectId(), tasks.get(1).getProjectId());
        assertEquals(task2.getName(), tasks.get(1).getName());
        assertEquals(task2.getCreationTimestamp(), tasks.get(1).getCreationTimestamp());

        long taskId = tasks.get(1).getId();
        this.mDatabase.mTaskDao().deleteTaskById(taskId);

        tasks = LiveDataTestUtil.getValue(this.mDatabase.mTaskDao().getTasks());
        assertEquals(5, tasks.size());
        assertEquals(task3.getProjectId(), tasks.get(1).getProjectId());
        assertEquals(task3.getName(), tasks.get(1).getName());
        assertEquals(task3.getCreationTimestamp(), tasks.get(1).getCreationTimestamp());
    }

}
