package test;

// если я правильно понял, я переименовал пакет Test в test и теперь все работает вроде бы :)

import static org.junit.jupiter.api.Assertions.*;

import managers.HistoryManager;
import managers.InMemoryHistoryManager;
import managers.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static tasks.Status.DONE;
import static tasks.Status.NEW;

class HistoryManagerTest extends InMemoryTaskManager {
    HistoryManager<Task> historyManager = new InMemoryHistoryManager();
    Task task1;
    Task task2;
    Epic epic1;
    Subtask subtask1;
    List<Task> history;
    ArrayList<Integer> arrayListSubtaskIdEpic1;
    Status statusEpic1;
    LocalDateTime startTimeEpic1;
    long durationEpic1;

    @BeforeEach
    public void beforeEach() {

        arrayListSubtaskIdEpic1 = new ArrayList<>();
        statusEpic1 = getterEpicStatus(arrayListSubtaskIdEpic1);
        startTimeEpic1 = getterEpicStartTime(arrayListSubtaskIdEpic1);
        durationEpic1 = getterEpicDuration(arrayListSubtaskIdEpic1);

        epic1 = createEpic(new Epic("Сходить в магазин", "Купить овощи фрукты и мясо"
                , arrayListSubtaskIdEpic1, statusEpic1, startTimeEpic1, durationEpic1));
        saveInTreeMapEpic(epic1);

        subtask1 = createSubtask(new Subtask(epic1.getId(), "Купить овощи",
                "Картошку капусту морковку свеклу", DONE, LocalDateTime.now().plusMinutes(60L),
                5L));
        saveInTreeMapSubtask(subtask1);

        addSubtaskInEpic(subtask1, epic1);
        epicUpdate(epic1);


        task1 = new Task("Прогуляться", "Прогуляться по парку", NEW,
                LocalDateTime.now().plusMinutes(3L), 30L);
        saveInTreeMapTask(task1);

        task2 = new Task("Пообедать", "Первое второе и компот", NEW,
                LocalDateTime.now().plusMinutes(35L), 10L);
        saveInTreeMapTask(task2);

        historyManager.addTaskInHistory(task1);
        historyManager.addTaskInHistory(task1);
        historyManager.addTaskInHistory(epic1);
        historyManager.addTaskInHistory(subtask1);

        history = historyManager.getHistory();
    }

    @Test
    void addTaskInHistory() {

        assertNotNull(history, "Пустая история просмотров");

        int expected = 3;
        int actual = history.size();

        assertEquals(expected, actual, "История просмотров дублируется");
    }

    @Test
    public void getTaskHistory() {

        assertNotNull(history, "Пустая история просмотров");

        int expected = 3;
        int actual = history.size();

        assertEquals(expected, actual, "История просмотров дублируется");

        historyManager.removeFromHistory(task1.getId());
        historyManager.removeFromHistory(epic1.getId());
        historyManager.removeFromHistory(subtask1.getId());

        history = historyManager.getHistory();

        assertTrue(history.isEmpty(), "Задачи не удаляются из истории просмотров");
    }

    @Test
    void removeTaskFromHistory() {

        assertNotNull(history, "Пустая история просмотров");

        int expected = 3;
        int actual = history.size();

        assertEquals(expected, actual, "История просмотров дублируется.");

        historyManager.removeFromHistory(task1.getId());
        historyManager.removeFromHistory(epic1.getId());
        historyManager.removeFromHistory(subtask1.getId());

        history = historyManager.getHistory();

        assertTrue(history.isEmpty(), "Задачи не удаляются из истории просмотров");
    }
}
