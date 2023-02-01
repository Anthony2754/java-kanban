package managers;

import node.Node;
import tasks.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager<Task> {

    private static Node first;
    private static Node last;
    private final Map<Integer, Node> hashMapHistory = new HashMap<>();
    private static final InMemoryHistoryManager CustomLinkedList = new InMemoryHistoryManager();

    public void linkLast(Task task) {
        final Node lastTask = last;
        final Node newNode = new Node(lastTask, task, null);
        last = newNode;
        if (lastTask == null)
            first = newNode;
        else
            lastTask.next = newNode;
    }

    private List<Task> getTasks() {
        List<Task> arrayListOfLastTasks = new ArrayList<>();
        int i = 0;
        for (Node node = first; node != null; node = node.next)
            arrayListOfLastTasks.add(i++, node.task);
        return arrayListOfLastTasks;
    }

    private void removeNode(Node node) {
        final Node next = node.next;
        final Node prev = node.prev;

        if (prev == null) {
            first = next;
        } else {
            prev.next = next;
            node.prev = null;
        }

        if (next == null) {
            last = prev;
        } else {
            next.prev = prev;
            node.next = null;
        }
        node.task = null;
    }

    @Override
    public void addTaskInHistory(Task task) {

        int taskId = task.getId();
        boolean haveNode = hashMapHistory.containsKey(taskId);
        if (haveNode) {
            removeFromHistory(taskId);
        }
        CustomLinkedList.linkLast(task);
        hashMapHistory.put(taskId, last);
    }

    @Override
    public void removeFromHistory(int id){
        Node node = hashMapHistory.get(id);
        if (node != null) {
            removeNode(node);
            hashMapHistory.remove(id);
        }
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

}
