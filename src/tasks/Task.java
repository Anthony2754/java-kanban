package tasks;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task {
    private String name;
    private String description;
    private int id;
    private Status status;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private long duration;


    public Task(String taskName, String taskDescription, Status taskStatus, LocalDateTime startTime, long duration) {
        this.name = taskName;
        this.description = taskDescription;
        this.status = taskStatus;
        this.startTime = startTime;
        this.duration = duration;
        this.endTime = getEndTime();
    }


    public Task(int taskId, String taskName, String taskDescription, Status taskStatus, LocalDateTime startTime,
                long duration) {
        this.id = taskId;
        this.name = taskName;
        this.description = taskDescription;
        this.status = taskStatus;
        this.startTime = startTime;
        this.duration = duration;
        this.endTime = getEndTime();
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

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }


    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getStartTime(){
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime){
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime(){
        if (this.startTime != null) {
            return this.startTime.plusMinutes(this.duration);
        } else {
            return null;
        }
    }

    public void setEndTime(LocalDateTime endTime){
        this.endTime = endTime;
    }

    public long getDuration(){
        return duration;
    }
    public void setDuration(long duration){
        this.duration = duration;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Task task = (Task) obj;
        return id == task.id && name.equals(task.name) && description.equals(task.description) && status == task.status
                && Objects.equals(startTime, task.startTime) && Objects.equals(endTime, task.endTime);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, name, description, status, startTime, duration, endTime);
    }

    @Override
    public String toString() {

        String taskToString = null;

        try {

            if (startTime != null) {
                taskToString = "Task{" +
                        "id=" + id  +
                        ", name=" + name +
                        ", description=" + description +
                        ", status=" + status  +
                        ", startTime=" + startTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))  +
                        ", duration=" + duration  +
                        ", endTime=" + endTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))  +
                        "}\n";

            } else {
                taskToString = "Task{" +
                        "id=" + id +
                        ", name=" + name +
                        ", description=" + description +
                        ", status=" + status +
                        ", startTime=" + null +
                        ", duration=" + duration +
                        ", endTime=" + null +
                        "}\n";
            }
        } catch (NullPointerException exc) {
            System.out.println("Ошибка ввода LocalDateTime: " + exc.getMessage());
        }
        return taskToString;
    }
}