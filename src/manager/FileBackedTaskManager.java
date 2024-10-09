package manager;

import task.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;


public class FileBackedTaskManager extends InMemoryTaskManager {
    static File file;

    static FileBackedTaskManager loadFromFile(File receivedFile) {

        file = receivedFile;
        FileBackedTaskManager loadManager = new FileBackedTaskManager();
        try {
            String[] split = (Files.readString(file.toPath(), StandardCharsets.UTF_8).split("\n"));

            loadManager.taskSortStartTime = Arrays.stream(split).skip(1)
                    .map(loadManager::fromString)
                    .peek(task -> {
                                if (task instanceof Epic) {
                                    loadManager.epics.put(task.getId(), (Epic) task);
                                } else if (task instanceof Subtask) {
                                    loadManager.subTasks.put(task.getId(), (Subtask) task);
                                } else {
                                    loadManager.tasks.put(task.getId(), task);
                                }
                            }
                    )
                    .filter(task -> !(task instanceof Epic))
                    .filter(task -> task.getStartTime() != null)
                    .sorted(Comparator.comparing(Task::getStartTime))
                    .collect(Collectors.toCollection(LinkedHashSet::new));
            if (split.length > 0) loadManager.nextId = split.length;

            return loadManager;
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка чтения файла");
        }

    }

    @Override
    public void addTask(Subtask subTask) {
        super.addTask(subTask);
        save();
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void addTask(Epic epic) {
        super.addTask(epic);
        save();
    }

    @Override
    public void delEpicById(Integer id) {
        super.delEpicById(id);
        save();
    }

    @Override
    public void update(Task task) {
        super.update(task);
        save();
    }

    @Override
    public void update(Subtask subtask) {
        super.update(subtask);
        save();
    }

    @Override
    public void update(Epic epic) {
        super.update(epic);
        save();
    }

    @Override
    public void delTaskById(Integer id) {
        super.delTaskById(id);
        save();
    }

    @Override
    public void delSubTaskById(Integer id) {
        super.delSubTaskById(id);
        save();
    }

    @Override
    public void delListTasks() {
        super.delListTasks();
        save();
    }

    @Override
    public void delListSubTasks() {
        super.delListSubTasks();
        save();
    }

    @Override
    public void delListEpics() {
        super.delListEpics();
        save();
    }

    private void save() {
        try {
            Writer fileWriter = new FileWriter(file);
            fileWriter.write("id,type,name,status,description,epic,duration,starTime\n");
            for (Task task : tasks.values()) {
                fileWriter.write(toString(task) + "\n");
            }
            for (Task epic : epics.values()) {
                fileWriter.write(toString(epic) + "\n");
            }
            for (Task subTask : subTasks.values()) {
                fileWriter.write(toString(subTask) + "\n");
            }

            fileWriter.close();
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка записи файла");
        }
    }


    private String toString(Task task) {
        Tasks i = Tasks.TASK;
        if (task instanceof Epic) {
            i = Tasks.EPIC;
            if (task.getStartTime() == null) {
                return ("%d,%s,%s,%s,%s,".formatted(task.getId(), i, task.getName(), task.getStatus(),
                        task.getDescription()));
            } else {
                return ("%d,%s,%s,%s,%s,%s,%s".formatted(task.getId(), i, task.getName(), task.getStatus(),
                        task.getDescription(), task.getDuration().toMinutes(), task.getStartTime().toString()));
            }
        } else if (task instanceof Subtask) {
            i = Tasks.SUBTASK;
            if (task.getStartTime() == null) {
                return ("%d,%s,%s,%s,%s,%d".formatted(task.getId(), i, task.getName(), task.getStatus(),
                        task.getDescription(), ((Subtask) task).getEpicId()));
            } else {
                return ("%d,%s,%s,%s,%s,%d,%s,%s".formatted(task.getId(), i, task.getName(), task.getStatus(),
                        task.getDescription(), ((Subtask) task).getEpicId(), task.getDuration().toMinutes(),
                        task.getStartTime().toString()));
            }
        } else {
            if (task.getStartTime() == null) {
                return ("%d,%s,%s,%s,%s".formatted(task.getId(), i, task.getName(), task.getStatus(),
                        task.getDescription()));
            } else {
                return ("%d,%s,%s,%s,%s,%s,%s".formatted(task.getId(), i, task.getName(), task.getStatus(),
                        task.getDescription(), task.getDuration().toMinutes(), task.getStartTime().toString()));
            }
        }
    }

    private Task fromString(String value) {
        String[] split = value.split(",");

        if (split[1].equals(Tasks.TASK.name())) {
            if (split.length <= 5) {
                Task taskFromString = new Task(split[2], split[4], Status.valueOf(split[3]));
                taskFromString.setId(parseInt(split[0]));
                return taskFromString;
            } else {
                Task taskFromString = new Task(split[2], split[4], Status.valueOf(split[3]),
                        Duration.ofMinutes(Long.parseLong(split[5])), LocalDateTime.parse(split[6]));
                taskFromString.setId(parseInt(split[0]));
                return taskFromString;
            }


        } else if (split[1].equals(Tasks.EPIC.name())) {
            Epic taskFromString = new Epic(split[2], split[4]);
            taskFromString.setStatus(Status.valueOf(split[3]));
            taskFromString.setId(parseInt(split[0]));
            if (split.length > 5) {
                taskFromString.setStartTime(LocalDateTime.parse(split[6]));
                taskFromString.setDuration(Duration.ofMinutes(Long.parseLong(split[5])));
                taskFromString.setEndTime(taskFromString.getStartTime().plus(taskFromString.getDuration()));
            }
            return taskFromString;
        } else {
            if (split.length <= 6) {
                Task taskFromString = new Subtask(split[2], split[4], Status.valueOf(split[3]), parseInt(split[5]));
                taskFromString.setId(parseInt(split[0]));
                return taskFromString;
            } else {
                Subtask taskFromString = new Subtask(split[2], split[4], Status.valueOf(split[3]), parseInt(split[5]),
                        Duration.ofMinutes(Long.parseLong(split[6])), LocalDateTime.parse(split[7]));
                taskFromString.setId(parseInt(split[0]));
                return taskFromString;
            }
        }
    }
}
