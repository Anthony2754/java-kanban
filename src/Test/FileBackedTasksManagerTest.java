package Test;

import managers.FileBackedTasksManager;
import managers.TaskManager;

import java.io.File;

class FileBackedTasksManagerTest extends TaskManagerTest {
    TaskManager manager = new FileBackedTasksManager(new File("TestSave.csv"));

    @Override
    TaskManager createManager() {
        return manager;
    }

}