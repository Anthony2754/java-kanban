package managers;

import servers.KVTaskClient;
import tasks.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public interface TaskManager {
    void saveInTreeMapTask(Task task);

    void saveInTreeMapEpic(Epic epic);

    void saveInTreeMapSubtask(Subtask subtask);


    void addSubtaskInEpic(Subtask subtask, Epic epic);


    List<Task> getTreeMapTask();

    List<Epic> getTreeMapEpic();

    List<Subtask> getTreeMapSubtask();


    void deletingAllTasks();

    void deletingAllEpics();

    void deletingAllSubtasks();

    Task getTaskById(int id);

    Epic getEpicById(int id);

    Subtask getSubtaskById(int id);


    Task createTask(Task task);

    Epic createEpic(Epic epic);

    Subtask createSubtask(Subtask subtask);


    void taskUpdate(Task task);

    void epicUpdate(Epic epic);

    void subtaskUpdate(Subtask subtask);


    void deleteTaskById(int id);

    void deleteEpicById(int id);

    void deleteSubtaskById(int id);

    ArrayList<Subtask> getArrayListSubtaskByEpicId(int id);

    Status getEpicStatus(ArrayList<Integer> arrayListSubtaskId);

    Status getterEpicStatus(ArrayList<Integer> listOfSubTaskId);

    List<Task> getHistory();

    void removeFromHistory(int id);

    Set<Task> getterPrioritizedTasks();

    LocalDateTime getterEpicStartTime(List<Integer> listOfSubtaskIdOfTheFirstEpicTask); //

    long getterEpicDuration(List<Integer> listOfSubtaskIdOfTheFirstEpicTask);

    LocalDateTime getterEpicEndTime(List<Integer> listOfSubTaskId);

    KVTaskClient getKVTaskClient();

    Integer toJson(String key) throws IOException, InterruptedException;
}

