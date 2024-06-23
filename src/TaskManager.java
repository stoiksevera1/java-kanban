import java.util.HashMap;
import java.util.ArrayList;

public class TaskManager {
    private int nextId = 1;
    HashMap<Integer, Task> tasks = new HashMap<>();
    HashMap<Integer, Epic> epics = new HashMap<>();
    HashMap<Integer, Subtask> subTasks = new HashMap<>();


    public void addTask(Task task) {
        task.setId(nextId);
        nextId++;
        tasks.put(task.getId(), task);
    }

    public void addTask(Subtask subTask) {
        if (epics.containsKey(subTask.getEpicId())) {
            subTask.setId(nextId);
            nextId++;
            subTasks.put(subTask.getId(), subTask);
            epics.get(subTask.getEpicId()).addSubId(subTask.getId());
            synStatusEpic(epics.get(subTask.getEpicId()));
        } else {
            System.out.println("Эпика с таким ID не сущестыует подзадача не может быть добавлена.");
        }
    }

    private void synStatusEpic(Epic epic) {
        int counterNEW = 0;
        int counterDONE = 0;
        for (Integer id : epic.getSubIds()) {
            if (subTasks.get(id).getStatus() == Status.NEW) {
                counterNEW++;
            }
            if (subTasks.get(id).getStatus() == Status.DONE) {
                counterDONE++;
            }
        }
        if (counterNEW == epic.getSubIds().size()) {
            epic.setStatus(Status.NEW);
        } else if (counterDONE == epic.getSubIds().size()) {
            epic.setStatus(Status.DONE);
        } else if (!epic.getSubIds().isEmpty()) {
            epic.setStatus(Status.IN_PROGRESS);
        } else {
            epic.setStatus(Status.NEW);
        }
//

    }


    public void addTask(Epic epic) {
        epic.setId(nextId);
        nextId++;
        epics.put(epic.getId(), epic);
    }

    public void update(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        } else {
            System.out.println("Задачи с таким ID нет");
        }
    }


    public void update(Subtask subtask) {
        if (subTasks.containsKey(subtask.getId()) && (subTasks.get(subtask.getId()).getEpicId() == subtask.getEpicId())) {
            subTasks.put(subtask.getId(), subtask);
            synStatusEpic(epics.get(subtask.getEpicId()));
        }
    }

    public void update(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            epics.get(epic.getId()).setName(epic.getName());
            epics.get(epic.getId()).setDescription(epic.getDescription());
        }
    }

    public ArrayList<Task> getListTasks() {
        return new ArrayList<>(tasks.values());
    }


    public ArrayList<Epic> getListEpics() {
        return new ArrayList<Epic>(epics.values());

    }


    public ArrayList<Subtask> getListSubTasks() {
        return new ArrayList<Subtask>(subTasks.values());

        }


    public ArrayList<Subtask> getListEpic(int id) {
        if(epics.containsKey(id)) {
            ArrayList<Subtask> subtasks = new ArrayList<>();
            for (Integer subId : epics.get(id).getSubIds()) {
                subtasks.add(subtasks.get(subId));
            }
            return subtasks;
        }

        return null;
    }


    public void delListSubTasks() {
        subTasks.clear();
        for (Epic epic : epics.values()) {
            epic.delAllSubId();
            synStatusEpic(epic);
        }
    }

    public void delListEpics() {
        epics.clear();
        subTasks.clear();
    }

    public void delListTasks() {
        tasks.clear();
    }

    public Task getTaskById(Integer id) {
        if (tasks.containsKey(id)) {
            return tasks.get(id);
        } else {
            System.out.println("Такой ID не найден!");
            return null;
        }
    }

    public Epic getEpicById(Integer id) {
        if (epics.containsKey(id)) {
            return epics.get(id);
        } else {
            System.out.println("Такой ID не найден!");
            return null;
        }
    }

    public Subtask getSubTaskById(Integer id) {
        if (subTasks.containsKey(id)) {
            return subTasks.get(id);
        } else {
            System.out.println("Такой ID не найден!");
            return null;
        }
    }

    public void delTaskById(Integer id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        } else {
            System.out.println("Такой ID не найден!");
        }
    }

    public void delEpicById(Integer id) {
        if (epics.containsKey(id)) {
            for (Integer subId : epics.get(id).getSubIds()) {
                subTasks.remove(subId);
            }
            epics.remove(id);
        } else {
            System.out.println("Такой ID не найден!");
        }
    }

    public void delSubTaskById(Integer id) {
        if (subTasks.containsKey(id)) {
            epics.get(subTasks.get(id).getEpicId()).delSubId(id);
            subTasks.remove(id);
        } else {
            System.out.println("Такой ID не найден!");
        }
    }
}
