import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private static final List<Task> history = new ArrayList<>(10);



    @Override
    public void add(Task task) {
     if(history.size() == 10){
         history.removeFirst();
     }
        history.add(task);
     }


    @Override
    public List<Task> getHistory() {
        return  history;
    }
}
