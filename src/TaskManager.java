import java.util.HashMap;


public class TaskManager {
    int nextId = 1;
    HashMap<Integer, Task> tasks = new HashMap<>();
    HashMap<Integer, Epic> epics = new HashMap<>();
    HashMap<Integer, Subtask> subTasks = new HashMap<>();


    public void addTask(Task task) {
        task.setId(nextId);
        nextId++;
        tasks.put(task.getId(), task);
    }

    public void addTask(Integer idEpic, Subtask subTask) {
        subTask.setId(nextId);
        nextId++;
        subTasks.put(subTask.getId(), subTask);
        subTask.setEpicId(idEpic);
        epics.get(idEpic).subIds.add(subTask.getId());
        synStatusEpic(epics.get(idEpic));
    }

    private void synStatusEpic(Epic epic) {

        for (Integer x : epic.subIds) {
            if (subTasks.get(x).getStatus() == Status.IN_PROGRESS) {
                epic.setStatus(Status.IN_PROGRESS);
                break;
            } else if (subTasks.get(x).getStatus() == Status.DONE) {
                for (Integer y : epic.subIds) {
                    if (subTasks.get(y).getStatus() != Status.IN_PROGRESS && subTasks.get(y).getStatus() != Status.NEW) {
                        epic.setStatus(Status.DONE);
                    } else {
                        epic.setStatus(Status.IN_PROGRESS);
                        break;
                    }
                }
            }
        }

    }


    public void addTask(Epic epic) {
        epic.setId(nextId);
        nextId++;
        epics.put(epic.getId(), epic);
    }

    public void update(Task task) {
        tasks.put(task.getId(), task);
    }

    public void update(int idEpic, Subtask subtask) {
        subtask.setEpicId(idEpic);
        subTasks.put(subtask.getId(), subtask);
        synStatusEpic(epics.get(subtask.getEpicId()));
    }

    public void update(Epic epic) {
        epics.put(epic.getId(), epic);
        synStatusEpic(epic);
    }

    public void getListTasks() {
        System.out.println("Список всех задач: ");
        for (Task task : tasks.values()) {
            System.out.println(task);
        }
    }

    public void getListEpics() {
        System.out.println("Список всех эпиков: ");
        for (Task epic : epics.values()) {
            System.out.println(epic);

        }
    }

    public void getListSubTasks() {
        System.out.println("Список всех подзадач: ");
        for (Task subtask : subTasks.values()) {
            System.out.println(subtask);

        }
    }

    public void getListEpic(Epic epic) {
        System.out.println("Список подзадач эпика " + epic.getName() + ": ");
        for (Integer subId : epic.subIds) {
            if (subTasks.get(subId) != null) {
                System.out.println(subTasks.get(subId));
            } else {
                break;
            }
        }
    }


    public void delListSubTasks() {
        subTasks.clear();
    }

    public void delListEpics() {
        epics.clear();
    }

    public void delListTasks() {
        tasks.clear();
    }
    public void getTaskById(Integer id) {
        if(tasks.containsKey(id)) {
            System.out.println("Задача найдена :");
            System.out.println(tasks.get(id));
        } else {
            System.out.println("Такой ID не найден!");
        }
    }

    public void getEpicById(Integer id) {
        if(epics.containsKey(id)) {
            System.out.println("Эпик найден: ");
            System.out.println(epics.get(id));
        } else {
            System.out.println("Такой ID не найден!");
        }
    }

    public void getSubTaskById(Integer id) {
        if(subTasks.containsKey(id)) {
            System.out.println("Подзадача найдена :");
            System.out.println(subTasks.get(id));
        } else {
            System.out.println("Такой ID не найден!");
        }
    }

    public void delTaskById(Integer id) {
        if(tasks.containsKey(id)) {
            System.out.println(tasks.remove(id));
        } else {
            System.out.println("Такой ID не найден!");
        }
    }

    public void delEpicById(Integer id) {
        if(epics.containsKey(id)) {
            System.out.println(epics.remove(id));
        } else {
            System.out.println("Такой ID не найден!");
        }
    }

    public void delSubTaskById(Integer id) {
        if(subTasks.containsKey(id)) {
            System.out.println(subTasks.remove(id));
        } else {
            System.out.println("Такой ID не найден!");
        }
    }
}
