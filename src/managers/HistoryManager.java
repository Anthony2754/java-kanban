package managers;

import tasks.Task;

import java.util.List;

public interface HistoryManager<E extends Task> {
    void addTaskInHistory(E e);

    void removeFromHistory(int id);

    List<E> getHistory();

}
