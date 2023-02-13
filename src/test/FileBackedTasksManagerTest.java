package test;

import managers.FileBackedTasksManager;
import managers.TaskManager;


import java.io.File;

class FileBackedTasksManagerTest extends TaskManagerTest<TaskManager> {
    TaskManager manager = new FileBackedTasksManager(new File("TestSave.csv"));

    private FileBackedTasksManagerTest() {
    }

    @Override
    TaskManager createManager() {
        return manager;
    }

}