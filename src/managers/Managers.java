package managers;

import tasks.Task;

public class Managers {

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    static HistoryManager<Task> getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
