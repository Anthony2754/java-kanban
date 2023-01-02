package managers;

import tasks.Task;

public class Node {

    Node next;
    Node prev;
    Task task;

    Node(Node prev, Task task, Node next) {
        this.task = task;
        this.next = next;
        this.prev = prev;
    }
}
