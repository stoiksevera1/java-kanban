import com.google.gson.Gson;
import handlers.HttpTaskServer;
import manager.InMemoryTaskManager;
import manager.TaskManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static handlers.HttpTaskServer.mg;
import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskManagerTasksTest {

    // создаём экземпляр InMemoryTaskManager
    TaskManager manager = new InMemoryTaskManager();
    // передаём его в качестве аргумента в конструктор HttpTaskServer
    HttpTaskServer taskServer = new HttpTaskServer(manager);
    Gson gson = HttpTaskServer.getGson();

    public HttpTaskManagerTasksTest() throws IOException {
    }

    @BeforeEach
    public void setUp() throws IOException {
        manager.delListTasks();
        manager.delListSubTasks();
        manager.delListEpics();
        taskServer.start();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
    }

    @Test
    public void testAddTask() throws IOException, InterruptedException {
        // создаём задачу
        Task task1 = new Task("Test 2", "Testing task 1",
                Status.NEW, Duration.ofMinutes(5), LocalDateTime.now());
        // конвертируем её в JSON

        String taskJson = gson.toJson(task1);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");

        HttpRequest request = HttpRequest.newBuilder().uri(url).header("Accept", "application/json").POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(201, response.statusCode());

        // проверяем, что создалась одна задача с корректным именем
        List<Task> tasksFromManager = manager.getListTasks();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Test 2", tasksFromManager.getFirst().getName(), "Некорректное имя задачи");
    }

    @Test
    public void testGetTask() throws IOException, InterruptedException {
        // создаём задачу
        Task task1 = new Task("Test 2", "Testing task 1",
                Status.NEW, Duration.ofMinutes(5), LocalDateTime.now());
        // конвертируем её в JSON


        manager.addTask(task1);
        String taskJson = gson.toJson(manager.getTask(1));
        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/1");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(200, response.statusCode());

        // проверяем, что создалась одна задача с корректным именем

        String testStr = response.body();
        assertEquals(taskJson, testStr, "Задачи не совпадают.");

    }

    @Test
    public void testGetTasks() throws IOException, InterruptedException {
        // создаём задачу
        Task task1 = new Task("Test 2", "Testing task 1",
                Status.NEW, Duration.ofMinutes(5), LocalDateTime.now());
        // конвертируем её в JSON
        Task task2 = new Task("Test 1", "Testing task 2",
                Status.NEW, Duration.ofMinutes(5), LocalDateTime.now().plusMinutes(50));

        manager.addTask(task1);
        manager.addTask(task2);
        String taskJson = gson.toJson(manager.getListTasks());

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Accept", "application/json")
                .GET()
                .build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(200, response.statusCode());

        // проверяем, что создалась одна задача с корректным именем


        String testStr = response.body();
        assertEquals(taskJson, testStr, "Задачи не совпадают.");
    }

    @Test
    public void testDelTaskEndNotFound() throws IOException, InterruptedException {
        // создаём задачу
        Task task1 = new Task("Test 2", "Testing task 1",
                Status.NEW, Duration.ofMinutes(5), LocalDateTime.now());
        // конвертируем её в JSON
        manager.addTask(task1);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/1");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Accept", "application/json")
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(201, response.statusCode());

        // проверяем, что создалась одна задача с корректным именем

        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(url)
                .header("Accept", "application/json")
                .GET()
                .build();
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response1.statusCode());

    }

    @Test
    public void testAddTaskNotAcceptable() throws IOException, InterruptedException {
        // создаём задачу
        Task task1 = new Task("Test 2", "Testing task 1",
                Status.NEW, Duration.ofMinutes(5), LocalDateTime.now());
        // конвертируем её в JSON
        Task task2 = new Task("Test 2", "Testing task 1",
                Status.NEW, Duration.ofMinutes(5), LocalDateTime.now());
        String taskJson = gson.toJson(task1);
        String taskJson2 = gson.toJson(task2);
        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");

        HttpRequest request = HttpRequest.newBuilder().uri(url).header("Accept", "application/json").POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(201, response.statusCode());
        HttpRequest request1 = HttpRequest.newBuilder().uri(url).header("Accept", "application/json").POST(HttpRequest.BodyPublishers.ofString(taskJson2)).build();
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
        List<Task> tasksFromManager = manager.getListTasks();
        assertEquals(406, response1.statusCode());
        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Test 2", tasksFromManager.getFirst().getName(), "Некорректное имя задачи");
    }


    @Test
    public void testAddEpic() throws IOException, InterruptedException {
        // создаём задачу


        Epic task1 = new Epic("Test 2", "Testing task 1");
        // конвертируем её в JSON

        String taskJson = gson.toJson(task1);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");

        HttpRequest request = HttpRequest.newBuilder().uri(url).header("Accept", "application/json").POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(201, response.statusCode());

        // проверяем, что создалась одна задача с корректным именем
        List<Epic> tasksFromManager = manager.getListEpics();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Test 2", tasksFromManager.getFirst().getName(), "Некорректное имя задачи");
    }

    @Test
    public void testGetEpic() throws IOException, InterruptedException {
        // создаём задачу
        Epic task1 = new Epic("Test 2", "Testing task 1");
        // конвертируем её в JSON

        manager.addTask(task1);
        String taskJson = gson.toJson(manager.getEpic(1));
        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/1");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(200, response.statusCode());

        // проверяем, что создалась одна задача с корректным именем

        String testStr = response.body();
        assertEquals(taskJson, testStr, "Задачи не совпадают.");

    }

    @Test
    public void testGetEpicSubtasks() throws IOException, InterruptedException {
        // создаём задачу
        Epic task1 = new Epic("Test 2", "Testing task 1");
        // конвертируем её в JSON
        Subtask task3 = new Subtask("Test 2", "Testing task 1",
                Status.NEW, 1, Duration.ofMinutes(5), LocalDateTime.now());
        // конвертируем её в JSON
        Subtask task2 = new Subtask("Test 1", "Testing task 2",
                Status.NEW, 1, Duration.ofMinutes(5), LocalDateTime.now().plusMinutes(50));

        manager.addTask(task1);
        manager.addTask(task2);
        manager.addTask(task3);
        String taskJson = gson.toJson(manager.getListEpic(1));
        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/1/subtasks");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(200, response.statusCode());

        // проверяем, что создалась одна задача с корректным именем

        String testStr = response.body();
        assertEquals(taskJson, testStr, "Задачи не совпадают.");

    }

    @Test
    public void testGetEpics() throws IOException, InterruptedException {
        // создаём задачу
        Epic task1 = new Epic("Test 2", "Testing task 1");
        // конвертируем её в JSON
        Epic task2 = new Epic("Test 1", "Testing task 2");

        manager.addTask(task1);
        manager.addTask(task2);
        String taskJson = gson.toJson(manager.getListEpics());

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Accept", "application/json")
                .GET()
                .build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(200, response.statusCode());

        // проверяем, что создалась одна задача с корректным именем


        String testStr = response.body();
        assertEquals(taskJson, testStr, "Задачи не совпадают.");
    }

    @Test
    public void testDelEpicEndNotFound() throws IOException, InterruptedException {
        // создаём задачу
        Epic epic = new Epic("Test 2", "Testing task 1");
        // конвертируем её в JSON
        manager.addTask(epic);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/1");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Accept", "application/json")
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(201, response.statusCode());

        // проверяем, что создалась одна задача с корректным именем

        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(url)
                .header("Accept", "application/json")
                .GET()
                .build();
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response1.statusCode());

    }

    @Test
    public void testAddSubtaskTask() throws IOException, InterruptedException {
        // создаём задачу
        Subtask task1 = new Subtask("Test 2", "Testing task 1",
                Status.NEW, 1, Duration.ofMinutes(5), LocalDateTime.now());
        // конвертируем её в JSON
        Epic epic = new Epic("Test 2", "Testing task 1");
        // конвертируем её в JSON
        manager.addTask(epic);
        String taskJson = gson.toJson(task1);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");

        HttpRequest request = HttpRequest.newBuilder().uri(url).header("Accept", "application/json").POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(201, response.statusCode());

        // проверяем, что создалась одна задача с корректным именем
        List<Task> tasksFromManager = manager.getListTasks();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Test 2", tasksFromManager.getFirst().getName(), "Некорректное имя задачи");
    }

    @Test
    public void testGetSubtaskTask() throws IOException, InterruptedException {

        Epic epic = new Epic("Test 2", "Testing task 1");

        manager.addTask(epic);
        Subtask task1 = new Subtask("Test 2", "Testing task 1",
                Status.NEW, 1, Duration.ofMinutes(5), LocalDateTime.now());


        manager.addTask(task1);
        String taskJson = gson.toJson(manager.getSubTask(2));

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/2");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(200, response.statusCode());

        // проверяем, что создалась одна задача с корректным именем

        String testStr = response.body();
        assertEquals(taskJson, testStr, "Задачи не совпадают.");

    }

    @Test
    public void testGetSubtaskTasks() throws IOException, InterruptedException {
        // создаём задачу
        Epic epic = new Epic("Test 2", "Testing task 1");
        // конвертируем её в JSON
        manager.addTask(epic);

        Subtask task1 = new Subtask("Test 2", "Testing task 1",
                Status.NEW, 1, Duration.ofMinutes(5), LocalDateTime.now());
        // конвертируем её в JSON
        Subtask task2 = new Subtask("Test 1", "Testing task 2",
                Status.NEW, 1, Duration.ofMinutes(5), LocalDateTime.now().plusMinutes(50));

        manager.addTask(task1);
        manager.addTask(task2);
        String taskJson = gson.toJson(manager.getListSubTasks());

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Accept", "application/json")
                .GET()
                .build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(200, response.statusCode());

        // проверяем, что создалась одна задача с корректным именем


        String testStr = response.body();
        assertEquals(taskJson, testStr, "Задачи не совпадают.");
    }

    @Test
    public void testDelSubtaskTaskEndNotFound() throws IOException, InterruptedException {
        // создаём задачу
        Epic epic = new Epic("Test 2", "Testing task 1");
        // конвертируем её в JSON
        manager.addTask(epic);
        Subtask task1 = new Subtask("Test 2", "Testing task 1",
                Status.NEW, 1, Duration.ofMinutes(5), LocalDateTime.now());
        // конвертируем её в JSON
        manager.addTask(task1);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/2");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Accept", "application/json")
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(201, response.statusCode());

        // проверяем, что создалась одна задача с корректным именем

        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(url)
                .header("Accept", "application/json")
                .GET()
                .build();
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response1.statusCode());

    }

    @Test
    public void testAddSubTaskNotAcceptable() throws IOException, InterruptedException {
        Epic epic = new Epic("Test 2", "Testing task 1");

        manager.addTask(epic);

        Subtask task1 = new Subtask("Test 2", "Testing task 1",
                Status.NEW, 1, Duration.ofMinutes(5), LocalDateTime.now());

        Subtask task2 = new Subtask("Test 2", "Testing task 1",
                Status.NEW, 1, Duration.ofMinutes(5), LocalDateTime.now());
        String taskJson = gson.toJson(task1);
        String taskJson2 = gson.toJson(task2);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");

        HttpRequest request = HttpRequest.newBuilder().uri(url).header("Accept", "application/json").POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(201, response.statusCode());
        HttpRequest request1 = HttpRequest.newBuilder().uri(url).header("Accept", "application/json").POST(HttpRequest.BodyPublishers.ofString(taskJson2)).build();
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
        List<Subtask> tasksFromManager = manager.getListSubTasks();
        assertEquals(406, response1.statusCode());
        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Test 2", tasksFromManager.getFirst().getName(), "Некорректное имя задачи");
    }

    @Test
    public void historyTest() throws IOException, InterruptedException {
        Epic epic = new Epic("Test 2", "Testing task 1");

        manager.addTask(epic);

        Subtask task1 = new Subtask("Test 2", "Testing task 1",
                Status.NEW, 1, Duration.ofMinutes(5), LocalDateTime.now());

        Subtask task2 = new Subtask("Test 2", "Testing task 1",
                Status.NEW, 1, Duration.ofMinutes(5), LocalDateTime.now().plusMinutes(35));
        Task task3 = new Task("Test 2", "Testing task 1",
                Status.NEW, Duration.ofMinutes(5), LocalDateTime.now().plusMinutes(25));
        // конвертируем её в JSON
        Task task4 = new Task("Test 1", "Testing task 2",
                Status.NEW, Duration.ofMinutes(5), LocalDateTime.now().plusMinutes(50));

        manager.addTask(task1);
        manager.addTask(task2);
        manager.addTask(task3);
        manager.addTask(task4);
        manager.getEpic(1);
        manager.getSubTask(2);
        manager.getSubTask(3);
        manager.getTask(4);
        manager.getTask(5);
        String taskJson = gson.toJson(manager.getHistory());

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/history");

        HttpRequest request = HttpRequest.newBuilder().uri(url).header("Accept", "application/json").GET().build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(200, response.statusCode());

        String testStr = response.body();
        assertEquals(taskJson, testStr, "Задачи не совпадают.");
    }

    @Test
    public void startTimetest() throws IOException, InterruptedException {
        Epic epic = new Epic("Test 2", "Testing task 1");

        manager.addTask(epic);

        Subtask task1 = new Subtask("Test 2", "Testing task 1",
                Status.NEW, 1, Duration.ofMinutes(5), LocalDateTime.now());

        Subtask task2 = new Subtask("Test 2", "Testing task 1",
                Status.NEW, 1, Duration.ofMinutes(5), LocalDateTime.now().plusMinutes(35));
        Task task3 = new Task("Test 2", "Testing task 1",
                Status.NEW, Duration.ofMinutes(5), LocalDateTime.now().plusMinutes(25));
        // конвертируем её в JSON
        Task task4 = new Task("Test 1", "Testing task 2",
                Status.NEW, Duration.ofMinutes(5), LocalDateTime.now().plusMinutes(50));
        manager.addTask(task1);
        manager.addTask(task2);
        manager.addTask(task3);
        manager.addTask(task4);

        String taskJson = gson.toJson(manager.getPrioritizedTasks());

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/prioritized");

        HttpRequest request = HttpRequest.newBuilder().uri(url).header("Accept", "application/json").GET().build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(200, response.statusCode());

        String testStr = response.body();
        assertEquals(taskJson, testStr, "Задачи не совпадают.");
    }
}