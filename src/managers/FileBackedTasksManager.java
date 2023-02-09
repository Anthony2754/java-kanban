package managers;

import managers.exceptions.ManagerCreateException;
import managers.exceptions.ManagerGetException;
import managers.exceptions.ManagerSaveException;
import tasks.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

import static tasks.Status.*;
import static tasks.TypeTask.*;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private File saveFile;

    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    public FileBackedTasksManager() {
    }


    public void save() {
        String id = "id";
        String type = "type";
        String name = "name";
        String status = "status";
        String description = "description";
        String epicId = "epic";
        String startTime = "startTime";
        String duration = "duration";
        String endTime = "endTime";

        String formatString = String.format("%s, %s, %s, %s, %s, %s, %s, %s, %s\n", id, type, name, status, description,
                epicId, startTime, duration, endTime);

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(saveFile, StandardCharsets.UTF_8
                , false))) {
            bufferedWriter.write(formatString);

            for (Task task : getTreeMapTask()) {
                bufferedWriter.write(taskToString(task) + System.lineSeparator());
            }

            for (Epic epic : getTreeMapEpic()) {
                bufferedWriter.write(epicToString(epic) + System.lineSeparator());
            }

            for (Subtask subtask : getTreeMapSubtask()) {
                bufferedWriter.write(subtaskToString(subtask) + System.lineSeparator());
            }

            bufferedWriter.write(System.lineSeparator());

            bufferedWriter.write(historyToString(inMemoryHistoryManager));
        } catch (IOException exc) {
            System.out.println(exc.getMessage());
        }

    }

    String taskToString(Task task) {

        String type = String.valueOf(task.getClass()).replace("class tasks.", "");
        String startTime = "null";
        String endTime = "null";

        if (task.getStartTime() != null) {
             startTime = task.getStartTime().format(formatter);
             endTime = task.getEndTime().format(formatter);
        }
        return String.format("%s, %s, %s, %s, %s, -, %s, %s, %s", task.getId(), type, task.getName(), task.getStatus()
                , task.getDescription(), startTime, task.getDuration(), endTime);
    }

    String epicToString(Epic task) {

        String type = String.valueOf(task.getClass()).replace("class tasks.", "");
        String startTime = "null";
        String endTime = "null";
        if (task.getStartTime() != null) {
            startTime = task.getStartTime().format(formatter);
            endTime = getterEpicEndTime(task.getArrayListSubtaskId()).format(formatter);
        }

        return String.format("%s, %s, %s, %s, %s, -, %s, %s, %s", task.getId(), type, task.getName(), task.getStatus()
                , task.getDescription(), startTime, task.getDuration(), endTime);
    }

    String subtaskToString(Subtask subtask) {

        String type = String.valueOf(subtask.getClass()).replace("class tasks.", "");
        String startTime = "null";
        String endTime = "null";

        if (subtask.getStartTime() != null) {
            startTime = subtask.getStartTime().format(formatter);
            endTime = subtask.getEndTime().format(formatter);
        }
        return String.format("%s, %s, %s, %s, %s, %s, %s, %s, %s", subtask.getId(), type, subtask.getName(),
                subtask.getStatus(), subtask.getDescription(), subtask.getEpicId(), startTime, subtask.getDuration(),
                endTime);
    }

    private static Task fromString(String value) {

        Task taskFromString = null;
        String[] arrayTasks = value.split(",");
        String type = arrayTasks[1].trim();
        String task = TASK.toString();
        String epic = EPIC.toString();
        String subtask = SUBTASK.toString();

        if (type.equalsIgnoreCase(task)) {

            int taskId = Integer.parseInt(arrayTasks[0].trim());
            String taskName = arrayTasks[2].trim();
            String taskDescription = arrayTasks[4].trim();
            Status taskStatus = Status.valueOf(arrayTasks[3].trim());

            LocalDateTime startTime = LocalDateTime.parse(arrayTasks[6].trim(), formatter);
            long duration = Long.parseLong(arrayTasks[7].trim());
            taskFromString = new Task(taskId, taskName, taskDescription, taskStatus, startTime, duration);
        }

        if (type.equalsIgnoreCase(epic)) {

            int epicId = Integer.parseInt(arrayTasks[0].trim());
            String epicName = arrayTasks[2].trim();
            String epicDescription = arrayTasks[4].trim();

            ArrayList<Integer> arrayListSubtaskIdInEpic = new ArrayList<>();

            Status epicStatus = Status.valueOf(arrayTasks[3].trim());
            LocalDateTime epicTaskStartTime = null;

            if (!arrayTasks[6].trim().matches(".*null.*")) {
                String startTimeTrim = arrayTasks[6].trim();
                epicTaskStartTime = LocalDateTime.parse(startTimeTrim, formatter);
            }
            long epicTaskDuration = Long.parseLong(arrayTasks[7].trim());
            taskFromString = new Epic(epicId, epicName, epicDescription, arrayListSubtaskIdInEpic, epicStatus,
                    epicTaskStartTime, epicTaskDuration);
        }

        if (type.equalsIgnoreCase(subtask)) {

            int subtaskId = Integer.parseInt(arrayTasks[0].trim());
            int epicId = Integer.parseInt(arrayTasks[5].trim());
            String subtaskName = arrayTasks[2].trim();
            String subtaskDescription = arrayTasks[4].trim();
            Status subtaskStatus = Status.valueOf(arrayTasks[3].trim());
            LocalDateTime startTime = LocalDateTime.parse(arrayTasks[6].trim(), formatter);
            long duration = Long.parseLong(arrayTasks[7].trim());

            taskFromString = new Subtask(subtaskId, epicId, subtaskName, subtaskDescription, subtaskStatus,
                    startTime, duration);
        }
        return taskFromString;

    }

    static String historyToString(HistoryManager manager) {

        List<Integer> arrayListIdFromHistory = new ArrayList<>();
        List<Task> history = manager.getHistory();

        for (Task task : history) {
            if (!arrayListIdFromHistory.contains(task.getId())) {
                arrayListIdFromHistory.add(task.getId());
            }
        }

        return arrayListIdFromHistory.toString().replaceFirst("\\[", "")
                .replaceFirst("]", "");
    }

    static List<Integer> historyFromString(String value) {

        List<Integer> arrayListIdHistoryFromString = new ArrayList<>();

        if (value.matches(",")) {
            String[] taskHistoryArray = value.split(",");
            for (String elem : taskHistoryArray) {
                arrayListIdHistoryFromString.add(Integer.parseInt(elem));
            }
        }
        return arrayListIdHistoryFromString;
    }

    public FileBackedTasksManager(File file) throws ManagerSaveException {
        this.saveFile = searchFile(file);
    }

    FileBackedTasksManager loadFromFile(File file) throws ManagerSaveException {

        FileBackedTasksManager downloadManager = new FileBackedTasksManager(file);
        List<Integer> historyFromFile = new ArrayList<>();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            String read;
            while ((read = bufferedReader.readLine()) != null) {

                if ((read.matches(".* Task.*"))) {
                    Task taskFromFile = fromString(read);
                    downloadManager.createTask(taskFromFile);
                    downloadManager.setId(taskFromFile.getId() + 1);
                }

                if ((read.matches(".*Epic.*"))) {
                    Epic epicFromFile = (Epic) fromString(read);
                    downloadManager.epicUpdate(epicFromFile);
                    downloadManager.setId(epicFromFile.getId() + 1);
                }

                if (read.matches((".*Subtask.*"))) {
                    Subtask downloadSubtask = (Subtask) fromString(read);
                    Epic epicForDownloadSubtask = downloadManager.getEpicById(downloadSubtask.getEpicId());
                    ArrayList<Integer> downloadArrayListSubtaskID = epicForDownloadSubtask.getArrayListSubtaskId();

                    if (!downloadManager.inMemoryHistoryManager.getHistory().contains(epicForDownloadSubtask.getId())) {
                        downloadManager.inMemoryHistoryManager.removeFromHistory(epicForDownloadSubtask.getId());
                    }
                    downloadManager.subtaskUpdate(downloadSubtask);
                    downloadManager.setId(downloadSubtask.getId() + 1);

                    downloadArrayListSubtaskID.add(downloadSubtask.getId());
                    downloadManager.epicUpdate(epicForDownloadSubtask);
                }

                if (read.matches("^\\d+$") || read.matches("^\\d+, \\d+.*")) {
                    historyFromFile.addAll(historyFromString(read));
                    for (Integer id : historyFromFile) {
                        try {
                            downloadManager.getTaskById(id);
                        } catch (ManagerGetException e) {
                            try {
                                downloadManager.getEpicById(id);
                            } catch (ManagerGetException ex) {
                                downloadManager.getSubtaskById(id);
                            }
                        }
                    }
                }

            }
        } catch (IOException exc) {
            System.out.println(exc.getMessage());
        }

        return downloadManager;
    }

    File searchFile(File file) throws ManagerSaveException {
        if (file.exists()) {
            System.out.println("Файл " + file.getName() + " существует");
            if (file.canRead()) {
                System.out.println("Файл " + file.getName() + " доступен для чтения");
            } else {
                throw new ManagerSaveException("Нет доступа для чтения " + file.getName());
            }

            if (file.canWrite()) {
                System.out.println("Файл " + file.getName() + " доступен для записи");
            } else {
                throw new ManagerSaveException("Файл " + file.getName() + " недоступен для записи");
            }
        } else {
            file = new File(file.getPath());
        }
        return file;
    }

    private static Epic getEpicFromGson(String body) {
        ArrayList<Integer> arrayListSubtaskId = new ArrayList<>();
        String[] keyValue;
        int id = 0;
        String name = null;
        String description = null;
        Status status = null;
        LocalDateTime startTime = null;
        long duration = 0;
        String[] stringArrayBody = body
                .replaceFirst("\\{\\s*\"", "")
                .replaceFirst("\"\\s*}", "")
                .split(",\\s*\"");

        for (String string : stringArrayBody) {
            keyValue = string.split(": ");
            String key = keyValue[0]
                    .replaceAll("\"", "");

            String value = keyValue[1]
                    .trim()
                    .replaceAll("\"", "");

            switch (key) {
                case "arrayListSubtaskId":
                    if (!value.equals("[]")) {
                        String[] stringArrayValue = value
                                .replaceFirst("\\[", "")
                                .replaceAll("\\s", "")
                                .replaceFirst("]", "")
                                .split(",");
                        for (String idSubTask : stringArrayValue) {
                            arrayListSubtaskId.add(Integer.valueOf(idSubTask));
                        }
                    }
                    continue;
                case "id":
                    id = Integer.parseInt(value);
                    continue;
                case "name":
                    name = value;
                    continue;
                case "description":
                    description = value;
                    continue;
                case "status":
                    switch (value) {
                        case "NEW":
                            status = NEW;
                            continue;
                        case "IN_PROGRESS":
                            status = IN_PROGRESS;
                            continue;
                        case "DONE":
                            status = DONE;
                            continue;
                    }
                    continue;
                case "startTime":
                    if (arrayListSubtaskId.size() != 0) {
                        startTime = LocalDateTime.parse(value, DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
                    }
                    continue;
                case "duration":
                    if (arrayListSubtaskId.size() != 0) {
                        duration = Long.parseLong(value);
                    }
                    continue;
                default:
                    break;
            }
        }
        if (id != 0) {
            return new Epic(id, name, description, arrayListSubtaskId, status, startTime, duration);
        } else {
            return new Epic(name, description, arrayListSubtaskId, status, startTime, duration);
        }
    }

    public static Epic getterEpicFromGson(String body) {
        return getEpicFromGson(body);
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
    public Task createTask(Task task) throws ManagerCreateException {
        Task newTask = super.createTask(task);
        return newTask;
    }

    @Override
    public Epic createEpic(Epic epic) throws ManagerCreateException  {
        return super.createEpic(epic);
    }

    @Override
    public Subtask createSubtask(Subtask subtask) throws ManagerCreateException  {
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
