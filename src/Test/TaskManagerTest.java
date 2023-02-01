package Test;

import managers.TaskManager;
import tasks.Epic;
import tasks.Status;
import static tasks.Status.*;
import tasks.Subtask;
import tasks.Task;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;


abstract class TaskManagerTest<T extends TaskManager> {
    T manager;
    abstract T createManager();
    Task task1;
    Task task2;
    Epic epic1;
    ArrayList<Integer> arrayListSubtaskIdEpic1;
    List<Subtask> arrayListSubtaskEpic;
    Status statusEpic1;
    LocalDateTime startTimeEpic1;
    long durationEpic1;
    Subtask subtask1;
    Subtask subtask2;
    Subtask subtask3;
    Epic epic2;
    ArrayList<Integer> arrayListSubtaskIdEpic2;
    Status statusEpic2;
    LocalDateTime startTimeEpic2;
    long durationEpic2;
    List<Task> listTask;
    List<Epic> listEpic;
    List<Subtask> listSubtask;

    @BeforeEach
    public void BeforeEach() {

        manager = createManager();

        arrayListSubtaskIdEpic1 = new ArrayList<>();
        statusEpic1 = manager.getterEpicStatus(arrayListSubtaskIdEpic1);
        startTimeEpic1 = manager.getterEpicStartTime(arrayListSubtaskIdEpic1);
        durationEpic1 = manager.getterEpicDuration(arrayListSubtaskIdEpic1);

        epic1 = manager.createEpic(new Epic("Сходить в магазин", "Купить овощи фрукты и мясо",
                arrayListSubtaskIdEpic1, statusEpic1, startTimeEpic1, durationEpic1));

        manager.saveInTreeMapEpic(epic1);

        subtask1 = manager.createSubtask(new Subtask(epic1.getId(), "Купить овощи",
                "Картошку капусту морковку свеклу", DONE, LocalDateTime.of(2023, 2,
                1, 20, 30),
                5L));
        manager.saveInTreeMapSubtask(subtask1);

        subtask2 = manager.createSubtask(new Subtask(epic1.getId(), "Купить фрукты",
                "Яблоки бананы мандарины киви", NEW, LocalDateTime.of(2023, 2, 1,
                20, 35),
                5L));

        manager.saveInTreeMapSubtask(subtask2);

        subtask3 = manager.createSubtask(new Subtask(epic1.getId(), "Купить мясо",
                "Говядина свинина", NEW, LocalDateTime.of(2023, 2, 1,
                20, 40), 5L));
        manager.saveInTreeMapSubtask(subtask3);

        manager.addSubtaskInEpic(subtask1, epic1);
        manager.addSubtaskInEpic(subtask2, epic1);
        manager.addSubtaskInEpic(subtask3, epic1);

        manager.epicUpdate(epic1);

        arrayListSubtaskIdEpic2 = new ArrayList<>();
        statusEpic2 = manager.getterEpicStatus(arrayListSubtaskIdEpic2);
        startTimeEpic2 = manager.getterEpicStartTime(arrayListSubtaskIdEpic2);
        durationEpic2 = manager.getterEpicDuration(arrayListSubtaskIdEpic2);

        epic2 = manager.createEpic(new Epic("Обслужить автомобиль",
                "Провести ТО двигателя", arrayListSubtaskIdEpic2, statusEpic2,
                startTimeEpic2, durationEpic2));

        manager.saveInTreeMapEpic(epic2);


        task1 = manager.createTask(new Task("Прогуляться", "Прогуляться по парку", NEW,
                LocalDateTime.of(2023, 2, 1, 21, 0), 30L));
        manager.saveInTreeMapTask(task1);

        task2 = manager.createTask(new Task("Пообедать", "Первое второе и компот", NEW,
                LocalDateTime.of(2023, 2, 1, 21, 35), 15L));
        manager.saveInTreeMapTask(task2);
    }

    @Test
    void addSubtaskInEpic() {

        int expected = epic1.getId();
        int actual = subtask1.getEpicId();

        assertEquals(expected, actual,"Epic ID не добавился в Subtask");
        assertNotNull(epic1.getArrayListSubtaskId(), "Subtask ID не добавился в список ID подзадач Epic");

        assertNotNull(manager.getEpicById(epic1.getId()), "Epic ID не существует");
        assertNotNull(manager.getSubtaskById(subtask1.getId()), "Subtask ID не существует");
    }

    @Test
    void getArrayListSubtaskByEpicId() {

        arrayListSubtaskEpic = manager.getArrayListSubtaskByEpicId(epic1.getId());

        assertNotNull(arrayListSubtaskEpic, "Список Subtask пуст");

        int expected = 3;
        int actual = arrayListSubtaskEpic.size();

        assertEquals(expected, actual, "Subtask не добавилась в список");

        assertNotNull(manager.getEpicById(epic1.getId()), "Epic ID не существует");
    }

    @Test
    void getterEpicTaskStatus() {

        arrayListSubtaskEpic = manager.getArrayListSubtaskByEpicId(epic1.getId());

        assertNotNull(arrayListSubtaskEpic, "Список Subtask пуст");

        Status actual1 = manager.getterEpicStatus(epic1.getArrayListSubtaskId());

        assertEquals(IN_PROGRESS, actual1, "Epic Статус IN_PROGRESS рассчитан не правильно");

        subtask1.setStatus(NEW);

        Status actual2 = manager.getterEpicStatus(epic1.getArrayListSubtaskId());

        assertEquals(NEW, actual2, "Epic Статус NEW рассчитан не правильно");

        subtask1.setStatus(DONE);
        subtask2.setStatus(DONE);
        subtask3.setStatus(DONE);
        Status actual3 = manager.getterEpicStatus(epic1.getArrayListSubtaskId());

        assertEquals(DONE, actual3, "Epic Статус DONE рассчитан не правильно");

        subtask1.setStatus(IN_PROGRESS);
        subtask2.setStatus(IN_PROGRESS);
        subtask3.setStatus(IN_PROGRESS);
        Status actual4 = manager.getterEpicStatus(epic1.getArrayListSubtaskId());

        assertEquals(IN_PROGRESS, actual4, "Epic Статус IN_PROGRESS рассчитан не правильно");
    }

    @Test
    void getterEpicStartTime() {

        arrayListSubtaskEpic = manager.getArrayListSubtaskByEpicId(epic1.getId());

        assertNotNull(arrayListSubtaskEpic, "Список Subtask пуст");

        LocalDateTime expected = LocalDateTime.of(2023, 2, 1, 20, 30);
        LocalDateTime actual = manager.getterEpicStartTime(epic1.getArrayListSubtaskId());

        assertEquals(expected, actual, "Дата и время начала самой ранней Subtask рассчитаны не правильно");

        assertNotNull(manager.getEpicById(epic1.getId()), "Epic ID не существует");
    }

    @Test
    void getterEpicDuration() {

        arrayListSubtaskEpic = manager.getArrayListSubtaskByEpicId(epic1.getId());

        assertNotNull(arrayListSubtaskEpic, "Список Subtask пуст");

        long expected = 15L;
        long actual = manager.getterEpicDuration(epic1.getArrayListSubtaskId());

        assertEquals(expected, actual, "Продолжительность всех Subtask рассчитана не правильно");

        assertNotNull(manager.getEpicById(epic1.getId()), "Epic ID не существует");
    }

    @Test
    void getterEpicEndTime() {

        arrayListSubtaskEpic = manager.getArrayListSubtaskByEpicId(epic1.getId());

        assertNotNull(arrayListSubtaskEpic, "Список Subtask пуст");

        LocalDateTime expected = LocalDateTime.of(2023, 2, 1, 20, 45);
        LocalDateTime actual = manager.getterEpicEndTime(epic1.getArrayListSubtaskId()).truncatedTo(ChronoUnit.MINUTES);

        assertEquals(expected, actual, "Дата и время начала самой поздней Subtask рассчитана не правильно");

        assertNotNull(manager.getEpicById(epic1.getId()), "Epic ID  не существует");
    }

    @Test
    void createTask() {

        listTask = manager.getTreeMapTask();
        listEpic = manager.getTreeMapEpic();
        listSubtask = manager.getTreeMapSubtask();

        assertNotNull(listTask, "Список Task пуст");
        assertNotNull(listEpic, "Список Epic пуст");
        assertNotNull(listSubtask, "Список Subtask пуст");

        Task expected1 = new Task(6,"Прогуляться", "Прогуляться по парку", NEW,
                LocalDateTime.of(2023, 2, 1, 21, 0), 30L);
        Task actual1 = task1;

        assertEquals(expected1, actual1,"Task задача создана не правильно");

        Epic expected2 = new Epic(1, "Сходить в магазин", "Купить овощи фрукты и мясо",
                new ArrayList<>(List.of(2, 3, 4)), IN_PROGRESS,
                LocalDateTime.of(2023, 2, 1, 20, 30), 15L);

        Epic actual2 = epic1;

        assertEquals(expected2, actual2,"Epic задача создана не правильно");

        Subtask expected3 = new Subtask(2, 1, "Купить овощи",
                "Картошку капусту морковку свеклу", DONE,
                LocalDateTime.of(2023, 2, 1, 20, 30), 5L);

        Subtask actual3 = subtask1;

        assertEquals(expected3, actual3,"Subtask задача создана не правильно");

        assertNotNull(manager.getTaskById(task1.getId()), "Task ID не существует");
        assertNotNull(manager.getEpicById(epic1.getId()), "Epic ID задачи не существует");
        assertNotNull(manager.getSubtaskById(subtask1.getId()), "Subtask ID не существует");
    }

    @Test
    void saveTask() {

        listTask = manager.getTreeMapTask();

        assertNotNull(listTask, "Список Task задач пуст");

        Task expected = new Task(6, "Прогуляться", "Прогуляться по парку", NEW,
                LocalDateTime.of(2023, 2, 1, 21, 0), 30L);

        Task actual = manager.getTaskById(6);

        assertEquals(expected, actual,"Task сохранена не правильно");

        assertNotNull(manager.getTaskById(task1.getId()), "Task ID не существует");
    }

    @Test
    void saveEpic() {

        listEpic = manager.getTreeMapEpic();

        assertNotNull(listEpic, "Список Epic задач пуст");

        Epic expected = new Epic(1, "Сходить в магазин", "Купить овощи фрукты и мясо",
                new ArrayList<>(List.of(2, 3, 4)), IN_PROGRESS,
                LocalDateTime.of(2023, 2, 1, 20, 30),15L);
        Epic actual = manager.getEpicById(epic1.getId());

        assertEquals(expected, actual,"Epic Задача сохранена не правильно");

        assertNotNull(manager.getEpicById(epic1.getId()), "Epic ID не существует");
    }

    @Test
    void saveSubtask() {

        listSubtask = manager.getTreeMapSubtask();

        assertNotNull(listSubtask, "Список Subtask задач пуст");

        Subtask expected = new Subtask(2, 1, "Купить овощи",
                "Картошку капусту морковку свеклу", DONE,
                LocalDateTime.of(2023, 2, 1, 20, 30), 5L);

        Subtask actual = manager.getSubtaskById(subtask1.getId());

        assertEquals(expected, actual,"Subtask сохранена не верно");

        assertNotNull(manager.getSubtaskById(subtask1.getId()), "Subtask ID не существует");
    }

    @Test
    void getTaskById() {

        listTask = manager.getTreeMapTask();

        assertNotNull(listTask, "Список Task задач пуст");

        Task expected = new Task(6, "Прогуляться", "Прогуляться по парку", NEW
                , LocalDateTime.of(2023, 2, 1, 21, 0), 30L);

        Task actual = manager.getTaskById(6);

        assertEquals(expected, actual,"Task получена не правильно");

        assertNotNull(manager.getTaskById(task1.getId()), "Task ID не существует");
    }

    @Test
    void getEpicById() {

        listEpic = manager.getTreeMapEpic();

        assertNotNull(listEpic, "Список задач пуст");

        Epic expected = new Epic(1, "Сходить в магазин", "Купить овощи фрукты и мясо",
                new ArrayList<>(List.of(2, 3, 4)), IN_PROGRESS,
                LocalDateTime.of(2023, 2, 1, 20, 30),15L);

        Epic actual = manager.getEpicById(epic1.getId());

        assertEquals(expected, actual,"Epic получена не правильно");

        assertNotNull(manager.getEpicById(epic1.getId()), "Epic ID не существует");
    }

    @Test
    void getSubtaskById() {

        listSubtask = manager.getTreeMapSubtask();

        assertNotNull(listSubtask, "Список Subtask пуст");

        Subtask expected = new Subtask(2, 1, "Купить овощи",
                "Картошку капусту морковку свеклу", DONE,
                LocalDateTime.of(2023, 2, 1, 20, 30), 5L);
        Subtask actual = manager.getSubtaskById(subtask1.getId());

        assertEquals(expected, actual,"Подзадача получена не правильно");

        assertNotNull(manager.getSubtaskById(subtask1.getId()), "Subtask ID не существует");
    }

    @Test
    void updateTask() {

        listTask = manager.getTreeMapTask();

        assertNotNull(listTask, "Список Task пуст");

        Task expected = new Task(6, "Прогуляться", "Прогуляться по парку", DONE,
                LocalDateTime.of(3033, 3, 30, 13, 33), 33L);

        manager.taskUpdate(expected);
        Task actual = manager.getTaskById(6);

        assertEquals(expected, actual,"Задача обновлена не правильно");

        assertNotNull(manager.getTaskById(task1.getId()), "Task ID не существует");
    }

    @Test
    void updateEpic() {

        listEpic = manager.getTreeMapEpic();

        assertNotNull(listEpic, "Список Epic пуст");

        Epic expected = new Epic(1, "Сходить в магазин", "Купить овощи фрукты и мясо",
                new ArrayList<>(List.of(2, 3, 4)), NEW,
                LocalDateTime.of(3033, 3, 30, 13, 33), 33L);

        manager.epicUpdate(expected);
        Epic actual = manager.getEpicById(epic1.getId());

        assertEquals(expected, actual,"Epic обновлен не верно");

        assertNotNull(manager.getEpicById(epic1.getId()), "Epic ID не существует");
    }

    @Test
    void updateSubtask() {

        listSubtask = manager.getTreeMapSubtask();

        assertNotNull(listSubtask, "Список Subtask пуст");

        Subtask expected = new Subtask(2, 1, "Купить овощи",
                "Картошку капусту морковку свеклу", DONE,
                LocalDateTime.of(3033, 3, 30, 13, 33), 33L);

        manager.subtaskUpdate(expected);
        Subtask actual = manager.getSubtaskById(subtask1.getId());

        assertEquals(expected, actual,"Subtask обновлена не правильно");

        assertNotNull(manager.getSubtaskById(subtask1.getId()), "Subtask ID не существует");
    }

    @Test
    void getTreeMapTask() {

        listTask = manager.getTreeMapTask();

        assertNotNull(listTask, "Список Task пуст");

        List<Task> expected = new ArrayList<>(List.of(task1, task2));
        List<Task> actual = manager.getTreeMapTask();

        assertEquals(expected, actual,"Task список получен не неправильно");

        assertNotNull(manager.getTaskById(task1.getId()), "ID Task1 не существует");
        assertNotNull(manager.getTaskById(task2.getId()), "ID Task2 не существует");
    }

    @Test
    void getTreeMapEpic() {

        listEpic = manager.getTreeMapEpic();

        assertNotNull(listEpic, "Список Epic пуст");

        List<Epic> expected = new ArrayList<>(List.of(epic1, epic2));
        List<Epic> actual = manager.getTreeMapEpic();

        assertEquals(expected, actual,"Список Epic задач получен не правильно");

        assertNotNull(manager.getEpicById(epic1.getId()), "ID Epic1 не существует");
        assertNotNull(manager.getEpicById(epic2.getId()), "ID Epic2 не существует");
    }

    @Test
    void getTreeMapSubtask() {

        listSubtask = manager.getTreeMapSubtask();

        assertNotNull(listSubtask, "Список Subtask пуст");

        List<Subtask> expected = new ArrayList<>(List.of(subtask1, subtask2, subtask3));
        List<Subtask> actual = manager.getTreeMapSubtask();

        assertEquals(expected, actual,"Список Subtask получен не правильно");

        assertNotNull(manager.getSubtaskById(subtask1.getId()), "ID Subtask1 не существует");
        assertNotNull(manager.getSubtaskById(subtask2.getId()), "ID Subtask2 не существует");
        assertNotNull(manager.getSubtaskById(subtask3.getId()), "ID Subtask3 не существует");
    }

    @Test
    void getPrioritizedTasks() {

        listTask = manager.getTreeMapTask();
        listEpic = manager.getTreeMapEpic();
        listSubtask = manager.getTreeMapSubtask();

        assertNotNull(listTask, "Список Task пуст");
        assertNotNull(listEpic, "Список Epic задач пуст");
        assertNotNull(listSubtask, "Список Subtask пуст");

        Set<Task> expected = new TreeSet<>((task1, task2) -> {
            if ((task1.getStartTime() != null) && (task2.getStartTime() != null)) {
                return task1.getStartTime().compareTo(task2.getStartTime());
            } else if (task1.getStartTime() == null) {
                return 1;
            } else if (null == task2.getStartTime()) {
                return -1;
            } else {
                return 0;
            }
        });
        expected.addAll(manager.getTreeMapTask());
        expected.addAll(manager.getTreeMapSubtask());

        Set<Task> actual = manager.getterPrioritizedTasks();

        assertEquals(expected, actual,"Список отсортированных задач получен не правильно");

        assertNotNull(manager.getEpicById(epic1.getId()), "ID Epic1 не существует");
        assertNotNull(manager.getEpicById(epic2.getId()), "ID Epic2 не существует");

        assertNotNull(manager.getSubtaskById(subtask1.getId()), "ID Subtask1 не существует");
        assertNotNull(manager.getSubtaskById(subtask2.getId()), "ID Subtask2 не существует");
        assertNotNull(manager.getSubtaskById(subtask3.getId()), "ID Subtask3 не существует");

        assertNotNull(manager.getTaskById(task1.getId()), "ID Task1 не существует");
        assertNotNull(manager.getTaskById(task2.getId()), "ID Task2 не существует");
    }

    @Test
    void deleteTaskById() {

        listTask = manager.getTreeMapTask();

        assertNotNull(listTask, "Список Task пуст");

        assertNotNull(manager.getTaskById(task1.getId()), "Task ID не существует");

        manager.deleteTaskById(task1.getId());
        Task actual = manager.getTaskById(task1.getId());

        assertNull(actual,"Задача удалена не правильно");
    }

    @Test
    void deleteEpicById() {

        listEpic = manager.getTreeMapEpic();

        assertNotNull(listEpic, "Список Epic пуст");

        assertNotNull(manager.getEpicById(epic1.getId()), "Epic ID не существует");

        manager.deleteEpicById(epic1.getId());
        Epic actual = manager.getEpicById(epic1.getId());

        assertNull(actual,"Epic удален не правильно");
    }

    @Test
    void deleteSubtaskById() {

        listSubtask = manager.getTreeMapSubtask();

        assertNotNull(listSubtask, "Список Subtask пуст");

        assertNotNull(manager.getSubtaskById(subtask1.getId()), "Subtask ID не существует");

        manager.deleteSubtaskById(subtask1.getId());
        Subtask actual = manager.getSubtaskById(subtask1.getId());

        assertNull(actual,"Subtask удален не правильно");
    }

    @Test
    void deletingAllTasks() {

        listTask = manager.getTreeMapTask();

        assertNotNull(listTask, "Список Task пуст");

        assertNotNull(manager.getTaskById(task1.getId()), "ID Task1 не существует");
        assertNotNull(manager.getTaskById(task2.getId()), "ID Task2 не существует");

        manager.deletingAllTasks();
        List<Task> actual = manager.getTreeMapTask();

        assertTrue(actual.isEmpty(),"Task удален не правильно");
    }

    @Test
    void deletingAllEpics() {

        listEpic = manager.getTreeMapEpic();

        assertNotNull(listEpic, "Список Epic пуст");

        assertNotNull(manager.getEpicById(epic1.getId()), "ID Epic1 не существует");
        assertNotNull(manager.getEpicById(epic2.getId()), "ID Epic2 не существует");

        manager.deletingAllEpics();

        List<Epic> actual1 = manager.getTreeMapEpic();
        List<Subtask> actual2 = manager.getTreeMapSubtask();

        assertTrue(actual1.isEmpty(),"Epic удален не правильно");
        assertTrue(actual2.isEmpty(),"Subtask удален не правильно");
    }

    @Test
    void deletingAllSubtasks() {

        listSubtask = manager.getTreeMapSubtask();

        assertNotNull(listSubtask, "Список Subtask пуст");

        assertNotNull(manager.getSubtaskById(subtask1.getId()), "ID Subtask1 не существует");
        assertNotNull(manager.getSubtaskById(subtask2.getId()), "ID Subtask2 не существует");
        assertNotNull(manager.getSubtaskById(subtask3.getId()), "ID Subtask3 не существует");

        manager.deletingAllSubtasks();
        List<Subtask> actual = manager.getTreeMapSubtask();

        assertTrue(actual.isEmpty(),"Subtask удален не правильно");
    }
}