import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subIds = new ArrayList<>();


    public Epic(String name, String description) {
        super(name, description, Status.NEW);


    }

    public void delSubId(int id){
        subIds.remove(id);
    }

    public void delAllSubId(){
        subIds.clear();
    }

    public void addSubId(int id) {
        subIds.add(id);
    }

    public ArrayList<Integer> getSubIds() {
        return new ArrayList<>(subIds);
    }
}
