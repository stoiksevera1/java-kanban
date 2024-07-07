import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    void addTask(Task task);

    void addTask(Subtask subTask);

    void addTask(Epic epic);

    void update(Task task);

    void update(Subtask subtask);

    void update(Epic epic);

    ArrayList<Task> getListTasks();

    ArrayList<Epic> getListEpics();

    ArrayList<Subtask> getListSubTasks();

    ArrayList<Subtask> getListEpic(int id);

    void delListSubTasks();

    void delListEpics();

    void delListTasks();

    Task getTask(Integer id);

    Epic getEpic(Integer id);

    Subtask getSubTask(Integer id);

    void delTaskById(Integer id);

    void delEpicById(Integer id);

    void delSubTaskById(Integer id);


}
