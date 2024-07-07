import java.util.HashMap;
import java.util.ArrayList;


public class InMemoryTaskManager implements TaskManager {
    private int nextId = 1;
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, Subtask> subTasks = new HashMap<>();

    HistoryManager history = Managers.getDefaultHistory();

    @Override
    public void addTask(Task task) {
        task.setId(nextId);
        nextId++;
        tasks.put(task.getId(), task);
    }

    @Override
    public void addTask(Subtask subTask) {
        if (epics.containsKey(subTask.getEpicId())) {
            subTask.setId(nextId);
            nextId++;
            subTasks.put(subTask.getId(), subTask);
            epics.get(subTask.getEpicId()).addSubId(subTask);
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


    @Override
    public void addTask(Epic epic) {
        epic.setId(nextId);
        nextId++;
        epics.put(epic.getId(), epic);
    }

    @Override
    public void update(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        } else {
            System.out.println("Задачи с таким ID нет");
        }
    }


    @Override
    public void update(Subtask subtask) {
        if (subTasks.containsKey(subtask.getId())) {
            subTasks.put(subtask.getId(), subtask);
            synStatusEpic(epics.get(subtask.getEpicId()));
        }
    }

    @Override
    public void update(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            epics.get(epic.getId()).setName(epic.getName());
            epics.get(epic.getId()).setDescription(epic.getDescription());
        }
    }

    @Override
    public ArrayList<Task> getListTasks() {
        return new ArrayList<>(tasks.values());
    }


    @Override
    public ArrayList<Epic> getListEpics() {
        return new ArrayList<>(epics.values());

    }


    @Override
    public ArrayList<Subtask> getListSubTasks() {
        return new ArrayList<>(subTasks.values());

    }


    @Override
    public ArrayList<Subtask> getListEpic(int id) {
        if (epics.containsKey(id)) {
            ArrayList<Subtask> subtasks = new ArrayList<>();
            for (Integer subId : epics.get(id).getSubIds()) {
                subtasks.add(subTasks.get(subId));
            }
            return subtasks;
        }

        return null;
    }


    @Override
    public void delListSubTasks() {
        subTasks.clear();
        for (Epic epic : epics.values()) {
            epic.delAllSubId();
            synStatusEpic(epic);
        }
    }

    @Override
    public void delListEpics() {
        epics.clear();
        subTasks.clear();
    }

    @Override
    public void delListTasks() {
        tasks.clear();
    }

    @Override
    public Task getTask(Integer id) {
        if (tasks.containsKey(id)) {
        history.add(tasks.get(id));
            return tasks.get(id);
        } else {
            System.out.println("Такой ID не найден!");
            return null;
        }
    }

    @Override
    public Epic getEpic(Integer id) {
        if (epics.containsKey(id)) {
            history.add(epics.get(id));
            return epics.get(id);
        } else {
            System.out.println("Такой ID не найден!");
            return null;
        }
    }

    @Override
    public Subtask getSubTask(Integer id) {
        if (subTasks.containsKey(id)) {
            history.add(subTasks.get(id));
            return subTasks.get(id);
        } else {
            System.out.println("Такой ID не найден!");
            return null;
        }
    }

    @Override
    public void delTaskById(Integer id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        } else {
            System.out.println("Такой ID не найден!");
        }
    }

    @Override
    public void delEpicById(Integer id) {
        if (epics.containsKey(id)) {
            for (Integer subId : epics.get(id).getSubIds()) {
                subTasks.remove(subId);
              epics.get(id).delAllSubId();
            }
            epics.remove(id);
        } else {
            System.out.println("Такой ID не найден!");
        }
    }

    @Override
    public void delSubTaskById(Integer id) {
        if (subTasks.containsKey(id)) {
            epics.get(subTasks.get(id).getEpicId()).delSubId(id);
            subTasks.remove(id);

        } else {
            System.out.println("Такой ID не найден!");
        }
    }

}

