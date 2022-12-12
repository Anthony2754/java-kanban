package managers;

import tasks.Task;

import java.util.List;
import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {
    private List<Task> listOfLastTasks = new ArrayList<>(10);

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
