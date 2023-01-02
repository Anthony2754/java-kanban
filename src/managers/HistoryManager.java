package managers;

import tasks.Task;

import java.util.List;

public interface HistoryManager<E extends Task> {
    void add(E e);

    void remove(int id);

    List<E> getHistory();

}
