package task;

import java.time.Duration;
import java.time.LocalDateTime;

public class Task {
    private String name;
    private String description;
    private int id;
    private Status status;
    private Duration duration;
    private LocalDateTime startTime;

    public Task(String name, String description, Status status, Duration duration, LocalDateTime startTime) {
        setDescription(description);
        setName(name);
        setStatus(status);
        setDuration(duration);
        setStartTime(startTime);
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDescription(String description) {

        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {

        this.id = id;
    }

    public void setStatus(Status status) {

        this.status = status;
    }

    public int getId() {

        return id;
    }


    public Status getStatus() {

        return status;
    }

    @Override
    public String toString() {
        return "task.Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                '}';
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return startTime.plus(duration);
    }
}
