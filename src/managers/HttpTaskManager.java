package managers;

import adapters.AdapterForLocalDataTime;
import com.google.gson.*;
import servers.KVTaskClient;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.*;


public class HttpTaskManager extends FileBackedTasksManager {

    public KVTaskClient getKVTaskClient() {
        return kVTaskClient;
    }

    private final KVTaskClient kVTaskClient;

    public HttpTaskManager(URI uri) throws IOException, InterruptedException {
        kVTaskClient = new KVTaskClient(uri);
    }

    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .registerTypeAdapter(LocalDateTime.class, new AdapterForLocalDataTime())
            .create();

    public Integer toJson(String key) throws IOException, InterruptedException {
        final List<Task> managerToJson = new ArrayList<>();
        if (getTreeMapTask().size() != 0) {
            managerToJson.addAll(getTreeMapTask());
        }

        if (getTreeMapEpic().size() != 0) {
            managerToJson.addAll(getTreeMapEpic());
        }

        if (getTreeMapSubtask().size() != 0) {
            managerToJson.addAll(getTreeMapSubtask());
        }

        String taskManager = gson.toJson(managerToJson);

        if (getHistory().size() != 0) {
            List<Integer> listIdFromHistory = new ArrayList<>();
            List<Task> list = getHistory();
            for (Task task : list) {
                listIdFromHistory.add(task.getId());
            }
            taskManager = taskManager.replaceFirst("\\s+]$", ",\n  {\n    \"listIdFromHistory\": ");
            taskManager += gson.toJson(listIdFromHistory) + "\n}\n]";
        }

        kVTaskClient.put(key, taskManager);
        return kVTaskClient.getResponse().statusCode();
    }

    public static TaskManager fromJson(String key) throws IOException {
        HttpTaskManager httpTaskManager = null;
        String managerFromGson = null;

        try {
            httpTaskManager = new HttpTaskManager(URI.create("http://localhost:8081/"));///////////////////////////////////////
            managerFromGson = httpTaskManager.getKVTaskClient().load(key); // id задач выводится на 3 месте вместо 1
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if ((httpTaskManager != null) && (managerFromGson != null) && (!managerFromGson.equals(""))) {
            managerFromGson = managerFromGson.replaceFirst("\\[\\s*", "");
            managerFromGson = managerFromGson.replaceFirst("\\s+]$", "");
            if (managerFromGson.contains("},")) {
                String[] array = managerFromGson.split("},\n\\s*");
                for (String gsonFormat : array) {

                    if (gsonFormat.contains("arrayListSubtaskId")) {
                        Epic epicTask = FileBackedTasksManager.getterEpicFromGson(gsonFormat);
                        httpTaskManager.createEpic(epicTask);
                        httpTaskManager.saveInTreeMapEpic(epicTask);
                    } else if (gsonFormat.contains("epicId")) {

                        if (!gsonFormat.contains("}")) {
                            gsonFormat += "}";
                        }

                        Subtask subTask = gson.fromJson(gsonFormat, Subtask.class);
                        httpTaskManager.createSubtask(subTask);
                        httpTaskManager.saveInTreeMapSubtask(subTask);
                    } else if (gsonFormat.contains("listOfIdsFromHistory")) {

                        if (!gsonFormat.contains("}")) {
                            gsonFormat += "}";
                        }

                        String ids = gsonFormat
                                .replaceFirst("\\{\\s*\"listOfIdsFromHistory\": \\[\\s*", "")
                                .replaceFirst("\\s*]\\s*}", "");

                        if (ids.contains(",")) {
                            String[] arrayOfIdsFromHistory = ids.split(",\\s*");
                            for (String id : arrayOfIdsFromHistory) {
                                if (httpTaskManager.getTaskById(Integer.parseInt(id)) != null) {
                                    httpTaskManager.getTaskById(Integer.parseInt(id));
                                } else if (httpTaskManager.getEpicById(Integer.parseInt(id)) != null) {
                                    httpTaskManager.getEpicById(Integer.parseInt(id));
                                } else if (httpTaskManager.getSubtaskById(Integer.parseInt(id)) != null) {
                                    httpTaskManager.getSubtaskById(Integer.parseInt(id));
                                } else {
                                    return null;
                                }
                            }
                        } else {
                            if (httpTaskManager.getTaskById(Integer.parseInt(ids)) != null) {
                                httpTaskManager.getTaskById(Integer.parseInt(ids));
                            } else if (httpTaskManager.getEpicById(Integer.parseInt(ids)) != null) {
                                httpTaskManager.getEpicById(Integer.parseInt(ids));
                            } else if (httpTaskManager.getSubtaskById(Integer.parseInt(ids)) != null) {
                                httpTaskManager.getSubtaskById(Integer.parseInt(ids));
                            } else {
                                return null;
                            }
                        }
                    } else {

                        if (!gsonFormat.contains("}")) {
                                 gsonFormat += "}";
                               }

                        Task task = gson.fromJson(gsonFormat, Task.class);
                        if (task.getDescription() != null && task.getName() != null) {
                            httpTaskManager.createTask(task);
                            httpTaskManager.saveInTreeMapTask(task);
                        }
                    }
                }
            } else {
                if (managerFromGson.contains("arrayListSubtaskId")) {
                    Epic epic = FileBackedTasksManager.getterEpicFromGson(managerFromGson);
                    httpTaskManager.createEpic(epic);
                    httpTaskManager.saveInTreeMapEpic(epic);
                } else if (managerFromGson.contains("epicId")) {
                    Subtask subtask = gson.fromJson(managerFromGson, Subtask.class);
                    httpTaskManager.createSubtask(subtask);
                    httpTaskManager.saveInTreeMapSubtask(subtask);
                } else if (managerFromGson.contains("listIdFromHistory")) {
                    String ids = managerFromGson
                            .replaceFirst("\\{\\s*\"listIdFromHistory\": \\[\\s*", "")
                            .replaceFirst("\\s*]\\s*}", "");

                    if (ids.contains(",")) {
                        String[] arrayIdFromHistory = ids.split(", ");
                        for (String id : arrayIdFromHistory) {
                            if (httpTaskManager.getTaskById(Integer.parseInt(id)) != null) {
                                httpTaskManager.getTaskById(Integer.parseInt(id));
                            } else if (httpTaskManager.getEpicById(Integer.parseInt(id)) != null) {
                                httpTaskManager.getEpicById(Integer.parseInt(id));
                            } else if (httpTaskManager.getSubtaskById(Integer.parseInt(id)) != null) {
                                httpTaskManager.getSubtaskById(Integer.parseInt(id));
                            } else {
                                return null;
                            }
                        }
                    } else {
                        if (httpTaskManager.getTaskById(Integer.parseInt(ids)) != null) {
                            httpTaskManager.getTaskById(Integer.parseInt(ids));
                        } else if (httpTaskManager.getEpicById(Integer.parseInt(ids)) != null) {
                            httpTaskManager.getEpicById(Integer.parseInt(ids));
                        } else if (httpTaskManager.getSubtaskById(Integer.parseInt(ids)) != null) {
                            httpTaskManager.getSubtaskById(Integer.parseInt(ids));
                        } else {
                            return null;
                        }
                    }
                } else {

                        Task task = gson.fromJson(managerFromGson, Task.class);
                        if (task.getDescription() != null && task.getName() != null) {
                        httpTaskManager.createTask(task);
                        httpTaskManager.saveInTreeMapTask(task);
                    }
                }
            }
        }
        return httpTaskManager;
    }

    @Override
    public void saveInTreeMapTask(Task task) {
        super.saveInTreeMapTask(task);
    }

    @Override
    public void saveInTreeMapEpic(Epic epic) {
        super.saveInTreeMapEpic(epic);
    }

    @Override
    public void saveInTreeMapSubtask(Subtask subtask) {
        super.saveInTreeMapSubtask(subtask);
    }

    @Override
    public void addSubtaskInEpic(Subtask subtask, Epic epic) {
        super.addSubtaskInEpic(subtask, epic);
    }

    @Override
    public ArrayList<Task> getTreeMapTask() {
        return super.getTreeMapTask();
    }

    @Override
    public ArrayList<Epic> getTreeMapEpic() {
        return super.getTreeMapEpic();
    }

    @Override
    public ArrayList<Subtask> getTreeMapSubtask() {
        return super.getTreeMapSubtask();
    }

    @Override
    public void deletingAllTasks() {
        super.deletingAllTasks();
    }

    @Override
    public void deletingAllEpics() {
        super.deletingAllEpics();
    }

    @Override
    public void deletingAllSubtasks() {
        super.deletingAllSubtasks();
    }

    @Override
    public Task getTaskById(int id) {
        return super.getTaskById(id);
    }

    @Override
    public Epic getEpicById(int id) {
        return super.getEpicById(id);
    }

    @Override
    public Subtask getSubtaskById(int id) {
        return super.getSubtaskById(id);
    }

    @Override
    public Task createTask(Task task) {
        Task newTask = super.createTask(task);
        return newTask;
    }

    @Override
    public Epic createEpic(Epic epic) {
        return super.createEpic(epic);
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        return super.createSubtask(subtask);
    }

    @Override
    public void taskUpdate(Task task) {
        super.taskUpdate(task);
    }

    @Override
    public void epicUpdate(Epic epic) {
        super.epicUpdate(epic);
    }

    @Override
    public void subtaskUpdate(Subtask subtask) {
        super.subtaskUpdate(subtask);
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
    }

    @Override
    public void deleteEpicById(int id) {
        super.deleteEpicById(id);
    }

    @Override
    public void deleteSubtaskById(int id) {
        super.deleteSubtaskById(id);
    }

    @Override
    public ArrayList<Subtask> getArrayListSubtaskByEpicId(int id) {
        return super.getArrayListSubtaskByEpicId(id);
    }


    @Override
    public List<Task> getHistory() {
        return super.getHistory();
    }

    @Override
    public void removeFromHistory(int id) {
        super.removeFromHistory(id);
    }

    @Override
    public Status getterEpicStatus(ArrayList<Integer> listOfSubTaskId) {
        return super.getterEpicStatus(listOfSubTaskId);
    }

    @Override
    public LocalDateTime getterEpicStartTime(List<Integer> listSubtaskId) {
        return super.getterEpicStartTime(listSubtaskId);
    }

    @Override
    public long getterEpicDuration(List<Integer> listSubtaskId) {
        return super.getterEpicDuration(listSubtaskId);
    }

    @Override
    public LocalDateTime getterEpicEndTime(List<Integer> listSubtaskId) {
        return super.getterEpicEndTime(listSubtaskId);
    }

    @Override
    public Set<Task> getterPrioritizedTasks() {
        return super.getterPrioritizedTasks();
    }

}