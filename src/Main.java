public class Main {

    public static void main(String[] args) {

        System.out.println("Поехали!");
        TaskManager taskManager = new TaskManager();

        Epic epic1 = new Epic("Сон", "Спать");
        Epic epic2 = new Epic("Еда", "Есть");
        Task task1 = new Task("Бег", "пробежать как можно дольше", Status.DONE);
        Task task2 = new Task("Приседания", "приседать как можно больше", Status.NEW);
        Subtask subTask1 = new Subtask("долго спать", "проспать как можно дольше", Status.NEW);
        Subtask subTask2 = new Subtask("очень долго спать", "спать в два раза дольше чем в первый раз", Status.DONE);
        Subtask subtask3 = new Subtask("Много есть", "получить ачивку обжорство", Status.IN_PROGRESS);

        taskManager.addTask(epic1);
        taskManager.addTask(epic2);
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addTask(1, subTask1);
        taskManager.addTask(1, subTask2);
        taskManager.addTask(2, subtask3);


        taskManager.getListTasks();
        taskManager.getListEpics();
        taskManager.getListSubTasks();

        taskManager.getListEpic(epic1);

        taskManager.getTaskById(3);
        taskManager.getEpicById(2);
        taskManager.getSubTaskById(5);
        taskManager.getTaskById(5);
        taskManager.getEpicById(3);
        taskManager.getSubTaskById(2);
        System.out.println("КОНЕЦ");

        Subtask subtask4 = new Subtask("Много есть", "получить ачивку обжорство", Status.DONE);
        subtask4.setId(subtask3.getId());
        taskManager.update(2, subtask4);
        Subtask subTask5 = new Subtask("долго спать", "проспать как можно дольше", Status.DONE);
        subTask5.setId(subTask1.getId());
        taskManager.update(1, subTask5);

        subTask5.setId(subTask1.getId());
        taskManager.getListTasks();
        taskManager.getListEpics();
        taskManager.getListSubTasks();

        taskManager.getListEpic(epic1);
        taskManager.delEpicById(1);
        System.out.println("КОНЕЦ");
    }
}
