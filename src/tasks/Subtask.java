package tasks;

import java.util.Objects;
public class Subtask extends Task {
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
