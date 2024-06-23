public class Subtask extends Task {
    private int epicId;

    public Subtask(String name, String description, Status status, int epicId) {

        super(name, description, status);
    }

    public int getEpicId() {

        return epicId;
    }

}