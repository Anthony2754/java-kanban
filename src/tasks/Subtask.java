package tasks;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
public class Subtask extends Task {
    private int epicId;

    public Subtask(int epicId, String subtaskName, String subtaskDescription, Status subtaskStatus,
        LocalDateTime startTime, long duration) {

        super(subtaskName, subtaskDescription, subtaskStatus, startTime, duration);
        this.epicId = epicId;
    }
    public Subtask(int subtaskId, int epicId, String subtaskName, String subtaskDescription, Status subtaskStatus,
        LocalDateTime startTime, long duration) {

        super(subtaskName, subtaskDescription, subtaskStatus, startTime, duration);
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

        String startTime = null;
        String endTime = null;

        if (getStartTime() != null) {
            startTime = getStartTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
        }
        if (getEndTime() != null) {
            endTime = getEndTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
        }

        String subtaskToString = null;

        try {
            subtaskToString = "Subtask{" +
                    "id= " + getId() +
                    ", epicId= " + epicId +
                    ", subtaskName= " + getName() +
                    ", description= " + getDescription() +
                    ", status= " + getStatus() +
                    ", startTime= " + startTime +
                    ", endTime= " + endTime +
                    ", duration= " + getDuration() + "}";
        } catch (NullPointerException ex) {
            System.out.println(ex.getMessage());
        }
        return subtaskToString;
    }
}
