public class Task {
    private String name;
    private String description;
    private int id;
    private Status status;

    protected Task(String name, String description, Status status) {
        setDescription(description);
        setName(name);
        setStatus(status);

    }

    public void setDescription(String description) {

        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {

        this.id = id;
    }

    public void setStatus(Status status) {

        this.status = status;
    }

    public int getId() {

        return id;
    }


    public Status getStatus() {

        return status;
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                '}';
    }
}
