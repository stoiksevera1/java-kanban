import java.util.ArrayList;

public class Epic extends Task {
   ArrayList<Integer> subIds = new ArrayList<>();


   public Epic(String name, String description) {
      super(name, description, Status.NEW);

   }
}
