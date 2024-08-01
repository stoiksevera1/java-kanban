package manager;

import task.Task;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;


public class InMemoryHistoryManager implements HistoryManager {

    private Node<Task> tail = null;
    private Node<Task> head = null;

    private Map<Integer, Node<Task>> historyKey = new HashMap<>();


    @Override
    public void add(Task task) {
        if (historyKey.containsKey(task.getId())) {
            remove(task.getId());
            historyKey.put(task.getId(), linkLast(task));
        } else {
            historyKey.put(task.getId(), linkLast(task));
        }
    }


    @Override
    public List<Task> getHistory() {
        List<Task> historyList = new ArrayList<>();
        if (head != null) {
            Node<Task> iterableNode = head;
            while (iterableNode != null) {
                historyList.add(iterableNode.data);
                iterableNode = iterableNode.next;
            }
        }
        return historyList;
    }

    @Override
    public void remove(int id) {
        Node<Task> removeNode = historyKey.get(id);
        if (removeNode != null) {
            if (removeNode.prev == null) {
                head = head.next;
                if (head != null) {
                    head.prev = null;
                }
            } else if (removeNode.next == null) {
                tail = tail.prev;
                if (tail != null) {
                    tail.next = null;
                }
            } else {
                removeNode.prev.next = removeNode.next;
                removeNode.next.prev = removeNode.prev;
            }
            historyKey.remove(id);
        }
    }

    public Node<Task> linkLast(Task task) {
        Node<Task> newNode = new Node<>(task);

        if (head == null) {
            head = newNode;
            tail = newNode;
            return head;
        } else {
            newNode.prev = tail;
            tail.next = newNode;
            tail = newNode;

        }
        return tail;
    }

}

