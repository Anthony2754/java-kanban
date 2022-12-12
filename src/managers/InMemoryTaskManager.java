package managers;

import tasks.*;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;


public class InMemoryTaskManager implements TaskManager {
    private final TreeMap<Integer, Task> treeMapTask = new TreeMap<>();
    private final TreeMap<Integer, Epic> treeMapEpic = new TreeMap<>();
    private final TreeMap<Integer, Subtask> treeMapSubtask = new TreeMap<>();
    private int id = 0;
    private final HistoryManager inMemoryHistoryManager = Managers.getDefaultHistory();

    @Override
    public void saveInTreeMapTask(Task task) {
        id += 1;
        task.setId(id);
        treeMapTask.put(id, task);
    }

    @Override
    public void saveInTreeMapEpic(Epic epic) {
        id += 1;
        epic.setId(id);
        treeMapEpic.put(id, epic);
    }

    @Override
    public void saveInTreeMapSubtask(Subtask subtask) {
        id += 1;
        subtask.setId(id);
        treeMapSubtask.put(id, subtask);
    }


    @Override
    public void addSubtaskInEpic(Subtask subtask, Epic epic) {
        if (epic.getArrayListSubtaskId().contains(subtask.getId())) {
            epic.getArrayListSubtaskId().remove(subtask.getId());
        }
        epic.getArrayListSubtaskId().add(subtask.getId());
        subtask.setEpicId(epic.getId());
    }


    @Override
    public ArrayList<Task> getTreeMapTask() {
        return new ArrayList<>(treeMapTask.values());
    }

    @Override
    public ArrayList<Epic> getTreeMapEpic() {
        return new ArrayList<>(treeMapEpic.values());
    }

    @Override
    public ArrayList<Subtask> getTreeMapSubtask() {
        return new ArrayList<>(treeMapSubtask.values());
    }


    @Override
    public void deletingAllTasks() {
        treeMapTask.clear();
    }

    @Override
    public void deletingAllEpics() {
        for (int key : treeMapEpic.keySet()) {
            treeMapEpic.get(key).getArrayListSubtaskId().clear();
        }
        treeMapEpic.clear();
        treeMapSubtask.clear();
    }


    @Override
    public Task getTaskById(int id) {
        Task task = treeMapTask.get(id);
        if (task != null) {
            inMemoryHistoryManager.add(task);
        }
        return task;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = treeMapEpic.get(id);
        if (epic != null) {
            inMemoryHistoryManager.add(epic);
        }
        return epic;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = treeMapSubtask.get(id);
        if (subtask != null) {
            inMemoryHistoryManager.add(subtask);
        }
        return subtask;
    }


    @Override
    public Task createTask(Task task) {
        return new Task(task.getName(), task.getDescription(), task.getStatus());
    }

    @Override
    public Epic createEpic(Epic epic) {
        return new Epic(epic.getName(), epic.getDescription(), epic.getArrayListSubtaskId(), epic.getStatus());
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        return new Subtask(subtask.getEpicId(), subtask.getName(), subtask.getDescription(), subtask.getStatus());
    }


    @Override
    public void taskUpdate(Task task) {
        Task newTask = new Task(task.getId(), task.getName(), task.getDescription(), task.getStatus());
        treeMapTask.put(task.getId(), newTask);
    }

    @Override
    public void epicUpdate(Epic epic) {
        Task.Status epicStatus = getEpicStatus(epic.getArrayListSubtaskId());
        Epic newEpic = new Epic(epic.getId(), epic.getName(), epic.getDescription(),
                epic.getArrayListSubtaskId(), epicStatus);
        epic.setStatus(epicStatus);
        treeMapEpic.put(epic.getId(), newEpic);
    }

    @Override
    public void subtaskUpdate(Subtask subtask) {
        Subtask newSubtask = new Subtask(subtask.getId(), subtask.getEpicId(), subtask.getName()
                , subtask.getDescription(), subtask.getStatus());
        treeMapSubtask.put(subtask.getId(), newSubtask);

        Task.Status newStatus = getEpicStatus(treeMapEpic.get(subtask.getEpicId()).getArrayListSubtaskId());
        treeMapEpic.get(subtask.getEpicId()).setStatus(newStatus);
    }


    @Override
    public void deleteTaskById(int id) {
        treeMapTask.remove(id);
    }

    @Override
    public void deleteEpicById(int id) {
        for (int subtaskId : treeMapEpic.get(id).getArrayListSubtaskId()) {
            deleteSubtaskById(subtaskId);
        }
        treeMapEpic.remove(id);
    }


    @Override
    public void deleteSubtaskById(int id) {
        treeMapSubtask.remove(id);
    }


    @Override
    public ArrayList<Subtask> getArrayListSubtaskByEpicId(int id) {
        ArrayList<Subtask> arrayListSubtaskByEpicId = new ArrayList<>();
        for (int subtaskId : treeMapSubtask.keySet()) {
            if (id == getSubtaskById(subtaskId).getEpicId()) {
                arrayListSubtaskByEpicId.add(getSubtaskById(subtaskId));
            }
        }
        return arrayListSubtaskByEpicId;
    }


    @Override
    public Task.Status getEpicStatus(ArrayList<Integer> arrayListSubtaskId) {
        Task.Status epicStatus;
        int statusNew = 0;
        int statusDone = 0;

        for (Integer id : arrayListSubtaskId) {
            if (treeMapSubtask.get(id).getStatus().equals(Task.Status.NEW)) {
                statusNew++;
            }
            if (treeMapSubtask.get(id).getStatus().equals(Task.Status.DONE)) {
                statusDone++;
            }
        }

        if ((arrayListSubtaskId.isEmpty()) || (statusNew == arrayListSubtaskId.size())) {
            epicStatus = Task.Status.NEW;
        } else if (statusDone == arrayListSubtaskId.size()) {
            epicStatus = Task.Status.DONE;
        } else {
            epicStatus = Task.Status.IN_PROGRESS;
        }
        return epicStatus;
    }

    @Override
      public List<Task> getHistory() {
        return inMemoryHistoryManager.getHistory();
    }
}