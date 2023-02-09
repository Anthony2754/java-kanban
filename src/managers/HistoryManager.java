package managers;

import tasks.Task;

import java.util.List;

public interface HistoryManager {
    void addTaskInHistory(Task e);

    void removeFromHistory(int id);

    List<Task> getHistory();

}
