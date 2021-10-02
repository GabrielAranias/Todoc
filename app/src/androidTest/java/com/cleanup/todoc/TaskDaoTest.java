package com.cleanup.todoc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.cleanup.todoc.database.AppDatabase;
import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;
import com.cleanup.todoc.utils.LiveDataTestUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

@RunWith(AndroidJUnit4.class)
public class TaskDaoTest {

    private AppDatabase db;
    private static final long projectId = 1;
    private static final Project testProject = new Project(projectId, "test_project", 0xFF4fe070);
    private static final Task testTask = new Task(projectId, "test_task", 0);

    @Rule
    public InstantTaskExecutorRule rule = new InstantTaskExecutorRule();

    @Before
    public void initDb() {
        this.db = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().getContext(),
                AppDatabase.class)
                .allowMainThreadQueries()
                .build();
    }

    @After
    public void closeDb() {
        db.close();
    }

    @Test
    public void createAndGetProject() throws InterruptedException {
        this.db.projectDao().insertProject(testProject);
        List<Project> project = LiveDataTestUtil.getValue(this.db.projectDao().getProjects());
        assertTrue(project.get(0).getId() == projectId
                && project.get(0).getName().equals(testProject.getName())
                && project.get(0).getColor() == testProject.getColor());
    }

    @Test
    public void checkEmptyListWhenNoTaskAdded() throws InterruptedException {
        List<Task> tasks = LiveDataTestUtil.getValue(this.db.taskDao().getTasks());
        assertTrue(tasks.isEmpty());
    }

    @Test
    public void createAndGetTask() throws InterruptedException {
        this.db.projectDao().insertProject(testProject);
        this.db.taskDao().insertTask(testTask);
        List<Task> tasks = LiveDataTestUtil.getValue(this.db.taskDao().getTasks());
        assertEquals(1, tasks.size());
    }

    @Test
    public void deleteTask() throws InterruptedException {
        createAndGetTask();
        Task addedTask = LiveDataTestUtil.getValue(this.db.taskDao().getTasks()).get(0);
        this.db.taskDao().deleteTask(addedTask.getId());
        List<Task> tasks = LiveDataTestUtil.getValue(this.db.taskDao().getTasks());
        assertTrue(tasks.isEmpty());
    }
}
