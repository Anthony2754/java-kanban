package managers;

import tasks.*;
import java.util.ArrayList;
import static tasks.Status.NEW;
import static tasks.Status.DONE;

public class Main {
    public static void main(String[] args) {

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


        System.out.println("2.1 Получение списка всех задач:");
        System.out.println(manager.getTreeMapTask());
        System.out.println(manager.getTreeMapEpic());
        System.out.println(manager.getTreeMapSubtask());


        System.out.println();
        System.out.println("2.3 Получение по идентификатору:");

        System.out.println(manager.getEpicById(1));
        System.out.println("История просмотров:");
        System.out.println(manager.getHistory());
        System.out.println();

        System.out.println(manager.getSubtaskById(2));
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


        System.out.println();
        System.out.println("2.6 Удаление по идентификатору:");

        //manager.deleteEpicById(1);
        manager.deleteEpicById(5);
        manager.deleteSubtaskById(3);

        System.out.println(manager.getTreeMapEpic());
        System.out.println(manager.getTreeMapSubtask());

        System.out.println();
        System.out.println("История просмотров:");
        System.out.println(manager.getHistory());

    }
}