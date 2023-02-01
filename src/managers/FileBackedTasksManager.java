package managers;

import tasks.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

import static tasks.Status.DONE;
import static tasks.Status.NEW;
import static tasks.TypeTask.*;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private final File saveFile;

    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    public static void main(String[] args) throws ManagerSaveException {

        FileBackedTasksManager manager = new FileBackedTasksManager(new File("Save.csv"));


        ArrayList<Integer> arrayListSubtaskIdFirstEpic = new ArrayList<>();
        Status statusFirstEpic = manager.getterEpicStatus(arrayListSubtaskIdFirstEpic);
        LocalDateTime startTimeOfTheFirstEpicTask = manager.getterEpicStartTime(arrayListSubtaskIdFirstEpic);
        long durationOfTheFirstEpicTask = manager.getterEpicDuration(arrayListSubtaskIdFirstEpic);

        Epic firstEpic = manager.createEpic(new Epic("Сходить в магазин",
                "Купить овощи фрукты и мясо", arrayListSubtaskIdFirstEpic,
                statusFirstEpic, startTimeOfTheFirstEpicTask, durationOfTheFirstEpicTask));
        manager.saveInTreeMapEpic(firstEpic);

        Subtask firstSubtaskInFirstEpic = manager.createSubtask(new Subtask(firstEpic.getId(),
                "Купить овощи", "Картошку капусту морковку свеклу", DONE,
                LocalDateTime.now().plusMinutes(60L), 5L));
        manager.saveInTreeMapSubtask(firstSubtaskInFirstEpic);

        Subtask secondSubtaskInFirstEpic = manager.createSubtask(new Subtask(firstEpic.getId(),
                "Купить фрукты", "Яблоки бананы мандарины киви", NEW,
                LocalDateTime.now().plusMinutes(65), 5L));
        manager.saveInTreeMapSubtask(secondSubtaskInFirstEpic);

        Subtask thirdSubtaskInFirstEpic = manager.createSubtask(new Subtask(firstEpic.getId(), "Купить мясо",
                "Говядина свинина", NEW, LocalDateTime.now().plusMinutes(70L), 5L));
        manager.saveInTreeMapSubtask(thirdSubtaskInFirstEpic);

        manager.addSubtaskInEpic(firstSubtaskInFirstEpic, firstEpic);
        manager.addSubtaskInEpic(secondSubtaskInFirstEpic, firstEpic);
        manager.addSubtaskInEpic(thirdSubtaskInFirstEpic, firstEpic);
        manager.epicUpdate(firstEpic);

        System.out.println();
        System.out.println("2.1 Получение списка всех epic задач:");
        System.out.println(manager.getTreeMapEpic());

        System.out.println();
        System.out.println("2.1 Получение списка всех subtask задач:");
        System.out.println(manager.getTreeMapSubtask());

        ArrayList<Integer> arrayListSubtaskIdSecondEpic = new ArrayList<>();
        Status statusSecondEpic = manager.getterEpicStatus(arrayListSubtaskIdSecondEpic);
        LocalDateTime startTimeOfTheSecondEpicTask = manager.getterEpicStartTime(arrayListSubtaskIdSecondEpic);
        long durationOfTheSecondEpicTask = manager.getterEpicDuration(arrayListSubtaskIdSecondEpic);

        Epic secondEpic = manager.createEpic(new Epic("Обслужить автомобиль",
                "Провести ТО двигателя", arrayListSubtaskIdSecondEpic, statusSecondEpic,
                startTimeOfTheSecondEpicTask,
                durationOfTheSecondEpicTask));

        manager.saveInTreeMapEpic(secondEpic);


        System.out.println();
        System.out.println("2.1 Получение списка всех epic задач:");
        System.out.println(manager.getTreeMapEpic());

        Task firstTask = manager.createTask(new Task("Прогуляться", "Прогуляться по парку", NEW, //DONE,
                LocalDateTime.now().plusMinutes(3L), 30L));
        manager.saveInTreeMapTask(firstTask);
        manager.taskUpdate(firstTask);

        Task secondTask = manager.createTask(new Task("Пообедать", "Первое второе и компот", NEW,
                LocalDateTime.now().plusMinutes(35L), 10L));
        manager.saveInTreeMapTask(secondTask);
        manager.taskUpdate(secondTask);
        System.out.println();
        System.out.println("2.1 Получение списка всех задач:");
        System.out.println(manager.getTreeMapTask());

        try {

        System.out.println();
        System.out.println("2.3 Получение по идентификатору:");

        System.out.println(manager.getEpicById(5));
        System.out.println(manager.getSubtaskById(4));
        System.out.println(manager.getSubtaskById(3));
        System.out.println(manager.getSubtaskById(2));
        System.out.println(manager.getEpicById(1));
        System.out.println(manager.getTaskById(6));
        System.out.println(manager.getTaskById(7));
        System.out.println(manager.getSubtaskById(2));
        System.out.println(manager.getEpicById(5));
        System.out.println(manager.getTaskById(7));
        System.out.println(manager.getEpicById(1));
        System.out.println(manager.getTaskById(7));


        System.out.println();
        System.out.println("История просмотров:");
        System.out.println(manager.getHistory());
        System.out.println();

        System.out.println();
        System.out.println("Возвращение списка задач и подзадач в заданном порядке:");
        System.out.println(manager.getterPrioritizedTasks());

        FileBackedTasksManager downloadManager = manager.loadFromFile(new File("Save.csv"));

        System.out.println();
        System.out.println("Загруженный список задач:");
        System.out.println(downloadManager.getTreeMapTask());
        System.out.println(downloadManager.getTreeMapEpic());
        System.out.println(downloadManager.getTreeMapSubtask());

        System.out.println();
        System.out.println("Загруженная история просмотров:");
        System.out.println(downloadManager.getHistory());

        System.out.println();
        System.out.println("    Метод для возвращения списка задач и подзадач в заданном порядке:");
        System.out.println(manager.getterPrioritizedTasks());
        } catch (NullPointerException | ClassCastException e) {
            System.out.println(e.getMessage());
        }

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
        String startTime = task.getStartTime().format(formatter);
        String endTime = task.getEndTime().format(formatter);

        return String.format("%s, %s, %s, %s, %s, -, %s, %s, %s", task.getId(), type, task.getName(), task.getStatus()
                , task.getDescription(), startTime, task.getDuration(), endTime);
    }

    String epicToString(Epic task) {

        String type = String.valueOf(task.getClass()).replace("class tasks.", "");
        String startTime = null;
        String endTime = null;
        if (task.getStartTime() != null) {
            startTime = task.getStartTime().format(formatter);
            endTime = getterEpicEndTime(task.getArrayListSubtaskId()).format(formatter);
        }

        return String.format("%s, %s, %s, %s, %s, -, %s, %s, %s", task.getId(), type, task.getName(), task.getStatus()
                , task.getDescription(), startTime, task.getDuration(), endTime);
    }

    String subtaskToString(Subtask subtask) {

        String type = String.valueOf(subtask.getClass()).replace("class tasks.", "");
        String startTime = subtask.getStartTime().format(formatter);
        String endTime = subtask.getEndTime().format(formatter);
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

            int epicTaskId = Integer.parseInt(arrayTasks[0].trim());
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
            taskFromString = new Epic(epicTaskId, epicName, epicDescription, arrayListSubtaskIdInEpic, epicStatus,
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

    static String historyToString(HistoryManager<Task> manager) {

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
                    downloadManager.taskUpdate(taskFromFile);
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

                if (read.matches("^\\d")) {
                    historyFromFile.addAll(historyFromString(read));
                }
            }
        } catch (IOException exc) {
            System.out.println(exc.getMessage());
        }

        for (Integer id : historyFromFile) {
            Task task = downloadManager.getTaskById(id);
            Epic epic = downloadManager.getEpicById(id);
            Subtask subtask = downloadManager.getSubtaskById(id);

            if (task != null) {
                inMemoryHistoryManager.addTaskInHistory(task);
            }

            if (epic != null) {
                inMemoryHistoryManager.addTaskInHistory(epic);
            }

            if (subtask != null) {
                inMemoryHistoryManager.addTaskInHistory(subtask);
            }
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
        save();
    }

    @Override
    public void deletingAllEpics() {
        super.deletingAllEpics();
        save();
    }

    @Override
    public void deletingAllSubtasks() {
        super.deletingAllSubtasks();
        save();
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
        save();
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
        save();
    }

    @Override
    public void deleteEpicById(int id) {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public void deleteSubtaskById(int id) {
        super.deleteSubtaskById(id);
        save();
    }

    @Override
    public ArrayList<Subtask> getArrayListSubtaskByEpicId(int id) {
        return super.getArrayListSubtaskByEpicId(id);
    }


    @Override
    public List<Task> getHistory() {
        save();
        return super.getHistory();
    }

    @Override
    public void removeFromHistory(int id) {
        super.removeFromHistory(id);
        save();
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
