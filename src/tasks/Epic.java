package tasks;

import java.util.ArrayList;
import java.util.Objects;


public class Epic extends Task {
    private final ArrayList<Integer> arrayListSubtaskId;

    public Epic(String epicName, String epicDescription, ArrayList<Integer> ArrayListSubtaskIdOfEpic,
                Status epicStatus) {
        super(epicName, epicDescription, epicStatus);
        this.arrayListSubtaskId = new ArrayList<>(ArrayListSubtaskIdOfEpic);
    }


    public Epic(int epicId, String epicName, String epicDescription, ArrayList<Integer> ArrayListSubtaskIdOfEpic,
                Status epicStatus) {
        super(epicName, epicDescription, epicStatus);
        this.setId(epicId);
        this.arrayListSubtaskId = ArrayListSubtaskIdOfEpic;
    }

    public ArrayList<Integer> getArrayListSubtaskId() {
        return arrayListSubtaskId;
    }

    @Override
    // Если я все правильно понял, то сначала идут обычные методы, а потом уже @Override, но оно тут так и есть
    // Но в классе Task был 1 метод не на месте
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        if (!super.equals(obj)) return false;
        Epic epic = (Epic) obj;
        return arrayListSubtaskId.equals(epic.arrayListSubtaskId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), arrayListSubtaskId);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id= " + getId() +
                ", epicName= " + getName() +
                ", description= " + getDescription() +
                ", arrayListSubtaskId= " + arrayListSubtaskId +
                ", status= " + getStatus() + "}";
    }
}