package tasks;

import java.util.Objects;


public class Task {
    private final String name;
    private final String description;
    private int id;
    private Status status;

    public Task(String taskName, String taskDescription, Status taskStatus) {
        this.name = taskName;
        this.description = taskDescription;
        this.status = taskStatus;
    }


    public Task(int taskId, String taskName, String taskDescription, Status taskStatus) {
        this.id = taskId;
        this.name = taskName;
        this.description = taskDescription;
        this.status = taskStatus;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Status getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Task task = (Task) obj;
        return id == task.id && name.equals(task.name) && description.equals(task.description) && status == task.status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }


    @Override
    public int hashCode() {

        return Objects.hash(id, name, description, status);
    }

    @Override
    public String toString() {
        return "Task{" +
                "id= " + id +
                ", taskName= " + name +
                ", description= " + description +
                ", status= " + status + "}";
    }


    public enum Status {
        NEW,
        DONE,
        IN_PROGRESS
    }
}