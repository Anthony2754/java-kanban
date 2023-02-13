package test;

import managers.Managers;
import managers.TaskManager;


class InMemoryTaskManagerTest extends TaskManagerTest<TaskManager> {
    TaskManager manager = Managers.getDefault();

    protected InMemoryTaskManagerTest() {
    }

    @Override
    TaskManager createManager() {
        return manager;
    }
}