package manager;

import task.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;


public class FileBackedTaskManager extends InMemoryTaskManager {
    static File file;

    static FileBackedTaskManager loadFromFile(File receivedFile) {

        file = receivedFile;
        FileBackedTaskManager loadManager = new FileBackedTaskManager();
        try {
            String[] split = (Files.readString(file.toPath(), StandardCharsets.UTF_8).split("\n"));
            int idIn = 0;
            for (int i = 1; i < split.length; i++) {
                String[] stringTask = split[i].split(",");
                if ((Integer.parseInt(stringTask[0])) > idIn) idIn = Integer.parseInt(stringTask[0]);
                if (stringTask[1].equals(Tasks.EPIC.name())) {
                    loadManager.epics.put(Integer.parseInt(stringTask[0]), (Epic) loadManager.fromString(split[i]));
                } else if (stringTask[1].equals(Tasks.SUBTASK.name())) {
                    loadManager.subTasks.put(Integer.parseInt(stringTask[0]), (Subtask) loadManager.fromString(split[i]));
                } else {
                    loadManager.tasks.put(Integer.parseInt(stringTask[0]), loadManager.fromString(split[i]));
                }
            }
            for (Subtask subTask : loadManager.subTasks.values()) {
                loadManager.epics.get(subTask.getEpicId()).addSubId(subTask);
            }
            loadManager.nextId = idIn + 1;
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
            fileWriter.write("id,type,name,status,description,epic\n");
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
            return ("%d,%s,%s,%s,%s".formatted(task.getId(), i, task.getName(), task.getStatus(), task.getDescription()));
        } else if (task instanceof Subtask) {
            i = Tasks.SUBTASK;
            return ("%d,%s,%s,%s,%s,%d".formatted(task.getId(), i, task.getName(), task.getStatus(), task.getDescription(), ((Subtask) task).getEpicId()));
        } else {
            return ("%d,%s,%s,%s,%s".formatted(task.getId(), i, task.getName(), task.getStatus(), task.getDescription()));
        }
    }

    private Task fromString(String value) {
        String[] split = value.split(",");
        Task taskFromString;
        if (split[1].equals(Tasks.TASK.name())) {
            taskFromString = new Task(split[2], split[4], Status.valueOf(split[3]));

        } else if (split[1].equals(Tasks.EPIC.name())) {
            taskFromString = new Epic(split[2], split[4]);
        } else {
            taskFromString = new Subtask(split[2], split[4], Status.valueOf(split[3]), Integer.parseInt(split[5]));
        }
        taskFromString.setId(Integer.parseInt(split[0]));
        return taskFromString;
    }
}
