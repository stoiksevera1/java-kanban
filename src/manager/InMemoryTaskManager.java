package manager;

import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;


public class InMemoryTaskManager implements TaskManager {
    protected int nextId = 1;
    protected Map<Integer, Task> tasks = new HashMap<>();
    protected Map<Integer, Epic> epics = new HashMap<>();
    protected Map<Integer, Subtask> subTasks = new HashMap<>();

    public HistoryManager history = new InMemoryHistoryManager();
    protected Set<Task> taskSortStartTime = new TreeSet<>(Comparator.comparing(Task::getStartTime));

    @Override
    public void addTask(Task task) {
        task.setId(nextId);
        nextId++;
        if (taskSortStartTime.stream()
                .noneMatch(task1 -> intersectionOfTasks(task1, task))
        ) {
            tasks.put(task.getId(), task);
            if (task.getStartTime() != null) {
                taskSortStartTime.add(task);
            }
        } else {
            System.out.println("Задача не может быть добавлена. Время выполнения пересекается с ранее созданой задачей");
        }
    }

    @Override
    public void addTask(Subtask subTask) {
        if (epics.containsKey(subTask.getEpicId())) {
            subTask.setId(nextId);
            nextId++;
            if (taskSortStartTime.stream()
                    .noneMatch(task1 -> intersectionOfTasks(task1, subTask))
            ) {
                subTasks.put(subTask.getId(), subTask);
                if (subTask.getStartTime() != null) {
                    taskSortStartTime.add(subTask);
                }
                epics.get(subTask.getEpicId()).addSubId(subTask);
                synStatusEpic(epics.get(subTask.getEpicId()));
                synTimeEpic(epics.get(subTask.getEpicId()));
            } else {
                System.out.println("Задача не может быть добавлена. Время выполнения пересекается с ранее созданой задачей");
            }

        } else {
            System.out.println("Эпика с таким ID не существует подзадача не может быть добавлена.");
        }

    }

    private void synTimeEpic(Epic epic) {
        if (epic.getSubIds().size() < 2) {
            epic.setStartTime(subTasks.get(epic.getSubIds().getFirst()).getStartTime());
            epic.setDuration(subTasks.get(epic.getSubIds().getFirst()).getDuration());
        } else {
            epic.getSubIds().stream()
                    .map(subId -> subTasks.get(subId))
                    .min(Comparator.comparing(Subtask::getStartTime))
                    .ifPresent(subtask -> epic.setStartTime(subtask.getStartTime()));

            epic.getSubIds().stream()
                    .map(subId -> subTasks.get(subId))
                    .max(Comparator.comparing(Subtask::getStartTime))
                    .ifPresent(subtask -> epic.setEndTime(subtask.getStartTime().plus(subtask.getDuration())));

            epic.setDuration(Duration.ofMinutes(epic.getSubIds().stream()
                    .map(subId -> subTasks.get(subId))
                    .map(Task::getDuration)
                    .mapToInt(Duration::toMinutesPart)
                    .sum()
            ));

        }
    }

    private boolean intersectionOfTasks(Task task1, Task task2) {
        if (task1.getStartTime().equals(task2.getStartTime())) {
            return true;
        } else
            return (!(task1.getStartTime().isBefore(task2.getStartTime()) & task1.getEndTime().isBefore(task2.getStartTime())))
                    && (!task1.getStartTime().isAfter(task2.getEndTime()));
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
            taskSortStartTime.remove(task);
            if (taskSortStartTime.stream()
                    .noneMatch(task1 -> intersectionOfTasks(task1, task))
            ) {
                tasks.put(task.getId(), task);
                if (task.getStartTime() != null) {
                    taskSortStartTime.add(task);
                }
            } else {
                System.out.println("Задача не может быть добавлена. Время выполнения пересекается с ранее созданой задачей");
            }
        } else {
            System.out.println("Задачи с таким ID нет");
        }
    }


    @Override
    public void update(Subtask subtask) {
        if (subTasks.containsKey(subtask.getId())) {
            taskSortStartTime.remove(subtask);
            if (taskSortStartTime.stream()
                    .noneMatch(task1 -> intersectionOfTasks(task1, subtask))
            ) {
                subTasks.put(subtask.getId(), subtask);
                if (subtask.getStartTime() != null) {
                    taskSortStartTime.add(subtask);
                }
                synStatusEpic(epics.get(subtask.getEpicId()));
                synTimeEpic(epics.get(subtask.getEpicId()));
            } else {
                System.out.println("Задача не может быть добавлена. Время выполнения пересекается с ранее созданой задачей");
            }
        } else {
            System.out.println("Задачи с таким ID нет");
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
            return epics.get(id).getSubIds().stream()
                    .map(subId -> subTasks.get(subId))
                    .collect(Collectors.toCollection(ArrayList::new));
        }
        return null;
    }


    @Override
    public void delListSubTasks() {
        for (Task subTask : subTasks.values()) {
            taskSortStartTime.remove(subTask);
            history.remove(subTask.getId());
        }
        subTasks.clear();
        for (Epic epic : epics.values()) {
            epic.delAllSubId();
            synStatusEpic(epic);

        }
    }

    @Override
    public void delListEpics() {
        for (Task epic : epics.values()) {
            history.remove(epic.getId());
        }
        for (Task subTask : subTasks.values()) {
            taskSortStartTime.remove(subTask);
            history.remove(subTask.getId());
        }
        epics.clear();
        subTasks.clear();
    }

    @Override
    public void delListTasks() {
        for (Task task : tasks.values()) {
            taskSortStartTime.remove(task);
            history.remove(task.getId());
        }
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
            taskSortStartTime.remove(tasks.get(id));
            tasks.remove(id);
            history.remove(id);

        } else {
            System.out.println("Такой ID не найден!");
        }
    }

    @Override
    public void delEpicById(Integer id) {
        if (epics.containsKey(id)) {
            for (Integer subId : epics.get(id).getSubIds()) {
                history.remove(subId);
                taskSortStartTime.remove(subTasks.get(subId));
                subTasks.remove(subId);
            }
            history.remove(id);
            epics.remove(id);
        } else {
            System.out.println("Такой ID не найден!");
        }
    }

    @Override
    public void delSubTaskById(Integer id) {
        if (subTasks.containsKey(id)) {
            epics.get(subTasks.get(id).getEpicId()).delSubId(id);
            taskSortStartTime.remove(subTasks.get(id));
            subTasks.remove(id);
            history.remove(id);

        } else {
            System.out.println("Такой ID не найден!");
        }
    }

    public List<Task> getHistory() {
        return history.getHistory();
    }

    public void removeHistoryById(Integer id) {
        history.remove(id);
    }

    public Set<Task> getPrioritizedTasks() {
        return taskSortStartTime;
    }


}

