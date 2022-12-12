package managers;

import tasks.Task;

import java.util.LinkedList;
import java.util.List;


public class InMemoryHistoryManager implements HistoryManager {
    private final List<Task> listOfLastTasks = new LinkedList<>();

    @Override
    public void add(Task task) {
        if (listOfLastTasks.size() == 10) {
            listOfLastTasks.remove(0);
        }
        listOfLastTasks.add(task);
    }

    @Override
    public List<Task> getHistory() {
        return listOfLastTasks;
    }
}
