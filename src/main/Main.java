package main;

import manager.Manager;
import tasks.Task;
import tasks.Epic;
import java.util.ArrayList;
import static tasks.Task.Status.New;
import static tasks.Task.Status.Done;

public class Main {
    public static void main(String[] args) {

        Manager manager = new Manager();

        Task firstTask = new Task("Прогулятся", "Прогулятся по парку", Done);
        manager.saveInTreeMapTask(firstTask);

        Task secondTask = new Task("Пообедать", "Первое, второе и компот", New);
        manager.saveInTreeMapTask(secondTask);


        ArrayList<Integer> arrayListSubtaskIdFirstEpic = new ArrayList<>();
        Task.Status statusFirstEpic = manager.getEpicStatus(arrayListSubtaskIdFirstEpic);
        Epic firstEpic = new Epic("Сходить в магазин", "Купить овощи и фрукты",
                arrayListSubtaskIdFirstEpic, statusFirstEpic);
        manager.saveInTreeMapEpic(firstEpic);

        Epic.Subtask firstSubtaskInFirstEpic = new Epic.Subtask(firstEpic.getId(), "Купить овощи",
                "Картошку, капусту, морковку, свеклу", Done);
        manager.saveInTreeMapSubtask(firstSubtaskInFirstEpic);

        Epic.Subtask secondSubtaskInFirstEpic = new Epic.Subtask(firstEpic.getId(), "Купить фрукты",
                "Яблоки, бананы, мандарины, киви", New);
        manager.saveInTreeMapSubtask(secondSubtaskInFirstEpic);

        manager.addSubtaskInEpic(firstSubtaskInFirstEpic, firstEpic);
        manager.addSubtaskInEpic(secondSubtaskInFirstEpic, firstEpic);
        manager.epicUpdate(firstEpic);


        ArrayList<Integer> arrayListSubtaskIdSecondEpic = new ArrayList<>();
        Task.Status statusSecondEpic = manager.getEpicStatus(arrayListSubtaskIdSecondEpic);
        Epic secondEpic = new Epic("Обслужить автомобиль", "Провести ТО двигателя",
                arrayListSubtaskIdSecondEpic, statusSecondEpic);
        manager.saveInTreeMapEpic(secondEpic);

        Epic.Subtask firstSubtaskInSecondEpic = new Epic.Subtask(secondEpic.getId(), "Замена расходников",
                "Заменить: масло и фильтры", New);
        manager.saveInTreeMapSubtask(firstSubtaskInSecondEpic);

        manager.addSubtaskInEpic(firstSubtaskInSecondEpic, secondEpic);
        manager.epicUpdate(secondEpic);


        System.out.println("2.1 Получение списка всех задач:");
        System.out.println(manager.getTreeMapTask());
        System.out.println(manager.getTreeMapEpic());
        System.out.println(manager.getTreeMapSubtask());


        System.out.println("\n2.2 Удаление всех задач:");
        manager.deletingAllTasks();
        manager.deletingAllEpics();

        System.out.println(manager.getTreeMapTask());
        System.out.println(manager.getTreeMapEpic());
        System.out.println(manager.getTreeMapSubtask());


        System.out.println("\n2.4 Создание. Сам объект должен передаваться в качестве параметра:");

        Task createNewFirstTask = manager.createTask(firstTask);
        manager.saveInTreeMapTask(createNewFirstTask);

        Task createNewSecondTask = manager.createTask(secondTask);
        manager.saveInTreeMapTask(createNewSecondTask);

        System.out.println(createNewFirstTask);
        System.out.println(createNewSecondTask);


        Epic createNewFirstEpic = manager.createEpic(firstEpic);
        manager.saveInTreeMapEpic(createNewFirstEpic);

        Epic.Subtask createNewFirstSubtaskInFirstEpic = manager.createSubtask(firstSubtaskInFirstEpic);
        manager.saveInTreeMapSubtask(createNewFirstSubtaskInFirstEpic);
        manager.addSubtaskInEpic(createNewFirstSubtaskInFirstEpic, createNewFirstEpic);

        Epic.Subtask createNewSecondSubtaskInFirstEpic = manager.createSubtask(secondSubtaskInFirstEpic);
        manager.saveInTreeMapSubtask(createNewSecondSubtaskInFirstEpic);
        manager.addSubtaskInEpic(createNewSecondSubtaskInFirstEpic, createNewFirstEpic);
        manager.epicUpdate(createNewFirstEpic);

        System.out.println(createNewFirstEpic);
        System.out.println(createNewFirstSubtaskInFirstEpic);
        System.out.println(createNewSecondSubtaskInFirstEpic);


        Epic createNewSecondEpic = manager.createEpic(secondEpic);
        manager.saveInTreeMapEpic(createNewSecondEpic);

        Epic.Subtask createNewFirstSubtaskInSecondEpic = manager.createSubtask(firstSubtaskInSecondEpic);
        manager.saveInTreeMapSubtask(createNewFirstSubtaskInSecondEpic);
        manager.addSubtaskInEpic(createNewFirstSubtaskInSecondEpic, createNewSecondEpic);
        manager.epicUpdate(createNewSecondEpic);

        System.out.println(createNewSecondEpic);
        System.out.println(createNewFirstSubtaskInSecondEpic);


        System.out.println("\n2.3 Получение по идентификатору:");
        System.out.println(manager.getTaskById(8));
        System.out.println(manager.getTaskById(9));
        System.out.println(manager.getEpicById(10));
        System.out.println(manager.getSubtaskById(11));
        System.out.println(manager.getSubtaskById(12));
        System.out.println(manager.getEpicById(13));
        System.out.println(manager.getSubtaskById(14));


        System.out.println("\n2.5 Обновление. Новая версия объекта с верным идентификатором передаются в виде "
                + "параметра:");

        createNewSecondTask.setStatus(Done);
        manager.taskUpdate(createNewSecondTask);

        createNewSecondSubtaskInFirstEpic.setStatus(Done);
        manager.subtaskUpdate(createNewSecondSubtaskInFirstEpic);
        manager.epicUpdate(createNewFirstEpic);

        System.out.println(manager.getTreeMapTask());
        System.out.println(manager.getTreeMapEpic());
        System.out.println(manager.getTreeMapSubtask());


        System.out.println("\n3.1 Получение списка всех подзадач определённого эпика:");
        System.out.println(manager.getArrayListSubtaskByEpicId(10));
        System.out.println(manager.getArrayListSubtaskByEpicId(13));


        System.out.println("\n2.6 Удаление по идентификатору:");

        manager.deleteTaskById(8);
        manager.deleteEpicById(13);
        manager.deleteSubtaskById(14);

        System.out.println(manager.getTreeMapTask());
        System.out.println(manager.getTreeMapEpic());
        System.out.println(manager.getTreeMapSubtask());
    }
}