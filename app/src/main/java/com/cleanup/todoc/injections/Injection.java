package com.cleanup.todoc.injections;

import android.content.Context;

import com.cleanup.todoc.database.AppDatabase;
import com.cleanup.todoc.repositories.ProjectDataRepository;
import com.cleanup.todoc.repositories.TaskDataRepository;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Injection {
    public static TaskDataRepository provideTaskDataSource(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        return new TaskDataRepository(db.taskDao());
    }

    public static ProjectDataRepository provideProjectDataSource(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        return new ProjectDataRepository(db.projectDao());
    }

    public static Executor provideExecutor() {
        return Executors.newSingleThreadExecutor();
    }

    public static ViewModelFactory provideViewModelFactory(Context context) {
        TaskDataRepository taskDataSource = provideTaskDataSource(context);
        ProjectDataRepository projectDataSource = provideProjectDataSource(context);
        Executor executor = provideExecutor();
        return new ViewModelFactory(taskDataSource, projectDataSource, executor);
    }
}
