package test;

import managers.Managers;
import managers.TaskManager;


class InMemoryTaskManagerTest extends TaskManagerTest {
    TaskManager manager = Managers.getDefault();

    @Override
    TaskManager createManager() {
        return manager;
    }
}