package tasks;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;


public class Epic extends Task {
    private ArrayList<Integer> arrayListSubtaskId;

    public Epic(String epicName, String epicDescription, ArrayList<Integer> ArrayListSubtaskIdOfEpic,
                Status epicStatus, LocalDateTime startTime, long duration) {

        super(epicName, epicDescription, epicStatus, startTime, duration);
        this.arrayListSubtaskId = new ArrayList<>(ArrayListSubtaskIdOfEpic);
    }


    public Epic(int epicId, String epicName, String epicDescription, ArrayList<Integer> ArrayListSubtaskIdOfEpic,
                Status epicStatus, LocalDateTime startTime, long duration) {
        super(epicName, epicDescription, epicStatus, startTime, duration);
        this.setId(epicId);
        this.arrayListSubtaskId = ArrayListSubtaskIdOfEpic;
    }

    public ArrayList<Integer> getArrayListSubtaskId() {
        return arrayListSubtaskId;
    }

    public void setArrayListSubtaskId(ArrayList<Integer> arrayListSubtaskId) {
        this.arrayListSubtaskId = arrayListSubtaskId;
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

        String startTime = null;
        String endTime = null;

        if (getStartTime() != null) {
            startTime = getStartTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
        }
        if (getEndTime() != null) {
            endTime = getEndTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
        }

        String epicToString = null;

        try {
            epicToString = "Epic{" +
                    "id= " + getId() +
                    ", epicName= " + getName() +
                    ", description= " + getDescription() +
                    ", arrayListSubtaskId= " + arrayListSubtaskId +
                    ", status= " + getStatus() +
                    ", startTime= " + startTime +
                    ", endTime= " + endTime +
                    ", duration= " + getDuration() + "}";
        } catch (NullPointerException ex) {
            System.out.println(ex.getMessage());
        }
        return epicToString;
    }
}