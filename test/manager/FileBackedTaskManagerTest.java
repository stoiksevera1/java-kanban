package manager;

import org.junit.jupiter.api.Test;
import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

import java.io.File;
import java.io.IOException;


import java.time.Duration;
import java.time.LocalDateTime;




class FileBackedTaskManagerTest extends TaskManagerTest {
    File file1;

    @Test
    void loadFromFile() throws IOException {
        FileBackedTaskManager manager1 = FileBackedTaskManager.loadFromFile(File.createTempFile("testFile", ".csv"));
    }


    @Test
    void loadFromFile2() {
        file1 = new File("text.csv");
        FileBackedTaskManager manager1 = FileBackedTaskManager.loadFromFile(file1);
        Epic epic1 = new Epic("Сон", "Спать");
        Epic epic2 = new Epic("Еда", "Есть");
        Task task1 = new Task("Бег", "пробежать как можно дольше", Status.DONE);
        Task task2 = new Task("Приседания", "приседать как можно больше", Status.NEW, Duration.ofMinutes(10), LocalDateTime.of(2024, 11, 25, 12, 50));
        Subtask subTask1 = new Subtask("долго спать", "проспать как можно дольше", Status.DONE, 1, Duration.ofMinutes(15), LocalDateTime.of(2024, 11, 20, 12, 50));
        Subtask subTask2 = new Subtask("очень долго спать", "спать в два раза дольше чем в первый раз", Status.DONE, 1, Duration.ofMinutes(50), LocalDateTime.of(2024, 11, 27, 12, 50));
        Subtask subtask3 = new Subtask("Много есть", "получить ачивку обжорство", Status.IN_PROGRESS, 2, Duration.ofMinutes(10), LocalDateTime.of(2024, 11, 22, 12, 50));

        manager1.addTask(epic1);
        manager1.addTask(epic2);
        manager1.addTask(task1);
        manager1.addTask(task2);

        manager1.addTask(subTask1);
        manager1.addTask(subTask2);
        manager1.addTask(subtask3);
    }
}