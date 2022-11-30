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


    public static class Subtask extends Task {
        private int epicId;

        public Subtask(int epicId, String subtaskName, String subtaskDescription, Status subtaskStatus) {
            super(subtaskName, subtaskDescription, subtaskStatus);
            this.epicId = epicId;
        }


        public Subtask(int subtaskId, int epicId, String subtaskName, String subtaskDescription, Status subtaskStatus) {
            super(subtaskName, subtaskDescription, subtaskStatus);
            this.setId(subtaskId);
            this.epicId = epicId;
        }

        public int getEpicId() {
            return epicId;
        }

        public void setEpicId(int epicId) {
            this.epicId = epicId;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            if (!super.equals(obj)) return false;
            Subtask subtask = (Subtask) obj;
            return epicId == subtask.epicId;
        }

        @Override
        public int hashCode() {
            return Objects.hash(super.hashCode(), epicId);
        }

        @Override
        public String toString() {
            return "Subtask{" +
                    "id= " + getId() +
                    ", epicId= " + epicId +
                    ", subtaskName= " + getName() +
                    ", description= " + getDescription() +
                    ", status= " + getStatus() + "}";
        }
    }
}