package manager;

import task.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;


public class FileBackedTaskManager extends InMemoryTaskManager {

    static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager loadManager = new FileBackedTaskManager();
        try {
            String[] split = (Files.readString(file.toPath(), StandardCharsets.UTF_8).split("\n"));
            for (int i = 1; i < split.length; i++) {
String[] stringTask = split[i].split(",");
    if (stringTask[1].equals(Tasks.EPIC.name())) {
        loadManager.epics.put(Integer.parseInt(stringTask[0]), (Epic) loadManager.fromString(split[i]));
        loadManager.nextId++;
        loadManager.epics.get(Integer.parseInt(stringTask[0])).setId(Integer.parseInt(stringTask[0]));
    } else if (stringTask[1].equals(Tasks.SUBTASK.name())) {
        loadManager.subTasks.put(Integer.parseInt(stringTask[0]), (Subtask) loadManager.fromString(split[i]));
        loadManager.nextId++;
        loadManager.subTasks.get(Integer.parseInt(stringTask[0])).setId(Integer.parseInt(stringTask[0]));
    } else {
        loadManager.tasks.put(Integer.parseInt(stringTask[0]), loadManager.fromString(split[i]));
        loadManager.nextId++;
        loadManager.tasks.get(Integer.parseInt(stringTask[0])).setId(Integer.parseInt(stringTask[0]));
    }



            }
            return loadManager;
        } catch (IOException e) {
            throw new RuntimeException(e);
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

    private void save()  {
        try {
            try {
                Writer fileWriter = new FileWriter("text.csv");
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
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
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

       Task fromString(String value) {
        String[] split = value.split(",");
        if (split[1].equals(Tasks.TASK.name())) {
            return new Task(split[2], split[4], Status.valueOf(split[3]));
        } else if (split[1].equals(Tasks.EPIC.name())) {
            return new Epic(split[2], split[4]);
        } else {
            return new Subtask(split[2], split[4], Status.valueOf(split[3]), Integer.parseInt(split[5]));

        }
    }
}
