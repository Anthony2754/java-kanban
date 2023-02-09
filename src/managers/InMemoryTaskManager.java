package managers;

import managers.exceptions.*;
import tasks.*;
import servers.KVTaskClient;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;


public class InMemoryTaskManager implements TaskManager {
    private final Map<Integer, Task> treeMapTask = new TreeMap<>();
    private final Map<Integer, Epic> treeMapEpic = new TreeMap<>();
    private final Map<Integer, Subtask> treeMapSubtask = new TreeMap<>();
    private int id = 0;
    protected HistoryManager inMemoryHistoryManager = Managers.getDefaultHistory();

    public void setId(int id) {
        this.id = id;
    }

    public void checkTimeCrossing(Task task) {
        LocalDateTime startTime = task.getStartTime();
        LocalDateTime endTime = task.getEndTime();
        Set<Task> setSorted = getPrioritizedTasks();

        for (var taskFromTheList : setSorted) {
            if (taskFromTheList.getStartTime() != null) {
                LocalDateTime startTimeFromSetSorted = taskFromTheList.getStartTime();
                LocalDateTime endTimeTaskFrmSetSorted = taskFromTheList.getEndTime();
                if ((startTime.isAfter(startTimeFromSetSorted) && startTime.isBefore(endTimeTaskFrmSetSorted))
                        || (endTime.isAfter(startTimeFromSetSorted) && endTime.isBefore(endTimeTaskFrmSetSorted))
                        || startTimeFromSetSorted.isAfter(startTime) && endTimeTaskFrmSetSorted.isBefore(endTime)
                        || startTime.isAfter(startTimeFromSetSorted) && endTime.isBefore(endTimeTaskFrmSetSorted)) {
                    throw new ManagerCreateException("Задачи и подзадачи пересекаются по времени");
                }
            }
        }
    }

    private final Set<Task> treeSetPrioritizedTasks = new TreeSet<>((task1, task2) -> {
        if ((task1.getStartTime() != null) && (task2.getStartTime() != null)) {
            return task1.getStartTime().compareTo(task2.getStartTime());
        } else if (task1.getStartTime() == null) {
            return 1;
        } else if (null == task2.getStartTime()) {
            return -1;
        }else {
            return 0;
        }
    });

    private Set<Task> getPrioritizedTasks() {
        return treeSetPrioritizedTasks;
    }

    private LocalDateTime getEpicStartTime(List<Integer> listSubtaskId) {
        try {
            Subtask subtaskForStartTime;

            LocalDateTime startTime = null;
            if (listSubtaskId.size() != 0) {
                subtaskForStartTime = treeMapSubtask.get(listSubtaskId.get(0));
                startTime = subtaskForStartTime.getStartTime();
            }
            for (int i = 1; i < listSubtaskId.size(); i++) {
                Subtask subtask = treeMapSubtask.get(listSubtaskId.get(i));
                if (subtask.getStartTime().isBefore(startTime)) {
                    startTime = subtask.getStartTime();
                }
            }
            return startTime;
        } catch (ClassCastException | NullPointerException exc) {
            System.out.println(exc.getMessage());
            return null;
        }
    }


    private long getEpicDuration(List<Integer> listSubtaskId) {
        long durationEpic = 0;
        for (var id : listSubtaskId) {
            Subtask subTask = treeMapSubtask.get(id);
            if (subTask != null) {
                durationEpic += subTask.getDuration();
            }
        }
        return durationEpic;
    }

    private LocalDateTime getEpicEndTime(List<Integer> listSubtaskId) {
        try {
            Subtask subtaskForStartTime;

            LocalDateTime startTime = null;
            if (listSubtaskId.size() != 0) {
                subtaskForStartTime = treeMapSubtask.get(listSubtaskId.get(0));
                startTime = subtaskForStartTime.getEndTime();
            }
            for (int i = 1; i < listSubtaskId.size(); i++) {
                Subtask subTask = treeMapSubtask.get(listSubtaskId.get(i));
                if (subTask.getEndTime().isAfter(startTime)) {
                    startTime = subTask.getEndTime();
                }
            }
            return startTime;
        } catch (ClassCastException | NullPointerException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }


    @Override
    public void saveInTreeMapTask(Task task) {
        checkTimeCrossing(task);
        id += 1;
        task.setId(id);
        treeMapTask.put(id, task);
        setId(id);
        treeSetPrioritizedTasks.add(task);
    }

    @Override
    public void saveInTreeMapEpic(Epic epic) {
        id += 1;
        epic.setId(id);
        treeMapEpic.put(id, epic);
        setId(id);
    }

    @Override
    public void saveInTreeMapSubtask(Subtask subtask) {
        checkTimeCrossing(subtask);
        id += 1;
        subtask.setId(id);
        treeMapSubtask.put(id, subtask);
        setId(id);
        treeSetPrioritizedTasks.add(subtask);
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
    public void deletingAllSubtasks() {
        for (int key : treeMapSubtask.keySet()) {
            Subtask subtask = treeMapSubtask.get(key);
            if (subtask != null) {
                Epic epic = treeMapEpic.get(subtask.getEpicId());
                if (epic != null) {
                    List<Integer> listSubtaskId = epic.getArrayListSubtaskId();
                    listSubtaskId.clear();
                    epicUpdate(epic);
                }
            }
        }
        treeMapSubtask.clear();
    }

    @Override
    public Task getTaskById(int id) {
        Task task = treeMapTask.get(id);
        if (task != null) {
            inMemoryHistoryManager.addTaskInHistory(task);
        }
        return task;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = treeMapEpic.get(id);
        if (epic != null) {
            inMemoryHistoryManager.addTaskInHistory(epic);
        }
        return epic;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = treeMapSubtask.get(id);
        if (subtask != null) {
            inMemoryHistoryManager.addTaskInHistory(subtask);
        }

        return subtask;

    }

    @Override
    public Task createTask(Task task) {
        checkTimeCrossing(task);

        return new Task(task.getId(), task.getName(), task.getDescription(), task.getStatus(), task.getStartTime(),
                task.getDuration());
    }

    @Override
    public Epic createEpic(Epic epic) {

            Epic newEpic = new Epic(epic.getName(), epic.getDescription(), epic.getArrayListSubtaskId(), epic.getStatus(),
                    epic.getStartTime(), epic.getDuration());

            return newEpic;

    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        checkTimeCrossing(subtask);

                Subtask newSubtask = new Subtask(subtask.getEpicId(), subtask.getName(), subtask.getDescription(), subtask.getStatus(),
                        subtask.getStartTime(), subtask.getDuration());
                return newSubtask;

    }


    @Override
    public void taskUpdate(Task task) {

        checkTimeCrossing(task);
        task.setId(task.getId());
        task.setName(task.getName());
        task.setDescription(task.getDescription());
        task.setStatus(task.getStatus());
        task.setStartTime(task.getStartTime());
        task.setDuration(task.getDuration());
        task.setEndTime(task.getEndTime());

        Task newTask = new Task(task.getId(), task.getName(), task.getDescription(), task.getStatus(),
                task.getStartTime(), task.getDuration());
        treeMapTask.put(task.getId(), newTask);
        treeSetPrioritizedTasks.add(task);
    }

    @Override
    public void epicUpdate(Epic epic) {

        ArrayList<Integer> arrayListSubtaskId = epic.getArrayListSubtaskId();
        epic.setArrayListSubtaskId(arrayListSubtaskId);
        Status epicTaskStatus = getEpicStatus(arrayListSubtaskId);
        epic.setStatus(epicTaskStatus);
        LocalDateTime epicTaskStartTime = getEpicStartTime(arrayListSubtaskId);
        epic.setStartTime(epicTaskStartTime);
        long epicTaskDuration = getEpicDuration(arrayListSubtaskId);
        epic.setDuration(epicTaskDuration);
        epic.setEndTime(epic.getEndTime());

        Status epicStatus = getEpicStatus(epic.getArrayListSubtaskId());
        Epic newEpic = new Epic(epic.getId(), epic.getName(), epic.getDescription(),
                epic.getArrayListSubtaskId(), epicStatus, epic.getStartTime(), epic.getDuration());
        epic.setStatus(epicStatus);
        treeMapEpic.put(epic.getId(), newEpic);
        treeSetPrioritizedTasks.add(epic);
    }

    @Override
    public void subtaskUpdate(Subtask subtask) {

        checkTimeCrossing(subtask);
        subtask.setId(subtask.getId());
        subtask.setEpicId(subtask.getEpicId());
        subtask.setName(subtask.getName());
        subtask.setDescription(subtask.getDescription());
        subtask.setStatus(subtask.getStatus());
        subtask.setStartTime(subtask.getStartTime());
        subtask.setDuration(subtask.getDuration());
        subtask.setEndTime(subtask.getEndTime());

        Subtask newSubtask = new Subtask(subtask.getId(), subtask.getEpicId(), subtask.getName()
                , subtask.getDescription(), subtask.getStatus(), subtask.getStartTime(), subtask.getDuration());
        treeMapSubtask.put(subtask.getId(), newSubtask);
        treeSetPrioritizedTasks.add(subtask);

        Epic epic = getTreeMapEpic().get(subtask.getEpicId());
        if (epic != null) {
            ArrayList<Integer> arrayListSubtaskId = epic.getArrayListSubtaskId();
            Status newStatus = getEpicStatus(arrayListSubtaskId);
            epic.setStatus(newStatus);
        }

    }


    @Override
    public void deleteTaskById(int id) {

        Task task = treeMapTask.get(id);
        if (task != null) {
            treeSetPrioritizedTasks.remove(task);
        }
        treeMapTask.remove(id);
        inMemoryHistoryManager.removeFromHistory(id);
    }

    @Override
    public void deleteEpicById(int id) {

        Epic epic = treeMapEpic.get(id);
        if (epic != null) {
            for (int subtaskId : treeMapEpic.get(id).getArrayListSubtaskId()) {
                inMemoryHistoryManager.removeFromHistory(subtaskId);
                Subtask subtask = treeMapSubtask.get(id);
                if (subtask != null) {
                    treeSetPrioritizedTasks.remove(subtask);
                }
                treeMapSubtask.remove(subtaskId);
            }
            inMemoryHistoryManager.removeFromHistory(id);
            treeMapEpic.remove(id);
        }
    }


    @Override
    public void deleteSubtaskById(int id) {

        Subtask subtask = treeMapSubtask.get(id);
        if (subtask != null) {
            int epicId = subtask.getEpicId();
            Epic epic = treeMapEpic.get(epicId);
            if (epic != null) {
                epic.getArrayListSubtaskId().remove((Integer) id);
                inMemoryHistoryManager.removeFromHistory(id);
                treeSetPrioritizedTasks.remove(subtask);
                treeMapSubtask.remove(id);
                epicUpdate(epic);
            }
        }
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
    public Status getEpicStatus(ArrayList<Integer> arrayListSubtaskId) {
        Status epicStatus;
        int statusNew = 0;
        int statusDone = 0;

        for (Integer id : arrayListSubtaskId) {
            if (treeMapSubtask.get(id).getStatus().equals(Status.NEW)) {
                statusNew++;
            }
            if (treeMapSubtask.get(id).getStatus().equals(Status.DONE)) {
                statusDone++;
            }
        }

        if ((arrayListSubtaskId.isEmpty()) || (statusNew == arrayListSubtaskId.size())) {
            epicStatus = Status.NEW;
        } else if (statusDone == arrayListSubtaskId.size()) {
            epicStatus = Status.DONE;
        } else {
            epicStatus = Status.IN_PROGRESS;
        }
        return epicStatus;
    }

    @Override
    public Status getterEpicStatus(ArrayList<Integer> listOfSubTaskId) {
        return getEpicStatus(listOfSubTaskId);
    }

    @Override
    public List<Task> getHistory() {
        return inMemoryHistoryManager.getHistory();
    }


    @Override
    public void removeFromHistory(int id) {
        inMemoryHistoryManager.removeFromHistory(id);
    }


    @Override
    public Set<Task> getterPrioritizedTasks() {
        return getPrioritizedTasks();
    }


    @Override
    public LocalDateTime getterEpicStartTime(List<Integer> listSubtaskId) {
        return getEpicStartTime(listSubtaskId);
    }


    @Override
    public long getterEpicDuration(List<Integer> listSubtaskId) {
        return getEpicDuration(listSubtaskId);
    }

    @Override
    public LocalDateTime getterEpicEndTime(List<Integer> listSubtaskId) {
        return getEpicEndTime(listSubtaskId);
    }



    @Override
    public KVTaskClient getKVTaskClient() {
        return null;
    }

    @Override
    public Integer toJson(String key) throws IOException, InterruptedException {
        return null;
    }

}