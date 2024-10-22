package task;


import com.google.gson.annotations.Expose;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {





     private ArrayList<Integer> subIds = new ArrayList<>();
       private LocalDateTime endTime;




    public Epic(String name, String description) {
        super(name,
                description,
                  Status.NEW);


    }


    public void delSubId(Integer id) {
        subIds.remove(id);
    }

    public void delAllSubId() {
        subIds.clear();
    }

    public void addSubId(Task subtask) {
        if (subtask instanceof Subtask) {
            subIds.add(subtask.getId());
        }

    }

    public ArrayList<Integer> getSubIds() {
        return new ArrayList<>(subIds);
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}
