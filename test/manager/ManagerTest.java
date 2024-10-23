package manager;

import org.junit.jupiter.api.AfterEach;

import org.junit.jupiter.api.Test;
import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

import java.io.File;

import java.time.Duration;
import java.time.LocalDateTime;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

abstract class TaskManagerTest {
    File file1;

    TaskManager taskManager1 = Managers.getDefault();

    @AfterEach
    void delALL() {
        taskManager1.delListTasks();
        taskManager1.delListSubTasks();
        taskManager1.delListEpics();
    }

    @Test
    void epicStatusTest() {
        Epic epic = new Epic("Test addNewTask", "Test addNewTask description");
        taskManager1.addTask(epic);

        Subtask task1 = new Subtask("Test addNewTask", "Test addNewTask description", Status.NEW, 1, Duration.ofMinutes(25), LocalDateTime.of(2024, 11, 20, 12, 50));
        taskManager1.addTask(task1);

        Subtask task2 = new Subtask("Test addNewTask", "Test addNewTask description", Status.NEW, 1, Duration.ofMinutes(25), LocalDateTime.of(2024, 11, 26, 12, 50));
        taskManager1.addTask(task2);
        assertEquals(Status.NEW, epic.getStatus());
        task1.setStatus(Status.DONE);
        task2.setStatus(Status.DONE);
        taskManager1.update(task1);
        taskManager1.update(task2);
        assertEquals(Status.DONE, epic.getStatus());
        task1.setStatus(Status.NEW);
        task2.setStatus(Status.DONE);
        taskManager1.update(task1);
        taskManager1.update(task2);
        assertEquals(Status.IN_PROGRESS, epic.getStatus());
        task1.setStatus(Status.IN_PROGRESS);
        task2.setStatus(Status.IN_PROGRESS);
        taskManager1.update(task1);
        taskManager1.update(task2);
        assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }

    @Test
    void addNewSubTaskAndEpicStatusTest() {
        Epic epic = new Epic("Test addNewTask", "Test addNewTask description");
        taskManager1.addTask(epic);
        final int epicId = epic.getId();
        Subtask task = new Subtask("Test addNewTask", "Test addNewTask description", Status.DONE, epicId, Duration.ofMinutes(25), LocalDateTime.of(2024, 11, 20, 12, 50));
        taskManager1.addTask(task);
        final int taskId = task.getId();

        final Task savedTask = taskManager1.getSubTask(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Subtask> tasks = taskManager1.getListSubTasks();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.getFirst(), "Задачи не совпадают.");
        assertNotNull(taskManager1.getEpic(taskManager1.getSubTask(2).getEpicId()), "Эпик не найден.");
        assertEquals(Status.DONE, epic.getStatus());
    }

    @Test
    void crossTimeTask() throws ManagerSaveException{

        Epic epic1 = new Epic("Сон", "Спать");
        Epic epic2 = new Epic("Еда", "Есть");
        Task task1 = new Task("Бег", "пробежать как можно дольше", Status.DONE, Duration.ofMinutes(10), LocalDateTime.of(2024, 11, 20, 12, 50));
        Task task2 = new Task("Приседания", "приседать как можно больше", Status.NEW, Duration.ofMinutes(10), LocalDateTime.of(2024, 11, 20, 12, 55));
        Subtask subTask1 = new Subtask("долго спать", "проспать как можно дольше", Status.DONE, 1, Duration.ofMinutes(15), LocalDateTime.of(2024, 11, 20, 12, 45));
        Subtask subTask2 = new Subtask("очень долго спать", "спать в два раза дольше чем в первый раз", Status.DONE, 1, Duration.ofMinutes(50), LocalDateTime.of(2024, 11, 20, 12, 50));
        Subtask subtask3 = new Subtask("Много есть", "получить ачивку обжорство", Status.IN_PROGRESS, 2, Duration.ofMinutes(10), LocalDateTime.of(2024, 11, 20, 11, 50));
        Subtask subtask4 = new Subtask("Очень Много есть", "получить ачивку обжорство", Status.IN_PROGRESS, 2, Duration.ofMinutes(10), LocalDateTime.of(2024, 11, 20, 13, 50));
        Throwable thrown = assertThrows(ManagerSaveException.class, () -> {
        taskManager1.addTask(epic1);
        taskManager1.addTask(epic2);
        taskManager1.addTask(task1);
        taskManager1.addTask(task2);
        taskManager1.addTask(subTask1);
        taskManager1.addTask(subTask2);
        taskManager1.addTask(subtask3);
        taskManager1.addTask(subtask4);
        assertEquals(0, epic1.getSubIds().size());


        });
        assertNotNull(thrown.getMessage());
    }

    @Test
    void testHistory() throws NullPointerException {
        Epic epic1 = new Epic("Сон", "Спать");
        Epic epic2 = new Epic("Еда", "Есть");
        Task task1 = new Task("Бег", "пробежать как можно дольше", Status.DONE, Duration.ofMinutes(25), LocalDateTime.of(2024, 11, 1, 12, 50));
        Task task2 = new Task("Приседания", "приседать как можно больше", Status.NEW, Duration.ofMinutes(25), LocalDateTime.of(2024, 11, 2, 12, 50));
        Subtask subTask1 = new Subtask("долго спать", "проспать как можно дольше", Status.NEW, 1, Duration.ofMinutes(25), LocalDateTime.of(2024, 11, 3, 12, 50));
        Subtask subTask2 = new Subtask("очень долго спать", "спать в два раза дольше чем в первый раз", Status.DONE, 1, Duration.ofMinutes(25), LocalDateTime.of(2024, 11, 4, 12, 50));
        Subtask subtask3 = new Subtask("Много есть", "получить ачивку обжорство", Status.IN_PROGRESS, 2, Duration.ofMinutes(25), LocalDateTime.of(2024, 11, 5, 12, 50));

        taskManager1.addTask(epic1);
        taskManager1.addTask(epic2);
        taskManager1.addTask(task1);
        taskManager1.addTask(task2);

        taskManager1.addTask(subTask1);
        taskManager1.addTask(subTask2);
        taskManager1.addTask(subtask3);

        taskManager1.getEpic(2);
        taskManager1.getEpic(2);
        taskManager1.getEpic(1);

        taskManager1.getTask(4);
        taskManager1.getTask(3);
        taskManager1.getTask(4);
        taskManager1.getTask(3);
        taskManager1.getTask(4);
        taskManager1.getSubTask(5);
        taskManager1.getSubTask(6);
        taskManager1.getTask(4);
        taskManager1.getEpic(1);
        taskManager1.getEpic(2);
        taskManager1.getEpic(1);

        taskManager1.getTask(4);
        taskManager1.getTask(3);
        taskManager1.getTask(4);
        taskManager1.getTask(3);
        taskManager1.getTask(4);
        taskManager1.getSubTask(5);
        taskManager1.getSubTask(6);
        taskManager1.getTask(4);
        taskManager1.getEpic(1);

        Subtask subtask4 = new Subtask("мало  есть", "похудеть", Status.IN_PROGRESS, 1, Duration.ofMinutes(25), LocalDateTime.of(2024, 11, 17, 12, 50));
        subtask4.setId(6);
        taskManager1.update(subtask4);


        List<Task> history1 = taskManager1.getHistory();

        Task taskSave = history1.get(4);

        assertNotEquals(subTask2, taskManager1.getSubTask(6));
        assertEquals(task2, taskSave);
        assertEquals(6, taskManager1.getHistory().size());
        taskManager1.removeHistoryById(1);
        assertEquals(5, taskManager1.getHistory().size());
        taskManager1.removeHistoryById(3);
        assertEquals(4, taskManager1.getHistory().size());
        taskManager1.removeHistoryById(6);
        assertEquals(3, taskManager1.getHistory().size());

    }

    @Test
    public void testException() {
        file1 = new File("/filsa");
        assertThrows(ManagerSaveException.class, () -> {
            FileBackedTaskManager.loadFromFile(file1);
        }, "Ошибка чтения файла");
    }

    @Test
    void addNewTask() {
        Task task = new Task("Test addNewTask", "Test addNewTask description", Status.NEW, Duration.ofMinutes(25), LocalDateTime.of(2024, 11, 20, 12, 50));
        taskManager1.addTask(task);
        final int taskId = task.getId();

        final Task savedTask = taskManager1.getTask(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = taskManager1.getListTasks();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.getFirst(), "Задачи не совпадают.");
    }

    @Test
    void addNewEpic() {
        Epic task1 = new Epic("Test addNewTask", "Test addNewTask description");
        taskManager1.addTask(task1);
        final int taskId = task1.getId();

        final Task savedTask = taskManager1.getEpic(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task1, savedTask, "Задачи не совпадают.");

        final List<Epic> tasks = taskManager1.getListEpics();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task1, tasks.getFirst(), "Задачи не совпадают.");
    }


    @Test
    void addEpicInEpic() { //пришлось метод добавления самой подзадачи изменять  больше никакого теста вголову не пришло
        Epic task1 = new Epic("Test addNewTask", "Test addNewTask description");
        taskManager1.addTask(task1);

        Epic task2 = new Epic("Test addNewTask", "Test addNewTask description");
        taskManager1.addTask(task2);

        task1.addSubId(task2);
        assertEquals(0, task1.getSubIds().size());

        task1.addSubId(task1);
        assertEquals(0, task1.getSubIds().size());
    }



    @Test
    void initialUtilClass() {
        InMemoryTaskManager taskManager5 = (InMemoryTaskManager) Managers.getDefault();
        TaskManager taskManager3 = Managers.getDefault();

    }

    @Test
    void addAllTask() { //пришлось метод добавления самой подзадачи изменять  больше никакого теста вголову не пришло
        Task task = new Task("Test addNewTask", "Test addNewTask description", Status.NEW, Duration.ofMinutes(25), LocalDateTime.of(2024, 11, 20, 12, 50));
        taskManager1.addTask(task);

        Epic epic = new Epic("Test addNewTask", "Test addNewTask description");
        taskManager1.addTask(epic);

        Subtask subtask = new Subtask("Test addNewTask", "Test addNewTask description", Status.NEW, 2, Duration.ofMinutes(25), LocalDateTime.of(2024, 11, 19, 12, 50));
        taskManager1.addTask(subtask);

        Task task1 = taskManager1.getTask(1);
        Task epic1 = taskManager1.getEpic(2);
        Task subtask2 = taskManager1.getSubTask(3);
        assertEquals(task, task1);
        assertEquals(epic, epic1);
        assertEquals(subtask, subtask2);
    }


    @Test
    void standFieldsTask() {
        Task task = new Task("Test addNewTask", "Test addNewTask description", Status.NEW, Duration.ofMinutes(25), LocalDateTime.of(2024, 11, 19, 12, 50));
        String name1 = task.getName();
        String description1 = task.getDescription();
        int id1 = task.getId();
        Status status1 = task.getStatus();
        taskManager1.addTask(task);
        assertEquals(name1, taskManager1.getTask(1).getName());
        assertEquals(description1, taskManager1.getTask(1).getDescription());
        assertEquals(status1, taskManager1.getTask(1).getStatus());
        assertNotEquals(id1, task.getId());
    }


    @Test
    void testDellSubTask() throws NullPointerException {
        Epic epic1 = new Epic("Сон", "Спать");
        Epic epic2 = new Epic("Еда", "Есть");
        Subtask subTask1 = new Subtask("долго спать", "проспать как можно дольше", Status.NEW, 1, Duration.ofMinutes(25), LocalDateTime.of(2024, 11, 21, 12, 50));
        Subtask subTask2 = new Subtask("очень долго спать", "спать в два раза дольше чем в первый раз", Status.DONE, 1, Duration.ofMinutes(25), LocalDateTime.of(2024, 11, 21, 13, 50));
        Subtask subtask3 = new Subtask("Много есть", "получить ачивку обжорство", Status.IN_PROGRESS, 1);
        taskManager1.addTask(epic1);
        taskManager1.addTask(epic2);
        taskManager1.addTask(subTask1);
        taskManager1.addTask(subTask2);
        taskManager1.addTask(subtask3);
        taskManager1.delSubTaskById(3);
        taskManager1.delSubTaskById(4);
        taskManager1.delSubTaskById(5);
        assertEquals(0, epic1.getSubIds().size());
        Throwable thrown = assertThrows(NullPointerException.class, () -> {
            taskManager1.getListSubTasks();
        });
        assertNotNull(thrown.getMessage());

    }

    @Test
    void testDellAllEpic() {
        Epic epic1 = new Epic("Сон", "Спать");
        Epic epic2 = new Epic("Еда", "Есть");
        Subtask subTask1 = new Subtask("долго спать", "проспать как можно дольше", Status.NEW, 1, Duration.ofMinutes(25), LocalDateTime.of(2024, 11, 20, 12, 50));
        Subtask subTask2 = new Subtask("очень долго спать", "спать в два раза дольше чем в первый раз", Status.DONE, 1, Duration.ofMinutes(25), LocalDateTime.of(2024, 11, 24, 12, 50));
        Subtask subtask3 = new Subtask("Много есть", "получить ачивку обжорство", Status.IN_PROGRESS, 2, Duration.ofMinutes(25), LocalDateTime.of(2024, 11, 19, 12, 50));
        taskManager1.addTask(epic1);
        taskManager1.addTask(epic2);
        taskManager1.addTask(subTask1);
        taskManager1.addTask(subTask2);
        taskManager1.addTask(subtask3);
        taskManager1.delListEpics();
        Throwable thrown = assertThrows(NullPointerException.class, () -> {
            taskManager1.getListSubTasks();
            taskManager1.getListEpics().size();
        });
        assertNotNull(thrown.getMessage());


    }

    @Test
    void testDellAllSubTask() throws NullPointerException{
        Epic epic1 = new Epic("Сон", "Спать");
        Epic epic2 = new Epic("Еда", "Есть");
        Subtask subTask1 = new Subtask("долго спать", "проспать как можно дольше", Status.NEW, 1, Duration.ofMinutes(25), LocalDateTime.of(2024, 11, 21, 12, 50));
        Subtask subTask2 = new Subtask("очень долго спать", "спать в два раза дольше чем в первый раз", Status.DONE, 1, Duration.ofMinutes(25), LocalDateTime.of(2024, 11, 19, 12, 50));
        Subtask subtask3 = new Subtask("Много есть", "получить ачивку обжорство", Status.IN_PROGRESS, 2, Duration.ofMinutes(25), LocalDateTime.of(2024, 11, 20, 12, 50));
        taskManager1.addTask(epic1);
        taskManager1.addTask(epic2);
        taskManager1.addTask(subTask1);
        taskManager1.addTask(subTask2);
        taskManager1.addTask(subtask3);
        taskManager1.delListSubTasks();
        Throwable thrown = assertThrows(NullPointerException.class, () -> {
        List<Subtask> testListSubtask = taskManager1.getListSubTasks();
    });
    assertNotNull(thrown.getMessage());



    }
}

