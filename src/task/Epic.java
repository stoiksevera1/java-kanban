package task;

import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    private ArrayList<Integer> subIds = new ArrayList<>();


    public Epic(String name, String description) {
        super(name, description, Status.NEW);


    }

    public void delSubId(Integer id){
        subIds.remove(id);
    }

    public void delAllSubId(){
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
}
