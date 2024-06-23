public class Main {

    public static void main(String[] args) {

        System.out.println("Поехали!");
        TaskManager taskManager = new TaskManager();

        Epic epic1 = new Epic("Сон", "Спать");
        Epic epic2 = new Epic("Еда", "Есть");
        Task task1 = new Task("Бег", "пробежать как можно дольше", Status.DONE);
        Task task2 = new Task("Приседания", "приседать как можно больше", Status.NEW);
        Subtask subTask1 = new Subtask("долго спать", "проспать как можно дольше", Status.NEW, 1);
        Subtask subTask2 = new Subtask("очень долго спать", "спать в два раза дольше чем в первый раз", Status.DONE, 1);
        Subtask subtask3 = new Subtask("Много есть", "получить ачивку обжорство", Status.IN_PROGRESS, 2);

        taskManager.addTask(epic1);
        taskManager.addTask(epic2);
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addTask(subTask1);
        taskManager.addTask(subTask2);
        taskManager.addTask(subtask3);


        System.out.println(taskManager.getListTasks());
        System.out.println(taskManager.getListEpics());
        System.out.println(taskManager.getListSubTasks());

        System.out.println(taskManager.getListEpic(1));

        System.out.println(taskManager.getTaskById(3));
        System.out.println(taskManager.getEpicById(2));
        System.out.println(taskManager.getSubTaskById(5));
        System.out.println(taskManager.getTaskById(5));
        System.out.println(taskManager.getEpicById(3));
        System.out.println(taskManager.getSubTaskById(2));
        System.out.println("КОНЕЦ");

        Subtask subtask4 = new Subtask("Много есть", "получить ачивку обжорство", Status.DONE, 2);
        subtask4.setId(subtask3.getId());
        taskManager.update(subtask4);
        Subtask subTask5 = new Subtask("долго спать", "проспать как можно дольше", Status.DONE, 1);
        subTask5.setId(subTask1.getId());
        taskManager.update(subTask5);

        subTask5.setId(subTask1.getId());
        System.out.println(taskManager.getListTasks());
        taskManager.getListEpics();
        taskManager.getListSubTasks();

        taskManager.getListEpic(1);
        taskManager.delEpicById(1);
        System.out.println("КОНЕЦ");
    }
}
