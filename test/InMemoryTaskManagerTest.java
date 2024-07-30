
import manager.*;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryTaskManagerTest {


    TaskManager taskManager1 = Managers.getDefault();


    @Test
    void addNewTask() {
        Task task = new Task("Test addNewTask", "Test addNewTask description", Status.NEW);
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
    void addNewSubTask() {
        Epic epic = new Epic("Test addNewTask", "Test addNewTask description");
        taskManager1.addTask(epic);
        final int epicId = epic.getId();
        Subtask task = new Subtask("Test addNewTask", "Test addNewTask description", Status.NEW, epicId);
        taskManager1.addTask(task);
        final int taskId = task.getId();

        final Task savedTask = taskManager1.getSubTask(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Subtask> tasks = taskManager1.getListSubTasks();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.getFirst(), "Задачи не совпадают.");
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
    void addSubTaskInSubtask() { //пришлось метод добавления самой подзадачи изменять  больше никакого теста вголову не пришло
        Epic epic = new Epic("Test addNewTask", "Test addNewTask description");
        taskManager1.addTask(epic);

        Subtask task1 = new Subtask("Test addNewTask", "Test addNewTask description", Status.NEW, 1);
        taskManager1.addTask(task1);

        Subtask task2 = new Subtask("Test addNewTask", "Test addNewTask description", Status.NEW, 2);
        taskManager1.addTask(task2);

    }

    @Test
    void initialUtilClass() {
        InMemoryTaskManager taskManager2 = (InMemoryTaskManager) Managers.getDefault();
        TaskManager taskManager3 = Managers.getDefault();

    }

    @Test
    void addAllTask() { //пришлось метод добавления самой подзадачи изменять  больше никакого теста вголову не пришло
        Task task = new Task("Test addNewTask", "Test addNewTask description", Status.NEW);
        taskManager1.addTask(task);

        Epic epic = new Epic("Test addNewTask", "Test addNewTask description");
        taskManager1.addTask(epic);

        Subtask subtask = new Subtask("Test addNewTask", "Test addNewTask description", Status.NEW, 2);
        taskManager1.addTask(subtask);

        Task task1 = taskManager1.getTask(1);
        Task epic1 = taskManager1.getEpic(2);
        Task subtask2 = taskManager1.getSubTask(3);
        assertEquals(task, task1);
        assertEquals(epic, epic1);
        assertEquals(subtask, subtask2);
    }

    @Test
    void conflictTask() {
        Task task = new Task("Test addNewTask", "Test addNewTask description", Status.NEW);
        taskManager1.addTask(task);
        Task task2 = new Task("Test addNewTask", "Test addNewTask description", Status.NEW);
        task2.setId(1);
        taskManager1.addTask(task2);
        assertNotEquals(task, task2);
        assertNotEquals(task.getId(), task2.getId());

    }

    @Test
    void standFieldsTask() {
        Task task = new Task("Test addNewTask", "Test addNewTask description", Status.NEW);
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
    void testHistory() {
        Epic epic1 = new Epic("Сон", "Спать");
        Epic epic2 = new Epic("Еда", "Есть");
        Task task1 = new Task("Бег", "пробежать как можно дольше", Status.DONE);
        Task task2 = new Task("Приседания", "приседать как можно больше", Status.NEW);
        Subtask subTask1 = new Subtask("долго спать", "проспать как можно дольше", Status.NEW, 1);
        Subtask subTask2 = new Subtask("очень долго спать", "спать в два раза дольше чем в первый раз", Status.DONE, 1);
        Subtask subtask3 = new Subtask("Много есть", "получить ачивку обжорство", Status.IN_PROGRESS, 2);

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
        taskManager1.getTask(5);
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
        taskManager1.getTask(5);
        taskManager1.getTask(4);
        taskManager1.getTask(3);
        taskManager1.getTask(4);
        taskManager1.getTask(3);
        taskManager1.getTask(4);
        taskManager1.getSubTask(5);
        taskManager1.getSubTask(6);
        taskManager1.getTask(4);
        taskManager1.getEpic(1);

        Subtask subtask4 = new Subtask("мало  есть", "похудеть", Status.IN_PROGRESS, 1);
        subtask4.setId(6);
        taskManager1.update(subtask4);


        List<Task> history = taskManager1.getHistory();

        Task taskSave = history.get(4);

        assertNotEquals(subTask2, taskManager1.getSubTask(6));
        assertEquals(task2, taskSave);
    }

    @Test
    void testDellSubTask() {
        Epic epic1 = new Epic("Сон", "Спать");
        Epic epic2 = new Epic("Еда", "Есть");
        Subtask subTask1 = new Subtask("долго спать", "проспать как можно дольше", Status.NEW, 1);
        Subtask subTask2 = new Subtask("очень долго спать", "спать в два раза дольше чем в первый раз", Status.DONE, 1);
        Subtask subtask3 = new Subtask("Много есть", "получить ачивку обжорство", Status.IN_PROGRESS, 2);
        taskManager1.addTask(epic1);
        taskManager1.addTask(epic2);
        taskManager1.addTask(subTask1);
        taskManager1.addTask(subTask2);
        taskManager1.addTask(subtask3);
        taskManager1.delSubTaskById(3);
        taskManager1.delSubTaskById(4);
        taskManager1.delSubTaskById(5);
        assertEquals(0, epic1.getSubIds().size());
        assertEquals(0, subtask3.getId());


    }

    @Test
    void testDellAllEpic() {
        Epic epic1 = new Epic("Сон", "Спать");
        Epic epic2 = new Epic("Еда", "Есть");
        Subtask subTask1 = new Subtask("долго спать", "проспать как можно дольше", Status.NEW, 1);
        Subtask subTask2 = new Subtask("очень долго спать", "спать в два раза дольше чем в первый раз", Status.DONE, 1);
        Subtask subtask3 = new Subtask("Много есть", "получить ачивку обжорство", Status.IN_PROGRESS, 2);
        taskManager1.addTask(epic1);
        taskManager1.addTask(epic2);
        taskManager1.addTask(subTask1);
        taskManager1.addTask(subTask2);
        taskManager1.addTask(subtask3);
        taskManager1.delListEpics();

        assertEquals(0, epic1.getSubIds().size());
        assertEquals(0, subtask3.getId());
    }

    @Test
    void testDellAllSubTask() {
        Epic epic1 = new Epic("Сон", "Спать");
        Epic epic2 = new Epic("Еда", "Есть");
        Subtask subTask1 = new Subtask("долго спать", "проспать как можно дольше", Status.NEW, 1);
        Subtask subTask2 = new Subtask("очень долго спать", "спать в два раза дольше чем в первый раз", Status.DONE, 1);
        Subtask subtask3 = new Subtask("Много есть", "получить ачивку обжорство", Status.IN_PROGRESS, 2);
        taskManager1.addTask(epic1);
        taskManager1.addTask(epic2);
        taskManager1.addTask(subTask1);
        taskManager1.addTask(subTask2);
        taskManager1.addTask(subtask3);
        taskManager1.delListSubTasks();

        assertEquals(0, epic1.getSubIds().size());
        assertEquals(0, subtask3.getId());
    }
}

