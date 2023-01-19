package managers;

import tasks.*;
import java.util.ArrayList;
import static tasks.Status.NEW;
import static tasks.Status.DONE;

public class Main {
    public static void main(String[] args) {
/*
        TaskManager manager = Managers.getDefault();


        ArrayList<Integer> arrayListSubtaskIdFirstEpic = new ArrayList<>();
        Status statusFirstEpic = manager.getEpicStatus(arrayListSubtaskIdFirstEpic);

        Epic firstEpic = new Epic("Сходить в магазин", "Купить овощи, фрукты и мясо",
                arrayListSubtaskIdFirstEpic, statusFirstEpic);
        manager.saveInTreeMapEpic(firstEpic);

        Subtask firstSubtaskInFirstEpic = new Subtask(firstEpic.getId(), "Купить овощи",
                "Картошку, капусту, морковку, свеклу", DONE);
        manager.saveInTreeMapSubtask(firstSubtaskInFirstEpic);

        Subtask secondSubtaskInFirstEpic = new Subtask(firstEpic.getId(), "Купить фрукты",
                "Яблоки, бананы, мандарины, киви", NEW);
        manager.saveInTreeMapSubtask(secondSubtaskInFirstEpic);

        Subtask thirdSubtaskInFirstEpic = new Subtask(firstEpic.getId(), "Купить мясо",
                "Говядина, свинина", NEW);
        manager.saveInTreeMapSubtask(thirdSubtaskInFirstEpic);

        manager.addSubtaskInEpic(firstSubtaskInFirstEpic, firstEpic);
        manager.addSubtaskInEpic(secondSubtaskInFirstEpic, firstEpic);
        manager.addSubtaskInEpic(thirdSubtaskInFirstEpic, firstEpic);
        manager.epicUpdate(firstEpic);


        ArrayList<Integer> arrayListSubtaskIdSecondEpic = new ArrayList<>();
        Status statusSecondEpic = manager.getEpicStatus(arrayListSubtaskIdSecondEpic);

        Epic secondEpic = new Epic("Обслужить автомобиль", "Провести ТО двигателя",
                arrayListSubtaskIdSecondEpic, statusSecondEpic);
        manager.saveInTreeMapEpic(secondEpic);

        manager.epicUpdate(secondEpic);


        Task firstTask = new Task("Прогуляться", "Прогуляться по парку", DONE);
        manager.saveInTreeMapTask(firstTask);

        Task secondTask = new Task("Пообедать", "Первое, второе и компот", NEW);
        manager.saveInTreeMapTask(secondTask);


        System.out.println("2.1 Получение списка всех задач:");
        System.out.println(manager.getTreeMapTask());
        System.out.println(manager.getTreeMapEpic());
        System.out.println(manager.getTreeMapSubtask());


        System.out.println();
        System.out.println("2.3 Получение по идентификатору:");

        System.out.println(manager.getEpicById(1));
        System.out.println(manager.getTaskById(6));
        System.out.println("История просмотров:");
        System.out.println(manager.getHistory());
        System.out.println();

        System.out.println(manager.getSubtaskById(2));
        System.out.println(manager.getTaskById(7));
        System.out.println("История просмотров:");
        System.out.println(manager.getHistory());
        System.out.println();

        System.out.println(manager.getSubtaskById(3));
        System.out.println("История просмотров:");
        System.out.println(manager.getHistory());
        System.out.println();

        System.out.println(manager.getSubtaskById(4));
        System.out.println("История просмотров:");
        System.out.println(manager.getHistory());
        System.out.println();

        System.out.println(manager.getEpicById(1));
        System.out.println("История просмотров:");
        System.out.println(manager.getHistory());
        System.out.println();

        System.out.println(manager.getSubtaskById(2));
        System.out.println("История просмотров:");
        System.out.println(manager.getHistory());
        System.out.println();

        System.out.println(manager.getEpicById(5));
        System.out.println("История просмотров:");
        System.out.println(manager.getHistory());
        System.out.println();

        System.out.println(manager.getSubtaskById(2));
        System.out.println("История просмотров:");
        System.out.println(manager.getHistory());
        System.out.println();

        System.out.println(manager.getSubtaskById(4));
        System.out.println("История просмотров:");
        System.out.println(manager.getHistory());
        System.out.println();

        System.out.println(manager.getEpicById(5));
        System.out.println("История просмотров:");
        System.out.println(manager.getHistory());
        System.out.println();

        System.out.println(manager.getSubtaskById(4));
        System.out.println("История просмотров:");
        System.out.println(manager.getHistory());
        System.out.println();

        System.out.println(manager.getEpicById(5));
        System.out.println("История просмотров:");
        System.out.println(manager.getHistory());
        System.out.println();


/*
        System.out.println();
        System.out.println("2.2 Удаление всех задач:");
        manager.deletingAllTasks();
        manager.deletingAllEpics();

        System.out.println(manager.getTreeMapTask());
        System.out.println(manager.getTreeMapEpic());
        System.out.println(manager.getTreeMapSubtask());

        System.out.println();
        System.out.println("История просмотров:");
        System.out.println(manager.getHistory());
 */
/*
        System.out.println();
        System.out.println("2.4 Создание. Сам объект должен передаваться в качестве параметра:");

        Task createNewFirstTask = manager.createTask(firstTask);
        manager.saveInTreeMapTask(createNewFirstTask);

        Task createNewSecondTask = manager.createTask(secondTask);
        manager.saveInTreeMapTask(createNewSecondTask);

        System.out.println(createNewFirstTask);
        System.out.println(createNewSecondTask);


        Epic createNewFirstEpic = manager.createEpic(firstEpic);
        manager.saveInTreeMapEpic(createNewFirstEpic);

        Subtask createNewFirstSubtaskInFirstEpic = manager.createSubtask(firstSubtaskInFirstEpic);
        manager.saveInTreeMapSubtask(createNewFirstSubtaskInFirstEpic);
        manager.addSubtaskInEpic(createNewFirstSubtaskInFirstEpic, createNewFirstEpic);

        Subtask createNewSecondSubtaskInFirstEpic = manager.createSubtask(secondSubtaskInFirstEpic);
        manager.saveInTreeMapSubtask(createNewSecondSubtaskInFirstEpic);
        manager.addSubtaskInEpic(createNewSecondSubtaskInFirstEpic, createNewFirstEpic);
        manager.epicUpdate(createNewFirstEpic);

        Subtask createNewThirdSubtaskInFirstEpic = manager.createSubtask(thirdSubtaskInFirstEpic);
        manager.saveInTreeMapSubtask(createNewThirdSubtaskInFirstEpic);
        manager.addSubtaskInEpic(createNewThirdSubtaskInFirstEpic, createNewFirstEpic);
        manager.epicUpdate(createNewFirstEpic);

        System.out.println(createNewFirstEpic);
        System.out.println(createNewFirstSubtaskInFirstEpic);
        System.out.println(createNewSecondSubtaskInFirstEpic);
        System.out.println(createNewThirdSubtaskInFirstEpic);


        Epic createNewSecondEpic = manager.createEpic(secondEpic);
        manager.saveInTreeMapEpic(createNewSecondEpic);

        //Subtask createNewFirstSubtaskInSecondEpic = manager.createSubtask(firstSubtaskInSecondEpic);
        //manager.saveInTreeMapSubtask(createNewFirstSubtaskInSecondEpic);
        //manager.addSubtaskInEpic(createNewFirstSubtaskInSecondEpic, createNewSecondEpic);
        manager.epicUpdate(createNewSecondEpic);

        System.out.println(createNewSecondEpic);
        //System.out.println(createNewFirstSubtaskInSecondEpic);



        System.out.println();
        System.out.println("2.5 Обновление. Новая версия объекта с верным идентификатором передаются в виде "
                + "параметра:");

        createNewSecondTask.setStatus(DONE);
        manager.taskUpdate(createNewSecondTask);

        createNewSecondSubtaskInFirstEpic.setStatus(DONE);
        manager.subtaskUpdate(createNewSecondSubtaskInFirstEpic);
        manager.epicUpdate(createNewFirstEpic);

        System.out.println(manager.getTreeMapTask());
        System.out.println(manager.getTreeMapEpic());
        System.out.println(manager.getTreeMapSubtask());


        System.out.println();
        System.out.println("3.1 Получение списка всех подзадач определённого эпика:");
        System.out.println(manager.getArrayListSubtaskByEpicId(1));
        System.out.println(manager.getArrayListSubtaskByEpicId(5));


        System.out.println();
        System.out.println("2.6 Удаление по идентификатору:");

        //manager.deleteEpicById(1);
        manager.deleteEpicById(5);
        manager.deleteSubtaskById(3);
        manager.deleteSubtaskById(12);
        manager.deleteTaskById(6);

        System.out.println(manager.getTreeMapTask());
        System.out.println(manager.getTreeMapEpic());
        System.out.println(manager.getTreeMapSubtask());

        System.out.println();
        System.out.println("История просмотров:");
        System.out.println(manager.getHistory());


        System.out.println();
        System.out.println(String.valueOf(firstTask.getClass()));
        System.out.println();
        System.out.println(String.valueOf(firstEpic.getClass()));
        System.out.println();
        System.out.println(String.valueOf(firstSubtaskInFirstEpic.getClass()));
*/
    }
}