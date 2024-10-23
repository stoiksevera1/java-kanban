package manager;

import task.Epic;
import task.Subtask;
import task.Task;


import java.util.List;
import java.util.Set;

public interface TaskManager {
    void addTask(Task task);

    void addTask(Subtask subTask);

    void addTask(Epic epic);

    void update(Task task);

    void update(Subtask subtask);

    void update(Epic epic);

    List<Task> getListTasks();

    List<Epic> getListEpics();

    List<Subtask> getListSubTasks();

    List<Subtask> getListEpic(int id);

    void delListSubTasks();

    void delListEpics();

    void delListTasks();

    Task getTask(Integer id);

    Epic getEpic(Integer id);

    Subtask getSubTask(Integer id);

    void delTaskById(Integer id);

    void delEpicById(Integer id);

    void delSubTaskById(Integer id);

    List<Task> getHistory();

    public void removeHistoryById(Integer id);

    public Set<Task> getPrioritizedTasks();

    public int getNextId();
}
