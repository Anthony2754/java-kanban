package test;


import managers.HttpTaskManager;
import managers.TaskManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import servers.KVServer;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import static managers.Managers.getDefaultManager;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class HttpTaskManagerTest extends TaskManagerTest {
    String key = "key1";
    TaskManager manager = new HttpTaskManager(URI.create("http://localhost:8078/"));

    HttpTaskManagerTest() throws IOException, InterruptedException {
    }

    @Override
    TaskManager createManager() {
        return manager;
    }

    @BeforeAll
    public static void start() throws IOException {
        kVServer = new KVServer();
        kVServer.start();
    }

    @Test
    void toJson() throws IOException, InterruptedException {

        listTask = manager.getTreeMapTask();
        assertNotNull(listTask, "Список Task пуст");
        listEpic = manager.getTreeMapEpic();
        assertNotNull(listEpic, "Список Epic задач пуст");
        listSubtask = manager.getTreeMapSubtask();
        assertNotNull(listSubtask, "Список Subtask пуст");
        listOfTaskHistory = manager.getHistory();
        assertNotNull(listOfTaskHistory, "Список истории задач пуст");

        assertNotNull(manager.getTaskById(task1.getId()), "ID Task1 не существует");
        assertNotNull(manager.getTaskById(task2.getId()), "ID Task2 не существует");
        assertNotNull(manager.getEpicById(epic1.getId()), "ID Epic1 не существует");
        assertNotNull(manager.getEpicById(epic2.getId()), "ID Epic2 не существует");
        assertNotNull(manager.getSubtaskById(subtask1.getId()), "ID Subtask1 не существует");
        assertNotNull(manager.getSubtaskById(subtask2.getId()), "ID Subtask2 не существует");
        assertNotNull(manager.getSubtaskById(subtask3.getId()), "ID Subtask3 не существует");

        statusCode = manager.toJson(key);
        assertEquals(200, statusCode, "Значение для ключа " + key + " не обновлено");

    }

    @Test
    void fromJson() throws IOException, InterruptedException {
        statusCode = manager.toJson(key);

        TaskManager actualManager = getDefaultManager(key);

        List<Task> listActualTask = actualManager.getTreeMapTask();
        assertNotNull(listActualTask, "Список задач не восстановился");
        List<Epic> listActualEpic = actualManager.getTreeMapEpic();
        assertNotNull(listActualEpic, "Список Epic задач не восстановился");
        List<Subtask> listActualSubtask = actualManager.getTreeMapSubtask();
        assertNotNull(listActualSubtask, "Список подзадач не восстановился");
        List<Task> listActualTaskHistory = actualManager.getHistory();
        assertNotNull(listActualTaskHistory, "Список истории задач пуст");


        assertEquals(manager.getTaskById(task1.getId()), actualManager.getTaskById(task1.getId())
                , "Задача " + task1 + " восстановилась не правильно");
        assertEquals(manager.getTaskById(task2.getId()), actualManager.getTaskById(task2.getId())
                , "Задача " + task2 + " восстановилась не правильно");
        assertEquals(manager.getEpicById(epic1.getId()), actualManager.getEpicById(epic1.getId())
                , "Задача " + epic1 + " восстановилась не правильно");
        assertEquals(manager.getEpicById(epic2.getId()), actualManager.getEpicById(epic2.getId())
                , "Задача " + epic2 + " восстановилась не правильно");
        assertEquals(manager.getSubtaskById(subtask1.getId()), actualManager.getSubtaskById(subtask1.getId())
                , "Задача " + subtask1 + " восстановилась не правильно");
        assertEquals(manager.getSubtaskById(subtask2.getId()), actualManager.getSubtaskById(subtask2.getId())
                , "Задача " + subtask2 + " восстановилась не правильно");
        assertEquals(manager.getSubtaskById(subtask3.getId()), actualManager.getSubtaskById(subtask3.getId())
                , "Задача " + subtask3 + " восстановилась не правильно");
    }
}